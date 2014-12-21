import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import javax.xml.bind.DatatypeConverter;

import org.apache.mahout.cf.taste.common.TasteException;


public class hashTree{
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://tweetmap.crsarl5br9bw.us-east-1.rds.amazonaws.com:3306/tweet";
    public Connection conn;
    public String table = "testPhoto";
    
    private static hashTree instance = null;
    private static HashMap<Integer, String> sToBinary= new HashMap<Integer, String>();
    
    private hashTree() {
    	conn = null;
    }
    
    public static hashTree getInstance() {
    	if (instance == null)
    		instance = new hashTree();
    	return instance;
    }
    
    public void close(){
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws IOException, TasteException {
    	// example: how to use!
    	hashTree ht = hashTree.getInstance();
    	
		ht.initDB();
		
		// input id: 5
		String[] close = ht.findFourClosest(5);
		for(String s: close){
			System.out.println(s);
		}

		ht.close();
    }
    
    public String getHash(int id){
    	String ret = null;
    	String sql = "select * from " + table + " where id = '"+ id +"'";

        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                ret = rs.getString("hash");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public void selectAll(){

    	String sql = "select * from " + table ;

        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
            	int id = rs.getInt("id");
                String value = rs.getString("hash");
                System.out.println(id + " " +value);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    
    public int getDiff(int target, int compare, String a, String b){
    	String sa = null;
    	if(sToBinary.containsKey(target)) sa = sToBinary.get(target);
    	else{
    		System.out.println(a);
    		byte[] aa = DatatypeConverter.parseHexBinary(a);
    		sa = toBinary(aa);
    		sToBinary.put(target, sa);
    	}
    	
    	String sb = null;
    	if(sToBinary.containsKey(compare)) sb = sToBinary.get(compare);
    	else{
    		byte[] bb = DatatypeConverter.parseHexBinary(b);
    		sb = toBinary(bb);
    		sToBinary.put(compare, sb);
    	}

		int counter = 0;
		
		for(int i=0;i<sa.length();i++){
			if(sa.charAt(i)!=sb.charAt(i))
				counter++;
		}
		return counter;
    }
    
    class SimilarPhoto{
    	int id;
    	int diff;
    	
    	SimilarPhoto(int id, int diff){
    		this.id = id;
    		this.diff = diff;
    	}
    }
    
    class PhotoComparator implements Comparator<SimilarPhoto>{

		@Override
		public int compare(SimilarPhoto o1, SimilarPhoto o2) {
			if(o1.diff < o2.diff)
				return -1;
			else if(o1.diff > o2.diff)
				return 1;
			else return 0;
		}
    	
    }
    
    public String[] findFourClosest(int target){
    	String[] ret = new String[4];
    	PriorityQueue<SimilarPhoto> queue = new PriorityQueue<SimilarPhoto>(4, new PhotoComparator());
 
    	String targetHash = getHash(target);
    	if(targetHash==null) {
    		System.out.println("No hash value associated with id " + target);
    		return ret;
    	}
    	String sql = "SELECT * FROM "+table;

        Statement stmt;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
            	int id = rs.getInt("id");
            	if(id == target) continue;
            	
                String value = rs.getString("hash");
 
                int diff = getDiff(target, id, targetHash, value);
                SimilarPhoto sp = new SimilarPhoto(id, diff);
                queue.add(sp);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int i = 0;

        while(!queue.isEmpty()){
        	SimilarPhoto sp = queue.remove();
        	ret[i] = Integer.toString(sp.id);
        	System.out.println("id : "+sp.id + " diff: "+ sp.diff);
        	i++;
        	if(i>=4) break; 
        }
        
        while(i<4){
        	ret[i] = null;
        	i++;
        }
        return ret;

    }
    
    public String toBinary(byte[] bytes){
    	StringBuilder sb = new StringBuilder(bytes.length*Byte.SIZE);
    	
    	for(int i=0;i<Byte.SIZE*bytes.length;i++){
    		sb.append((bytes[i/Byte.SIZE]<<i%Byte.SIZE&0x80)==0?'0':'1');
    	}
    	
    	return sb.toString();
    }
    
    public String hexToBin(String s) {
    	  return new BigInteger(s, 16).toString(2);
    	}
    

    public void initDB(){
    	File password = new File("pass.txt");
        String pass = null;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(password));
            pass = br.readLine();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    	try {
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "xiaojing", pass);
            System.out.println("connection finished");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void createTable(String name) {
    	this.table = name;
        System.out.println("Creating table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " +name+ " "+
                    "(id int not NULL, " +
                    " hash VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished creating table");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void deleteTable(String name) {
        System.out.println("Deleting table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "DROP TABLE " + name;
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished deleting table");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void insert(int id, String hash) {
        System.out.println("Inserting into table " +table );
        String sql = "INSERT INTO " +table + " VALUES (?,?)";
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, hash);

            ps.executeUpdate();

            ps.close();

            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}

