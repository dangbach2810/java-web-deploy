package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.converter.UserConverter;
import com.laptrinhjavaweb.dto.AssignmentBuildingDTO;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.security.utils.SecurityUtils;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.utils.UploadFileUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@Service
@PropertySource("classpath:application.properties")
public class BuildingService implements IBuildingService {

    @Value("${dir.default}")
    private String dirDefault;

    private final UploadFileUtils uploadFileUtils;
    private final BuildingRepository buildingRepository;
    private final BuildingConverter buildingConverter;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
   // private final RentAreaRepository rentAreaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingService.class);

    @Autowired
    public BuildingService(UploadFileUtils uploadFileUtils, BuildingRepository buildingRepository, BuildingConverter buildingConverter, UserRepository userRepository, UserConverter userConverter) {
        this.uploadFileUtils = uploadFileUtils;
        this.buildingRepository = buildingRepository;
        this.buildingConverter = buildingConverter;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
      //  this.rentAreaRepository = rentAreaRepository;
    }


    @Override
    public BuildingDTO findById(Long id) {
        BuildingEntity buildingEntity = Optional.of(buildingRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("Building not found!"));
        return buildingConverter.toDTO(buildingEntity);

    }

    @Override
    public List<BuildingSearchResponse> findByCondition(BuildingSearchRequest request, Pageable pageable) {
        if(SecurityUtils.getAuthorities().contains("ROLE_STAFF")){
            Long staffId = SecurityUtils.getPrincipal().getId();
            request.setStaffId(staffId);
        }
        List<BuildingEntity> foundBuildings = buildingRepository.findByCondition(buildingConverter.toBuilder(request), pageable);
        return foundBuildings.stream()
               .map(buildingConverter::toSearchResponse)
               .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuildingDTO save(BuildingDTO buildingDTO) {
        Long buildingId = buildingDTO.getId();
        BuildingEntity buildingEntity = buildingConverter.toEntity((buildingDTO));

        if(buildingId != null){
            BuildingEntity foundBuilding = Optional.of(buildingRepository.findById(buildingId)).get()
                    .orElseThrow(() -> new NotFoundException("Building not found!"));
            buildingEntity.setAvatar(foundBuilding.getAvatar());
            buildingEntity.setUsers(foundBuilding.getUsers());
        }
        saveThumbnail(buildingDTO, buildingEntity);
        return buildingConverter.toDTO(buildingRepository.save(buildingEntity));
    }

    @Override
    @Transactional
    public void delete(List<Long> buildingIds) {
        if(buildingIds != null && !buildingIds.isEmpty()){
            Long count = buildingRepository.countByIdIn(buildingIds);
            if(count != buildingIds.size()){
                throw new NotFoundException("Building not found!");
            }
            buildingRepository.deleteByIdIn(buildingIds);
        }
    }


    @Override
    public Map<String, String> getAllRentTypes() {
        return Arrays.stream(BuildingTypesEnum.values()).collect(Collectors.toMap(Enum::toString, BuildingTypesEnum::getBuildingTypeValue));
    }

    @Override
    public Map<String, String> getAllDistricts() {
        return Arrays.stream(DistrictsEnum.values()).collect(Collectors.toMap(Enum::toString, DistrictsEnum::getDistrictValue));
    }

    @Override
    public int countByCondition(BuildingSearchRequest request) {
        if(SecurityUtils.getAuthorities().contains("ROLE_STAFF")){
            Long staffId = SecurityUtils.getPrincipal().getId();
            request.setStaffId(staffId);
        }
        return buildingRepository.countByCondition(buildingConverter.toBuilder(request));
    }

    @Override
    public List<StaffResposeDTO> loadStaffByBuildingId(Long buildingId) {
        List<UserEntity> allStaffs = userRepository.findByStatusAndRoles_Code(SystemConstant.ACTIVE_STATUS, "STAFF");
        return allStaffs.stream().map(staff ->
        {
            StaffResposeDTO staffRespose = userConverter.toAssignmentStaffResponseDTO(staff);

            for(BuildingEntity building : staff.getBuildings())
            {
                if(buildingId.equals(building.getId())){
                    staffRespose.setChecked("checked");
                    break;
                }
            }
            return staffRespose;
        }).collect(Collectors.toList());
    }

    @Override
    public void assignBuildingToStaffs(AssignmentBuildingDTO assignmentBuildingDTO) {
        Long buildingId = assignmentBuildingDTO.getBuildingId();
        List<Long> staffIdsRequest = assignmentBuildingDTO.getStaffIds();

        BuildingEntity foundBuilding = Optional.of(buildingRepository.findById(buildingId)).get()
                .orElseThrow(() -> new NotFoundException("Building not found"));
        List<UserEntity> foundUsers = userRepository.findByIdIn(staffIdsRequest);
        foundBuilding.setUsers(foundUsers);
        buildingRepository.save(foundBuilding);

    }

    @Override
    public List<BuildingSearchResponse> getAllBuilding(Pageable pageable) {
        List<BuildingSearchResponse> results = new ArrayList<>();
        List<BuildingEntity> entities = buildingRepository.getAllBuildings(pageable);
        for (BuildingEntity item : entities){
            results.add(buildingConverter.toSearchResponse(item));
        }
        return results;
    }

    private void saveThumbnail(BuildingDTO buildingDTO, BuildingEntity buildingEntity) {
        String path = "/building/" + buildingDTO.getImageName();
        if (null != buildingDTO.getImageBase64()) {
            if (null != buildingEntity.getAvatar()) {
                if (!path.equals(buildingEntity.getAvatar())) {
                    File file = new File(dirDefault + buildingEntity.getAvatar());
                    file.delete();
                }
            }
            byte[] bytes = Base64.decodeBase64(buildingDTO.getImageBase64().getBytes());
            uploadFileUtils.writeOrUpdate(path, bytes);
            buildingEntity.setAvatar(path);
        }
    }
}
