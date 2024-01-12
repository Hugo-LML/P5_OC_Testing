package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

public class TeacherServiceTest {
  
  @Mock
  private TeacherRepository teacherRepository;

  @InjectMocks
  private TeacherService teacherService;

  @Test
  public void testFindAllTeachers() {
    // Arrange
    List<Teacher> expectedTeachers = List.of(
      Teacher.builder()
        .id(123L)
        .lastName("Doe")
        .firstName("John")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build(),
      Teacher.builder()
        .id(456L)
        .lastName("Doey")
        .firstName("Johnny")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build()
    );
    when(teacherRepository.findAll()).thenReturn(expectedTeachers);

    // Act
    List<Teacher> actualTeachers = teacherService.findAll();

    // Assert
    assertNotNull(actualTeachers);
    assertEquals(expectedTeachers.size(), actualTeachers.size());
    assertIterableEquals(expectedTeachers, actualTeachers);
  }

  @Test
  public void testFindTeacherById() {
    // Arrange
    Long teacherIdToFind = 123L;
    Teacher expectedTeacher = 
      Teacher.builder()
        .id(123L)
        .lastName("Doe")
        .firstName("John")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
    when(teacherRepository.findById(teacherIdToFind)).thenReturn(Optional.of(expectedTeacher));

    // Act
    Teacher actualTeacher = teacherService.findById(teacherIdToFind);

    // Assert
    assertNotNull(actualTeacher);
    assertSame(expectedTeacher, actualTeacher);
  }

}
