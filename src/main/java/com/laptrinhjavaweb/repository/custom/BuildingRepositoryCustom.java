package com.laptrinhjavaweb.repository.custom;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BuildingRepositoryCustom {
	List<BuildingEntity> findByCondition(BuildingSearchBuilder builder, Pageable pageable);
	int countByCondition(BuildingSearchBuilder builder);
	List<BuildingEntity> getAllBuildings(Pageable pageable);
}
