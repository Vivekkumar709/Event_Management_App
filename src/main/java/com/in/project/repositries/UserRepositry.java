package com.in.project.repositries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.project.entities.User;

public interface UserRepositry  extends JpaRepository< User,Long>
{
    public User findByEmail(String email);
}
