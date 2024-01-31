package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.service.AuctionService;
import com.example.auctionservicesaplication.service.CategoryService;
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

    @Mock
    private AuctionService auctionService;

    @Mock
    private CategoryService categoryService;

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