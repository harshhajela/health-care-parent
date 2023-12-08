package com.hajela.careprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "provider-documents")
public class ProviderDocumentEntity {
    @Id
    private String id;
    private String email;
    private String title;
    private String fileName;
    private Binary image;
    private DocumentStatus status;
    private LocalDateTime dateUploaded;

}
