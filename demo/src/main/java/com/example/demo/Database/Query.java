package com.example.demo.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Query {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    获取所有用户信息
    public List<UserInfo> AllInfo() {
        String sql = "SELECT * FROM user_info";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }


    //查询成绩
    public List<Grades> AllGrades() {
        //左连接 User_Info, 以获取 name
        String sql = "SELECT g.id, u.`Name`, g.`subject`, g.score\n" +
                "FROM grades g LEFT JOIN User_Info u on g.student_id=u.Id";
        return jdbcTemplate.query(sql, new GradesRowMapper());
    }

    // 修改AllInfo方法
    public Page<UserInfo> AllInfoTest(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);       // 封装了分页的相关信息

        String sql = "SELECT * FROM user_info LIMIT ? OFFSET ?";        //limit 限制输出几行
                                                                        // offset 跳过的行数
        // 逻辑则是：假如当前页面为2，且每页只显示5行。 -> 2*5(跳过 offset), 显示 limit 5
        List<UserInfo> list = jdbcTemplate.query(sql, new Object[]{size, page * size}, new UserRowMapper());

        // 查询总数以供分页计算
        String countSql = "SELECT COUNT(*) FROM user_info";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);

        return new PageImpl<>(list, pageRequest, total);//创建'PageImpl对象' 实现了 page 接口，用于手动 分页
    }

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

    public Boolean dropAll() {
        String sql = "DELETE FROM user_info WHERE id > 100";
        jdbcTemplate.update(sql);
        return true;
    }



    public Boolean uploadData() {
        //开启 事物
        String transa = "START TRANSACTION;";
        jdbcTemplate.update(transa);

//        String sql = "INSERT INTO user_info (Name, Password, Role) VALUES (?, ?, ?)";
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                UserInfo user = userList.get(i);
//                ps.setString(1, user.getUsername());
//                ps.setString(2, user.getPassword());
//                ps.setString(3, user.getRole());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return userList.size();
//            }
//        });
        generateAndUploadData(100000);

        String endTransac = "COMMIT";
        jdbcTemplate.update(transa);

        return true;
    }


    private String generateRole() {
        return new Random().nextBoolean() ? "Admin" : "Guest";
    }

    // 生成随机字符串
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        while (length > 0) {
            result.append(characters.charAt(random.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }

    public void generateAndUploadData(int numberOfUsers) {
        String sql = "INSERT INTO user_info (Name, Password, Role) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, generateRandomString(8)); // Name
                ps.setString(2, generateRandomString(7)); // Password
                ps.setString(3, generateRole());         // Role
            }

            @Override
            public int getBatchSize() {
                return numberOfUsers;
            }
        });
    }

}

//    List<UserInfo> UserInfos = Query.yourMethod(某个UserId);


