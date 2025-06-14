package com.in.project.repositries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.project.entities.Register;

public interface RegisterRepositry extends JpaRepository<Register,Long>
{
    public List<Register> findByUserId(Long userid);
    public void deleteByUserIdAndEventId(Long userId, Long eventId);


}
