package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.entity.Basket;
import com.altech.electronicstore.assignment.entity.BasketProduct;
import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.dto.InsertBasketRequest;
import com.altech.electronicstore.assignment.entity.Product;
import com.altech.electronicstore.assignment.dto.Receipt;
import com.altech.electronicstore.assignment.dto.ReceiptItem;
import com.altech.electronicstore.assignment.repositories.BasketRepository;
import com.altech.electronicstore.assignment.services.deal.DealFactory;
import com.altech.electronicstore.assignment.services.deal.DealHandler;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BasketService {
    private final static Logger logger = LoggerFactory.getLogger(BasketService.class);

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DealFactory dealFactory;

    @Autowired
    private DealService dealService;

    @Transactional
    public Basket addProduct(InsertBasketRequest request) {
        var productId = request.getProductId();
        var userId = request.getUserId();

        logger.info("Adding product {} to basket for user {}", productId, userId);
        Basket basket = getSingletonBasket(userId);
        Product product = productService.getProductById(productId);

        if (product.getStock() - request.getQuantity() < 0) {
            logger.warn("Product {} is out of stock", productId);
            throw new APIException(APICode.PRODUCT_OUT_OF_STOCK);
        }

        product.setStock(product.getStock() - request.getQuantity());
        productService.updateProduct(product); // Use updateProduct instead of insertProduct

        for (BasketProduct bp : basket.getBasketProducts()) {
            if (bp.getProduct().getId().equals(productId)) {
                logger.warn("Product {} already exists in basket for user {}, updating quantity", productId, userId);
                bp.setQuantity(bp.getQuantity() + request.getQuantity());
                return basketRepository.save(basket);
            }
        }

        BasketProduct basketProduct = new BasketProduct();
        basketProduct.setBasket(basket);
        basketProduct.setProduct(product);
        basketProduct.setQuantity(request.getQuantity());


        basket.getBasketProducts().add(basketProduct);
        product.getBasketProducts().add(basketProduct);


        logger.debug("Product {} added to basket for user {}", productId, userId);
        return basketRepository.save(basket);
    }

    public Basket getSingletonBasket(Long userId) {
        logger.debug("Retrieving basket for user {}", userId);
        return basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    logger.info("No basket found for user {}, creating new basket", userId);
                    Basket newBasket = new Basket();
                    newBasket.setUserId(userId);
                    return basketRepository.save(newBasket);
                });
    }

    @Transactional
    public Basket removeProduct(Long userId, Long productId) {
        logger.info("Removing product {} from basket for user {}", productId, userId);
        Basket basket = getSingletonBasket(userId);
        Product product = productService.getProductById(productId);

        var productInBasket = basket.getBasketProducts().stream()
                .filter(bp -> bp.getProduct().getId().equals(productId))
                .findFirst();

        if (productInBasket.isEmpty()) {
            logger.warn("Product {} not found in basket for user {}", productId, userId);
            throw new APIException(APICode.PRODUCT_NOT_FOUND);
        }
        basket.getBasketProducts().remove(productInBasket.get());
        // Update product stock
        product.setStock(product.getStock() + productInBasket.get().getQuantity());
        productService.updateProduct(product);

        return basketRepository.save(basket);
    }

    public Receipt generateReceipt(Long basketId, String codesApplied) {
        logger.info("Generating receipt for basket {}", basketId);
        Basket basket = getSingletonBasket(basketId);
        Receipt receipt = new Receipt();


        var items = new ArrayList<ReceiptItem>();
        for (var basketProduct : basket.getBasketProducts()) {
            int quantity = basketProduct.getQuantity();
            Product product = basketProduct.getProduct();


            ReceiptItem item = new ReceiptItem();
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setTotalPrice(product.getPrice() * quantity);

            items.add(item);
        }
        receipt.setItems(items);


        receipt = applyDeals(codesApplied, receipt);

        var total = receipt.getItems().stream()
                .mapToDouble(item -> (item.getTotalPrice() - item.getDiscountedPrice()))
                .sum();

        receipt.setTotalPrice(total);
        logger.info("Receipt generated for basket {} with total price {}", basketId, total);
        return receipt;
    }

    public Receipt applyDeals(String codes, Receipt receipt) {
        for (String code : codes.split(",")) {
            code = code.trim();
            if (code.isEmpty()) {
                continue;
            }

            Deal deal = dealService.getDealByCode(code);
            if (deal == null) {
                logger.warn("Deal code {} not found", code);
                return receipt;
            }

            DealHandler handler = dealFactory.getHandler(deal.getCode());
            if (handler == null) {
                logger.warn("No handler found for deal code {}", deal.getCode());
                return receipt; // Handler not found – return early
            }

            handler.validateDeal(deal);

            logger.info("Applying deal {} to receipt", deal.getCode());
            receipt = handler.applyDeal(receipt);
        }

        return receipt;
    }

}