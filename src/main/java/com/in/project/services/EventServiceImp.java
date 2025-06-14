package com.in.project.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.project.entities.Event;
import com.in.project.repositries.EventRepositry;

@Service
public class EventServiceImp implements EventService
{
    @Autowired
    private EventRepositry eventrepositry;
    @Override
    public boolean createEvent(Event event){
        try{
            eventrepositry.save(event);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
