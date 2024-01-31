package com.example.auctionservicesaplication.service;


import com.example.auctionservicesaplication.message.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.example.auctionservicesaplication.model.Role;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.RoleRepository;
import com.example.auctionservicesaplication.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    // Test 1: getAllUsers() - Testuje, czy wszystkie użytkowniki są pobierane.
    public void getAllUsers_retrievesAllUsers() {
        // Arrange
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User());
    }

    @Test
// Test 2: getUserById() - Testuje pobieranie konkretnego użytkownika na podstawie identyfikatora.
    public void getUserById_withValidId_retrievesCorrectUser() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        User expectedUser = new User();
        expectedUser.setId(userId.longValue());
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getUserById(userId);

        // Assert
        assertEquals(expectedUser, actualUser, "The user should be found with the correct ID");
    }

    @Test
// Test 3: getUserById() - Rzuca wyjątek, gdy nie odnaleziono identyfikatora.
    public void getUserById_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId),
                "A UserNotFoundException should be thrown if the user is not found");
    }

    @Test
// Test 4: registerUser() - Zapisuje nowego użytkownika z poprawnymi danymi.
    public void registerUser_withValidData_savesUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setEmail("test@test.com");
        newUser.setPassword("password123");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        userService.registerUser(newUser);

        // Assert
        verify(userRepository).save(newUser);
    }

    @Test
// Test 5: registerUser() - Rzuca wyjątek, gdy próbuje zapisać nullowego użytkownika.
    public void registerUser_withNullUser_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null),
                "Should throw IllegalArgumentException when trying to save a null user.");
    }

    @Test
// Test 6: editUser() - Aktualizuje użytkownika o poprawnym identyfikatorze i danych.
    public void editUser_withValidId_updatesUserCorrectly() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        User existingUser = new User();
        existingUser.setId(userId.longValue());
        existingUser.setUsername("oldUsername");

        User editedUser = new User();
        editedUser.setUsername("newUsername");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.editUser(userId, editedUser);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("newUsername", savedUser.getUsername(), "The username should be updated to the new value.");
    }

    @Test
// Test 7: editUser() - Rzuca wyjątek, gdy nie znaleziono użytkownika o podanym identyfikatorze.
    public void editUser_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.editUser(userId, new User()),
                "A UserNotFoundException should be thrown if the user is not found");
    }

    @Test
// Test 8: deleteUser() - Usuwa użytkownika o poprawnym identyfikatorze.
    public void deleteUser_withValidId_deletesUser() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        User userToDelete = new User();
        userToDelete.setId(userId.longValue());
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).delete(userToDelete);
    }

    @Test
// Test 9: deleteUser() - Rzuca wyjątek, gdy próbuje usunąć użytkownika o niepoprawnym identyfikatorze.
    public void deleteUser_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId),
                "A UserNotFoundException should be thrown if the user is not found");
    }

    @Test
// Test 10: assignAdminRole() - Testuje, czy rola administratora jest pomyślnie dodawana użytkownikowi, który jej nie posiada.
    public void assignAdminRole_addsRoleToUser() {
        // Arrange
        User user = new User();
        user.setRoles(new HashSet<>());
        Role adminRole = new Role("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);

        // Act
        userService.assignAdminRole(user);

        // Assert
        assertTrue(user.getRoles().contains(adminRole), "Admin role should be added to the user");
        verify(userRepository).save(user);
    }

    @Test
// Test 11: assignAdminRole() - Testuje, czy metoda nie dodaje roli administratora, jeśli użytkownik ją już posiada.
    public void assignAdminRole_doesNotAddRoleIfAlreadyPresent() {
        // Arrange
        Role adminRole = new Role("ROLE_ADMIN");
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);

        // Act
        userService.assignAdminRole(user);

        // Assert
        assertEquals(1, user.getRoles().size(), "No new roles should be added");
        verify(userRepository, never()).save(user);
    }

    @Test
// Test 12: getUserByUsername() - Testuje, czy metoda prawidłowo pobiera użytkownika po nazwie użytkownika.
    public void getUserByUsername_retrievesUser() {
        // Arrange
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getUserByUsername(username);

        // Assert
        assertEquals(expectedUser, actualUser, "Should retrieve the correct user");
    }

    @Test
// Test 13: loadUserByUsername() - Testuje, czy metoda prawidłowo wczytuje informacje o użytkowniku (UserDetails) dla podanej nazwy użytkownika.
    public void loadUserByUsername_loadsUserDetails() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEnabled(true);
        Set<Role> roles = Set.of(new Role("ROLE_USER"));
        user.setRoles(roles);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertEquals(username, userDetails.getUsername(), "Username should match");
        assertEquals(user.getPassword(), userDetails.getPassword(), "Password should match");
        assertTrue(userDetails.isEnabled(), "User should be enabled");
        assertTrue(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains("ROLE_USER"), "Should contain ROLE_USER authority");
    }
}