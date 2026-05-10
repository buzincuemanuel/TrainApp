package com.buzincuemanuel.trainapp.controller;

import com.buzincuemanuel.trainapp.dto.UserRegistrationDto;
import com.buzincuemanuel.trainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDto form) {

        if (userService.findByEmail(form.getEmail()).isPresent()) {
            return "redirect:/register?error=email_exists";
        }

        userService.register(form);

        return "redirect:/login?success";
    }
}