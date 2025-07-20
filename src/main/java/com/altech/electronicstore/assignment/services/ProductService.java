package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.entity.Product;
import com.altech.electronicstore.assignment.dto.ProductFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.altech.electronicstore.assignment.repositories.ProductRepository;

import static com.altech.electronicstore.assignment.common.APICode.PRODUCT_OUT_OF_STOCK;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void insertProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        getProductById(product.getId());
        productRepository.save(product);
    }

    public void removeProduct(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }


    //TODO: Implement this method to add a deal to a product
    public Deal addDealToProduct(Long id, Deal deal) {
        return deal;
    }

//    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id).
                orElseThrow(() -> new APIException(PRODUCT_OUT_OF_STOCK));

    }

    public Page<Product> filterProducts(ProductFilterRequest filter) {

        Sort sort = filter.getSortDirection().equalsIgnoreCase("desc") ?
                Sort.by(filter.getSortBy()).descending() : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort
        );

        return productRepository.findByFilters(
                filter.getCategory(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getAvailable(),
                filter.getSearchQuery(),
                pageable
        );
    }


}