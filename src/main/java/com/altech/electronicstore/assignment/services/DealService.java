package com.altech.electronicstore.assignment.services;

import com.altech.electronicstore.assignment.common.APIException;
import com.altech.electronicstore.assignment.dto.Deal;
import com.altech.electronicstore.assignment.dto.DealFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.altech.electronicstore.assignment.repositories.DealRepository;

import static com.altech.electronicstore.assignment.common.APICode.DEAL_NOT_FOUND;

@Service
public class DealService {

    @Autowired
    private DealRepository dealRepository;



    public Deal getDealById(Long id) {
        return dealRepository.findById(id)
            .orElseThrow(() -> new APIException(DEAL_NOT_FOUND));
    }

    // get deal by code
    public Deal getDealByCode(String code) {
        return dealRepository.findByCode(code)
            .orElseThrow(() -> new APIException(DEAL_NOT_FOUND));
    }

    // insert deal
    public Deal insertDeal(Deal deal) {
        if (deal.getId() != null && dealRepository.existsById(deal.getId())) {
            throw new APIException(DEAL_NOT_FOUND);
        }
        return dealRepository.save(deal);
    }

    public void removeDeal(Long id) {
        getDealById(id);
        dealRepository.deleteById(id);
    }

    public void updateDeal(Deal deal) {
        getDealById(deal.getId());
        dealRepository.save(deal);
    }

    public Page<Deal> filterDeals(DealFilterRequest filter) {
        Sort sort = filter.getSortDirection().equalsIgnoreCase("desc") ?
                Sort.by(filter.getSortBy()).descending() : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort
        );

        return dealRepository.findByFilters(
                filter.getActive(),
                filter.getSearchQuery(),
                pageable

        );
    }

}
