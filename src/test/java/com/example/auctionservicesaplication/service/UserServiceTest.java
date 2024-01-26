package com.example.auctionservicesaplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.UserRepository;
import com.example.auctionservicesaplication.service.UserService;
import com.example.auctionservicesaplication.message.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Test 1: getAllUsers() - Tests that all users are retrieved.
    @Test
    public void getAllUsers_retrievesAllUsers() {
        // Arrange
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User()); // Add mock Users as needed
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertEquals(expectedUsers, actualUsers, "The returned users should match the expected ones");
    }

    // Test 2: getUserById() - Tests retrieving a specific user by their ID.
    @Test
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

    // Test 3: getUserById() - Throws exception when ID is not found.
    @Test
    public void getUserById_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId),
                "A UserNotFoundException should be thrown if the user is not found");
    }

    // Test 4: registerUser() - Saves a new user with valid data.
    @Test
    public void registerUser_withValidData_savesUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setEmail("test@test.com");
        newUser.setPassword("password123");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        userService.registerUser(newUser);

        // Assert
        verify(userRepository).save(newUser);
    }

    // Test 5: registerUser() - Throws exception when trying to save a null user.
    @Test
    public void registerUser_withNullUser_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null),
                "Should throw IllegalArgumentException when trying to save a null user.");
    }

    // Test 6: editUser() - Updates a user with valid ID and data.
    @Test
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

    // Test 7: editUser() - Throws exception when the user ID is not found.
    @Test
    public void editUser_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.editUser(userId, new User()),
                "A UserNotFoundException should be thrown if the user is not found");
    }

    // Test 8: deleteUser() - Deletes a user with a valid ID.
    @Test
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

    // Test 9: deleteUser() - Throws exception when trying to delete a user with an invalid ID.
    @Test
    public void deleteUser_withInvalidId_throwsUserNotFoundException() {
        // Arrange
        BigDecimal userId = BigDecimal.ONE;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId),
                "A UserNotFoundException should be thrown if the user is not found");
    }
}
