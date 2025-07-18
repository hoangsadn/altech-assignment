package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.auth.AuthUtil;
import com.altech.electronicstore.assignment.dto.Basket;
import com.altech.electronicstore.assignment.dto.InsertBasketRequest;
import com.altech.electronicstore.assignment.dto.Receipt;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.dto.UserProfile;
import com.altech.electronicstore.assignment.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final AuthUtil authUtil;

    private final BasketService basketService;

    @PostMapping("/add")
    public Response<Basket> addProductToBasket(@RequestBody InsertBasketRequest request) {
        UserProfile userId = authUtil.getUserInfo();
        Basket updatedBasket = basketService.addProduct(userId.getId(), request.getProductId());
        return Response.success(updatedBasket);
    }

    @DeleteMapping("/remove/{productId}")
    public Response<Basket> removeProductFromBasket(@PathVariable Long productId) {
        UserProfile userId = authUtil.getUserInfo();
        Basket updatedBasket = basketService.removeProduct(userId.getId(), productId);
        return Response.success(updatedBasket);
    }

    @GetMapping
    public Response<Basket> getBasket() {
        UserProfile userId = authUtil.getUserInfo();
        Basket basket = basketService.getSingletonBasket(userId.getId());
        return Response.success(basket);
    }


    @GetMapping("/receipt")
    public Response<Receipt> getReceipt(){
        UserProfile userId = authUtil.getUserInfo();
        Receipt receipt = basketService.generateReceipt(userId.getId());
        return Response.success(receipt);

    }
}