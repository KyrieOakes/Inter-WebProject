package com.example.demo.Filter;

import com.example.demo.Database.Query;
import com.example.demo.Database.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(urlPatterns = "/Dashboard/*")
public class DashboardFilter implements Filter {

    @Autowired
    private Query query;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Custom initialization can go here if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            // Perform the cookie query when entering the page
            List<String> CookieFeedback = new ArrayList<>();
            CookieFeedback = CheckCurrentUser(query, request);
            for (int i = 0; i < CookieFeedback.size(); i++) {
                System.out.println(CookieFeedback.get(i));
            }

            if (CookieFeedback.isEmpty()){
                System.out.println("当前没有用户，如果报错，参考这条");
            }else {
                System.out.println("当前登陆用户为："+CookieFeedback.get(1));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Cleanup can go here if needed
    }

    public static List<String> CheckCurrentUser(Query query,HttpServletRequest request) {
//        Query query = new Query();
        Cookie[] cookies = request.getCookies();
        List<String> finalResult = new ArrayList<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieID = cookie.getName();
                String cookieValue = cookie.getValue();

                // Attempt to convert cookieID to integer
                try {
                    int id = Integer.parseInt(cookieID);

                    // Get all user info
                    List<UserInfo> userInfos = query.AllInfo();
                    for (UserInfo userInfo : userInfos) {
                        if (userInfo.getId().equals(id) && userInfo.getUsername().equals(cookieValue)) {
                            finalResult.add(cookieID);
                            finalResult.add(cookieValue);
                            // Return ID and name
                            return finalResult;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Log the exception or handle it as needed
                }
            }
        }
        return finalResult;
    }
}
