package com.mateuslll.dev.shortner.repository;

import com.mateuslll.dev.shortner.entities.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
}