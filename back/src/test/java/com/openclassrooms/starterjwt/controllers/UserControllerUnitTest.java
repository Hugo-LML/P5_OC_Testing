package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
public class UserControllerUnitTest {
  
  @Mock
  private UserMapper userMapper;

  @Mock
  private UserService userService;

  @Mock
  private SecurityContext securityContext;

  @InjectMocks
  private UserController userController;

  @Test
  public void testFindUserByIdFound() {
    // Arrange
    Long userIdToFind = 123L;
    User expectedUser = 
      User.builder()
      .id(123L)
      .email("user123@gmail.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(LocalDateTime.parse("2024-01-18T00:00:00"))
      .updatedAt(LocalDateTime.parse("2024-01-18T00:00:00"))
      .build();
    UserDto userDto = new UserDto(userIdToFind, "user123@gmail.com", "Doe", "John", false, "test123", LocalDateTime.parse("2024-01-18T00:00:00"), LocalDateTime.parse("2024-01-18T00:00:00"));
    
    when(userService.findById(userIdToFind)).thenReturn(expectedUser);
    when(userMapper.toDto(expectedUser)).thenReturn(userDto);

    // Act
    ResponseEntity<?> response = userController.findById(userIdToFind.toString());
    UserDto responseBody = (UserDto) response.getBody();

    // Assert
    assertNotNull(responseBody);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedUser.getId(), responseBody.getId());
    assertEquals(expectedUser.getEmail(), responseBody.getEmail());
    assertEquals(expectedUser.getLastName(), responseBody.getLastName());
    assertEquals(expectedUser.getFirstName(), responseBody.getFirstName());
    assertEquals(expectedUser.getPassword(), responseBody.getPassword());
    assertEquals(expectedUser.getCreatedAt(), responseBody.getCreatedAt());
    assertEquals(expectedUser.getUpdatedAt(), responseBody.getUpdatedAt());
  }

  @Test
  public void testFindUserByIdNotFound() {
    // Arrange
    Long userIdToFind = 123L;
    when(userService.findById(userIdToFind)).thenReturn(null);

    // Act
    ResponseEntity<?> response = userController.findById(userIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteUser() {
    // Arrange
    Long userIdToFind = 123L;
    User expectedUser = 
      User.builder()
      .id(123L)
      .email("user123@gmail.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(null)
      .updatedAt(null)
      .build();

    UserDetailsImpl userDetailsImpl = new UserDetailsImpl(123L, "user123@gmail.com", "John", "Doe", false, "test123");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userService.findById(userIdToFind)).thenReturn(expectedUser);
    doNothing().when(userService).delete(userIdToFind);

    // Act
    ResponseEntity<?> response = userController.save(userIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteUserNotFound() {
    // Arrange
    Long userIdToFind = 123L;
    when(userService.findById(userIdToFind)).thenReturn(null);

    // Act
    ResponseEntity<?> response = userController.save(userIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testFindUserByIdBadRequest() {
    // Act
    ResponseEntity<?> response = userController.findById("userId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testDeleteUserUnauthorized() {
    // Arrange
    Long userIdToFind = 123L;
    User expectedUser = 
      User.builder()
      .id(123L)
      .email("user123@gmail.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(null)
      .updatedAt(null)
      .build();

    UserDetailsImpl userDetailsImpl = new UserDetailsImpl(123L, "user456@gmail.com", "John", "Doe", false, "test123");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userService.findById(userIdToFind)).thenReturn(expectedUser);

    // Act
    ResponseEntity<?> response = userController.save(userIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void testDeleteUserBadRequest() {
    // Act
    ResponseEntity<?> response = userController.save("userId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
