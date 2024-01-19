package com.example.demo.Outer;

import com.example.demo.Database.Query;
import com.example.demo.Database.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class Enroll_Validation {
    @Autowired
    private Query query;


    @RequestMapping("/Login_Check")
    public String Acquire(@RequestParam("username") String username,
                          @RequestParam ("password") String password,
                          HttpServletResponse response,
                          Model md){

        // Validate the account and pw
        List<UserInfo> UserInfos = query.AllInfo();
        Boolean result = false;
        Integer index = 0;
        for (int i = 0; i < UserInfos.size(); i++) {
            if(UserInfos.get(i).getUsername().equals(username) && UserInfos.get(i).getPassword().equals(password)){
                result = true;
                index = i;
                break;
            }
        }

        if (result) {
//             用户验证成功后，设置Cookie
            try {
                Integer targetId = UserInfos.get(index).getId();
                String ConId = Integer.toString(targetId);
                Cookie cookie = new Cookie(ConId, UserInfos.get(index).getUsername());
                cookie.setPath("/Dashboard");
                cookie.setMaxAge(180);
                response.addCookie(cookie);
            }catch (Exception e){
                System.out.printf(String.valueOf(e));
            }

            System.out.println("已为 "+UserInfos.get(index).getUsername()+"用户添加 Cookie");

            return "Dashboard/Homepage.html";
        } else {
            md.addAttribute("result", "Wrong information u entered!");
            return "Login";
        }

    }

}
