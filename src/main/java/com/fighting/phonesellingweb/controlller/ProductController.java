package com.fighting.phonesellingweb.controlller;

import com.fighting.phonesellingweb.model.Favorite;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.ProductViewHistory;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;

@Controller
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private UserService userService;
    private PhoneService phoneService;
    private CommentService commentService;
    private ProductViewHistoryService productViewHistoryService;
    private FavoriteService favoriteService;

    @GetMapping({"", "/"})
    public String getProduct() {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getProductDetail(@CookieValue(name="email", required = false) String email, @PathVariable("id") int id, Model model) {
        User user = userService.findUserByEmail(email);
        Phone phone = phoneService.findPhoneById(id);
        model.addAttribute("phone", phone);
        model.addAttribute("comments", phone.getComments());

        if (user != null) {
            ProductViewHistory history = new ProductViewHistory();
            history.setUser(user);
            history.setPhone(phone);
            history.setViewedAt(LocalDateTime.now());
            productViewHistoryService.save(history);
        }

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

    @PostMapping("/favorite/add/{id}")
    public String addToFavorites(@CookieValue(name="email", required = false) String email, @PathVariable int id) {
        User user = userService.findUserByEmail(email);

        if (user != null) {
            if (favoriteService.isFavorite(user, id)) {
                return "redirect:/product/" + id;
            }

            Phone phone = phoneService.findPhoneById(id);
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setPhone(phone);
            favoriteService.save(favorite);
        }

        return "redirect:product/" + id;
    }

    @PostMapping("/favorite/delete/{id}")
    public String deleteFromFavorites(@CookieValue(name="email", required = false) String email, @PathVariable int id) {
        User user = userService.findUserByEmail(email);

        if (user != null) {
            if (!favoriteService.isFavorite(user, id)) {
                return "redirect:/favorite";
            }

            Phone phone = phoneService.findPhoneById(id);
            Favorite favorite = favoriteService.findFavoriteByUserAndPhone(user, phone);
            favoriteService.delete(favorite);
        }

        return "redirect:/favorite";
    }
}
