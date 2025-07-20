package com.altech.electronicstore.assignment.controllers;

import com.altech.electronicstore.assignment.entity.Deal;
import com.altech.electronicstore.assignment.dto.DealFilterRequest;
import com.altech.electronicstore.assignment.dto.Response;
import com.altech.electronicstore.assignment.common.APICode;
import com.altech.electronicstore.assignment.services.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @Operation(
        summary = "Create a new deal. Only accessible by admin users",
        description = "Creates a new deal. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal created successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can create deals.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> createDeal(@RequestBody Deal deal) {
        dealService.insertDeal(deal);
        return Response.success(APICode.DEAL_CREATED.getMessage());
    }

    @Operation(
        summary = "Update an existing deal. Only accessible by admin users",
        description = "Updates the details of an existing deal. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal updated successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can update deals.")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> updateDeal(@RequestBody Deal deal) {
        dealService.updateDeal(deal);
        return Response.success(APICode.DEAL_UPDATED.getMessage());
    }

    @Operation(
        summary = "Remove a deal. Only accessible by admin users",
        description = "Removes a deal by its ID. Only accessible by admin users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal removed successfully."),
        @ApiResponse(responseCode = "404", description = "Deal not found."),
        @ApiResponse(responseCode = "401", description = "Unauthorized access."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins can remove deals.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> removeDeal(@PathVariable Long id) {
        dealService.removeDeal(id);
        return Response.success(APICode.DEAL_REMOVED.getMessage());
    }

    @Operation(
        summary = "Get deal by ID",
        description = "Retrieves a deal by its unique ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deal retrieved successfully."),
        @ApiResponse(responseCode = "404", description = "Deal not found.")
    })
    @GetMapping("/{id}")
    public Deal getDealById(@PathVariable Long id) {
        return dealService.getDealById(id);
    }


    @Operation(
            summary = "Filter deals",
            description = "Retrieves a paginated list of deals based on filter criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deals retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters.")
    })
    @GetMapping
    public Response<Page<Deal>> filterDeals(DealFilterRequest filter) {
        Page<Deal> deals = dealService.filterDeals(filter);
        return Response.success(deals);
    }




}
