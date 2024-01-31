package com.example.auctionservicesaplication.controller;

<<<<<<< HEAD
=======
import com.example.auctionservicesaplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
>>>>>>> origin/master
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.CategoryService;
<<<<<<< HEAD
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuctionControllerTest {
=======
import com.example.auctionservicesaplication.service.UserService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuctionControllerTest {
    @Autowired
    private MockMvc mockMvc;
>>>>>>> origin/master

    @Mock
    private AuctionService auctionService;

    @Mock
    private CategoryService categoryService;

<<<<<<< HEAD
    @InjectMocks
    private AuctionController auctionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }


    @Test
        // Test sprawdza, czy metoda getAuctionDetails zwraca poprawny widok oraz atrybuty modelu.
    void getAuctionDetails_ShouldReturnAuctionDetailsPage() throws Exception {
        BigDecimal auctionId = BigDecimal.valueOf(1);
        when(auctionService.getAuctionById(auctionId)).thenReturn(new Auction());

        mockMvc.perform(get("/auctions/{auctionId}", auctionId))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionDetails"))
                .andExpect(model().attributeExists("auction"));
    }

    @Test
        // Test sprawdza, czy metoda getCreateForm zwraca poprawny widok oraz niezbędne atrybuty modelu.
    void getCreateForm_ShouldReturnCreateFormPage() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(new Category(), new Category()));

        mockMvc.perform(get("/auctions/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createdForm"))
                .andExpect(model().attributeExists("auction"))
                .andExpect(model().attributeExists("categories"));
    }

}
=======
    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AuctionController auctionController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(auctionController)
                .build();
    }

    // Test 1: getAllAuctions() - Tests that the controller correctly fetches all auctions and adds them to the model.
    @Test
    public void getAllAuctions_displaysAllAuctions() {
        // Arrange
        List<Auction> expectedAuctions = new ArrayList<>();
        expectedAuctions.add(new Auction());
        when(auctionService.getAllAuction()).thenReturn(expectedAuctions);

        // Act
        String viewName = auctionController.getAllAuctions(model);

        // Assert
        verify(model).addAttribute("auctions", expectedAuctions);
        assertEquals("auctions", viewName, "Should return the auctions view name.");
    }

    // Test 2: getAuctionDetails() - Tests retrieving a specific auction by its ID.
    @Test
    public void getAuctionDetails_withValidId_displaysAuctionDetails() {
        // Arrange
        BigDecimal id = BigDecimal.ONE;
        Auction expectedAuction = new Auction();
        when(auctionService.getAuctionById(id)).thenReturn(expectedAuction);

        // Act
        String viewName = auctionController.getAuctionDetails(id, model);

        // Assert
        verify(model).addAttribute("auction", expectedAuction);
        assertEquals("auctionDetails", viewName, "Should return the auctionDetails view name.");
    }

    // Test 3: getAuctionDetails() - Tests handling of an invalid auction ID.
    @Test
    public void getAuctionDetails_withInvalidId_returnsNotFoundView() throws Exception {
        // Arrange
        BigDecimal id = BigDecimal.ONE;
        when(auctionService.getAuctionById(id)).thenThrow(new AuctionNotFoundException("Not found"));

        // Act & Assert
        mockMvc.perform(get("/auctions/" + id))
                .andExpect(status().isOk()) // Use the correct status that your 'auctionNotFound' view should return
                .andExpect(view().name("auctionNotFound"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    // Test 4: getCreateForm() - Tests displaying the create auction form with categories.
    @Test
    public void getCreateForm_displaysCreateFormWithCategories() {
        // Arrange
        List<Category> expectedCategories = new ArrayList<>();
        when(categoryService.getAllCategories()).thenReturn(expectedCategories);

        // Act
        String viewName = auctionController.getCreateForm(model);

        // Assert
        verify(model).addAttribute("categories", expectedCategories);
        assertEquals("createdForm", viewName, "Should return the createdForm view name.");
    }

    // Test 5: createAuction() - Tests creating an auction and redirecting to the auctions list with an authenticated user.
    @Test
    public void createAuction_withValidDataAndAuthenticatedUser_createsAuctionAndRedirects() {
        // Arrange
        Auction validAuction = new Auction();
        validAuction.setTitle("Valid Title");
        validAuction.setDescription("Valid Description");
        validAuction.setStartingPrice(new BigDecimal("100.00"));

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        User loggedInUser = new User(); // Assuming User is your user model class
        when(userService.getUserByUsername("testUser")).thenReturn(loggedInUser);

        // Act
        String viewName = auctionController.createAuction(validAuction, mock(Model.class), authentication);

        // Assert
        verify(auctionService).createAuction(validAuction, loggedInUser); // Expecting the valid auction and logged-in user to be passed to the service
        assertEquals("redirect:/auctions", viewName, "Should redirect to the auctions list view.");
    }

    // Test 6: getEditForm() - Tests displaying the edit form with specific auction details and categories.
    @Test
    public void getEditForm_withValidId_displaysEditFormWithCategories() {
        // Arrange
        BigDecimal id = BigDecimal.ONE;
        Auction expectedAuction = new Auction();
        List<Category> expectedCategories = new ArrayList<>();
        when(auctionService.getAuctionById(id)).thenReturn(expectedAuction);
        when(categoryService.getAllCategories()).thenReturn(expectedCategories);

        // Act
        String viewName = auctionController.getEditForm(id, model);

        // Assert
        verify(model).addAttribute("auction", expectedAuction);
        verify(model).addAttribute("categories", expectedCategories);
        assertEquals("editFormAuction", viewName, "Should return the editFormAuction view name.");
    }

    // Test 7: editAuction() - Tests updating auction details and redirecting to the auctions list.
    @Test
    public void editAuction_withValidId_updatesAuctionAndRedirects() {
        // Arrange
        BigDecimal id = BigDecimal.ONE;
        Auction auction = new Auction();
        auction.setId(id); // Set the ID to match the path variable
        when(auctionService.getAuctionById(id)).thenReturn(auction); // Ensure this call does not return null

        // Act
        String viewName = auctionController.editAuction(auction, id);

        // Assert
        verify(auctionService).editAuction(id, auction);
        assertEquals("redirect:/auctions", viewName, "Should redirect to the auctions list view.");
    }

    // Test 8: deleteAuction() - Tests deleting an auction and redirecting to the auctions list.
    @Test
    public void deleteAuction_withValidId_deletesAuctionAndRedirects() {
        // Arrange
        BigDecimal id = BigDecimal.ONE;
        Auction existingAuction = new Auction();
        existingAuction.setId(id);
        when(auctionService.getAuctionById(id)).thenReturn(existingAuction);

        // Act
        String viewName = auctionController.deleteAuction(id);

        // Assert
        verify(auctionService).deleteAuction(id);
        assertEquals("redirect:/auctions", viewName, "Should redirect to the auctions list view.");
    }

    // Test 9: createAuction() - Tests creating an auction and redirecting to the auctions list with an unauthenticated user.

    @Test
    public void createAuction_withoutAuthentication_redirectsToLogin() {
        // Arrange
        Auction auction = new Auction();

        // Act
        String viewName = auctionController.createAuction(auction, mock(Model.class), null);

        // Assert
        verify(auctionService, never()).createAuction(any(Auction.class), any(User.class));
        assertEquals("redirect:/login", viewName, "Should redirect to the login view when not authenticated.");
    }

    // Test 10: getEditForm() - Tests that an invalid auction ID returns a not found view.
    @Test
    public void getEditForm_withInvalidId_returnsNotFoundView() throws Exception {
        // Arrange
        BigDecimal invalidId = new BigDecimal("999");
        when(auctionService.getAuctionById(invalidId)).thenThrow(new AuctionNotFoundException("Not found"));

        // Act & Assert
        mockMvc.perform(get("/auctions/" + invalidId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionNotFound"));
    }

    // Test 11: editAuction() - Tests that an invalid auction ID does not update the auction and returns a not found view.
    @Test
    public void editAuction_withInvalidId_doesNotUpdateAndReturnsNotFoundView() throws Exception {
        // Arrange
        BigDecimal invalidId = new BigDecimal("999");
        Auction auction = new Auction();
        when(auctionService.getAuctionById(invalidId)).thenThrow(new AuctionNotFoundException("Not found"));

        // Act & Assert
        mockMvc.perform(post("/auctions/" + invalidId + "/edit")
                        .flashAttr("auction", auction))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionNotFound"));
    }

    // Test 12: deleteAuction() - Tests that an invalid auction ID does not delete an auction and returns a not found view.
    @Test
    public void deleteAuction_withInvalidId_doesNotDeleteAndReturnsNotFoundView() throws Exception {
        // Arrange
        BigDecimal invalidId = new BigDecimal("999");
        when(auctionService.getAuctionById(invalidId)).thenThrow(new AuctionNotFoundException("Not found"));

        // Act & Assert
        mockMvc.perform(get("/auctions/" + invalidId + "/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionNotFound"));
    }

}
>>>>>>> origin/master
