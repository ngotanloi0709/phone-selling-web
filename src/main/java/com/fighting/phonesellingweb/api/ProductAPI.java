package com.fighting.phonesellingweb.api;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.service.PhoneService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
@AllArgsConstructor
public class ProductAPI {
    private PhoneService phoneService;

    @GetMapping
    public ResponseEntity<List<Phone>> getAllPhones() {
        List<Phone> phones = phoneService.findAllPhones();
        return ResponseEntity.ok(phones);
    }

    @PostMapping("/create")
    public ResponseEntity<Phone> createPhone(@RequestBody Phone phone) {
        phoneService.createPhone(phone);
        return ResponseEntity.ok(phone);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Phone> getPhoneById(@PathVariable int id) {
        Phone phone = phoneService.findPhoneById(id);

        if (phone == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(phone);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable int id, @RequestBody Phone phone) {
        Phone existingPhone = phoneService.findPhoneById(id);

        if (existingPhone == null) {
            return ResponseEntity.notFound().build();
        }

        phone.setId(id);
        phoneService.updatePhone(phone);

        return ResponseEntity.ok(phone);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable int id) {
        Phone existingPhone = phoneService.findPhoneById(id);

        if (existingPhone == null) {
            return ResponseEntity.notFound().build();
        }

        phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
