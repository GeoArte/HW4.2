package com.skypro.springintegrationtesting.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTests {

  @Autowired
  MockMvc mockMvc;

  @Test
  void givenNoUsersInDatabase_whenGetUsers_thenEmptyJsonArray() throws Exception {
    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void givenNoUsersInDatabase_whenUserAdded_thenItExistsInList() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "test_name");

    mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObject.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("test_name"));

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("test_name"));
  }

  @Test
  void givenThereIsOneUserCreated_whenUserEdited_thenItChangedInDatabase() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "test_name");

    String createdUserString = mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObject.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("test_name"))
        .andReturn().getResponse().getContentAsString();
    JSONObject createdUser = new JSONObject(createdUserString);

    long id = createdUser.getLong("id");

    createdUser.put("name", "test_user2");
    mockMvc.perform(put("/user/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(createdUser.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("test_user2"));

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("test_user2"));
  }

  @Test
  void givenThereIsOneUserCreated_whenUserDeleted_thenUserListIsEmpty() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "test_name");

    String createdUserString = mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObject.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("test_name"))
        .andReturn().getResponse().getContentAsString();
    JSONObject createdUser = new JSONObject(createdUserString);
    long id = createdUser.getLong("id");

    mockMvc.perform(delete("/user/{id}", id))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void givenNoUsersInDatabase_whenDeleteOnEmptyList_thenNotFound() throws Exception {
    mockMvc.perform(delete("/user/10"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenNoUsersInDatabase_whenEditOnEmptyList_thenNotFound() throws Exception {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "test_name");
    mockMvc.perform(put("/user/10")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObject.toString()))
        .andExpect(status().isNotFound());
  }
}
