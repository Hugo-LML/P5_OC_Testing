package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
public class UserDetailsServiceImplUnitTest {
  
  @Test
  public void testUserDetailsImpl() {
    // Arrange
    Long expectedId = 123L;
    String expectedUsername = "user123@gmail.com";
    String expectedFirstName = "John";
    String expectedLastName = "Doe";
    Boolean expectedAdmin = false;
    String expectedPassword = "test123";

    UserDetailsImpl userDetails = UserDetailsImpl.builder()
      .id(expectedId)
      .username(expectedUsername)
      .firstName(expectedFirstName)
      .lastName(expectedLastName)
      .admin(expectedAdmin)
      .password(expectedPassword)
      .build();
    
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    UserDetailsImpl identicalUserDetails = UserDetailsImpl.builder().id(expectedId).build();
    UserDetailsImpl differentUserDetails = UserDetailsImpl.builder().id(456L).build();

    // Assert
    assertEquals(expectedId, userDetails.getId());
    assertEquals(expectedUsername, userDetails.getUsername());
    assertEquals(expectedFirstName, userDetails.getFirstName());
    assertEquals(expectedLastName, userDetails.getLastName());
    assertEquals(expectedAdmin, userDetails.getAdmin());
    assertEquals(expectedPassword, userDetails.getPassword());

    assertTrue(authorities.isEmpty());

    assertTrue(userDetails.isAccountNonExpired());
    assertTrue(userDetails.isAccountNonLocked());
    assertTrue(userDetails.isCredentialsNonExpired());
    assertTrue(userDetails.isEnabled());

    assertEquals(userDetails, identicalUserDetails);
    assertNotEquals(userDetails, differentUserDetails);
  }

}
