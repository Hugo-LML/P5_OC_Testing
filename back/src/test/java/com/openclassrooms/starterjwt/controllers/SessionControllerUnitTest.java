package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
public class SessionControllerUnitTest {
  
  @Mock
  private SessionMapper sessionMapper;

  @Mock
  private SessionService sessionService;

  @InjectMocks
  private SessionController sessionController;

  @Test
  public void testFindSessionById() {
    // Arrange
    Long sessionId = 123L;
    Session session = Session.builder()
      .id(sessionId)
      .name("sessionName")
      .date(new Date())
      .description("sessionDescription")
      .teacher(null)
      .users(null)
      .createdAt(null)
      .updatedAt(null)
      .build();
    SessionDto sessionDto = new SessionDto(sessionId, "sessionName", new Date(), null, "sessionDescription", null, null, null);

    when(sessionService.getById(sessionId)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    // Act
    ResponseEntity<?> response = sessionController.findById(sessionId.toString());
    SessionDto responseBody = (SessionDto) response.getBody();

    // Assert
    assertNotNull(responseBody);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(session.getId(), responseBody.getId());
  }

  @Test
  public void testFindSessionByIdBadRequest() {
    // Act
    ResponseEntity<?> response = sessionController.findById("sessionId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testFindSessionByIdNotFound() {
    // Arrange
    Long sessionIdToFind = 123L;
    when(sessionService.getById(sessionIdToFind)).thenReturn(null);

    // Act
    ResponseEntity<?> response = sessionController.findById(sessionIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindAllSessions() {
    // Arrange
    List<Session> expectedSessions = List.of(
      Session.builder()
        .id(123L)
        .name("NewSession")
        .date(new Date())
        .description("My Session Description")
        .teacher(null)
        .users(null)
        .createdAt(null)
        .updatedAt(null)
        .build(),
      Session.builder()
        .id(456L)
        .name("NewSession2")
        .date(new Date())
        .description("My Session Description Two")
        .teacher(null)
        .users(null)
        .createdAt(null)
        .updatedAt(null)
        .build()
    );
    List<SessionDto> sessionsDto = List.of(
      new SessionDto(123L, "NewSession", new Date(), null, "My Session Description", null, null, null),
      new SessionDto(456L, "NewSession2", new Date(), null, "My Session Description Two", null, null, null)
    );

    when(sessionService.findAll()).thenReturn(expectedSessions);
    when(sessionMapper.toDto(expectedSessions)).thenReturn(sessionsDto);

    // Act
    ResponseEntity<?> response = sessionController.findAll();
    List<SessionDto> responseBody = (List<SessionDto>) response.getBody();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
    assertEquals(expectedSessions.size(), sessionsDto.size());
    assertIterableEquals(sessionsDto, responseBody);
  }

  @Test
  public void testCreateSession() {
    // Arrange
    Long sessionId = 123L;
    Session session = Session.builder()
      .id(sessionId)
      .name("sessionName")
      .date(new Date())
      .description("sessionDescription")
      .teacher(null)
      .users(null)
      .createdAt(null)
      .updatedAt(null)
      .build();
    SessionDto sessionDtoInput = new SessionDto(sessionId, "sessionName", new Date(), null, "sessionDescription", null, null, null);
    SessionDto sessionDtoOutput = new SessionDto(sessionId, "sessionName", new Date(), null, "sessionDescription", null, null, null);

    when(sessionMapper.toEntity(sessionDtoInput)).thenReturn(session);
    when(sessionService.create(sessionMapper.toEntity(sessionDtoInput))).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDtoOutput);

    // Act
    ResponseEntity<?> response = sessionController.create(sessionDtoInput);
    SessionDto responseBody = (SessionDto) response.getBody();

    // Assert
    assertNotNull(responseBody);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDtoInput.getId(), responseBody.getId());
  }

  @Test
  public void testUpdateSession() {
    // Arrange
    Long sessionId = 123L;
    Session sessionUpdated = Session.builder()
      .id(sessionId)
      .name("sessionNameUpdated")
      .date(new Date())
      .description("sessionDescription")
      .teacher(null)
      .users(null)
      .createdAt(null)
      .updatedAt(null)
      .build();
    SessionDto sessionDtoInput = new SessionDto(sessionId, "sessionName", new Date(), null, "sessionDescription", null, null, null);
    SessionDto sessionDtoOutput = new SessionDto(sessionId, "sessionNameUpdated", new Date(), null, "sessionDescription", null, null, null);

    when(sessionMapper.toEntity(sessionDtoInput)).thenReturn(sessionUpdated);
    when(sessionService.update(sessionId, sessionMapper.toEntity(sessionDtoInput))).thenReturn(sessionUpdated);
    when(sessionMapper.toDto(sessionUpdated)).thenReturn(sessionDtoOutput);

    // Act
    ResponseEntity<?> response = sessionController.update(sessionId.toString(), sessionDtoInput);
    SessionDto responseBody = (SessionDto) response.getBody();

    // Assert
    assertNotNull(responseBody);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDtoInput.getId(), responseBody.getId());
  }

  @Test
  public void testUpdateSessionBadRequest() {
    // Act
    ResponseEntity<?> response = sessionController.update("sessionId", new SessionDto());

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testDeleteSession() {
    // Arrange
    Long sessionId = 123L;
    Session session = Session.builder()
      .id(sessionId)
      .name("sessionName")
      .date(new Date())
      .description("sessionDescription")
      .teacher(null)
      .users(null)
      .createdAt(null)
      .updatedAt(null)
      .build();
    
    when(sessionService.getById(sessionId)).thenReturn(session);
    doNothing().when(sessionService).delete(sessionId);

    // Act
    ResponseEntity<?> response = sessionController.save(sessionId.toString());

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testDeleteSessionNotFound() {
    // Arrange
    Long sessionId = 123L;
    when(sessionService.getById(sessionId)).thenReturn(null);

    // Act
    ResponseEntity<?> response = sessionController.save(sessionId.toString());

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteSessionBadRequest() {
    // Act
    ResponseEntity<?> response = sessionController.save("sessionId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testParticiapteSession() {
    // Arrange
    Long sessionId = 123L;
    Long userId = 123L;
    doNothing().when(sessionService).participate(sessionId, userId);

    // Act
    ResponseEntity<?> response = sessionController.participate(sessionId.toString(), userId.toString());

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testParticipateSessionBadRequest() {
    // Act
    ResponseEntity<?> response = sessionController.participate("sessionId", "userId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testNoLongerParticiapteSession() {
    // Arrange
    Long sessionId = 123L;
    Long userId = 123L;
    doNothing().when(sessionService).noLongerParticipate(sessionId, userId);

    // Act
    ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testNoLongerParticipateSessionBadRequest() {
    // Act
    ResponseEntity<?> response = sessionController.noLongerParticipate("sessionId", "userId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

}
