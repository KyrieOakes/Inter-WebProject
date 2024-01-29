package com.example.demo;


import com.example.demo.Database.Grades;
import com.example.demo.Database.Query;
import com.example.demo.Database.UserInfo;
import com.example.demo.RandomGenerator.ExcelReader;
import com.example.demo.RandomGenerator.RandomDataToExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static com.example.demo.Filter.DashboardFilter.CheckCurrentUser;

@Controller
public class InnerController {
    @Autowired
    public Query query;



    @GetMapping("/Dashboard/UserList")
    public String showUsers(Model model,
                            @RequestParam(defaultValue = "0") int page,         //接受 分页参数 page=当前页数
                                                                                // size 每页显示的 行数
                            @RequestParam(defaultValue = "10") int size) {

        Page<UserInfo> userInfos = query.AllInfoTest(page, size);
//        System.out.println("！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
//        System.out.println("Total Pages: " + userInfos.getTotalPages());
        model.addAttribute("usersList", userInfos.getContent());        // Page 自带的function 获取list<userinfo>
        model.addAttribute("totalPages", userInfos.getTotalPages());    // 返回一个整数
        model.addAttribute("currentPage", page);


        return "Dashboard/UserList";
    }

    @RequestMapping("/AddRandomUser")
    public String AddRandomUser(){


        Boolean result = query.uploadData();

        return "redirect:/Dashboard/UserList";
    }

    @RequestMapping("RmRandomUsers")
    public String RmRandomUser(){
        query.dropAll();

        System.out.println("删除成功");
        return "redirect:/Dashboard/UserList";
    }


    //-----------------------------用户删除 部分-----------------------------
    //---------------------------------------------------------------------
    @GetMapping("/Dashboard/DeleteUser")
    public String showUsersDelete(Model model, HttpServletRequest request,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        List<UserInfo> userInfos = query.AllInfo();
        model.addAttribute("usersList_delete", userInfos);
        return "Dashboard/DeleteUser";
    }

    @PostMapping(value = "DeleteUser1")
    public String deleteSelectedUsers() {return "redirect:/Dashboard/UserList";}

    // -----------------------------用户创建部分-----------------------------
    //---------------------------------------------------------------------
    @GetMapping("/Dashboard/AddUser")
    public String AddUser(HttpServletRequest request){


        System.out.println("进入 AddUser界面");
        return "Dashboard/AddUser";
    };

    @RequestMapping (value = "AddUser_here", method = RequestMethod.POST)
    public String AcquireInfom(@RequestParam("username") String username,
                               @RequestParam ("password") String password,
                               @RequestParam ("role") String role,
                               HttpServletRequest request,
                               Model md) {



        System.out.println("用户输入的用户名: <"+username+">, 密码: <"+password+">, role: <"+role+">");
        // 检查是否有特殊符号 和 长度
        if (check_input(username)) {
            md.addAttribute("Alert", "您的输入的用户名有特殊符号！");
            return "Dashboard/AddUser";
        } else if ( check_input(password)) {
            md.addAttribute("Alert", "您的输入的密码有特殊符号！");
            return "Dashboard/AddUser";
        } else if (username.length() > 10 || password.length() > 10) {
            md.addAttribute("Alert", "您输入的密码或账户名过长！");
            return "Dashboard/AddUser";
        } else if (!(role.equals("Admin") || role.equals("Guest"))) {
            md.addAttribute("Alert", "只支持 Admin 以及 Guest！");
            return "Dashboard/AddUser";
        }

        //数据库开始插入
        UserInfo newUser = new UserInfo(0, username, password, role);
        Boolean RegisterResult = query.registerUser(newUser);

        System.out.println("添加用户成功: "+username+"  "+password);

        //跳转网页
        if (RegisterResult) {
            return "redirect:/Dashboard/UserList";
        } else {
            md.addAttribute("result", "Error, Something wrong here.");
            return "Dashboard/AddUser";
        }
    }


    // -----------------------------用户信息修改-----------------------------
    //---------------------------------------------------------------------
    @GetMapping("/Dashboard/ModifyUser")
    public String showUserList(Model model,HttpServletRequest request) {


        List<UserInfo> userInfos = query.AllInfo();
        model.addAttribute("usersList", userInfos);
        return "Dashboard/ModifyUser";
    }

    @RequestMapping (value = "updateUser",method = RequestMethod.POST)
    public String updateUser(@RequestParam("MyId") String myIdStr,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("role") String role) {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        int Id = 0;

        try {
            Id = Integer.parseInt(myIdStr); // 将字符串转换为整数

        } catch (NumberFormatException e) {
            System.out.println("转换错误: " + e.getMessage());
            // 处理转换异常，可能需要返回一个错误页面或消息
        }

        // 更新用户信息的逻辑
        query.updateUserById(Id,username, password, role);
        return "redirect:/Dashboard/UserList";
    }

    //-----------------------------home page-----------------------------
    //---------------------------------------------------------------------
    @GetMapping("/Dashboard/Homepage")
    public String Homepage(HttpServletRequest request) {


        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                String cookieName = cookies[i].getName();
                String cookieValue = cookies[i].getValue();
                System.out.printf("用户类型: "+cookieName+", 用户名："+cookieValue+"\n");
            }
        }

        return "Dashboard/Homepage";
    }


    //-----------------------------log out-----------------------------
    //---------------------------------------------------------------------
    @GetMapping ("/Dashboard/Logout")
    public String initLogout() {
        return "Dashboard/Logout";
    }

    @RequestMapping(value = "/Dashboard/LogoutButton",method = RequestMethod.POST)
    public String Logout(HttpServletResponse response, HttpServletRequest request) {
        List<String> CookieFeedback = new ArrayList<>();
        CookieFeedback = CheckCurrentUser(query, request);
        if( CookieFeedback.size() == 0){
            return "redirect:/Error/501";
        }
//        System.out.printf("现在登陆的用户 \n用户ID: "+CookieFeedback.get(0)+", 用户名: "+CookieFeedback.get(1)+"\n");

        Cookie cookie = new Cookie(CookieFeedback.get(0), null);
        //将`Max-Age`设置为0
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        System.out.printf("已删除名字为: "+CookieFeedback.get(1)+" 用户的cookie\n");

        return "redirect:/Login";
    }

    //-----------------------------Grades-----------------------------
    //---------------------------------------------------------------------

    @GetMapping("/Dashboard/ViewGrades")
    public String initGrades(Model model) {
        List<Grades> grades = query.AllGrades();
        model.addAttribute("grades", grades);

        // 添加日志输出
        System.out.println("Grades size: " + grades.size());
        grades.forEach(grade -> System.out.println(grade));

        return "Dashboard/ViewGrades";
    }





    //-----------------------------检查 是否有 非法输入-----------------------------
    //---------------------------------------------------------------------
    private Boolean check_input(String content){
        // 正则表达式匹配任何非字母和非数字的字符
        String regex = "[^a-zA-Z0-9\\u4e00-\\u9fa5]";

        // 使用 String 的 matches 方法检查字符串是否匹配正则表达式
        Boolean result = content.matches(".*" + regex + ".*");

        return  result;
    }


}
