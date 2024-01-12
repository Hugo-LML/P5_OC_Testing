package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserServiceTest {
  
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  public UserServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testDeleteUser() {
    // Arrange
    Long userIdToDelete = 123L;
    when(userRepository.existsById(userIdToDelete)).thenReturn(true);

    // Act
    userService.delete(userIdToDelete);

    // Assert
    verify(userRepository, times(1)).deleteById(userIdToDelete);
  }

  @Test
  public void testFindUserById() {
    // Arrange
    Long userIdToFind = 123L;
    User expectedUser = User.builder()
      .id(123L)
      .email("user123@example.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();
    when(userRepository.findById(userIdToFind)).thenReturn(Optional.of(expectedUser));

    // Act
    User actualUser = userService.findById(userIdToFind);

    // Assert
    assertNotNull(actualUser);
    assertSame(expectedUser, actualUser);
  }

  @Test
  public void testFindUserByIdNotFound() {
    // Arrange
    Long userIdToFind = 123L;
    when(userRepository.findById(userIdToFind)).thenReturn(Optional.empty());

    // Act
    User userNotFound = userService.findById(userIdToFind);

    // Assert
    assertNull(userNotFound);
  }

}
