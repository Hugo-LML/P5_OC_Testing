package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerIT {

  @Autowired
  MockMvc mockMvc;

  String token;

  @BeforeAll
  public void getToken() throws Exception {
    String requestBodyLogin = "{" + "\"email\": \"yoga@studio.com\"," + "\"password\": \"$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq\"" + "}";

    MvcResult resultLogin = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyLogin))
      .andReturn();
    
    token = "Bearer " + JsonPath.read(resultLogin.getResponse().getContentAsString(), "$.token");
  }

  @Test
  public void testFindUserById() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
      .header("Authorization", token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1L))
      .andExpect(jsonPath("$.lastName").value("Admin"));
  }

  @Test
  public void testFindUserByIdNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/0")
      .header("Authorization", token))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testFindUserByIdBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/user")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFindUserByIdUnauthorized() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
      .header("Authorization", "incorrectToken"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  public void testDeleteUser() throws Exception {
    String requestBodyLogin = "{" + "\"email\": \"test@gmail.com\"," + "\"password\": \"$2a$10$kpC.ncOduhLmmcrvwUHBxe1vU67vuykJMGYgk/hM76cmsjUSNa7jK\"" + "}";

    MvcResult resultLogin = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyLogin))
      .andReturn();
    
    token = "Bearer " + JsonPath.read(resultLogin.getResponse().getContentAsString(), "$.token");

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
      .header("Authorization", token))
      .andExpect(status().isOk());
  }

  @Test
  public void testDeleteUserNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/0")
      .header("Authorization", token))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteUserBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/user")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

}
