package com.hajela.profileservice.service;

import com.hajela.profileservice.dto.CustomerProfileDto;
import com.hajela.profileservice.entity.CustomerProfileEntity;
import com.hajela.profileservice.entity.SaveCustomerProfileDto;
import com.hajela.profileservice.repository.CustomerProfileRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerProfileService {

    private final CustomerProfileRepo customerProfileRepo;
    private final JwtUtils jwtUtils;


    public Optional<CustomerProfileEntity> getCustomerProfileData(String authorizationHeader) {
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        Optional<CustomerProfileEntity> byEmail = customerProfileRepo.findByEmail(email);
        return byEmail;
    }

    public CustomerProfileEntity saveCustomerProfile(SaveCustomerProfileDto saveCustomerProfileDto, String authorizationHeader) {
        CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.from(saveCustomerProfileDto);
        Optional<CustomerProfileEntity> optionalCustomerProfile = getCustomerProfileData(authorizationHeader);
        if (optionalCustomerProfile.isPresent()) {
            customerProfileEntity.setId(optionalCustomerProfile.get().getId());
            customerProfileEntity.setCreatedAt(optionalCustomerProfile.get().getCreatedAt());
            customerProfileEntity.setUpdatedAt(LocalDateTime.now());
        }
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        customerProfileEntity.setEmail(email);
        return customerProfileRepo.save(customerProfileEntity);
    }
}
