package com.hajela.profileservice.controller;

import com.hajela.profileservice.dto.DocumentInfoDto;
import com.hajela.profileservice.dto.ResponseMessage;
import com.hajela.profileservice.service.ProviderDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/provider/documents")
@RequiredArgsConstructor
@Tag(name = "Provider Documents", description = "Healthcare provider document management operations")
@SecurityRequirement(name = "bearerAuth")
public class ProviderDocumentsController {

    private final ProviderDocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentInfoDto>> getAllDocuments(@RequestHeader(name = "Authorization") String authorizationHeader) {
        var documentInfoList = documentService.getAllDocumentsForProvider(authorizationHeader).stream()
                        .map(DocumentInfoDto::fromEntity)
                        .toList();
        return ResponseEntity.status(HttpStatus.OK).body(documentInfoList);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file,
                                                      @RequestHeader(name = "Authorization") String authorizationHeader,
                                                      @RequestParam("title") String title) {
        String message = "";
        try {
            documentService.saveDocument(authorizationHeader, file, title);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
