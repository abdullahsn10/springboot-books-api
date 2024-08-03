package com.abdul.postdb.controllers;

import com.abdul.postdb.TestDataUtil;
import com.abdul.postdb.domain.entities.BookEntity;
import com.abdul.postdb.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private BookService bookService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreatingBookSuccessfullyReturnsHttp201Created() throws Exception {
        // create a book and convert it to json
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                // request builder, PUT in our case
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                // content type
                        .contentType(MediaType.APPLICATION_JSON)
                // request body content
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatFullUpdateBookSuccessfullyReturnsHttp200() throws Exception {
        // create a book
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookA = bookService.createUpdateBook(testBookA.getIsbn(), testBookA);


        // update some book's info and convert to JSON
        String isbn = savedBookA.getIsbn();

        savedBookA.setTitle("Mastering Java");
        savedBookA.setIsbn("bla-bla-bla");

        String bookJson = objectMapper.writeValueAsString(savedBookA);

        mockMvc.perform(
                // request builder, PUT in our case
                MockMvcRequestBuilders.put("/books/"+isbn)
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body content
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }



    @Test
    public void testThatCreatingBookSuccessfullyReturnsSavedBook() throws Exception {
        // create a book and convert it to json
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                // request builder, PUT in our case
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body content
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatFullUpdateBookUpdatesTheBook() throws Exception {
        // create a book
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedBookA = bookService.createUpdateBook(testBookA.getIsbn(), testBookA);


        // update some book's info and convert to JSON
        String isbn = savedBookA.getIsbn();

        savedBookA.setTitle("Mastering Java");
        savedBookA.setIsbn("bla-bla-bla");

        String bookJson = objectMapper.writeValueAsString(savedBookA);

        mockMvc.perform(
                // request builder, PUT in our case
                MockMvcRequestBuilders.put("/books/"+isbn)
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body content
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(isbn)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(savedBookA.getTitle())
        );
    }





    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                // request builders, get in this case
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

//    @Test
//    public void testThatListBooksSuccessfullyReturnsListOfBooks() throws Exception {
//        // create a list of books in the database
//        BookEntity testBookA = TestDataUtil.createTestBookA(null);
//        bookService.createUpdateBook(testBookA.getIsbn(), testBookA);
//        BookEntity testBookB = TestDataUtil.createTestBookB(null);
//        bookService.createUpdateBook(testBookB.getIsbn(), testBookB);
//
//        // request and assertions
//        mockMvc.perform(
//                // request builders, get in this case
//                MockMvcRequestBuilders.get("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookA.getIsbn())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].title").value(testBookA.getTitle())
//        );
//    }

    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExists() throws Exception{
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookA.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookDoesNotExist() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/222")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookReturnsTheBookWhenBookExists() throws Exception{
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookA.getIsbn(), testBookA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBookA.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus200WhenBookExists() throws Exception{
        // create a test book and saved it
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedTestBook = bookService.createUpdateBook(testBookA.getIsbn(), testBookA);

        // update fields
        BookEntity updatedBook = BookEntity.builder()
                .isbn(null)
                .title("Updated Title")
                .authorEntity(null)
                .build();

        String bookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+savedTestBook.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateUpdatesTheBook() throws Exception{
        // create a test book and saved it
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        BookEntity savedTestBook = bookService.createUpdateBook(testBookA.getIsbn(), testBookA);

        // update fields
        BookEntity updatedBook = BookEntity.builder()
                .isbn(null)
                .title("Updated Title")
                .authorEntity(null)
                .build();

        String bookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+savedTestBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(updatedBook.getTitle())
        );
    }

    @Test
    public void testThatDeleteBookAlwaysReturnsHttpStatus204() throws Exception{
        // create a test book and save it
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

    }

    @Test
    public void testThatDeleteBookDeletesTheBook() throws Exception{
        // create a test book and save it
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.createUpdateBook(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        // verify that the book deleted
        Optional<BookEntity> result = bookService.findOne(testBookA.getIsbn());
        assertThat(result).isNotPresent();
    }


}
