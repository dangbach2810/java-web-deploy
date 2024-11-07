package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.dto.BuildingDTO;

import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
import com.laptrinhjavaweb.dto.response.BuildingSearchResponse;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;

import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.utils.BuildingTypeUtils;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class BuildingConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RentAreaRepository rentAreaRepository;

    public BuildingDTO toDTO(BuildingEntity entity){
        BuildingDTO buildingDTO = modelMapper.map(entity, BuildingDTO.class);
        //List<RentAreaEntity> rentAreas = rentAreaRepository.findByBuilding_Id(entity.getId());
        //String rentAreaStr = rentAreas.stream().map(item -> item.getValue().toString()).collect(Collectors.joining(","));
        String buildingTypes = entity.getType();
        if(StringUtils.isNotBlank(buildingTypes)){
            List<String> convertedBuildingType = Arrays.asList(buildingTypes.split(","));
            buildingDTO.setTypes(convertedBuildingType);
        }
        String rentAreaString = entity.getRentAreas().stream()
                .map(rentArea -> String.valueOf(rentArea.getValue()))
                .collect(Collectors.joining(","));
        buildingDTO.setRentArea(rentAreaString);
        return buildingDTO;
    }

    public BuildingSearchResponse toSearchResponse (BuildingEntity entity){
        BuildingSearchResponse result = modelMapper.map(entity, BuildingSearchResponse.class);
        List<String> address = new ArrayList<>();
        address.add(entity.getStreet());
        address.add(entity.getWard());
        String districtCode = entity.getDistrict();

        if(districtCode != null && !districtCode.isEmpty()){
            address.add(DistrictsEnum.valueOf(districtCode).getDistrictValue());
        }

        result.setAddress(address.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(", ")));
        String rentAreaString = entity.getRentAreas().stream()
                .map(rentArea -> String.valueOf(rentArea.getValue()))
                .collect(Collectors.joining(", "));

        result.setRentAreaDescription("Diện tích còn trống: " + rentAreaString);
        return result;
    }

    public BuildingEntity toEntity (BuildingDTO buildingDTO){
        BuildingEntity buildingEntity = modelMapper.map(buildingDTO, BuildingEntity.class);
        String rentArea = buildingDTO.getRentArea();

        List<String> buildingTypes = buildingDTO.getTypes();
        if(StringUtils.isNotBlank(rentArea)){
            List<String> convertedRentArea = Arrays.asList(rentArea.split(","));

            List<RentAreaEntity> rentAreas = convertedRentArea.stream().map((String value) -> {
                RentAreaEntity rentAreaEntity = new RentAreaEntity();
                rentAreaEntity.setValue(Integer.parseInt(value));
                rentAreaEntity.setBuilding(buildingEntity);
                return rentAreaEntity;
            }).collect(Collectors.toList());
            buildingEntity.setRentAreas(rentAreas);
        }
        if(!buildingTypes.isEmpty()){
            String convertedType = String.join(",",buildingTypes);
            buildingEntity.setType(convertedType);
        }
        return buildingEntity;
    }

    public BuildingSearchBuilder toBuilder(BuildingSearchRequest request) {
        return new BuildingSearchBuilder.Builder()
                .setName(request.getName())
                .setWard(request.getWard())
                .setStreet(request.getStreet())
                .setDistrict(request.getDistrict())
                .setBuildingTypes(request.getTypes())
                .setRentAreaFrom(request.getRentAreaFrom())
                .setRentAreaTo(request.getRentAreaTo())
                .setRentPriceFrom(request.getRentPriceFrom())
                .setRentPriceTo(request.getRentPriceTo())
                .setDirection(request.getDirection())
                .setFloorArea(request.getFloorArea())
                .setLevel(request.getLevel())
                .setManagerName(request.getManagerName())
                .setManagerPhone(request.getManagerPhone())
                .setNumberOfBasement(request.getNumberOfBasement())
                .setStaffId(request.getStaffId())
                .build();
    }

}
