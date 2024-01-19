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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TeacherControllerIT {

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
  public void testFindTeacherById() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1")
      .header("Authorization", token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  public void testFindTeacherByIdNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/0")
      .header("Authorization", token))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testFindTeacherByIdBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/myTeacher")
      .header("Authorization", token))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFindAllTeachers() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
      .header("Authorization", token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$[0].id").value(1L))
      .andExpect(jsonPath("$[1].id").value(2L));
  }
  
}
