package com.fighting.phonesellingweb.service;

import com.fighting.phonesellingweb.model.Role;
import com.fighting.phonesellingweb.model.User;
import com.fighting.phonesellingweb.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return (User) userRepository.findByEmail(email).orElseThrow();
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void register(String email, String password, String name, String phone) {
        User user = new User(email, passwordEncoder.encode(password), name, phone);
        userRepository.save(user);
    }

    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void setRole(String email, Role role) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
