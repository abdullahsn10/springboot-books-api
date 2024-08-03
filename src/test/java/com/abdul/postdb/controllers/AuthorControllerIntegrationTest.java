package com.abdul.postdb.controllers;

import com.abdul.postdb.TestDataUtil;
import com.abdul.postdb.domain.entities.AuthorEntity;
import com.abdul.postdb.services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
public class AuthorControllerIntegrationTest {

    private AuthorService authorService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201CreatedWhenTheUserIsAuthorized() throws Exception {

        // create test author and convert it to JSON using Jackson Object Mapper
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        // use Mock MVC and assertions
        mockMvc.perform(
                // request builder, POST in our case
                MockMvcRequestBuilders.post("/authors")
                // content type
                        .contentType(MediaType.APPLICATION_JSON)
                // request body
                        .content(authorJson)
                // add basic auth
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user2", "password"))

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }


    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {

        // create test author and convert it to JSON using Jackson Object Mapper
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        // use Mock MVC and assertions
        mockMvc.perform(
                // request builder, POST in our case
                MockMvcRequestBuilders.post("/authors")
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body
                        .content(authorJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abdullah")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("21")
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                // request builder, get in this case
                MockMvcRequestBuilders.get("/authors")
                        // content type which is JSON here
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        // create a list of authors in the db
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.save(testAuthorA);
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);
        authorService.save(testAuthorB);
        AuthorEntity testAuthorC = TestDataUtil.createTestAuthorC();
        testAuthorC.setId(null);
        authorService.save(testAuthorC);

        // request and assertions
        mockMvc.perform(
                // request builder, get in this case
                MockMvcRequestBuilders.get("/authors")
                        // content type which is JSON here
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abdullah")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value("21")
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + testAuthorA.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/22")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsTheAuthorWhenAuthorExists() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abdullah")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("21")
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {
        // create an author
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity createdTestAuthorA = authorService.save(testAuthorA);

        // update some authors info
        createdTestAuthorA.setName("Abdullah SN");

        // create JSON from this author
        String authorJson = objectMapper.writeValueAsString(createdTestAuthorA);

        mockMvc.perform(
                // request builder, put in this case
                MockMvcRequestBuilders.put("/authors/" + createdTestAuthorA.getId())
                // content type
                .contentType(MediaType.APPLICATION_JSON)
                // request body content which is the author in this case
                .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );


    }


    @Test
    public void testThatFullUpdateAuthorReturnsHttpStatus404WhenAuthorDoeNotExists() throws Exception {
        // create an author
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity createdTestAuthorA = authorService.save(testAuthorA);

        // update some authors info
        createdTestAuthorA.setName("Abdullah SN");

        // create JSON from this author
        String authorJson = objectMapper.writeValueAsString(createdTestAuthorA);

        mockMvc.perform(
                // request builder, put in this case
                MockMvcRequestBuilders.put("/authors/99")
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body content which is the author in this case
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        // create an author
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity createdTestAuthorA = authorService.save(testAuthorA);

        // update some authors info
        createdTestAuthorA.setName("Abdullah SN");
        createdTestAuthorA.setAge(30);

        // create JSON from this author
        String authorJson = objectMapper.writeValueAsString(createdTestAuthorA);

        mockMvc.perform(
                // request builder, put in this case
                MockMvcRequestBuilders.put("/authors/"+ createdTestAuthorA.getId())
                        // content type
                        .contentType(MediaType.APPLICATION_JSON)
                        // request body content which is the author in this case
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(createdTestAuthorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(createdTestAuthorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(createdTestAuthorA.getAge())
        );

    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus200WhenAuthorExists() throws Exception {
        // create a test author and save it
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity savedTestAuthor = authorService.save(testAuthorA);

        // save the id
        Long id = savedTestAuthor.getId();

        // update some info of the saved Test Author
        AuthorEntity updatedAuthor = AuthorEntity.builder()
                .id(null)
                .name("Abdullah SN")
                .age(null)
                .build();
        String authorJson = objectMapper.writeValueAsString(updatedAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateUpdatesFields() throws Exception {
        // create a test author and save it
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        AuthorEntity savedTestAuthor = authorService.save(testAuthorA);

        // save the id
        Long id = savedTestAuthor.getId();

        // update some info of the saved Test Author
        AuthorEntity updatedAuthor = AuthorEntity.builder()
                .id(null)
                .name("Abdullah SN")
                .age(null)
                .build();
        String authorJson = objectMapper.writeValueAsString(updatedAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTestAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedTestAuthor.getAge())
        );

    }

    @Test
    public void testThatDeleteAuthorAlwaysReturnsHttpStatus204() throws Exception{
        // create a test author and save it
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorDeletesTheAuthor() throws Exception{
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/"+testAuthorA.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent()
        );

        // verify that the author is already deleted
        Optional<AuthorEntity> result = authorService.findOne(testAuthorA.getId());
        assertThat(result).isNotPresent();
    }





}
