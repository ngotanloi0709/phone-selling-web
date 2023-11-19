package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Brand;
import com.fighting.phonesellingweb.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandService {
    BrandRepository brandRepository;

    public List<Brand> findAllBrands() {
        return brandRepository.findAll();
    }

    public void createBrand(Brand brand) {
        if (brandRepository.findByName(brand.getName()) != null) {
            return;
        }

        brandRepository.save(brand);
    }

    public Brand findBrandById(int i) {
        return brandRepository.findById(i).orElse(null);
    }

    public void updateBrand(Brand brand) {
        if (brandRepository.findByName(brand.getName()) != null) {
            return;
        }

        brandRepository.save(brand);
    }

    public void deleteBrand(Integer id) {
        brandRepository.deleteById(id);
    }
}
