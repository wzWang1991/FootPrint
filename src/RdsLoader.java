import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;

import com.amazonaws.auth.PropertiesCredentials;

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
	
    public static void main(String[] args) throws IOException, TasteException {
    	RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.selectAll("Users");
		
    }
    
    public void alterTable() {
    	String sql = "DROP TABLE InPlace";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void deleteDuplicatePhoto() {
    	String sql = "SELECT orig.PhotoID, dupl.PhotoID from Photoes as orig, Photoes as dupl where orig.URL = dupl.URL and orig.PhotoID < dupl.PhotoID";
    	Statement stmt;
    	HashSet<Integer> duplicateIds = new HashSet<>();
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int origId = rs.getInt("orig.PhotoID");
            	int duplId = rs.getInt("dupl.PhotoID");
            	duplicateIds.add(duplId);
                System.out.println("origId:"+origId+"  duplId:"+duplId);
            }
            for (int i : duplicateIds) {
            	sql = "DELETE from Photoes where PhotoID = " + i;
            	stmt.executeUpdate(sql);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void deleteTestPhoto() {
    	String sql = "SELECT PhotoID, URL from Photoes where URL like '%edu.columbia.cloud.footprint/1%'";
    	Statement stmt;
    	List<Integer> deleteId = new LinkedList<>();
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	int duplId = rs.getInt("PhotoID");
            	String url = rs.getString("URL");
            	deleteId.add(duplId);
                System.out.println("origId:"+duplId+"  url:"+url);
            }
            for (int i : deleteId) {
            	sql = "DELETE from Photoes where PhotoID = " + i;
            	stmt.executeUpdate(sql);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void updatePhotoPlace() {
    	double[][] latitudes = new double[6][2];
    	double[][] longitude = new double[6][2];
    	latitudes[0][0] = 40.7645841;
    	latitudes[0][1] = 40.8005207;
    	latitudes[1][0] = 44.1324467;
    	latitudes[1][1] = 45.1089567;
    	latitudes[2][0] = 35.7502786;
    	latitudes[2][1] = 36.8654286;
    	latitudes[3][0] = 43.066956;
    	latitudes[3][1] = 43.132055;
    	latitudes[4][0] = 42.22788;
    	latitudes[4][1] = 42.3988669;
    	latitudes[5][0] = 36.1296229;
    	latitudes[5][1] = 36.380623;
    	
    	longitude[0][0] = -73.9815758;
    	longitude[0][1] = -73.9493995;
    	longitude[1][0] = -111.1559858;
    	longitude[1][1] = -109.8241801;
    	longitude[2][0] = -113.9798954;
    	longitude[2][1] = -111.5870658;
    	longitude[3][0] = -79.075321;
    	longitude[3][1] = -78.9421621;
    	longitude[4][0] = 71.191113;
    	longitude[4][1] = 70.9232011;
    	longitude[5][0] = -115.414625;
    	longitude[5][1] = -115.062072;
    	
    	String sql = "SELECT PhotoID, Lat, Lon from Photoes";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int photoId = rs.getInt("PhotoID");
            	double lat = rs.getDouble("Lat");
            	double lng = rs.getDouble("Lon");
                System.out.println("photoId:"+photoId+"  lat:"+lat + "  lng:"+lng);
                for (int i = 0; i < 6; i++) {
                	if (lat >= latitudes[i][0] && lat <= latitudes[i][1] && lng >= longitude[i][0] && lng <= longitude[i][1]) {
                		System.out.println(i + 1);
                		Statement insertStmt = conn.createStatement();
                		int placeId = i + 1;
                		String insertSql = "INSERT INTO InPlace (PhotoId, PlaceId) " + 
                				"VALUES ("+photoId+", "+placeId+")";
                		System.out.println(insertSql);
                		insertStmt.executeUpdate(insertSql);
                		insertStmt.close();
                	}
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
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
            conn.close();
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
            conn.close();
            System.out.println("Finished deleting table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean registerNewUser(String inputEmail, String inputPassword, String nickName) {
    	System.out.println("Rigerster new User "+inputEmail);
    	if (inputEmail == null || inputEmail.length() == 0)
    		return false;
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
            conn.close();
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
                        " VALUES ('Cloud.Computing@columbia.edu', 'interesting', true, 'cloud')";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
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
            conn.close();
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
    
    public int insertPhotoTable(int userID, String date, String des, double lat, double lon, String url) {
    	Statement stmt;
    	des = des.replace("'", "\\'");
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Photoes (UserID, Date, Des, Lat, Lon, URL) " + 
    				"VALUES ("+userID+", '"+date+"', '"+des+"', "+lat+", "+lon+", '"+url+"')";
    		System.out.println(sql);
    		stmt.executeUpdate(sql);
    		sql = "SELECT LAST_INSERT_ID()";
    		ResultSet rs = stmt.executeQuery(sql);
    		int id = 0;
    		while (rs.next()) {
    			id = rs.getInt(1);
    		}
            stmt.close();
            conn.close();
            System.out.println("Finished inserting into table");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
            conn.close();
            System.out.println("Finished creating table Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createPlaceRelationTable() {
    	Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE InPlace "+
                    "(RelationId Integer NOT NULL AUTO_INCREMENT, " +
            		" PhotoId Integer NOT NULL, " + 
                    " PlaceId Integer NOT NULL, " + 
                    " PRIMARY KEY ( RelationId )," +
                    "FOREIGN KEY ( PhotoId ) REFERENCES Photoes(PhotoID), " +
                    "FOREIGN KEY ( PlaceId ) REFERENCES Places(PlaceId)) ";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished creating table InPlace");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createPlaceTable() {
    	Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE Places "+
                    "(PlaceId Integer NOT NULL AUTO_INCREMENT, " +
            		" Name VARCHAR(255) NOT NULL, " + 
                    " ne_lat Double, " + 
                    " ne_lng Double, " + 
                    " sw_lat Double, " + 
                    " sw_lng Double, " + 
                    " location_lat Double, " + 
                    " location_lng Double, " + 
                    " PRIMARY KEY ( PlaceId ))";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished creating table Places");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertPlace(String name, double ne_lat, double ne_lng, double sw_lat, double sw_lng, double location_lat, double location_lng) {
    	this.init();
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Places (Name, ne_lat, ne_lng, sw_lat, sw_lng, location_lat, location_lng) " + 
    				"VALUES ('"+name+"', "+ne_lat+", "+ne_lng+", "+sw_lat+", "+sw_lng+", "+location_lat+", "+location_lng+")";
    		System.out.println(sql);
    		stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
            System.out.println("Finished inserting into table Place");
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
                String hash = rs.getString("hash");
                System.out.println("photoId:"+photoId+" userId:"+userId+" date:"+date+
                		" res:"+res+" lat:"+lat+" lon:"+lon+" url:"+url+" hash:"+hash);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void pushPhotoToQueue() {
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
                String hash = rs.getString("hash");
                
                String message = "{\"photoId\": " + photoId + ", \"url\": \""+ url +"\"}";
                System.out.println(message);
                PropertiesCredentials propertiesCredentials = new PropertiesCredentials(Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties"));
	        	Sqs sqs = new Sqs(propertiesCredentials);
	        	String queueUrl = "https://sqs.us-east-1.amazonaws.com/846524277299/FootPrint";
	        	sqs.sendMessage(queueUrl, message);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public List<PhotoInfo> filterPhotoByTimeAndLocation (String season, double latBegin, 
    		double latEnd, double lonBegin, double lonEnd) {
    	season = season.toLowerCase();
    	List<PhotoInfo> res = new ArrayList<PhotoInfo> ();
    	String sql = "";
    	String sql1 = " and P.Lat>="+latBegin+" and P.Lat<="+latEnd+" and P.Lon>="+lonBegin+" and P.Lon<="+lonEnd;
    	switch (season) {
    	case "spring": 
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=3 and month(P.date)<=5) and U.UserID=P.UserID";
    		break;
    	case "summer":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=6 and month(P.Date)<=8) and U.UserID=P.UserID";
    		break;
    	case "autumn":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=9 and month(P.Date)<=11) and U.UserID=P.UserID";
    		break;
    	case "winter":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=12 or month(P.Date)<=2) and U.UserID=P.UserID";
    		break;
    	case "all":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url from Photoes P, "
    				+ "Users U where U.UserID=P.UserID";
    	}
    	sql = sql + sql1;
    	System.out.println(sql);
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	int photoId = rs.getInt("PhotoID");
            	String userName = rs.getString("Nickname");
                String date = rs.getString("Date");
                String des = rs.getString("Des");
                String url = rs.getString("URL");
                double lat = rs.getDouble("Lat");
                double lon = rs.getDouble("Lon");
                res.add(new PhotoInfo(photoId, date, userName, des, url, lat, lon));
            }
            rs.close();
            stmt.close();
            conn.close();
    	} catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	return res;
    }
    
    public void createCommentsTable () {
    	System.out.println("Creating comments table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE Comments "+
                    "(CommentsID Integer NOT NULL AUTO_INCREMENT, " +
            		" UserID Integer NOT NULL, " + 
                    " PhotoID Integer NOT NULL, " + 
                    " Comments VARCHAR(255), " + 
                    " Date TIMESTAMP," + 
                    " PRIMARY KEY ( CommentsID ), " + 
            		" FOREIGN KEY ( UserID ) REFERENCES Users(UserID), " +
            		" FOREIGN KEY ( PhotoID ) REFERENCES Photoes(PhotoID))";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished creating table Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int insertCommentsTable (int userID, int photoID, String comments, String date) {
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Comments (UserID, PhotoID, Comments, Date) " + 
    				"VALUES ("+userID+", "+photoID+", '"+comments+"', '"+date+"')";
    		stmt.executeUpdate(sql);
    		sql = "SELECT LAST_INSERT_ID()";
    		ResultSet rs = stmt.executeQuery(sql);
    		int id = 0;
    		while (rs.next()) {
    			id = rs.getInt(1);
    		}
            stmt.close();
            conn.close();
            System.out.println("Finished inserting into table");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } 
    }
    
    public void selectAllComments() {
    	String sql = "SELECT * FROM Comments";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int commentsId = rs.getInt("CommentsID");
            	int photoId = rs.getInt("PhotoID");
                int userId = rs.getInt("UserID");
                String comments = rs.getString("Comments");
                String date = rs.getString("Date");
                double sentiment = rs.getDouble("Sentiment");
                System.out.println("CommentsId:"+commentsId+" photoId:"+photoId+" userId:"+userId+
                		" Comments:"+comments+" date:"+date+" sentiment:"+sentiment);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    } 
    
    public Photo selectOnePhoto(int photoID, int userID) {
    	String sql = "Select DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.url, U.Nickname from Photoes P, Users U where P.PhotoID="+photoID+
    			" and U.UserID=P.UserID";
    	String userName = "";
    	String date = "";
    	String des = "";
    	String url = "";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	userName = rs.getString("Nickname");
                date = rs.getString("Date");
                des = rs.getString("Des");
                url = rs.getString("URL");
                break;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	 
    	List<Comment> comments = new ArrayList<Comment>();
    	sql = "select C.Date, C.Comments, U.Nickname from Users U, Comments C"+
    			" where C.PhotoID="+photoID+" and U.UserID=C.UserID";
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	String commentUserName = rs.getString("Nickname");
                String commentDate = rs.getString("Date");
                String commentContent = rs.getString("Comments");
                comments.add(new Comment(commentUserName, commentContent, commentDate));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    	int rating = selectOneRating(userID, photoID);
    	String avgRank = calculateAvgRank(photoID);
    	return new Photo(date, userName, des, url, null, comments, rating, avgRank, photoID);
    }
    
    public void createRatingTable () {
    	System.out.println("Creating rating table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE Ratings "+
                    "(RatingsID Integer NOT NULL AUTO_INCREMENT, " +
            		" UserID Integer NOT NULL, " + 
                    " PhotoID Integer NOT NULL, " + 
                    " Rank VARCHAR(255), " + 
                    " PRIMARY KEY ( RatingsID ), " + 
            		" FOREIGN KEY ( UserID ) REFERENCES Users(UserID), " +
            		" FOREIGN KEY ( PhotoID ) REFERENCES Photoes(PhotoID))";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished creating table Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void selectAllRatings() {
    	String sql = "SELECT * FROM Ratings";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int ratingsId = rs.getInt("RatingsID");
            	int photoId = rs.getInt("PhotoID");
                int userId = rs.getInt("UserID");
                int rank = rs.getInt("Rank");
                System.out.println("RatingsId:"+ratingsId+" photoId:"+photoId+" userId:"+userId+
                		" Rank:"+rank);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void selectAllInplace() {
    	String sql = "SELECT * FROM InPlace";
    	Statement stmt;
    	try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	int photoId = rs.getInt("PhotoId");
                int placeId = rs.getInt("PlaceId");
                System.out.println("photoId:"+photoId+" placeId:"+placeId);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        	System.err.println("Reconnect to database.");
            e.printStackTrace();
        }
    }
    
    public void insertRatingsTable (int userId, int photoId, int rank) {
    	Statement stmt;
    	String sql = "";
    	if (selectOneRating(userId, photoId) == 0) {
    		sql = "INSERT INTO Ratings (UserID, PhotoID, Rank) " + 
    				"VALUES ("+userId+", "+photoId+", "+rank+")";
    	}
    	else {
    		sql = "UPDATE Ratings SET Rank="+rank+" where UserID="+userId;
    	}
    	try {
    		stmt = conn.createStatement();
    		stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished inserting into table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int selectOneRating (int userId, int photoId) {
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "select Rank from Ratings where UserID="+userId+" and PhotoID="+photoId;
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			return rs.getInt("Rank");
    		}
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return 0;
    }
    
    private String calculateAvgRank (int photoId) {
    	NumberFormat formatter = new DecimalFormat("#0.0");     
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "select AVG(Rank) as avg from Ratings where PhotoID="+photoId;
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			double res = rs.getDouble("avg");
    			return(formatter.format(res));
    		}
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return formatter.format(0);
    }
    
    public void generateCsvForRatings() throws IOException {
    	FileWriter fw = new FileWriter("ratings.csv");
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "select * from Ratings";
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			String userId = String.valueOf(rs.getInt("UserID"));
    			String photoId = String.valueOf(rs.getInt("PhotoID"));
    			String rank = String.valueOf(rs.getInt("Rank"));
    			String line = userId+","+photoId+","+rank;
    			fw.write(line);
    			fw.write("\n");
    		}
            stmt.close();
            fw.flush();
            fw.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String[] selectOnePhoto(int photoId) {
    	
    	Statement stmt;
    	String[] res = new String[5];
    	try {
    		stmt = conn.createStatement();
    		String sql = "select U.Nickname, DATE_FORMAT(P.Date, '%Y-%m-%d %h:%i:%s') as Date, P.Des, P.URL, Places.Name as placename from Photoes P, Users U, Places, InPlace where P.PhotoID="+photoId+
    				" and P.UserID=U.UserID and Places.PlaceId = InPlace.PlaceId and InPlace.PhotoId=" + photoId;
    		System.out.println(sql);
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			res[0] = rs.getString("URL");
    			res[1] = rs.getString("Date");
    			res[2] = rs.getString("Nickname");
    			res[3] = rs.getString("Des");
    			res[4] = rs.getString("placename");
//    			String searchPlaceSql = "select Places.Name as placename from InPlace, Places where Places.PlaceId = InPlace.PlaceId and InPlace.PhotoId=" + photoId;
//    			Statement stmtSearch;
//    			stmtSearch = conn.createStatement();
//    			ResultSet placeRs = stmtSearch.executeQuery(searchPlaceSql);
//    			while (placeRs.next()) {
//    				res[4] = placeRs.getString("placename");
//    			}
//    			placeRs.close();
//    			stmtSearch.close();
    			rs.close();
                stmt.close();
                conn.close();
                return res;
    		}
    		
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return null;
    }
    
    public void addNewColumnToComments() {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "Select * from Photoes order by Date";
            ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			String url = rs.getString("URL");
    			String photoId = rs.getString("PhotoId");
    			System.out.println("url"+url+" photoId:"+photoId);
    		}
            stmt.close();
            conn.close();
            System.out.println("Finished adding new column to table Comments");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createDescriptionFile() throws IOException {
    	Statement stmt;
    	String prefix = "reuters-out/reut2-000.sgm-";
    	int tmp = 0;
        try {
            stmt = conn.createStatement();
            String sql = "Select * from Photoes";
            ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			String description = rs.getString("Des");
    			String filename = prefix + tmp + ".txt";
    			tmp++;
    			System.out.println(filename);
    			FileWriter fw = new FileWriter(filename);
    			fw.write(description);
    			fw.flush();
                fw.close();
    		}
            stmt.close();
            conn.close();
            System.out.println("Finished adding new column to table Comments");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<ClusterPhoto> clusterPhoto(String topTerm) {
    	Statement stmt;
    	List<ClusterPhoto> res = new ArrayList<ClusterPhoto>();
        try {
            stmt = conn.createStatement();
            String sql = "Select P.PhotoID, P.URL, P.Date, P.Des, U.Nickname from Photoes P, Users U where LOWER(Des) Like '%"+topTerm+"%'"
            		+ " and U.UserID=P.UserID ORDER BY RAND() LIMIT 4";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			int photoId = rs.getInt("PhotoID");
    			String url = rs.getString("URL");
    			String date = rs.getString("Date");
    			String username = rs.getString("Nickname");
    			String description = rs.getString("Des");
    			res.add(new ClusterPhoto(photoId, url, date, username, description));
    		}
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public void closeConn() throws SQLException {
    	conn.close();
    }
}
