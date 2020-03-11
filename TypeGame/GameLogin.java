package TeamWork;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 1. Enter username and password via keyboard
 * 2. Determine if the user is logged in successfully
 */

public class GameLogin {
    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Username");
        String username = sc.nextLine();
        System.out.println("Enter Password");
        String password = sc.nextLine();
        boolean flag = new GameLogin().login(username,password);
        if(flag){
            new GameFrame();
        }else {
            System.out.println("Username or password is incorrect");
        }
    }


    public boolean login(String username, String password) {
        if(username == null || password == null){
            return false;
        }
        Connection conn = null;
        String sql = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            sql ="select * from user where username ='"+username+"' and password ='"+password+"' ";
            stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          //boolean
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(stmt, conn, rs);
        }
        return false;
    }
}
