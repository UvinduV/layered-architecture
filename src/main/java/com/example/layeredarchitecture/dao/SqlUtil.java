package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
public class SqlUtil {
    public static <T>T test(String sql,Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i+1,args[i]);
        }
        if (sql.startsWith("SELECT")|| sql.startsWith("select")) {
            return (T)pstm.executeQuery();
        }else {
            return(T)(Boolean)(pstm.executeUpdate()>0);
        }
    }
}