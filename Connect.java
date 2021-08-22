package ujianOOP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Connect {
    private Statement st;
    private Connection con;

    public Connect() {
        try {
        	String url;
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	url="jdbc:mysql://localhost:3308/bike?user=bike&password=passwordbike";  
        	con = DriverManager.getConnection(url);  
        	System.out.println("Connection created"); 
            st = con.createStatement(1004, 1008);
            System.out.println("Connection Successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
        return rs;
    }

    public void executeUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Error");
        }
    }
}