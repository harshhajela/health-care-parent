package com.hajela.profileservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customer-profile")
public class CustomerProfileEntity {
    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String pinCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerProfileEntity from(SaveCustomerProfileDto profileDto) {
        return CustomerProfileEntity.builder()
                .firstName(profileDto.getFirstName())
                .lastName(profileDto.getLastName())
                .phoneNumber(profileDto.getPhoneNumber())
                .gender(profileDto.getGender())
                .dateOfBirth(profileDto.getDateOfBirth())
                .address1(profileDto.getAddress1())
                .address2(profileDto.getAddress2())
                .address3(profileDto.getAddress3())
                .city(profileDto.getCity())
                .state(profileDto.getState())
                .pinCode(profileDto.getPinCode())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
