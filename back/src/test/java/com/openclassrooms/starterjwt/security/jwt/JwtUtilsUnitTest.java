package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
public class JwtUtilsUnitTest {

  @Autowired
  private JwtUtils jwtUtils;
  
  @Test
  public void testGenerateJwtTokenNotNull() {
    // Arrange
    UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder()
      .id(123L)
      .username("user123@gmail.com")
      .firstName("John")
      .lastName("Doe")
      .admin(false)
      .password("test123")
      .build();

    // Act
    String token = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetailsImpl, null));

    // Assert
    assertNotNull(token);
  }

  @Test
  public void testGenerateJwtTokenValid() {
    // Arrange
    UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder()
      .id(123L)
      .username("user123@gmail.com")
      .firstName("John")
      .lastName("Doe")
      .admin(false)
      .password("test123")
      .build();

    // Act
    String token = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetailsImpl, null));

    // Assert
    assertTrue(jwtUtils.validateJwtToken(token));
  }

  @Test
  public void testGetUserNameFromJwtToken() {
    // Arrange
    UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder()
      .id(123L)
      .username("user123@gmail.com")
      .firstName("John")
      .lastName("Doe")
      .admin(false)
      .password("test123")
      .build();
    String expectedUsername = "user123@gmail.com";

    // Act
    String token = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetailsImpl, null));

    // Assert
    String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
    assertEquals(expectedUsername, extractedUsername);
  }

  @Test
  public void testValidateJwtTokenSignatureException() {
    // Arrange
    String token = Jwts.builder()
      .setSubject(("user123@gmail.com"))
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + 1000 * 60 * 24))
      .signWith(SignatureAlgorithm.HS512, "falsySecretKey")
      .compact();

    // Assert
    assertFalse(jwtUtils.validateJwtToken(token));
  }
  
  @Test
  public void testValidateJwtTokenMalformedJwtException() {
    // Assert
    assertFalse(jwtUtils.validateJwtToken("incorrectToken"));
  }
  
  @Test
  public void testValidateJwtTokenExpiredJwtException() {
    // Arrange
    String token = Jwts.builder()
      .setSubject(("user123@gmail.com"))
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() - 1000))
      .signWith(SignatureAlgorithm.HS512, "openclassrooms")
      .compact();

    // Assert
    assertFalse(jwtUtils.validateJwtToken(token));
  }
  
  @Test
  public void testValidateJwtTokenIllegalArgumentException() {
    // Assert
    assertFalse(jwtUtils.validateJwtToken(""));
  }

}
