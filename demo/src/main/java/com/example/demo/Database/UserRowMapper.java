package com.example.demo.Database;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserInfo> {


    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserInfo user = new UserInfo();
        user.setId(rs.getInt("Id"));
        user.setPassword(rs.getString("Password"));
        user.setUsername(rs.getString("Name"));
        user.setRole(rs.getString("Role"));
        return user;
    }
}
