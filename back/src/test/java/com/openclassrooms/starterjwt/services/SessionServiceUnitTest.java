package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class SessionServiceUnitTest {
  
  @Mock
  private SessionRepository sessionRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SessionService sessionService;

  private Session session;

  private Teacher teacher;

  private User user;

  @BeforeEach
  public void initSessionAndTeacher() {
    this.user = User.builder()
      .id(123L)
      .email("user123@gmail.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(null)
      .updatedAt(null)
      .build();
    this.teacher = Teacher.builder()
      .id(123L)
      .lastName("Doe")
      .firstName("John")
      .createdAt(null)
      .updatedAt(null)
      .build();
    this.session = Session.builder()
      .id(123L)
      .name("NewSession")
      .date(new Date())
      .description("My Session Description")
      .teacher(teacher)
      .users(new ArrayList<>())
      .createdAt(null)
      .updatedAt(null)
      .build();
  }

  @AfterEach
  public void undefSessionAndTeacher() {
    this.session = null;
    this.teacher = null;
  }

  @Test
  public void testCreateSession() {
    // Arrange
    when(sessionRepository.save(session)).thenReturn(session);

    // Act
    Session sessionCreated = sessionService.create(session);

    // Assert
    assertEquals(sessionCreated, sessionCreated);
    verify(sessionRepository, times(1)).save(session);
  }

  @Test
  public void testDeleteSession() {
    // Act
    sessionService.delete(123L);

    // Assert
    verify(sessionRepository, times(1)).deleteById(123L);;
  }

  @Test
  public void testFindAllSessions() {
    // Arrange
    List<Session> expectedSessions = List.of(
      Session.builder()
        .id(123L)
        .name("NewSession")
        .date(new Date())
        .description("My Session Description")
        .teacher(teacher)
        .users(new ArrayList<>())
        .createdAt(null)
        .updatedAt(null)
        .build(),
      Session.builder()
        .id(456L)
        .name("NewSession2")
        .date(new Date())
        .description("My Session Description Two")
        .teacher(teacher)
        .users(new ArrayList<>())
        .createdAt(null)
        .updatedAt(null)
        .build()
    );
    when(sessionRepository.findAll()).thenReturn(expectedSessions);

    // Act
    List<Session> actualSessions = sessionService.findAll();

    // Assert
    assertNotNull(actualSessions);
    assertEquals(expectedSessions.size(), actualSessions.size());
    assertIterableEquals(expectedSessions, actualSessions);
    verify(sessionRepository, times(1)).findAll();
  }

  @Test
  public void testFindSessionById() {
    // Arrange
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));

    // Act
    Session actualSession = sessionService.getById(123L);

    // Assert
    assertNotNull(actualSession);
    assertSame(session, actualSession);
    verify(sessionRepository, times(1)).findById(123L);
  }

  @Test
  public void testUpdateSession() {
    // Arrange
    Session updatedSession = Session.builder()
      .id(123L)
      .name("NewSessionUpdated")
      .date(new Date())
      .description("My Session Description Updated")
      .teacher(teacher)
      .users(new ArrayList<>())
      .createdAt(null)
      .updatedAt(null)
      .build();
    when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

    // Act
    Session actualSessionUpdated = sessionService.update(123L, updatedSession);

    // Assert
    assertNotNull(actualSessionUpdated);
    assertEquals(session.getId(), actualSessionUpdated.getId());
    verify(sessionRepository, times(1)).save(updatedSession);
  }

  @Test
  public void testParticiapteSession() {
    // Arrange  
    when(userRepository.findById(123L)).thenReturn(Optional.of(user));
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));
    when(sessionRepository.save(session)).thenReturn(session);

    // Act
    sessionService.participate(123L, 123L);

    // Assert
    verify(userRepository, times(1)).findById(123L);
    verify(sessionRepository, times(1)).findById(123L);
    verify(sessionRepository, times(1)).save(session);
    assertTrue(session.getUsers().contains(user));
  }

  @Test
  public void testNoLongerParticipateSession() {
    // Arrange
    session.getUsers().add(user);
  
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));
    when(sessionRepository.save(session)).thenReturn(session);

    // Act
    sessionService.noLongerParticipate(123L, 123L);

    // Assert
    verify(sessionRepository, times(1)).findById(123L);
    verify(sessionRepository, times(1)).save(session);
    assertFalse(session.getUsers().contains(user));
  }

  @Test
  public void testParticipateUndefinedSession() {
    // Arrange  
    when(userRepository.findById(123L)).thenReturn(Optional.of(user));
    when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

    // Assert
    assertThrows(NotFoundException.class, () -> sessionService.participate(999L, 123L));
  }

  @Test
  public void testParticipateUndefinedUser() {
    // Arrange  
    when(userRepository.findById(999L)).thenReturn(Optional.empty());
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));

    // Assert
    assertThrows(NotFoundException.class, () -> sessionService.participate(123L, 999L));
  }

  @Test
  public void testUserAlreadyParticipateSession() {
    // Arrange  
    session.getUsers().add(user);
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));
    when(userRepository.findById(123L)).thenReturn(Optional.of(user));

    // Assert
    assertThrows(BadRequestException.class, () -> sessionService.participate(123L, 123L));
  }

  @Test
  public void testUserNotAlreadyParticipateSession() {
    // Arrange  
    when(sessionRepository.findById(123L)).thenReturn(Optional.of(session));

    // Assert
    assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(123L, 123L));
  }

}
