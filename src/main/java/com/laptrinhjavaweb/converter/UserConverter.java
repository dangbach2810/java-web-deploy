package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.dto.response.StaffResposeDTO;
import com.laptrinhjavaweb.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO convertToDto (UserEntity entity){
        UserDTO result = modelMapper.map(entity, UserDTO.class);
        return result;
    }

    public UserEntity convertToEntity (UserDTO dto){
        UserEntity result = modelMapper.map(dto, UserEntity.class);
        return result;
    }

    public StaffResposeDTO toAssignmentStaffResponseDTO(UserEntity staff) {
        StaffResposeDTO staffRespose = new StaffResposeDTO();
        staffRespose.setStaffId(staff.getId());
        staffRespose.setFullName(staff.getFullName());
        staffRespose.setChecked("");
        return staffRespose;
    }
    /* public StaffResposeDTO toAssignmentStaffResposeDTO(UserEntity userEntity, boolean checked){
        StaffResposeDTO staffResposeDTO = new StaffResposeDTO();
        staffResposeDTO.setStaffId(userEntity.getId());
        staffResposeDTO.setFullName(userEntity.getFullName());
        staffResposeDTO.setChecked(checked ?"checked":"");
        return staffResposeDTO;
    }*/
}
