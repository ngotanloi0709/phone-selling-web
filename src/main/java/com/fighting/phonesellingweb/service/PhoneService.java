package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.repository.PhoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhoneService {
    PhoneRepository phoneRepository;

    public List<Phone> findAllPhones() {
        return phoneRepository.findAll();
    }

    public void createPhone(Phone phone) {
        phoneRepository.save(phone);
    }

    public Phone findPhoneById(int i) {
        return phoneRepository.findById(i).orElse(null);
    }

    public void updatePhone(Phone phone) {
        phoneRepository.save(phone);
    }

    public void deletePhone(Integer id) {
        phoneRepository.deleteById(id);
    }


    // Lấy danh sách sản phẩm ngẫu nhiên
    public List<Phone> findRandomPhones(int limit) {
        List<Phone> allPhones = phoneRepository.findAll();
        Collections.shuffle(allPhones);
        return allPhones.stream()
                .filter(phone -> phone.getImageUrl() != null)
                .limit(limit)
                .collect(Collectors.toList());
    }
    // Lấy sản phẩm theo thương hiệu
    public List<Phone> findPhonesByBrand(int brandId) {
        return phoneRepository.findByBrandId(brandId);
    }

    public Page<Phone> findRandomPhones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Phone> randomPhones = phoneRepository.findRandomPhones(pageable);
        return new PageImpl<>(randomPhones, pageable, randomPhones.size());
    }
}
