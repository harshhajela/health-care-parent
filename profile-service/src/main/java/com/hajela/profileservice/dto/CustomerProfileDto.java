package com.hajela.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hajela.profileservice.entity.CustomerProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfileDto {
    private String id;
    private String email;
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber = "";
    private String gender = "";
    private LocalDate dateOfBirth;
    private String address1 = "";
    private String address2 = "";
    private String address3 = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";

    public static CustomerProfileDto from(CustomerProfileEntity profileEntity) {
        return CustomerProfileDto.builder()
                .id(profileEntity.getId())
                .email(profileEntity.getEmail())
                .firstName(profileEntity.getFirstName())
                .lastName(profileEntity.getLastName())
                .phoneNumber(profileEntity.getPhoneNumber())
                .gender(profileEntity.getGender())
                .dateOfBirth(profileEntity.getDateOfBirth())
                .address1(profileEntity.getAddress1())
                .address2(profileEntity.getAddress2())
                .address3(profileEntity.getAddress3())
                .city(profileEntity.getCity())
                .state(profileEntity.getState())
                .pinCode(profileEntity.getPinCode())
                .build();
    }
}
