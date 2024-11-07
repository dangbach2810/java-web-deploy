package com.laptrinhjavaweb.repository.custom.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.enums.SpecialSearchParamEnum;
import com.laptrinhjavaweb.repository.custom.BuildingRepositoryCustom;
import com.laptrinhjavaweb.utils.QueryBuilderUtils;

import com.laptrinhjavaweb.utils.ValidateUtils;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public List<BuildingEntity> findByCondition(BuildingSearchBuilder builder, Pageable pageable) {
		StringBuilder finalQuery = new StringBuilder()
				.append("select b.*\n")
				.append(" from building as b\n");
		
		StringBuilder whereQuery = new StringBuilder();
		StringBuilder joinQuery = new StringBuilder();
		
		buildQuery(builder, whereQuery, joinQuery);

		finalQuery.append(joinQuery)
				.append(SystemConstant.ONE_EQUAL_ONE)
				.append(whereQuery)
				.append(SystemConstant.GROUP_BY_BUILDING_ID);


		Query query = entityManager.createNativeQuery(finalQuery.toString(), BuildingEntity.class)
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	@Override
	public int countByCondition(BuildingSearchBuilder builder) {
		StringBuilder finalQuery = new StringBuilder("SELECT count(distinct b.id)\n")
				.append(" from building b\n");

		StringBuilder whereQuery = new StringBuilder();
		StringBuilder joinQuery = new StringBuilder();

		buildQuery(builder, whereQuery, joinQuery);

		finalQuery.append(joinQuery)
				.append(SystemConstant.ONE_EQUAL_ONE)
				.append(whereQuery);

		Query query = entityManager.createNativeQuery(finalQuery.toString());
		return query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()): 0;
	}

	@Override
	public List<BuildingEntity> getAllBuildings(Pageable pageable) {
		StringBuilder sql = new StringBuilder("SELECT * FROM building")
				.append(" LIMIT ").append(pageable.getPageSize())
				.append(" OFFSET ").append(pageable.getOffset());
		System.out.println("Final query: " + sql.toString());

		Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
		return query.getResultList();
	}

	private void buildQuery(BuildingSearchBuilder builder, StringBuilder whereQuery, StringBuilder joinQuery) {
		try{
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
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

	private void buildQueryForNormalCase(Field field, StringBuilder whereQuery, List<String> specialSearchParams, BuildingSearchBuilder builder) throws IllegalAccessException {
		String fieldSearch = field.getName();
		Column column = field.getAnnotation(Column.class);
		String columnNameWithAlias = SystemConstant.BUILDING_ALIAS + column.name();
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

	private void buildQueryForSpecialCase(Field field, StringBuilder whereQuery, StringBuilder joinQuery, BuildingSearchBuilder builder) throws IllegalAccessException {
		String fieldSearch = field.getName();
		Column column = field.getAnnotation(Column.class);
		String columnNameWithAlias = SystemConstant.BUILDING_ALIAS + column.name();
		Object fieldValue = field.get(builder);

		buildQueryForRentArea(fieldSearch, fieldValue, column, whereQuery, joinQuery);

		buildQueryForRentPrice(fieldSearch, fieldValue, columnNameWithAlias, whereQuery);

		buildQueryFoBuildingType(fieldSearch, fieldValue, columnNameWithAlias, whereQuery, field);

		buildQueryForStaff(fieldSearch, fieldValue, column, whereQuery, joinQuery);

	}
	private void buildQueryForRentArea(String fieldSearch, Object fieldValue, Column column, StringBuilder whereQuery, StringBuilder joinQuery) {
		if((SystemConstant.RENT_AREA_FROM_SEARCH_PARAM.equals(fieldSearch) || SystemConstant.RENT_PRICE_TO_SEARCH_PARAM.equals(fieldSearch)) &&
				ValidateUtils.isValid(fieldValue)){
			if(!joinQuery.toString().contains("join rentarea")){
				joinQuery.append("join rentarea ra on ra.buildingid = b.id\n");
			}
			if(SystemConstant.RENT_AREA_FROM_SEARCH_PARAM.equals(fieldSearch)){
				whereQuery.append(QueryBuilderUtils.withOperator("ra." +column.name(), fieldValue, SystemConstant.GREATER_THAN_OPERATOR));
			}
			if(SystemConstant.RENT_AREA_TO_SEARCH_PARAM.equals(fieldSearch)){
				whereQuery.append(QueryBuilderUtils.withOperator("ra." +column.name(), fieldValue, SystemConstant.LESS_THAN_OPERATOR));
			}
		}
	}

	private void buildQueryForRentPrice(String fieldSearch, Object fieldValue, String columnNameWithAlias, StringBuilder whereQuery) {
		if(SystemConstant.RENT_PRICE_FROM_SEARCH_PARAM.equals(fieldSearch)
		|| SystemConstant.RENT_PRICE_TO_SEARCH_PARAM.equals(fieldSearch)
		&& ValidateUtils.isValid(fieldValue)){
			if(SystemConstant.RENT_PRICE_FROM_SEARCH_PARAM.equals(fieldSearch)){
				whereQuery.append(QueryBuilderUtils.withOperator(columnNameWithAlias, fieldValue, SystemConstant.GREATER_THAN_OPERATOR));
			}
			if(SystemConstant.RENT_PRICE_TO_SEARCH_PARAM.equals(fieldSearch)){
				whereQuery.append(QueryBuilderUtils.withOperator(columnNameWithAlias, fieldValue, SystemConstant.LESS_THAN_OPERATOR));
			}
		}
	}

	private void buildQueryFoBuildingType(String fieldSearch, Object fieldValue, String columnNameWithAlias, StringBuilder whereQuery, Field field) {
		if(SystemConstant.BUILDING_TYPE_SEARCH_PARAM.equals(fieldSearch) && field.getType().isAssignableFrom(List.class)
		&& fieldValue != null){
			List<String> types = (List<String>) fieldValue;
			if(!types.isEmpty()){
				whereQuery.append(QueryBuilderUtils.withOrAndLike(columnNameWithAlias, types));
			}
		}
	}

	private void buildQueryForStaff(String fieldSearch, Object fieldValue, Column column, StringBuilder whereQuery, StringBuilder joinQuery) {
		if(SystemConstant.STAFF_SEARCH_PARAM.equals(fieldSearch) && ValidateUtils.isValid(fieldValue)){
			joinQuery.append("join assignmentbuilding ab on ab.buildingid = b.id\n");
			whereQuery.append(QueryBuilderUtils.withOperator("ab." + column.name(), fieldValue, SystemConstant.EQUAL_OPERATOR));
		}
	}
}
