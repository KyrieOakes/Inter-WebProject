package com.example.demo.Database;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GradesRowMapper implements RowMapper<Grades> {


    @Override
    public Grades mapRow(ResultSet rs, int rowNum) throws SQLException {
        Grades grades = new Grades();
        grades.setId(rs.getInt("id"));
        grades.setStudent_name(rs.getString("Name"));
        grades.setSubject(rs.getString("subject"));
        grades.setScore(rs.getFloat("score"));
        return grades;
    }
}

