package com.fighting.phonesellingweb;

import com.fighting.phonesellingweb.model.Brand;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.Role;
import com.fighting.phonesellingweb.service.BrandService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@AllArgsConstructor
public class PhoneSellingWebApplication {
    private UserService userService;
    private BrandService brandService;
    private PhoneService phoneService;

    public static void main(String[] args) {
        SpringApplication.run(PhoneSellingWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(){
        return args -> {
//            addUsers();
//            addBrands();
//            addPhones();
        };
    }

    private void addUsers() {
        userService.register("ngotanloi0709@gmail.com", "loi@2003", "Ngô Tấn Lợi", "0123456789");
        userService.setRole("ngotanloi0709@gmail.com", Role.ROLE_ADMIN);
    }

    private void addBrands() {
        brandService.createBrand(new Brand("Brand 1"));
        brandService.createBrand(new Brand("Brand 2"));
        brandService.createBrand(new Brand("Brand 3"));
        brandService.createBrand(new Brand("Brand 4"));
        brandService.createBrand(new Brand("Brand 5"));
        brandService.createBrand(new Brand("Brand 6"));
        brandService.createBrand(new Brand("Brand 7"));
        brandService.createBrand(new Brand("Brand 8"));
        brandService.createBrand(new Brand("Brand 9"));
        brandService.createBrand(new Brand("Brand 10"));
    }

    private void addPhones() {
        phoneService.createPhone(new Phone("Phone 1", "Description 1", 1000.0, "Black", "4GB/128GB", "Android", "Snapdragon 865", "6.2-inch", "12MP", "3000mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 2", "Description 2", 1200.0, "White", "8GB/128GB", "iOS", "A14 Bionic", "6.5-inch", "16MP", "3500mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 3", "Description 3", 800.0, "Blue", "6GB/128GB", "Android", "Exynos 980", "6.0-inch", "8MP", "2800mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 4", "Description 4", 900.0, "Red", "12GB/128GB", "iOS", "A13 Bionic", "6.4-inch", "20MP", "3200mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 5", "Description 5", 1500.0, "Green", "16GB/128GB", "Android", "Kirin 990", "6.3-inch", "24MP", "4000mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 6", "Description 6", 1100.0, "Silver", "8GB/128GB", "iOS", "A12 Bionic", "6.1-inch", "12MP", "3500mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 7", "Description 7", 1300.0, "Gold", "6GB/128GB", "Android", "Snapdragon 855", "6.5-inch", "16MP", "3800mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 8", "Description 8", 950.0, "Gray", "4GB/128GB", "iOS", "A11 Bionic", "6.2-inch", "8MP", "3000mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 9", "Description 9", 1000.0, "Black", "6GB/128GB", "Android", "Exynos 990", "6.3-inch", "12MP", "3200mAh", "", brandService.findBrandById(1)));
        phoneService.createPhone(new Phone("Phone 10", "Description 10", 1200.0, "White", "8GB/128GB", "iOS", "A10 Fusion", "6.0-inch", "16MP", "3500mAh", "", brandService.findBrandById(1)));
    }

}
