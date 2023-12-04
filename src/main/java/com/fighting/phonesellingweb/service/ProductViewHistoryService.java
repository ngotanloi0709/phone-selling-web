package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.ProductViewHistory;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.repository.ProductViewHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductViewHistoryService {
    private ProductViewHistoryRepository productViewHistoryRepository;

    public void save(ProductViewHistory history) {
        productViewHistoryRepository.save(history);
    }

    public List<ProductViewHistory> getHistory(int userId) {
        return productViewHistoryRepository.findLatestViewHistoryByUserId(userId);
    }
}
