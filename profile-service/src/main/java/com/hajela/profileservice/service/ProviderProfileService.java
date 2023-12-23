package com.hajela.profileservice.service;

import com.hajela.profileservice.dto.ProviderProfileDto;
import com.hajela.profileservice.dto.ProvidersDto;
import com.hajela.profileservice.entity.ProviderDocumentEntity;
import com.hajela.profileservice.entity.ProviderProfileEntity;
import com.hajela.profileservice.repository.ProviderDocumentRepo;
import com.hajela.profileservice.repository.ProviderProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProviderProfileService {

    private final ProviderProfileRepo repository;
    private final ProviderDocumentRepo documentRepo;
    private final JwtUtils jwtUtils;

    public Optional<ProviderProfileEntity> getProviderByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<ProviderProfileEntity> updateProvider(String authorizationHeader, ProviderProfileDto providerDTO) {
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        Optional<ProviderProfileEntity> optionalProvider = repository.findByEmail(email);
        if (optionalProvider.isPresent()) {
            ProviderProfileEntity existingProvider = updateProfileEntity(providerDTO, optionalProvider.get());
            return Optional.of(repository.save(existingProvider));
        } else {
            var entity = ProviderProfileEntity.builder()
                    .email(email)
                    .firstName(providerDTO.getFirstName())
                    .lastName(providerDTO.getLastName())
                    .phoneNumber(providerDTO.getPhoneNumber())
                    .dateOfBirth(providerDTO.getDateOfBirth()) 
                    .gender(providerDTO.getGender()) 
                    .addressLine1(providerDTO.getAddressLine1()) 
                    .addressLine2(providerDTO.getAddressLine2()) 
                    .addressLine3(providerDTO.getAddressLine3()) 
                    .city(providerDTO.getCity()) 
                    .state(providerDTO.getState()) 
                    .pinCode(providerDTO.getPinCode()) 
                    .build();
            return Optional.of(repository.save(entity));
        }

    }

    private static ProviderProfileEntity updateProfileEntity(ProviderProfileDto providerDTO,
                                                             ProviderProfileEntity existingProvider) {
        existingProvider.setFirstName(providerDTO.getFirstName());
        existingProvider.setLastName(providerDTO.getLastName());
        existingProvider.setPhoneNumber(providerDTO.getPhoneNumber());
        existingProvider.setDateOfBirth(providerDTO.getDateOfBirth());
        existingProvider.setGender(providerDTO.getGender());
        existingProvider.setAddressLine1(providerDTO.getAddressLine1());
        existingProvider.setAddressLine2(providerDTO.getAddressLine2());
        existingProvider.setAddressLine3(providerDTO.getAddressLine3());
        existingProvider.setCity(providerDTO.getCity());
        existingProvider.setState(providerDTO.getState());
        existingProvider.setPinCode(providerDTO.getPinCode());
        if (providerDTO.getServicesOffered() != null && !providerDTO.getServicesOffered().isEmpty()) {
            existingProvider.setServicesOffered(providerDTO.getServicesOffered());
        }
        return existingProvider;
    }

    public Optional<ProviderProfileEntity> updateServices(String authorizationHeader, ProviderProfileDto providerDTO) {
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        Optional<ProviderProfileEntity> optionalProvider = repository.findByEmail(email);
        if (optionalProvider.isPresent()) {
            ProviderProfileEntity existingProvider = optionalProvider.get();
            existingProvider.setServicesOffered(providerDTO.getServicesOffered());
            return Optional.of(repository.save(existingProvider));
        } else {
            var entity = ProviderProfileEntity.builder()
                    .email(email)
                    .servicesOffered(providerDTO.getServicesOffered())
                    .build();
            return Optional.of(repository.save(entity));
        }
    }

    public Page<ProvidersDto> getAllProviders(Pageable pageable) {
        Page<ProviderProfileEntity> profilePage = repository.findAll(pageable);

        List<String> providerEmails = profilePage.getContent().stream()
                .map(ProviderProfileEntity::getEmail)
                .toList();

        List<ProviderDocumentEntity> profilePicDocuments =
                documentRepo.findByTitleAndEmailIn("PROFILE", providerEmails);

        List<ProvidersDto> providersDtos = profilePage.getContent().stream()
                .map(profile -> mapToProvidersDto(profile, profilePicDocuments))
                .toList();

        return new PageImpl<>(providersDtos, pageable, profilePage.getTotalElements());

    }

    private ProvidersDto mapToProvidersDto(ProviderProfileEntity profile, List<ProviderDocumentEntity> profilePicDocuments) {
        ProvidersDto providersDto = new ProvidersDto();
        // Map fields from ProviderProfileEntity to ProvidersDto
        providersDto.setId(profile.getId());
        providersDto.setEmail(profile.getEmail());
        providersDto.setFirstName(profile.getFirstName());
        providersDto.setLastName(profile.getLastName());
        providersDto.setCity(profile.getCity());
        providersDto.setState(profile.getState());
        providersDto.setServicesOffered(profile.getServicesOffered());

        // Find the corresponding profile pic document
        ProviderDocumentEntity profilePicDocument = profilePicDocuments.stream()
                .filter(doc -> doc.getEmail().equals(profile.getEmail()))
                .findFirst()
                .orElse(null);

        // Map profile pic details to ProvidersDto
        if (profilePicDocument != null) {
            providersDto.setImage(profilePicDocument.getImage());
        }

        return providersDto;
    }
}
