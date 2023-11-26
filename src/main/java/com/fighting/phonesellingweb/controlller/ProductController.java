package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.CommentService;
import com.fighting.phonesellingweb.service.PhoneService;
import com.fighting.phonesellingweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private UserService userService;
    private PhoneService phoneService;
    private CommentService commentService;

    @GetMapping({"", "/"})
    public String getProduct() {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getProductDetail(@CookieValue(name="email", required = false) String email,
                                   Model model,
                                   @PathVariable("id") int id) {
        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
        }

        model.addAttribute("phone", phoneService.findPhoneById(id));
        model.addAttribute("comments", phoneService.findPhoneById(id).getComments());

        return "phone_detail";
    }

    @PostMapping("/comment/add/{id}")
    public String addComment(@CookieValue(name = "email", required = false) String email,
                             @PathVariable("id") int id,
                             @RequestParam("content") String content) {
        Phone phone = phoneService.findPhoneById(id);
        User user = userService.findUserByEmail(email);
        commentService.addComment(phone, user, content);

        return "redirect:/product/" + id;
    }

    @PostMapping("/{product_id}/comment/delete/{comment_id}")
    public String deleteComment(@PathVariable("product_id") int product_id, @PathVariable("comment_id") int comment_id) {
        commentService.deleteComment(comment_id);

        return "redirect:/product/" + product_id;
    }
}
