package com.hajela.profileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidersDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private List<String> servicesOffered;
    private Binary image;
}
