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

    Page<Phone> findByBrand_Id(int brandId, Pageable pageable);

    Page<Phone> findByBrandId(int brandId, Pageable pageable);

    Page<Phone> findAll(Pageable pageable);

    @Query("SELECT p FROM Phone p ORDER BY RAND()")
    List<Phone> findRandomPhones(Pageable pageable);

    long countByBrandId(Integer brandId);


    @Query("SELECT p FROM Phone p WHERE p.name LIKE %:keyword%")
    List<Phone> searchByName(String keyword);

    @Query("SELECT p FROM Phone p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Phone> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
