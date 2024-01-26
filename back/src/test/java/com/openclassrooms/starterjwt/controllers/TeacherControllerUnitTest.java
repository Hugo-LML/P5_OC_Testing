package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
public class TeacherControllerUnitTest {
  
  @Mock
  private TeacherMapper teacherMapper;

  @Mock
  private TeacherService teacherService;

  @InjectMocks
  private TeacherController teacherController;

  @Test
  public void testFindTeacherByIdFound() {
    // Arrange
    Long teacherIdToFind = 123L;
    Teacher expectedTeacher = 
      Teacher.builder()
        .id(123L)
        .lastName("Doe")
        .firstName("John")
        .createdAt(null)
        .updatedAt(null)
        .build();
    TeacherDto teacherDto = new TeacherDto(teacherIdToFind, "Doe", "John", null, null);
    
    when(teacherService.findById(teacherIdToFind)).thenReturn(expectedTeacher);
    when(teacherMapper.toDto(expectedTeacher)).thenReturn(teacherDto);

    // Act
    ResponseEntity<?> response = teacherController.findById(teacherIdToFind.toString());
    TeacherDto responseBody = (TeacherDto) response.getBody();

    // Assert
    assertNotNull(responseBody);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedTeacher.getId(), responseBody.getId());
    assertEquals(expectedTeacher.getLastName(), responseBody.getLastName());
    assertEquals(expectedTeacher.getFirstName(), responseBody.getFirstName());
    assertEquals(expectedTeacher.getCreatedAt(), responseBody.getCreatedAt());
    assertEquals(expectedTeacher.getUpdatedAt(), responseBody.getUpdatedAt());
  }

  @Test
  public void testFindTeacherByIdNotFound() {
    // Arrange
    Long teacherIdToFind = 123L;
    when(teacherService.findById(teacherIdToFind)).thenReturn(null);

    // Act
    ResponseEntity<?> response = teacherController.findById(teacherIdToFind.toString());

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testFindTeacherByIdBadRequest() {
    // Act
    ResponseEntity<?> response = teacherController.findById("teacherId");

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindAllTeachersFound() {
    // Arrange
    List<Teacher> expectedTeachers = List.of(
      Teacher.builder()
        .id(123L)
        .lastName("Doe")
        .firstName("John")
        .createdAt(null)
        .updatedAt(null)
        .build(),
      Teacher.builder()
        .id(456L)
        .lastName("Doey")
        .firstName("Johnny")
        .createdAt(null)
        .updatedAt(null)
        .build()
    );
    List<TeacherDto> teachersDto = List.of(
      new TeacherDto(123L, "Doe", "John", null, null),
      new TeacherDto(456L, "Doey", "Johnny", null, null)
    );

    when(teacherService.findAll()).thenReturn(expectedTeachers);
    when(teacherMapper.toDto(expectedTeachers)).thenReturn(teachersDto);

    // Act
    ResponseEntity<?> response = teacherController.findAll();
    List<TeacherDto> responseBody = (List<TeacherDto>) response.getBody();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
    assertEquals(expectedTeachers.size(), teachersDto.size());
    assertIterableEquals(teachersDto, responseBody);
  }

}
