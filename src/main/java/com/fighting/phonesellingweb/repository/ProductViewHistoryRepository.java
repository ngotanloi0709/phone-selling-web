package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.ProductViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Long> {
    @Query("SELECT p FROM ProductViewHistory p WHERE p.id IN (SELECT MAX(p2.id) FROM ProductViewHistory p2 WHERE p2.user.id = :userId GROUP BY p2.phone.id) ORDER BY p.viewedAt DESC")
    List<ProductViewHistory> findLatestViewHistoryByUserId(@Param("userId") int userId);
}
