package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Favorite;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    public boolean existsByUserAndPhoneId(User user, int id);

    public List<Favorite> findAllByUser(User user);

    public Favorite findByUserAndPhone(User user, Phone phone);
}
