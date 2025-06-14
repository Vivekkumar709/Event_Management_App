package com.in.project.services;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.project.entities.Register;
import com.in.project.repositries.RegisterRepositry;

@Service
public class RegisterServiceImp implements RegisterService {
    @Autowired
    private RegisterRepositry registerRepositry;

    @Override
    public boolean registerEvent(Long eventid, Long userid) {
        if (userid == null || eventid == null)
            return false;
        else {
            try {
                Register registration = new Register();
                registration.setEventId(eventid);
                registration.setUserId(userid);
                registerRepositry.save(registration);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    }

}
