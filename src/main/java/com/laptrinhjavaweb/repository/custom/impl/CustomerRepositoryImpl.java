package com.laptrinhjavaweb.repository.custom.impl;

import com.laptrinhjavaweb.builder.CustomerSearchBuilder;
import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.enums.SpecialSearchParamEnum;
import com.laptrinhjavaweb.repository.custom.CustomerRepositoryCustom;
import com.laptrinhjavaweb.utils.QueryBuilderUtils;
import com.laptrinhjavaweb.utils.ValidateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<CustomerEntity> findByCondition(CustomerSearchBuilder builder, Pageable pageable) {
        StringBuilder finalQuery = new StringBuilder()
                .append("select c.*\n")
                .append(" from customer as c\n");

        StringBuilder whereQuery = new StringBuilder();
        StringBuilder joinQuery = new StringBuilder();

        buildQuery(builder, whereQuery, joinQuery);
        finalQuery.append(joinQuery)
                .append(SystemConstant.ONE_EQUAL_ONE)
                .append(whereQuery)
                .append(" group by c.id");

        Query query = entityManager.createNativeQuery(finalQuery.toString(), CustomerEntity.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());
        return query.getResultList();

        /*Query query = entityManager.createNativeQuery(finalQuery.toString(), BuildingEntity.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());
        return query.getResultList();*/
    }

    @Override
    public int countByCondition(CustomerSearchBuilder builder) {
        StringBuilder finalQuery = new StringBuilder("SELECT count(distinct c.id)\n")
                .append(" from customer c\n");

        StringBuilder whereQuery = new StringBuilder();
        StringBuilder joinQuery = new StringBuilder();

        buildQuery(builder, whereQuery, joinQuery);

        finalQuery.append(joinQuery)
                .append(SystemConstant.ONE_EQUAL_ONE)
                .append(whereQuery);

        Query query = entityManager.createNativeQuery(finalQuery.toString());
        return query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()): 0;
    }

    private void buildQuery(CustomerSearchBuilder builder, StringBuilder whereQuery, StringBuilder joinQuery) {
        try{
            Field[] fields = CustomerSearchBuilder.class.getDeclaredFields();
            List<String> specialSearchParams = getSpecialSearchParams();
            for(Field field : fields){
                field.setAccessible(true);

                buildQueryForNormalCase(field, whereQuery, specialSearchParams, builder);

                buildQueryForSpecialCase(field, whereQuery, joinQuery, builder);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<String> getSpecialSearchParams() {
        return Arrays.stream(SpecialSearchParamEnum.values()).map(SpecialSearchParamEnum::getValue).collect(Collectors.toList());
    }

    private void buildQueryForNormalCase(Field field, StringBuilder whereQuery, List<String> specialSearchParams, CustomerSearchBuilder builder) throws IllegalAccessException {
        String fieldSearch = field.getName();
        Column column = field.getAnnotation(Column.class);
        String columnNameWithAlias = SystemConstant.CUSTOMER_ALIAS + column.name();
        Object fieldValue = field.get(builder);
        if(!specialSearchParams.contains(fieldSearch) && ValidateUtils.isValid(fieldValue)){
            if(field.getType().isAssignableFrom((String.class))){
                whereQuery.append(QueryBuilderUtils.withLike(columnNameWithAlias, fieldValue.toString()));
            }
            else if(field.getType().isAssignableFrom(Integer.class) || field.getType().isAssignableFrom(Long.class)){
                whereQuery.append(QueryBuilderUtils.withOperator(columnNameWithAlias, fieldValue, SystemConstant.EQUAL_OPERATOR));
            }
        }
    }

    private void buildQueryForSpecialCase(Field field, StringBuilder whereQuery, StringBuilder joinQuery, CustomerSearchBuilder builder) throws IllegalAccessException {
        String fieldSearch = field.getName();
        Column column = field.getAnnotation(Column.class);
      //  String columnNameWithAlias = SystemConstant.CUSTOMER_ALIAS + column.name();
        Object fieldValue = field.get(builder);
        buildQueryForStaff(fieldSearch, fieldValue, column, whereQuery, joinQuery);

    }

    private void buildQueryForStaff(String fieldSearch, Object fieldValue, Column column, StringBuilder whereQuery, StringBuilder joinQuery) {
        if(SystemConstant.STAFF_SEARCH_PARAM.equals(fieldSearch) && ValidateUtils.isValid(fieldValue)){
            joinQuery.append("join assignmentcustomer as ac on ac.customerid = c.id\n");
            whereQuery.append(QueryBuilderUtils.withOperator("ac." + column.name(), fieldValue, SystemConstant.EQUAL_OPERATOR));
        }
    }
}
