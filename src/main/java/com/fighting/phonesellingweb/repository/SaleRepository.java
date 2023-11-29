package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    @Query("SELECT s FROM Sale s ORDER BY s.quantitySold DESC")
    Page<Sale> findBestSellingPhones(Pageable pageable);
}