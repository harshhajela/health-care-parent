package com.hajela.profileservice.service;

import com.hajela.profileservice.entity.DocumentStatus;
import com.hajela.profileservice.entity.ProviderDocumentEntity;
import com.hajela.profileservice.repository.ProviderDocumentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderDocumentService {

    private final ProviderDocumentRepo documentRepo;
    private final JwtUtils jwtUtils;

    public List<ProviderDocumentEntity> getAllDocumentsForProvider(String authorizationHeader) {
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        return documentRepo.findAllByEmail(email);
    }

    public void saveDocument(String authorizationHeader, MultipartFile file, String title) throws IOException {
        var email = jwtUtils.getEmailFromHeader(authorizationHeader);
        var entity = ProviderDocumentEntity.builder()
                .email(email)
                .title(title)
                .fileName(file.getOriginalFilename())
                .image(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                .status(DocumentStatus.REVIEW)
                .dateUploaded(LocalDateTime.now())
                .build();
        entity = documentRepo.insert(entity);
        log.info("Document with id={} uploaded for email={}", entity.getId(), email);
    }
}
