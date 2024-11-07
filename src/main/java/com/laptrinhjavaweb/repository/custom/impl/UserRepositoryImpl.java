package com.laptrinhjavaweb.repository.custom.impl;

import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.custom.UserRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<UserEntity> getUserByBuildingId(Long id) {
        String sql = "select * from user u inner join assignmentbuilding as a on u.id = a.staffid where a.buildingid = "+id+"";
        Query query = entityManager.createNativeQuery(sql, UserEntity.class);
        return query.getResultList();
    }
}
