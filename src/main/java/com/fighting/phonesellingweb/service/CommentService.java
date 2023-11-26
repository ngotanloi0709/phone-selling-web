package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Comment;
import com.fighting.phonesellingweb.model.Phone;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    public void addComment(Phone phone, User user, String content) {
        commentRepository.save(new Comment(phone, user, content));
    }

    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
}
