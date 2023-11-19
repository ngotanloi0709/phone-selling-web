package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.repository.PhoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
