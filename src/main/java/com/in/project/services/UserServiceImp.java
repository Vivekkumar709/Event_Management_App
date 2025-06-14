package com.in.project.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.project.entities.User;
import com.in.project.repositries.UserRepositry;

@Service
public class UserServiceImp implements UserService
{
    @Autowired
    private UserRepositry userRepositry;
    @Override
    public boolean registerUser(User user){
        try{
            userRepositry.save(user);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loginUser(String email, String password){
        User validuser = userRepositry.findByEmail(email);
        if(validuser != null && validuser.getPassword().equals(password)){
            return validuser;
        }
        return  null;
    }

    @Override
    public User getUserById(Long id){
        Optional<User> user=userRepositry.findById(id);
        return user.get();
    }
}

