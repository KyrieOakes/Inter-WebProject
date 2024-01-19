package com.example.demo.Outer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Outer_web {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/Login")
    public String Login(){
        return "Login";
    }

    @PostMapping("/Register")
    public String Register(){
        return "Register";
    }
    @PostMapping("/Homepage")
    public String Homepage(){
        return "Homepage";
    }



}
