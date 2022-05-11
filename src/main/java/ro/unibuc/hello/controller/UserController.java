package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.User;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Counted(value = "user.register.counter", description = "Times an user has been registered")
    @PostMapping("/register")
    @ResponseBody
    public User registerUser(@RequestBody User user){
        userRepository.save(user);
        return user;
    }

    @Timed(value = "user.register.time", description = "Time taken to get registered users")
    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
