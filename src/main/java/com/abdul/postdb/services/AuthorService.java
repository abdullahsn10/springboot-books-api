package com.abdul.postdb.services;

import com.abdul.postdb.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;


public interface AuthorService {
    AuthorEntity save(AuthorEntity authorEntity);


    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean isExists(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void delete(Long id);
}
