package com.abdul.postdb;

import com.abdul.postdb.domain.entities.AuthorEntity;
import com.abdul.postdb.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil() {}


    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abdullah")
                .age(21)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Ahmad")
                .age(42)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Wasim")
                .age(32)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("1633-2906-15")
                .title("Java Spring")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("1611-2905-14")
                .title("Python Django")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("2223-1423-60")
                .title("JavaScript")
                .authorEntity(authorEntity)
                .build();
    }
}
