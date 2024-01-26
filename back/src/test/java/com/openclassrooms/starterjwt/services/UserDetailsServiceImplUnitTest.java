package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
public class UserDetailsServiceImplUnitTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Test
	public void testLoadUserByUsername() {
		// Arrange
		User user = User.builder()
      .id(123L)
      .email("user123@gmail.com")
      .lastName("Doe")
      .firstName("John")
      .password("test123")
      .admin(false)
      .createdAt(null)
      .updatedAt(null)
      .build();

		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		// Act
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());

		// Assert
		assertEquals(user.getEmail(), userDetails.getUsername());
		assertEquals(user.getPassword(), userDetails.getPassword());
	}

	@Test
	public void testLoadByUsernameNotFound() {
		// Arrange
		String email = "nobody@gmail.com";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
		
		// Assert
		assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(email));
	}
}
