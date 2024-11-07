package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.builder.CustomerSearchBuilder;
import com.laptrinhjavaweb.dto.CustomerDTO;
import com.laptrinhjavaweb.dto.request.CustomerSearchRequest;
import com.laptrinhjavaweb.dto.response.CustomerSearchResponse;
import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.entity.TransactionEntity;
import com.laptrinhjavaweb.enums.TransactionsEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Component
public class CustomerConverter {
    @Autowired
    ModelMapper modelMapper;

    public CustomerSearchBuilder toBuilder(CustomerSearchRequest request){
        return new CustomerSearchBuilder.Builder()
                .setEmail(request.getEmail())
                .setFullName(request.getFullName())
                .setPhone(request.getPhone())
                .setStaffId(request.getStaffId())
                .build();
    }

    public CustomerSearchResponse toSearchResponse(CustomerEntity entity){
        CustomerSearchResponse result = modelMapper.map(entity, CustomerSearchResponse.class);
        if(entity.getUsers().size() > 0){
            result.setStaffName(entity.getUsers().get(0).getFullName());
        }
        result.setStatus("Đang xử lý");
        return result;
    }

    public CustomerEntity toEntity(CustomerDTO customerDTO){
        CustomerEntity customerEntity = modelMapper.map(customerDTO, CustomerEntity.class);
        return customerEntity;
    }

    public CustomerDTO toDTO(CustomerEntity customerEntity){
        CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);
        return customerDTO;
    }
}
