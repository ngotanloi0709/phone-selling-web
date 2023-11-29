package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.Sale;
import com.fighting.phonesellingweb.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;


    public Page<Phone> findBestSellingPhones(Pageable pageable) {
        return saleRepository.findBestSellingPhones(pageable)
                .map(Sale::getPhone);
    }


}
