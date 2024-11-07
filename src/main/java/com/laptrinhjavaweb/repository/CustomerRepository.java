package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.CustomerEntity;

import com.laptrinhjavaweb.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepositoryCustom {
    void deleteByIdIn(List<Long> customerIds);
    Long countByIdIn(List<Long> customerIds);
   // List<TransactionEntity> findByTransactions_Customer_Id(Long customerId);
}
