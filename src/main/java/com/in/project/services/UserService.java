package com.in.project.services;

import com.in.project.entities.User;

public interface UserService {

    public boolean registerUser(User user);
    public User loginUser(String email,String password);
    public User getUserById(Long id);
}
