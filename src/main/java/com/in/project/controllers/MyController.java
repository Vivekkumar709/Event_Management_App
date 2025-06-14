package com.in.project.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.in.project.entities.Event;
import com.in.project.entities.Register;
import com.in.project.entities.User;
import com.in.project.repositries.EventRepositry;
import com.in.project.repositries.RegisterRepositry;
import com.in.project.services.EventService;
import com.in.project.services.RegisterService;
import com.in.project.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyController {

    @Autowired
    private UserService userService;

    @GetMapping("/regPage")
    public String openRegPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/regForm")
    public String submitRegForm(@ModelAttribute("user") User user, Model model) {
        boolean status = userService.registerUser(user);
        if (status) {
            model.addAttribute("successmsg", "User Registered Successfully");
        } else {
            model.addAttribute("errormsg", "User not Registered");
        }
        return "register";
    }

    @GetMapping("/loginPage")
    public String openLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginForm")
    public String submitloginUser(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        HttpSession hs = request.getSession();
        User validuser = userService.loginUser(user.getEmail(), user.getPassword());
        if (validuser != null) {
            hs.setAttribute("userName", validuser.getName());
            hs.setAttribute("userId", validuser.getId());
            hs.setAttribute("userEmail", validuser.getEmail());
            hs.setAttribute("userPhoneno", validuser.getPhoneno());
            hs.setAttribute("userImg", validuser.getImage());
            hs.setAttribute("userAdmin", validuser.isAdmin());
            model.addAttribute("modelName", hs.getAttribute("userName"));
            return "redirect:/homePage";
        } else {
            model.addAttribute("errormsg", "Email Id and Password is incorrect");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logoutuser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        return "redirect:/homePage";
    }

    // -----eveents---------
    @Autowired
    private EventService eventService;

    @GetMapping("/createevntPage")
    public String openEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "createevent";
    }

    @PostMapping("/createeventForm")
    public String createEventForm(@ModelAttribute("event") Event event, Model model) {
        boolean status = eventService.createEvent(event);
        if (status) {
            model.addAttribute("successmsg", "Event is created successfully");
        } else
            model.addAttribute("errormsg", "Event is not created due to some error");
        return "redirect:/events";
    }

    // ----------delete event by admin --------------

    @PostMapping("/deleteEvent/{id}")
    public String postMethodName(@PathVariable("id") Long eventid) {
        eventRepositry.deleteById(eventid);
        return "redirect:/events";
    }

    // -----event list---------
    @Autowired
    private EventRepositry eventRepositry;

    @GetMapping("/events")
    public String showEvents(Model model) {
        List<Event> eventList = eventRepositry.findAll();
        model.addAttribute("events", eventList);
        return "event";
    }

    // ----gotoprofile----------
    @Autowired
    private RegisterRepositry registerRepositry;

    @GetMapping("/gotoProfile")
    public String gotoprofile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            List<Register> regevent = registerRepositry.findByUserId(userId);

            List<Long> eventIds = regevent.stream()
                    .map(Register::getEventId)
                    .collect(Collectors.toList());

            List<Event> events = eventRepositry.findByIdIn(eventIds);
            model.addAttribute("events", events);

            model.addAttribute("modelName", session.getAttribute("userName"));
            return "profile";
        }
        return "redirect:/loginPage"; // redirect to login if session is missing
    }

    // ----------user Register for event---------

    @GetMapping("/eventRegister/{id}")
    public String gotoforRegister(@PathVariable("id") Long eventid, Model model) {
        Optional<Event> mainEvent = eventRepositry.findById(eventid);
        if (mainEvent.isPresent()) {
            model.addAttribute("event", mainEvent.get());
            return "eventDetail";
        } else
            return "profile";
    }

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register/{id}")
    public String postMethodName(@PathVariable("id") Long eventid, @ModelAttribute("event") Event event, HttpSession hs,
            Model model) {

        Long userId = (Long) hs.getAttribute("userId");
        if (userId == null)
            return "redirect:/loginPage";
        boolean check = registerService.registerEvent(eventid, userId);
        if (check) {
            model.addAttribute("successmsg", "registration is successfull");
            model.addAttribute("modelName", hs.getAttribute("userName"));
            return "redirect:/gotoProfile";
        } else {
            return "login";
        }
    }

    // ------------HomePage handling-------------

    @GetMapping("/homePage")
    public String openHomePage(Model model) {
        List<Event> eventList = eventRepositry.findAll();
        model.addAttribute("events", eventList);
        return "home";
    }

    // ------About us -----------
    @GetMapping("/aboutPage")
    public String openAboutUs() {
        return "about-us";
    }

    // ----------------update profile -------------
    @GetMapping("/updateProfilePage")
    public String openUpdateProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/loginPage";

        User currentUser = userService.getUserById(userId);
        model.addAttribute("user", currentUser);

        return "updateProfile";
    }

    @PostMapping("/saveProfile")
    public String saveupdatedProfile(@ModelAttribute("user") User user, Model model, HttpSession session) {
        Long userid = (Long) session.getAttribute("userId");
        User currentUser = userService.getUserById(userid);
        currentUser.setName(user.getName());
        currentUser.setPhoneno(user.getPhoneno());
        currentUser.setPassword(user.getPassword());
        session.setAttribute("userName", currentUser.getName());
        session.setAttribute("userPhoneno", currentUser.getPhoneno());
        session.setAttribute("userPassword", currentUser.getPassword());
        session.setAttribute("userImg", currentUser.getImage());

        boolean st = userService.registerUser(currentUser);
        if (st)
            return "profile";
        else {
            model.addAttribute("errormsg", "Profile didn't updated");
            return "updateProfile";
        }
    }

    @PostMapping("/deleteRegister/{id}")
    public String ToDeleteRegister(@PathVariable("id") Long eventId,HttpSession hs) {
        Long userId= (Long)hs.getAttribute("userId");
        registerRepositry.deleteByUserIdAndEventId(userId, eventId);
        return "redirect:/gotoProfile";
    }

}
