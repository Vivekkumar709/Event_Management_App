package com.in.project.repositries;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.project.entities.Event;


public interface EventRepositry extends JpaRepository<Event,Long>{

    public List<Event> findByIdIn(List<Long> ids);

}
