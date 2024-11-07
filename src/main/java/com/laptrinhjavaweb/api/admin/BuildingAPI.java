package com.laptrinhjavaweb.api.admin;

import com.laptrinhjavaweb.dto.AssignmentBuildingDTO;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.response.ResponseDTO;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.service.impl.BuildingService;
import com.laptrinhjavaweb.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "buildingAPIOfAdmin")
@RequestMapping("/api/building")
public class BuildingAPI {
    @Autowired
    private BuildingService buildingService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<BuildingDTO> saveBuilding(@RequestBody BuildingDTO newBuilding){
        return ResponseEntity.ok(buildingService.save(newBuilding));
    }

    @GetMapping("/{buildingId}/staffs")
    public ResponseEntity<List<StaffResposeDTO>> loadStaff(@PathVariable Long buildingId){
        return ResponseEntity.ok(buildingService.loadStaffByBuildingId(buildingId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBuilding(@RequestBody List<Long> buildingIds){
        buildingService.delete(buildingIds);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/assignment/building")
    public ResponseEntity<Void> assignmentBuilding(@RequestBody AssignmentBuildingDTO assignmentBuildingDTO){
        buildingService.assignBuildingToStaffs(assignmentBuildingDTO);
        return ResponseEntity.noContent().build();
    }
}
