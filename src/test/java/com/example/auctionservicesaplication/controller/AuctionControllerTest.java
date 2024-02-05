package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.CategoryService;
import com.example.auctionservicesaplication.service.UserService;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
public class AuctionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuctionService auctionService;

    @Mock
    private CategoryService categoryService;

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

    // Test 1: getAllAuctions() - Test checks for the condition where the user should only receive the auctions they are selling
    @Test
    public void getAllAuctions_asRegularUser_displaysUserSpecificAuctions() {
        // Arrange
        List<Auction> expectedAuctions = new ArrayList<>();
        expectedAuctions.add(new Auction());
        User user = new User();
        user.setUsername("regularUser");
        when(userService.getUserByUsername("regularUser")).thenReturn(user);
        when(auctionService.getAuctionsBySeller(user)).thenReturn(expectedAuctions);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("regularUser");
        when(auth.getAuthorities()).thenReturn(Collections.emptyList()); // Regular user, no specific role

        // Act
        String viewName = auctionController.getAllAuctions(model, auth);

        // Assert
        verify(model).addAttribute("auctions", expectedAuctions);
        assertEquals("auctions", viewName, "Should return the auctions view name for regular user.");
    }


    // Test 1: getAllAuctions() - Tests that the controller correctly fetches all auctions and adds them to the model.
/*
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
*/

    // Test 2: getAuctionDetails() - Tests retrieving a specific auction by its ID.
    @Test
    public void getAuctionDetails_withValidId_displaysAuctionDetails() {
        // Arrange
        Long id = 1L;
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
        Long id = 1L;
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
        User loggedInUser = new User();
        when(userService.getUserByUsername("testUser")).thenReturn(loggedInUser);

        // Act
        String viewName = auctionController.createAuction(validAuction, mock(BindingResult.class), mock(Model.class), authentication);

        // Assert
        verify(auctionService).createAuction(validAuction, loggedInUser);
        assertEquals("redirect:/auctions", viewName, "Should redirect to the auctions list view.");
    }


    // Test 6: getEditForm() - Tests displaying the edit form with specific auction details and categories.
    @Test
    public void getEditForm_withValidId_displaysEditFormWithCategories() {
        // Arrange
        Long id = 1L;
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
        Long id = 1L;
        Auction auction = new Auction();
        auction.setId(id);
        BindingResult bindingResult = mock(BindingResult.class);
        when(auctionService.getAuctionById(id)).thenReturn(auction);

        // Act
        String viewName = auctionController.editAuction(auction, bindingResult, id, model);

        // Assert
        verify(auctionService).editAuction(id, auction);
        assertEquals("redirect:/auctions", viewName, "Should redirect to the auctions list view.");
    }

    // Test 8: deleteAuction() - Tests deleting an auction and redirecting to the auctions list.
    @Test
    public void deleteAuction_withValidId_deletesAuctionAndRedirects() {
        // Arrange
        Long id = 1L;
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
        BindingResult bindingResult = mock(BindingResult.class);

        // Act
        String viewName = auctionController.createAuction(auction, bindingResult, model, null);

        // Assert
        verify(auctionService, never()).createAuction(any(Auction.class), any(User.class));
        assertEquals("redirect:/login", viewName, "Should redirect to the login view when not authenticated.");
    }


    // Test 10: getEditForm() - Tests that an invalid auction ID returns a not found view.
    @Test
    public void getEditForm_withInvalidId_returnsNotFoundView() throws Exception {
        // Arrange
        Long invalidId = 999L;
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
        Long invalidId = 999L;
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
        Long invalidId = 999L;
        when(auctionService.getAuctionById(invalidId)).thenThrow(new AuctionNotFoundException("Not found"));

        // Act & Assert
        mockMvc.perform(get("/auctions/" + invalidId + "/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionNotFound"));
    }

/*    // Test 13: getAllAuctions() - Test checks for the condition where the user is an admin and should receive all auctions
    @Test
    public void getAllAuctions_asAdmin_displaysAllAuctions() {
        // Arrange
        List<Auction> expectedAuctions = new ArrayList<>();
        expectedAuctions.add(new Auction());
        when(auctionService.getAllAuction()).thenReturn(expectedAuctions);

        Authentication auth = mock(Authentication.class);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(auth.getAuthorities()).thenReturn(authorities);

        // Act
        String viewName = auctionController.getAllAuctions(model, auth);

        // Assert
        verify(model).addAttribute("auctions", expectedAuctions);
        assertEquals("auctions", viewName, "Should return the auctions view name for admin.");
    }
    */
}