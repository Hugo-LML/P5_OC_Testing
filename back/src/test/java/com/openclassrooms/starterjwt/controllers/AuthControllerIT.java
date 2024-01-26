package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.notNullValue;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthControllerIT {
  
  @Autowired
  MockMvc mockMvc;

  @Test
  public void testAuthenticateUser() throws Exception {
    String requestBodyLogin = "{" + "\"email\": \"yoga@studio.com\"," + "\"password\": \"$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyLogin))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(notNullValue()));
  }

  @Test
  public void testRegisterUser() throws Exception {
    String requestBodyRegister = "{" + "\"email\": \"testRegister@gmail.com\"," + "\"lastName\": \"Doey\"," + "\"firstName\": \"Johnny\"," + "\"password\": \"testRegister\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyRegister))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(notNullValue()));
  }

  @Test
  public void testAuthenticateUserIncorrectPassword() throws Exception {
    String requestBodyLogin = "{" + "\"email\": \"yoga@studio.com\"," + "\"password\": \"incorrectPassword\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyLogin))
      .andExpect(status().isUnauthorized());
  }

  @Test
  public void testRegisterUserEmailAlreadyTaken() throws Exception {
    String requestBodyRegister = "{" + "\"email\": \"yoga@studio.com\"," + "\"lastName\": \"Admin\"," + "\"firstName\": \"Admin\"," + "\"password\": \"$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq\"" + "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBodyRegister))
      .andExpect(status().isBadRequest());
  }

}
