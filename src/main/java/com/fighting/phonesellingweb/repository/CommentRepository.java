package com.fighting.phonesellingweb.repository;

import com.fighting.phonesellingweb.model.Comment;
import com.fighting.phonesellingweb.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    public List<Comment> findByPhone(Phone phone);
    public Comment findByUserIdAndPhoneId(Integer userId, Integer phoneId);

    public Comment findCommentById(int id);
}
