package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
    public Phone findByName(String name);
}
