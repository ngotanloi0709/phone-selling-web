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
    public List<Phone> findPhonesByBrand(int brand_id) {
        Pageable pageable = PageRequest.of(0, 10); // adjust the page number and size as needed
        Page<Phone> page = phoneRepository.findByBrandId(brand_id, pageable);
        return page.getContent();
    }

    public Page<Phone> findRandomPhones(Pageable pageable) {
        List<Phone> randomPhones = phoneRepository.findRandomPhones(pageable);
        return new PageImpl<>(randomPhones, pageable, randomPhones.size());
    }


    public Page<Phone> findAllPhones(Pageable pageable) {
            return phoneRepository.findAll(pageable);
        }




    public List<Phone> getAllPhones() {
        return phoneRepository.findAll();
    }

    public Page<Phone> findPhonesByBrand(int brand_id, Pageable pageable) {
        return phoneRepository.findByBrandId(brand_id, pageable);
    }
}
