package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class UserServiceUnitTest {
  
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  public UserServiceUnitTest() {
    MockitoAnnotations.openMocks(this);
  }

  // @Test
  // public void testDeleteUser() {
  //   // Arrange
  //   Long userIdToDelete = 123L;
  //   when(userRepository.existsById(userIdToDelete)).thenReturn(true);

  //   // Act
  //   userService.delete(userIdToDelete);

  //   // Assert
  //   verify(userRepository, times(1)).deleteById(userIdToDelete);
  // }

  // @Test
  // public void testFindUserById() {
  //   // Arrange
  //   Long userIdToFind = 123L;
  //   User expectedUser = User.builder()
  //     .id(123L)
  //     .email("user123@gmail.com")
  //     .lastName("Doe")
  //     .firstName("John")
  //     .password("test123")
  //     .admin(false)
  //     .createdAt(null)
  //     .updatedAt(null)
  //     .build();
  //   when(userRepository.findById(userIdToFind)).thenReturn(Optional.of(expectedUser));

  //   // Act
  //   User actualUser = userService.findById(userIdToFind);

  //   // Assert
  //   assertNotNull(actualUser);
  //   assertSame(expectedUser, actualUser);
  //   assertEquals(userIdToFind, actualUser.getId());
  // }

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
