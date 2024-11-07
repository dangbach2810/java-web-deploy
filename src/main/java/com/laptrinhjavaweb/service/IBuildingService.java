package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.AssignmentBuildingDTO;
import com.laptrinhjavaweb.dto.BuildingDTO;

import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IBuildingService {
    BuildingDTO findById(Long id);
    List<BuildingSearchResponse> findByCondition(BuildingSearchRequest request, Pageable pageable);
    BuildingDTO save (BuildingDTO buildingDTO);
    void delete (List<Long> buildingIds);
    //Long assignmentBuilding(AssignmentBuildingDTO assignmentBuildingDTO);
    Map<String, String> getAllRentTypes();
    Map<String, String> getAllDistricts();
    int countByCondition(BuildingSearchRequest request);
    List<StaffResposeDTO> loadStaffByBuildingId(Long buildingId);
    void assignBuildingToStaffs(AssignmentBuildingDTO assignmentBuildingDTO);
    List<BuildingSearchResponse> getAllBuilding(Pageable pageable);
}
