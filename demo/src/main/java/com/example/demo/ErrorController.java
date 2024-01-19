package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/Error/501")
    public String NotLogin(){
        return "/Error/501";
    }

}
