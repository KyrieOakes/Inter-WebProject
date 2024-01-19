package com.example.demo.Outer;

import com.example.demo.Database.Query;
import com.example.demo.Database.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterValidation {
    @Autowired
    private Query query;

    @RequestMapping("/Register_Check")
    public String Acquire(@RequestParam("username") String username,
                          @RequestParam ("password") String password,
                          Model md) {
        // 检查是否有特殊符号 和 长度
//        System.out.printf("用户输入: "+username+" "+password+"\n");
        if(check_input(username) || check_input(password)){
            md.addAttribute("result", "您的输入有特殊符号！");
            return "/Register";
        } else if (username.length() > 10 || password.length()>10) {
            md.addAttribute("result", "您输入的密码或账户名过长！");
            return "/Register";
        }

        //数据库开始插入
        UserInfo newUser = new UserInfo(0,username,password,"Guest");
        Boolean RegisterResult = query.registerUser(newUser);

        //跳转网页
        if (RegisterResult){
            return "/Login";
        }else {
            md.addAttribute("result", "Error, Something wrong here.");
            return "/Register";
        }


    }

    private Boolean check_input(String content){
        // 正则表达式匹配任何非字母和非数字的字符
        String regex = "[^a-zA-Z0-9\\u4e00-\\u9fa5]";

        // 使用 String 的 matches 方法检查字符串是否匹配正则表达式
        Boolean result = content.matches(".*" + regex + ".*");

        return  result;
    }
}
