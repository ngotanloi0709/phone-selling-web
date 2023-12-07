package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.*;
import com.fighting.phonesellingweb.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private UserService userService;
    private PhoneService phoneService;
    private BrandService brandService;
    private CommentService commentService;
    private OrderService  orderService;


    @GetMapping({"", "/"})
    public String getAdmin(){
        return "admin/index";
    }

    @GetMapping("/user")
    public String getUser(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("mapUser", new User());

        return "admin/user";
    }

    @GetMapping("/user/comment/{id}")
    public String getUserComment(@PathVariable Integer id, Model model){
        User user = userService.findUserById(id);
        model.addAttribute("userMapping", user);

        return "admin/user_comments";
    }

    @GetMapping("/product")
    public String getProduct(Model model) {
        model.addAttribute("phones", phoneService.findAllPhones());
        model.addAttribute("brands", brandService.findAllBrands());
        model.addAttribute("phone", new Phone());
        model.addAttribute("brand", new Brand());

        return "admin/product";
    }

    @GetMapping("/order")
    public String getOrder(){
        return "admin/order";
    }
//
//    Code Section for user
//
    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("mapUser") User mapUser) {
        userService.createUser(mapUser);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute("mapUser") User mapUser, @RequestParam String email) {
        User existingUser = userService.findUserByEmail(email);
        if (existingUser == null) {
            return "redirect:/admin/user";
        }

        existingUser.setName(mapUser.getName());
        existingUser.setAddress(mapUser.getAddress());
        existingUser.setPhone(mapUser.getPhone());
        existingUser.setLocked(mapUser.isLocked());

        userService.updateUser(existingUser);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam Integer id) {
        userService.deleteUser(id);

        return "redirect:/admin/user";
    }

    @PostMapping("/user/comment/delete")
    public String deleteComment(@RequestParam(name = "comment_id") Integer comment_id, @RequestParam(name = "user_id") Integer user_id) {
        commentService.deleteComment(comment_id);

        return "redirect:/admin/user/comment/" + user_id;
    }
//
//    Code Section for product(phone)
//
    @PostMapping("/product/add")
    public String addPhone(@ModelAttribute Phone phone) {
        phoneService.createPhone(phone);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/edit")
    public String editPhone(@ModelAttribute Phone phone) {
        phoneService.updatePhone(phone);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/delete")
    public String deletePhone(@RequestParam Integer id) {
        try {
            phoneService.deletePhone(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            return "redirect:/admin/product?error1=true";
        }
        return "redirect:/admin/product";
    }
//
//    Code Section for brand
//
    @PostMapping("/product/brand/add")
    public String addBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.createBrand(brand);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/edit")
    public String editBrand(@ModelAttribute Brand brand) {
        if (brandService.isBrandExists(brand.getName())) {
            return "redirect:/admin/product";
        }

        brandService.updateBrand(brand);

        return "redirect:/admin/product";
    }

    @PostMapping("/product/brand/delete")
    public String deleteBrand(@RequestParam Integer id) {
        try {
            brandService.deleteBrand(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            return "redirect:/admin/product?error2=true";
        }
        return "redirect:/admin/product";
    }

    // Code Section for order
    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("order", new Order());
        return "admin/orders";
    }

    @PostMapping("/orders/add")
    public String addOrder(@ModelAttribute("order") Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        orderService.saveOrder(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/editOrder";
    }

    @PostMapping("/orders/edit")
    public String updateOrder(@ModelAttribute("order") Order order) {
        orderService.updateOrder(order);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/delete")
    public String deleteOrder(@RequestParam("id") int id) {
        try {
            orderService.deleteOrder(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            return "redirect:/admin/orders?error=true";
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/getPhoneDetails/{phoneId}")
    public ResponseEntity<Phone> getPhoneDetails(@PathVariable int phoneId) {
        Phone phone = phoneService.getPhoneById(phoneId);
        return ResponseEntity.ok(phone);
    }

    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getOrderDetails/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.println("order: " + order);
            return new ResponseEntity<>(order, null, 200);
        }
        return ResponseEntity.notFound().build();
    }


}
