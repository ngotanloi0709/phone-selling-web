package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.CartItem;
import com.fighting.phonesellingweb.model.Order;
import com.fighting.phonesellingweb.model.OrderItem;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.CartService;
import com.fighting.phonesellingweb.service.OrderService;
import com.fighting.phonesellingweb.service.UserService;
import com.fighting.phonesellingweb.service.VNPayService;
import com.fighting.phonesellingweb.util.CookieUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class PaymentController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UserService UserService;
    private final VNPayService vnPayService;
    private final UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @PostMapping("/payment/processCashOnDelivery")
    public String processCashOnDelivery(HttpServletRequest request, Model model, HttpServletResponse response) {
        String email = getCookieValue(request, "email");
        User user = UserService.findUserByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getCartItems(user);
        Order order = orderService.createOrder(user, cartItems);


        model.addAttribute("order", order);
        model.addAttribute("orderItems", order.getOrderItems());
        sendOrderConfirmationEmail(user.getEmail(), order, user);

        // Clear the ordered items from the cart after the order has been placed and the email has been sent
        cartService.clearCart(user, order);
        // Update the cart cookie
        CookieUtil.createCookie("cart", String.valueOf(cartService.getCartItems(user).size()), response);

        return "redirect:/payment/orderConfirmation?id=" + order.getId();
    }



    private final String emailTemplate = "<html>\n" +
            "<head>\n" +
            "    <title>Xác nhận đơn hàng</title>\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; }\n" +
            "        .table { width: 100%; border-collapse: collapse; }\n" +
            "        .th, .td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n" +
            "        .th { background-color: #f2f2f2; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h1 class=\"mt-4\">Xác nhận đơn hàng</h1>\n" +
            "        <p>Xin chào, ${userName}</p>\n" +
            "        <p>Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi. Dưới đây là thông tin đơn hàng của bạn:</p>\n" +
            "        <ul>\n" +
            "            <li>Ngày đặt hàng: ${orderDate}</li>\n" +
            "            <li>Địa chỉ giao hàng: ${userAddress}</li>\n" +
            "            <li>Số điện thoại: ${userPhone}</li>\n" +
            "            <li>Email: ${userEmail}</li>\n" +
            "        </ul>\n" +
            "        <p class=\"font-weight-bold\">Tổng cộng: ${totalAmount}</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";


    private String buildEmailContent(User user, Order order) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(order.getId());
        System.out.println("Order Items: " + order.getOrderItems());

        return emailTemplate
                .replace("${userName}", user.getName())
                .replace("${orderDate}", order.getOrderDate().toString())
                .replace("${userAddress}", user.getAddress())
                .replace("${userPhone}", user.getPhone())
                .replace("${userEmail}", user.getEmail())
                .replace("${totalAmount}", String.format("%.2f", order.getTotalAmount()));
    }








    @SneakyThrows
    private void sendOrderConfirmationEmail(String to, Order order, User user) {
        String emailContent = buildEmailContent(user, order);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setText(emailContent, true); // set to true to send HTML
        helper.setTo(to);
        helper.setSubject("Xác nhận đơn hàng");
        helper.setFrom("phuongtdt9902@gmail.com");
        mailSender.send(message);
    }



    @GetMapping("/payment/orderConfirmation")
    public String orderConfirmation(@RequestParam("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        model.addAttribute("orderItems", order.getOrderItems());
        return "orderConfirmation";
    }


    @PostMapping("/payment/processVNPayPayment")
    public String processVNPayPayment(HttpServletRequest request, Model model) {
        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartService.getCartItems(user);
        Order order = orderService.createOrder(user, cartItems);

        int totalAmount = (int) (order.getTotalAmount() * 100); // Chuyển đổi sang đơn vị tiền tệ VNPay

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalAmount, "Order Description", baseUrl);

        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/api/payment/vnpay-payment")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        // Xử lý các tham số từ VNPay
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String secureHash = request.getParameter("vnp_SecureHash");
        // Các tham số khác như vnp_Amount, vnp_BankCode, vnp_PayDate, v.v.

        // Kiểm tra secureHash hoặc các thông tin khác để xác minh giao dịch

        String email = getCookieValue(request, "email");
        User user = userService.findUserByEmail(email);



        if (user == null) {
            return "redirect:/login";
        }
        Order order = orderService.findLatestOrderByUser(user);

        // Giả sử rằng responseCode = "00" là thành công
        if ("00".equals(responseCode)) {
            order.setStatus(Order.OrderStatus.COMPLETED);
            sendOrderConfirmationEmail(user.getEmail(), order, user);
        } else {
            order.setStatus(Order.OrderStatus.CANCELLED);
            model.addAttribute("message", "Thanh toán không thành công hoặc đã bị hủy.");
        }

        orderService.saveOrder(order);
        cartService.clearCart(user, order);

        return "00".equals(responseCode) ? "redirect:/payment/orderConfirmation?id=" + order.getId() : "payment_failure";
        }
    }










