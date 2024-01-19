package com.example.demo.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper; //map 到 User 对象

@Service
public class Query {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //获取所有用户信息
    public List<UserInfo> AllInfo() {
        String sql = "SELECT * FROM user_info";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }


//    //获取所有用户信息 By ID
//    public List<UserInfo> AllById(int userId) {
//        String sql = "SELECT * FROM your_table WHERE user_id = ?";
//        return jdbcTemplate.query(sql, new Object[]{userId}, new UserRowMapper());
//    }

    //插入注册信息
    public Boolean registerUser (UserInfo userInfo) {
        String sql = "INSERT INTO user_info (Name, Password, Role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userInfo.getUsername(), userInfo.getPassword(), userInfo.getRole());

        //检查是否插入成功
        List<UserInfo> UserInfos = AllInfo();
        Boolean FinalResult = false;
        for (int i = 0; i < UserInfos.size(); i++) {
            if(UserInfos.get(i).getUsername().equals(userInfo.getUsername()) && UserInfos.get(i).getPassword().equals(userInfo.getPassword())){
                FinalResult = true;
                break;
            }
        }
        return FinalResult;
    }

    //删除用户 by UserID
    public void DeleteByUserid(Integer UserID){
        String sql = "DELETE FROM user_info WHERE Id = ?";
        jdbcTemplate.update(sql, UserID);
    }

    public void updateUserById(Integer userId, String username, String password, String role) {
//        System.out.printf("!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
        System.out.printf(userId+" "+username+" "+password+" "+role+"\n");
        // SQL 更新语句的基础部分
        StringBuilder sqlBuilder = new StringBuilder("UPDATE user_info SET ");

        // 用于存储参数的列表
        List<Object> params = new ArrayList<>();

        // 检查每个参数，如果非空则添加到 SQL 语句和参数列表中
        if (username != null && !username.isEmpty()) {
            sqlBuilder.append("username = ?, ");
            params.add(username);
        }
        if (password != null && !password.isEmpty()) {
            sqlBuilder.append("password = ?, ");
            params.add(password);
        }
        if (role != null && !role.isEmpty()) {
            sqlBuilder.append("role = ?, ");
            params.add(role);
        }

        // 移除最后一个逗号和空格
        if (!params.isEmpty()) {
            sqlBuilder.setLength(sqlBuilder.length() - 2); // 移除最后的逗号和空格
        }

        // 添加 WHERE 子句
        sqlBuilder.append(" WHERE id = ?");
        params.add(userId);

        // 转换 StringBuilder 为 String
        String sql = sqlBuilder.toString();

        // 使用 JdbcTemplate 执行 SQL 语句
        jdbcTemplate.update(sql, params.toArray());
    }

}

//    List<UserInfo> UserInfos = Query.yourMethod(某个UserId);


