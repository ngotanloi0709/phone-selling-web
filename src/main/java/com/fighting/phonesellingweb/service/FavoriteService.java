package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Favorite;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.repository.FavoriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FavoriteService {
    private FavoriteRepository favoriteRepository;

    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    public boolean isFavorite(User user, int id) {
        return favoriteRepository.existsByUserAndPhoneId(user, id);
    }

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findAllByUser(user);
    }

    public Favorite findFavoriteByUserAndPhone(User user, Phone phone) {
        return favoriteRepository.findByUserAndPhone(user, phone);
    }

    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}
