package com.hajela.careprovider.repository;

import com.hajela.careprovider.entity.ProviderDocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderDocumentRepo extends MongoRepository<ProviderDocumentEntity, String> {

    List<ProviderDocumentEntity> findAllByEmail(String email);

    List<ProviderDocumentEntity> findByTitleAndEmailIn(String title, List<String> emails);
}
