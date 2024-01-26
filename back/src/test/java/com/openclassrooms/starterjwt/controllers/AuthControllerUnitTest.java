package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtUtils jwtUtils;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private AuthController authController;
  
  @Test
  public void testAuthenticateUserSuccessfully() {
    // Arrange
    String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBnbWFpbC5jb20iLCJpYXQiOjE3MDE0NDg5MzcsImV4cCI6MTcwMTQ1MDM3N30.E1var5xurPYCFHVNa--BxqQ2i_9n_a4E7FLrPPBUtE4";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("user123@gmail.com");
    loginRequest.setPassword("test123");

    UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder()
      .id(123L)
      .username("user123@gmail.com")
      .firstName("John")
      .lastName("Doe")
      .admin(false)
      .password("test123")
      .build();

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);

    when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()))).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);
    when(userRepository.findByEmail(userDetailsImpl.getUsername())).thenReturn(Optional.of(new User(123L, "user123@gmail.com", "Doe", "John", "test123", false, null, null)));

    // Act
    ResponseEntity<?> response = authController.authenticateUser(loginRequest);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());

    JwtResponse jwtResponse = (JwtResponse) response.getBody();
    assertNotNull(jwtResponse);
    assertEquals(jwt, jwtResponse.getToken());
    assertEquals(123L, jwtResponse.getId());
    assertEquals("user123@gmail.com", jwtResponse.getUsername());
    assertEquals("John", jwtResponse.getFirstName());
    assertEquals("Doe", jwtResponse.getLastName());
    assertEquals(false, jwtResponse.getAdmin());
  }

  @Test
  public void testRegisterUserSuccessfully() {
    // Arrange
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("user123@gmail.com");
    signupRequest.setPassword("test123");
    signupRequest.setLastName("Doe");
    signupRequest.setFirstName("John");

    when(userRepository.existsByEmail("user123@gmail.com")).thenReturn(false);
    when(passwordEncoder.encode("test123")).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(new User());

    // Act
    ResponseEntity<?> response = authController.registerUser(signupRequest);
    MessageResponse responseBody = (MessageResponse) response.getBody();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
  }

  @Test
  public void testRegisterUserEmailAlreadyTaken() {
    // Arrange
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("user123@gmail.com");

    when(userRepository.existsByEmail("user123@gmail.com")).thenReturn(true);

    // Act
    ResponseEntity<?> response = authController.registerUser(signupRequest);
    MessageResponse responseBody = (MessageResponse) response.getBody();

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(responseBody);
  }

}
