package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.dto.ChangePasswordRequest;
import com.fighting.phonesellingweb.dto.ProfileUpdateRequest;
import com.fighting.phonesellingweb.model.Order;
import com.fighting.phonesellingweb.model.OrderItem;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.OrderService;
import com.fighting.phonesellingweb.service.UserService;
import com.fighting.phonesellingweb.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private OrderService orderService;
    private JavaMailSender mailSender;


    private void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your password reset code");
        message.setText("Your password reset code is: " + verificationCode);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println(e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate a 6-digit code
        return String.valueOf(code);
    }

    @GetMapping("/login")
    public String getLogin(@CookieValue(name="email", required = false) String email){
        if (email != null) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/register")
    public String getRegister(@CookieValue(name="email", required = false) String email){
        if (email != null) {
            return "redirect:/";
        }

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@RequestParam Map<String, String> account, Model model) {
        try {
            String email = account.get("email");
            String password = account.get("password");
            String repeatPassword = account.get("repeat-password");
            String name = account.get("name");
            String phone = account.get("phone");

            if (!password.equals(repeatPassword)) {
                return "redirect:/account/register?error1=true";
            }

            if (userService.isUserExists(email)) {
                return "redirect:/account/register?error2=true";
            }

            userService.register(email, password, name, phone);

            return "redirect:/account/login";
        } catch (Exception e) {
            e.printStackTrace();

            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        CookieUtil.clearCookies(response);

        return "redirect:/";
    }

    @GetMapping("/profile")
    public String getDetailUser() {
        return "profile";
    }

    @PostMapping("/profile")
    public String updateUser(@CookieValue(name = "email", required = false) String email,
                             @ModelAttribute ProfileUpdateRequest request) {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            if (request.getAvatar().getOriginalFilename() != null && !request.getAvatar().getOriginalFilename().isEmpty()) {
                String fileName = StringUtils.cleanPath(request.getAvatar().getOriginalFilename());
                if (fileName.contains("..")) {
                    System.out.println("not a a valid file");
                }
                try {
                    user.setAvatar(request.getAvatar().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            user.setPhone(request.getPhone());
            user.setName(request.getName());
            user.setAddress(request.getAddress());

            userService.updateUser(user);

            return "redirect:/account/profile";
        }

        return "redirect:/";
    }

    @GetMapping("/change-pwd")
    public String changePwd() {
        return "change-password";
    }

    @PostMapping("/change-pwd")
    public String changePwd(@CookieValue(name = "email", required = false) String email,
                            @ModelAttribute ChangePasswordRequest request, HttpServletResponse response) {
        if (email == null) {
            return "login";
        }

        User user = userService.findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.updateUser(user);
        CookieUtil.clearCookies(response);

        return "redirect:/";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        HttpServletResponse response,
                                        Model model) {
        Optional<User> userOptional = userService.findUserByEmailOptional(email);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "No account found with the provided email.");
            return "forgot_password";
        }

        User user = userOptional.get();
        String verificationCode = generateVerificationCode();
        sendVerificationCode(email, verificationCode);

        CookieUtil.createCookie("forgot_password_email", email, response);
        CookieUtil.createCookie("verificationCode", verificationCode, response);
        CookieUtil.createCookie("codeTime", String.valueOf(System.currentTimeMillis()), response);

        return "redirect:/account/verify-code";
    }

    @GetMapping("/verify-code")
    public String showVerificationCodeForm(@CookieValue(name="verificationCode", required = false) String verificationCode) {
        if (verificationCode == null) {
            return "redirect:/account/login";
        }
        return "verify_code";
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String code,
                             @CookieValue(name="verificationCode", required = false) String verificationCode,
                             @CookieValue(name="codeTime", required = false) Long codeTime,
                             Model model) {
        if (verificationCode == null || !verificationCode.equals(code) || System.currentTimeMillis() - codeTime > 5 * 60 * 1000) {
            model.addAttribute("error", "Invalid or expired verification code.");
            return "verify_code";
        }

        return "redirect:/account/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@CookieValue(name="verificationCode", required = false) String verificationCode,
                                        @CookieValue(name="forgot_password_email", required = false) String email,
                                        @CookieValue(name="codeTime", required = false) Long codeTime, Model model) {
        if (verificationCode == null || email == null || codeTime == null || System.currentTimeMillis() - codeTime > 5 * 60 * 1000) {
            model.addAttribute("error", "Invalid or expired verification code.");
            return "verify_code";
        }

        model.addAttribute("forgot_password_email", email);
        model.addAttribute("token", verificationCode);

        return "reset_password";
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String password,
                                @RequestParam String confirmPassword,
                                HttpServletRequest   request, // Add HttpServletRequest as a parameter
                                HttpServletResponse response, Model model) {
        String email = CookieUtil.getCookieValue(request, "forgotPasswordEmail");

        if (email == null) {
            model.addAttribute("error", "No email found for password reset.");
            return "reset_password";
        }

        if (email == null) {
            model.addAttribute("error", "No email found in session.");
            return "reset_password";
        }

        userService.resetPassword(email, passwordEncoder.encode(password));
        CookieUtil.clearCookies(response);

        return "redirect:/account/login";
    }



    @GetMapping("/order")
    public String getUserOrders(Model model, Principal principal) {
        String currentUserName = principal.getName();
        User user = userService.findUserByEmail(currentUserName);
        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "user_order";
    }
    @GetMapping("/orders/view/{id}")
    public String viewOrderItems(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderItem> orderItems = order.getOrderItems();
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        return "user_view_order_items";
    }



}
