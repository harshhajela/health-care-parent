package com.hajela.profileservice.dto;

import com.hajela.profileservice.entity.DocumentStatus;
import com.hajela.profileservice.entity.ProviderDocumentEntity;
import lombok.*;
import org.bson.types.Binary;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInfoDto {
    private String title;
    private String fileName;
    @ToString.Exclude
    private Binary image;
    private DocumentStatus status;
    private LocalDateTime dateUploaded;

    public static DocumentInfoDto fromEntity(ProviderDocumentEntity entity) {
        DocumentInfoDto dto = new DocumentInfoDto();
        dto.setTitle(entity.getTitle());
        dto.setFileName(entity.getFileName());
        dto.setImage(entity.getImage());
        dto.setStatus(entity.getStatus());
        dto.setDateUploaded(entity.getDateUploaded());
        return dto;
    }
}
