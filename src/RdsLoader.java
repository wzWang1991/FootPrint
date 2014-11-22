import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RdsLoader {
	
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://footprint.cgr7pyr447yn.us-east-1.rds.amazonaws.com:3306/FPDatabase";
	Connection conn;

	private String password = null;;
    
    private static RdsLoader instance = null;
    private RdsLoader() {
    	conn = null;
    }
    
    public static RdsLoader getInstance() {
    	if (instance == null)
    		instance = new RdsLoader();
    	return instance;
    }
    
    public boolean isConnected() {
    	return conn != null;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    	if (conn == null)
    		init();
    }
    
    public boolean isPasswordSet() {
    	return this.password != null;
    }
	
    public static void main(String[] args) {
    	RdsLoader instanceTmp = getInstance();
    	instanceTmp.setPassword("cloudcomwyhq");
//    	instanceTmp.deleteTable("Users");
//    	instanceTmp.createUsersInfoTable();
//    	instanceTmp.insert("Users");
//    	instanceTmp.selectAll("Users");
//    	instanceTmp.checkPassword("carr@gmail.com","henry");
//    	System.out.println(instanceTmp.registerNewUser("qiuzhen@gmail.com", "zq2130", "qq"));
	}
    
    public void init() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "FPDatabase", password);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    private void createUsersInfoTable() {
        System.out.println("Creating table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE Users "+
                    "(UserID Integer NOT NULL AUTO_INCREMENT, " +
                    " Email VARCHAR(255), " +
                    " Password VARCHAR(255), " +
                    " FaceBook BOOLEAN, " +
                    " Nickname VARCHAR(255), " +
                    " PRIMARY KEY ( UserID ))";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished creating table Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    private void deleteTable(String name) {
        System.out.println("Deleting table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "DROP TABLE " + name;
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished deleting table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean registerNewUser(String inputEmail, String inputPassword, String nickName) {
    	System.out.println("Rigerster new User "+inputEmail);
    	User user=selectUser(inputEmail);
    	if (user!=null)
    		return false;
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Users (Email, Password, FaceBook, Nickname)"+
                    " VALUES ('"+inputEmail+"', '"+inputPassword+"', false, '"+nickName+"')";
    		stmt.executeUpdate(sql);
            stmt.close();
    	}
    	catch (SQLException e) {
            e.printStackTrace();
        } 
    	return true;
    }
   
    private void insert(String table) {
        System.out.println("Inserting into table " +table );
        Statement stmt;
//        String email="carr@gmail.com";
//        String password="henry";
//		Boolean facebook=true;
//        String nickname="carr";
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO " +table + " (Email, Password, FaceBook, Nickname)"+
                        " VALUES ('carr@gmail.com', 'henry', true, 'carr')";

            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished inserting into table");
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public void selectAll(String table) {
    	String sql = "SELECT * FROM "+table;
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int userID = rs.getInt("userID");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                Boolean faceBook = rs.getBoolean("FaceBook");
                String nickname = rs.getString("Nickname");
                System.out.println("userID:"+userID+" email:"+email+" password:"+password+
                		" faceBook:"+faceBook+" nickname:"+nickname);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public User selectUser(String inputEmail) {
    	String sql = "SELECT * from Users where Email='"+inputEmail+"'";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	int userID = rs.getInt("userID");
        		String email = rs.getString("Email");
        		String password = rs.getString("Password");
        		Boolean faceBook = rs.getBoolean("FaceBook");
        		String nickname = rs.getString("Nickname");
        		rs.close();
            	stmt.close();
    			return new User(userID,email,password,faceBook,nickname);
            }
    	}catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	return null;
    }
    
    public boolean checkPassword(String inputEmail, String intputPassword) {
    	User user=selectUser(inputEmail);
    	if (user!=null || intputPassword.equals(user.getPassword())) {
    		System.out.println("hi,"+user.getNickname()+"~ you are loging in!");
    		return true;
    	}
    	else return false;
    }
}
