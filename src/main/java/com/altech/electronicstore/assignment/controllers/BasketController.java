package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.AuthUtil;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.InsertBasketRequest;
import com.altech.electronicstore.assignment.dto.Receipt;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.dto.UserProfile;
import com.altech.electronicstore.assignment.services.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
@Tag(name = "Basket", description = "Operations related to user's shopping basket")
public class BasketController {

    private final AuthUtil authUtil;

    private final BasketService basketService;

    @Operation(
        summary = "Add product to basket",
        description = "Adds a product to the authenticated user's basket. Returns the updated basket."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid product or request data."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    @PostMapping("/add")
    public Response<Basket> addProductToBasket(@RequestBody InsertBasketRequest request) {
        UserProfile userId = authUtil.getUserInfo();
        request.setUserId(userId.getId());
        Basket updatedBasket = basketService.addProduct(request);
        return Response.success(updatedBasket);
    }

    @Operation(
        summary = "Remove product from basket",
        description = "Removes a product from the authenticated user's basket. Returns the updated basket."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully."),
        @ApiResponse(responseCode = "404", description = "Product not found in basket."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    @DeleteMapping("/remove/{productId}")
    public Response<Basket> removeProductFromBasket(@PathVariable Long productId) {
        UserProfile userId = authUtil.getUserInfo();
        Basket updatedBasket = basketService.removeProduct(userId.getId(), productId);
        return Response.success(updatedBasket);
    }

    @Operation(
        summary = "Get user's basket",
        description = "Retrieves the current basket for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Basket retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    @GetMapping
    public Response<Basket> getBasket() {
        UserProfile userId = authUtil.getUserInfo();
        Basket basket = basketService.getSingletonBasket(userId.getId());
        return Response.success(basket);
    }

    @Operation(
        summary = "Get basket receipt",
        description = "Generates and returns a receipt for the current basket of the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipt generated successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access.")
    })
    @GetMapping("/receipt")
    // add
    public Response<Receipt> getReceipt(@RequestParam String code) {
        UserProfile userId = authUtil.getUserInfo();
        Receipt receipt = basketService.generateReceipt(userId.getId(), code);
        return Response.success(receipt);

    }
}