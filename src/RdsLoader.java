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
import java.util.ArrayList;
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
    }
    
    public boolean isPasswordSet() {
    	return this.password != null;
    }
	
    public static void main(String[] args) {
    	RdsLoader instance = RdsLoader.getInstance();
		instance.init();
//		instance.deleteTable("Photoes");
//		instance.createPhotoTable();
//		instance.selectAllPhotoes();
//		instance.insertPhotoTable(2, "2014-9-10 12:12:12", "really good", 12.12, 23.23, "https://s3.amazonaws.com/footprint.linhuang/cen.jpeg");
//		instance.selectAllPhotoes();
		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation("winter", 0, 0, 0, 0);
		System.out.println(res.size());
		for (int i = 0; i <res.size(); i++) {
			System.out.println(res.get(i).title);
			System.out.println(res.get(i).date);
			System.out.println(res.get(i).content);
			System.out.println(res.get(i).images.get(0));
		}
//		instance.insertPhotoTable(2, "2014-12-10 12:10:10", "it is too cold, but I love it!!! Fantastic!!", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter.jpg");
//		instance.insertPhotoTable(1, "2014-11-10 23:08:31", "Bright Buildings !!!!!!!!!!! I will never leave NYC!!!!", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter1.jpg");
//		instance.insertPhotoTable(2, "2014-11-13 04:02:21", "Hey, Hey, my girlfriend is pretty, right?~~", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter2.jpg");
//		instance.insertPhotoTable(1, "2014-12-20 08:05:48", "A long way...", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter3.jpg");
    }
    
    public void init() {
        try {
        	setPassword("cloudcomwyhq");
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
    	UserInfo user=selectUser(inputEmail);
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
    
    public UserInfo selectUser(String inputEmail) {
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
    			return new UserInfo(userID,email,password,faceBook,nickname);
            }
    	}catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	return null;
    }
    
    public UserInfo checkPassword(String inputEmail, String intputPassword) {
    	UserInfo userInfo=selectUser(inputEmail);
    	if (userInfo!=null && intputPassword.equals(userInfo.getPassword())) {
    		System.out.println("hi,"+userInfo.getNickname()+"~ you are loging in!");
    		return userInfo;
    	}
    	else return null;
    }
    
    public void insertPhotoTable(int userID, String date, String des, double lat, double lon, String url) {
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Photoes (UserID, Date, Des, Lat, Lon, URL) " + 
    				"VALUES ("+userID+", '"+date+"', '"+des+"', "+lat+", "+lon+", '"+url+"')";
    		System.out.println(sql);
    		stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished inserting into table");
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public void createPhotoTable() {
    	System.out.println("Creating photo table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE Photoes "+
                    "(PhotoID Integer NOT NULL AUTO_INCREMENT, " +
            		" UserID Integer NOT NULL, " + 
                    " Date TIMESTAMP, " + 
                    " Des VARCHAR(255), " + 
            		" Lat Double, " +
                    " Lon Double, " + 
            		" URL VARCHAR(255), " + 
                    " PRIMARY KEY ( PhotoID ), " + 
            		" FOREIGN KEY ( UserID ) REFERENCES Users(UserID))";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished creating table Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void selectAllPhotoes() {
    	String sql = "SELECT * FROM Photoes";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int photoId = rs.getInt("PhotoID");
                int userId = rs.getInt("UserID");
                String date = rs.getString("Date");
                String res = rs.getString("Des");
                double lat = rs.getDouble("Lat");
                double lon = rs.getDouble("Lon");
                String url = rs.getString("url");
                System.out.println("photoId:"+photoId+" userId:"+userId+" date:"+date+
                		" res:"+res+" lat:"+lat+" lon:"+lon+" url:"+url);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public List<PhotoInfo> filterPhotoByTimeAndLocation (String season, double latBegin, 
    		double latEnd, double lonBegin, double lonEnd) {
    	season = season.toLowerCase();
    	List<PhotoInfo> res = new ArrayList<PhotoInfo> ();
    	String timeBegin = "";
    	String timeEnd = "";
    	switch (season) {
    	case "spring": 
    		timeBegin = "2014-02-01 00:00:00";
    		timeEnd = "2014-04-30 23:59:59";
    		break;
    	case "summer":
    		timeBegin = "2014-05-01 00:00:00";
    		timeEnd = "2014-07-31 23:59:59";
    		break;
    	case "autumn":
    		timeBegin = "2014-08-01 00:00:00";
    		timeEnd = "2014-10-31 23:59:59";
    		break;
    	case "winter":
    		timeBegin = "2014-11-01 00:00:00";
    		timeEnd = "2015-01-31 23:59:59";
    		break;
    	case "all":
    		timeBegin = "2000-01-01 00:00:00";
    		timeEnd = "2020-01-01 00:00:00";
    	}
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "select U.Nickname, P.Date, P.Des, P.url from Photoes P, Users U where Date>='"
    				+timeBegin+"' and Date<='"+timeEnd+"' and U.UserID=P.UserID";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	String userName = rs.getString("Nickname");
                String date = rs.getString("Date");
                String des = rs.getString("Des");
                String url = rs.getString("URL");
                res.add(new PhotoInfo(date, userName, des, url));
            }
            rs.close();
            stmt.close();
    	} catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	return res;
    }
}
