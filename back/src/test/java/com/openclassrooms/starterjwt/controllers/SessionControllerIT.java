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
public class SessionControllerIT {
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
  public void testFindSessionById() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
      .header("Authorization", token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1L))
      .andExpect(jsonPath("$.name").value("NewSession"));
  }

  @Test
  public void testFindSessionByIdNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/0")
      .header("Authorization", token))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testFindSessionByIdBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/session")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFindAllSessions() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
      .header("Authorization", token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].id").value(1L))
      .andExpect(jsonPath("$[1].id").value(2L));
  }

  @Test
  public void testCreateSession() throws Exception {
    String requestBodyCreate = "{" + "\"name\": \"sessionTest\"," + "\"description\": \"descriptionTest\"" + "\"teacher_id\": 1," + "\"date\": \"2024-01-19 00:00:00\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
      .header("Authorization", token)
      .content(requestBodyCreate))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("sessionTest"));
  }

  @Test
  public void testUpdateSession() throws Exception {
    String requestBodyUpdate = "{" + "\"name\": \"sessionTestUpdated\"," + "\"description\": \"descriptionTest\"" + "\"teacher_id\": 1," + "\"date\": \"2024-01-19 00:00:00\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
      .header("Authorization", token)
      .content(requestBodyUpdate))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("sessionTestUpdated"));
  }

  @Test
  public void testUpdateSessionBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/api/session/session")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteSession() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/2")
      .header("Authorization", token))
      .andExpect(status().isOk());
  }

  @Test
  public void testDeleteSessionNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/0")
      .header("Authorization", token))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteSessionBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/session")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testParticiapteSession() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
      .header("Authorization", token))
      .andExpect(status().isOk());
  }

  @Test
  public void testNoLongerParticiapteSession() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/2")
      .header("Authorization", token))
      .andExpect(status().isOk());
  }

  @Test
  public void testParticiapteSessionBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/session/sessionId/participate/userId")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testNoLongerParticiapteSessionBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/sessionId/participate/userId")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }
}
