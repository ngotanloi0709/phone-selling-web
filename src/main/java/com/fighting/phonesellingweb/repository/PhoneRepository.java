package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
    public Phone findByName(String name);

    List<Phone> findByBrandId(int brandId);

    Page<Phone> findAll(Pageable pageable);

    @Query("SELECT p FROM Phone p ORDER BY RAND()")
    List<Phone> findRandomPhones(Pageable pageable);

}
