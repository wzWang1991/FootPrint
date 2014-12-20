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
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;

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
		instance.insertPhotoTable(9,"2014-7-30 19:00:45","I have never seen this sunset before!",40.693788, -74.047483,"https://s3.amazonaws.com/edu.columbia.cloud.footprint/StatueofLiberty/2.jpg");//		instance.insert("Users");
//		instance.deleteTable("Comments");
//		instance.deleteTable("Photoes");
//		instance.createPhotoTable();
//		instance.selectAllPhotoes();
//		instance.insertPhotoTable(2, "2014-9-10 12:12:12", "really good", 12.12, 23.23, "https://s3.amazonaws.com/footprint.linhuang/cen.jpeg");
//		instance.selectAllPhotoes();
//		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation("winter", 40, 42, -74, -72);
//		System.out.println(res.size());
//		for (int i = 0; i <res.size(); i++) {
//			System.out.println(res.get(i).title);
//			System.out.println(res.get(i).date);
//			System.out.println(res.get(i).content);
//			System.out.println(res.get(i).images.get(0));
//		}
//		instance.insertPhotoTable(2, "2014-12-10 12:10:10", "it is too cold, but I love it!!! Fantastic!!", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter.jpg");
//		instance.insertPhotoTable(1, "2014-11-10 23:08:31", "Bright Buildings !!!!!!!!!!! I will never leave NYC!!!!", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter1.jpg");
//		instance.insertPhotoTable(2, "2014-11-13 04:02:21", "Hey, Hey, my girlfriend is pretty, right?~~", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter2.jpg");
//		instance.insertPhotoTable(1, "2014-12-20 08:05:48", "A long way...", 0, 3, "https://s3.amazonaws.com/footprint.linhuang/winter3.jpg");
//		instance.deleteTable("Comments");
//		instance.createCommentsTable();
//		instance.insertCommentsTable(2, 5, "Pretty Cool!!", "2014-11-11 03:04:49");
//		instance.insertCommentsTable(2, 5, "I live besides the central park, how lucky I am!", "2014-11-11 09:05:28");
//		instance.insertCommentsTable(1, 5, "I should spend time go there!", "2014-11-11 18:05:48");
//		instance.insertCommentsTable(2, 6, "haha so pretty!", "2014-11-13 04:02:48");
//		instance.insertCommentsTable(1, 6, "I admire you!", "2014-11-13 08:05:48.0");
//		instance.insertCommentsTable(1, 6, "sooooooooooo beautiful! that is miracle", "2014-11-13 15:05:48.0");
//		instance.insertCommentsTable(1, 7, "Sooooo long!", "2014-11-11 03:04:49");
//		instance.insertCommentsTable(2, 7, "I wish to play with snow", "2014-12-25 10:24:31");
//		instance.insertCommentsTable(1, 7, "good picture", "2014-12-25 19:12:32");
//		instance.insertCommentsTable(2, 8, "I have been there once!", "2014-12-12 09:04:24");
//		instance.insertCommentsTable(1, 8, "how lovely this bridge is!", "2014-12-13 10:31:16");
//		instance.insertCommentsTable(2, 8, "Fantanstic!!!!!!!!!!!!!!!!", "2014-12-14 21:52:01");
//		instance.selectAllComments();
//		Photo P = instance.selectOnePhoto(6,3);
//		System.out.println(P.content);
//		for (int i = 0; i < P.similarPhotos.size(); i++) {
//			System.out.println(P.similarPhotos.get(i).photoId);
//		}
//		for (int i = 0; i < P.comments.size(); i++) {
//			Comment C = P.comments.get(i);
//			System.out.println(C.title);
//			System.out.println(C.content);
//		}
//		System.out.println(P.avgRank);
//		System.out.println(P.rating);
//		instance.createRatingTable();
//		instance.insertRatingsTable(1, 6, 5);
//		instance.insertRatingsTable(2, 7, 5);
//		instance.selectAllRatings();
//		System.out.println(instance.selectOneRating(1, 6));
//		instance.insertRatingsTable(1, 6, 3);
//		System.out.println(instance.selectOneRating(3, 6));
//		instance.insertPhotoTable(2, "2013-6-10 02:08:20", "This stone memorial sits at the Park's Fifth Avenue perimeter wall. It features an engraved profile of the renowned American newspaper editor for which it was named and an adjacent curved granite bench.", 40.791814, -73.953171, "https://s3-us-west-1.amazonaws.com/centralpark/arthur-brisbane-l.jpg");
//		instance.insertPhotoTable(3, "2014-2-18 17:23:41", "One of the Park's most picturesque landscapes, the reservoir is 40 feet deep and holds a billion gallons of water. ", 40.784962, -73.963374, "https://s3-us-west-1.amazonaws.com/centralpark/reservoir-l.jpg");
//		instance.insertPhotoTable(4, "2014-1-4 16:23:01", "Seneca Village may possibly have been Manhattan's first stable community of African American property owners.", 40.782781, -73.970122, "https://s3-us-west-1.amazonaws.com/centralpark/seneca-village-l.jpg");
//		instance.insertPhotoTable(8, "2014-4-27 14:31:09", "The Pond is one of Central Parks seven naturalistic water bodies. When Frederick Law Olmsted and Calvert Vaux designed Central Park, they imagined an immediate reprieve from the City's streets. ", 40.766109, -73.973985, "https://s3-us-west-1.amazonaws.com/centralpark/pond-l.jpg");
//		instance.insertPhotoTable(9, "2014-5-31 18:32:12", "Although today the Park's largest lawn without ballfields features people it was originally the home to a flock of pure bred sheep from 1864 until 1934.", 40.772638, -73.975305, "https://s3-us-west-1.amazonaws.com/centralpark/sheep-meadow-l.jpg");
//		instance.insertPhotoTable(10, "2009-8-7 08:04:24", "This area is popular with families and children because of the famous climbing sculptures, the story-telling programs, the model boats, the cafe, and the site in the children's classic Stuart Little. ", 40.774279, -73.967344, "https://s3-us-west-1.amazonaws.com/centralpark/conservatory-water-l.jpg");
//		instance.insertRatingsTable(1, 8, 4);
//		instance.insertRatingsTable(1, 9, 5);
//		instance.insertRatingsTable(1, 11, 4);
//		instance.insertRatingsTable(1, 12, 3);
//		instance.insertRatingsTable(1, 14, 5);
//		instance.insertRatingsTable(2, 8, 5);
//		instance.insertRatingsTable(2, 10, 5);
//		instance.insertRatingsTable(2, 11, 5);
//		instance.insertRatingsTable(2, 12, 3);
//		instance.insertRatingsTable(2, 15, 4);
//		instance.insertRatingsTable(3, 6, 2);
//		instance.insertRatingsTable(3, 7, 3);
//		instance.insertRatingsTable(3, 8, 4);
//		instance.insertRatingsTable(3, 10, 5);
//		instance.insertRatingsTable(3, 11, 3);
//		instance.insertRatingsTable(4, 12, 5);
//		instance.insertRatingsTable(4, 13, 5);
//		instance.insertRatingsTable(4, 14, 5);
//		instance.insertRatingsTable(4, 16, 5);
//		instance.insertRatingsTable(4, 18, 2);
//		instance.insertRatingsTable(5, 7, 3);
//		instance.insertRatingsTable(5, 9, 3);
//		instance.insertRatingsTable(5, 11, 3);
//		instance.insertRatingsTable(5, 13, 3);
//		instance.insertRatingsTable(5, 14, 4);
//		instance.insertRatingsTable(5, 15, 4);
//		instance.insertRatingsTable(5, 16, 4);
//		instance.insertRatingsTable(5, 17, 5);
//		instance.insertRatingsTable(6, 10, 3);
//		instance.insertRatingsTable(6, 11, 5);
//		instance.insertRatingsTable(6, 13, 3);
//		instance.insertRatingsTable(6, 14, 4);
//		instance.insertRatingsTable(7, 6, 5);
//		instance.insertRatingsTable(7, 7, 5);
//		instance.insertRatingsTable(7, 8, 5);
//		instance.insertRatingsTable(8, 7, 2);
//		instance.insertRatingsTable(8, 9, 2);
//		instance.insertRatingsTable(8, 11, 5);
//		instance.insertRatingsTable(8, 13, 4);
//		instance.insertRatingsTable(8, 14, 3);
//		instance.insertRatingsTable(8, 15, 2);
//		instance.insertRatingsTable(9, 13, 4);
//		instance.insertRatingsTable(9, 14, 5);
//		instance.insertRatingsTable(9, 15, 3);
//		instance.insertRatingsTable(9, 16, 2);
//		instance.insertRatingsTable(10, 12, 5);
//		instance.insertRatingsTable(10, 13, 3);
//		instance.insertRatingsTable(10, 14, 4);
//		instance.insertRatingsTable(10, 17, 2);
//		instance.insertRatingsTable(10, 18, 1);
//		instance.insertRatingsTable(10, 19, 5);
//		instance.insertRatingsTable(11, 19, 5);
//		instance.insertRatingsTable(11, 20, 5);
//		instance.generateCsvForRatings();
//		Recommender.recomender(1);
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
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, P.Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=3 and month(P.date)<=5) and U.UserID=P.UserID";
    		break;
    	case "summer":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, P.Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=6 and month(P.Date)<=8) and U.UserID=P.UserID";
    		break;
    	case "autumn":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, P.Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=9 and month(P.Date)<=11) and U.UserID=P.UserID";
    		break;
    	case "winter":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, P.Date, P.Des, P.url from Photoes P, "
    				+ "Users U where (month(P.Date)>=12 or month(P.Date)<=2) and U.UserID=P.UserID";
    		break;
    	case "all":
    		sql = "select P.Lat, P.Lon, U.Nickname, P.PhotoID, P.Date, P.Des, P.url from Photoes P, "
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
    
    public void insertCommentsTable (int userID, int photoID, String comments, String date) {
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO Comments (UserID, PhotoID, Comments, Date) " + 
    				"VALUES ("+userID+", "+photoID+", '"+comments+"', '"+date+"')";
    		stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Finished inserting into table");
        } catch (SQLException e) {
            e.printStackTrace();
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
                System.out.println("CommentsId:"+commentsId+" photoId:"+photoId+" userId:"+userId+
                		" Comments:"+comments+" date:"+date);
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
    	String sql = "Select P.Date, P.Des, P.url, U.Nickname from Photoes P, Users U where P.PhotoID="+photoID+
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
    	return new Photo(date, userName, des, url, null, comments, rating, avgRank);
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
            System.out.println("Finished inserting into table");
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
    
    public String getPhotoUrl(int photoId) {
    	Statement stmt;
    	try {
    		stmt = conn.createStatement();
    		String sql = "select URL from Photoes where PhotoID="+photoId;
    		ResultSet rs = stmt.executeQuery(sql);
    		while(rs.next()){
    			String url = String.valueOf(rs.getString("URL"));
    			return url;
    		}
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return "";
    }
}
