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
    @GetMapping("/orders/view/{id}")
    public String viewOrderItems(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderItem> orderItems = order.getOrderItems();
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        return "admin/view_order_items";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderItem> orderItems = order.getOrderItems();
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        return "admin/edit_order_status";    }

    @PostMapping("/orders/edit")
    public String updateOrder(@ModelAttribute Order order, Model model) {
        orderService.updateOrder(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("order", new Order());
        return "admin/order";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") int id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }


}
