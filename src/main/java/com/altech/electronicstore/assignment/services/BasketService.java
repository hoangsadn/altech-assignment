package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.models.Basket;
import com.altech.electronicstore.assignment.models.Product;
import com.altech.electronicstore.assignment.models.Receipt;
import com.altech.electronicstore.assignment.models.ReceiptItem;
import com.altech.electronicstore.assignment.repositories.BasketRepository;
import com.altech.electronicstore.assignment.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public Basket addProduct(Long userId, Long productId) {
        Basket basket = getSingletonBasket(userId);

        Product product = productService.getProductById(productId);

        if (product.getStock() <= 0) {
            throw new APIException(APICode.PRODUCT_OUT_OF_STOCK);
        }

        product.setStock(product.getStock() - 1);
        productService.insertProduct(product);

        basket.getProducts().add(product);
        return basketRepository.save(basket);
    }

    public Basket getSingletonBasket(Long userId) {
        return basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUserId(userId);
                    return basketRepository.save(newBasket);
                });
    }

    @Transactional
    public Basket removeProduct(Long userId, Long productId) {
        var basket = getSingletonBasket(userId);

        var product = productService.getProductById(productId);

        if (basket.getProducts().removeIf(p -> p.getId().equals(productId))) {
            product.setStock(product.getStock() + 1);
            productService.insertProduct(product);
        }

        return basketRepository.save(basket);
    }


    public Receipt generateReceipt(Long basketId) {
        var basket = getSingletonBasket(basketId);

        Receipt receipt = new Receipt();
        Map<Long, Integer> productCounts = new HashMap<>();
        Map<Long, Product> productMap = new HashMap<>();

        // Count products and store product details
        for (Product product : basket.getProducts()) {
            productCounts.merge(product.getId(), 1, Integer::sum);
            productMap.putIfAbsent(product.getId(), product);
        }

        double totalPrice = 0.0;

        // Process each unique product
        for (Map.Entry<Long, Integer> entry : productCounts.entrySet()) {
            Product product = productMap.get(entry.getKey());
            int quantity = entry.getValue();
            double unitPrice = product.getPrice();
            double itemTotal = unitPrice * quantity;

            // Apply 10% discount for 3 or more items
            if (quantity >= 3) {
                itemTotal *= 0.9; // 10% discount
                receipt.getDealsApplied().add("10% off for 3+ " + product.getName());
            }

            receipt.getItems().add(new ReceiptItem(
                    product.getName(),
                    quantity,
                    unitPrice,
                    itemTotal
            ));

            totalPrice += itemTotal;
        }

        receipt.setTotalPrice(totalPrice);
        return receipt;
    }
}