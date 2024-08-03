package com.abdul.postdb.controllers;

import com.abdul.postdb.domain.dto.AuthorDto;
import com.abdul.postdb.domain.entities.AuthorEntity;
import com.abdul.postdb.mappers.Mapper;
import com.abdul.postdb.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;


    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }


    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        // convert AuthorDto into AuthorEntity to match the expected arg of the service
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        // convert saved author entity into dto and return it with Http201 Created
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity),
                HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        // get a list of author entities from the service
        List<AuthorEntity> authorEntities = authorService.findAll();
        // convert the list of author entities into list of author Dto using Stream
        return authorEntities.stream()
                .map(authorMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("id") Long id) {
        // get optional founded author
        Optional<AuthorEntity> foundAuthor = authorService.findOne(id);
        // if the author found, then map it to author dto and returns a response entity
        // else return a response entity of NOT FOUND
        return foundAuthor.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id,
                                                      @RequestBody AuthorDto authorDto) {
        // check if the author exists
        if(authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // save the author
        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedUpdatedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(
                authorMapper.mapTo(savedUpdatedAuthorEntity),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable("id") Long id,
                                    @RequestBody AuthorDto authorDto) {
        // check if the author exists
        if(authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // save the author
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedUpdatedAuthorEntity = authorService.partialUpdate(id, authorEntity);

        return new ResponseEntity<>(authorMapper.mapTo(savedUpdatedAuthorEntity),
                HttpStatus.OK);

    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable("id") Long id) {
        // always return Http 204
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}