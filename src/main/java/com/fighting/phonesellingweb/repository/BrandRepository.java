package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    public Brand findByName(String name);
}
