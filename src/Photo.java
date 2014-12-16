import java.util.LinkedList;
import java.util.List;


public class Photo {
	public String date;
	public String dateFormat;
	public String title;
	public String content;
	public List<String> simmilarUrls;
	public List<Comment> comments;
	
	public Photo(String date, String title, String content, String url, List<String> urls, List<Comment> comments) {
		this.date = date;
		this.dateFormat = "D/MM/YY h:mm A";
		this.title = title;
		this.content = content;
		this.simmilarUrls = new LinkedList<String>();
		this.simmilarUrls.add("https://s3.amazonaws.com/footprint.linhuang/winter.jpg");
		this.simmilarUrls.add("https://s3.amazonaws.com/footprint.linhuang/winter1.jpg");
		this.simmilarUrls.add("https://s3.amazonaws.com/footprint.linhuang/winter2.jpg");
		this.simmilarUrls.add("https://s3.amazonaws.com/footprint.linhuang/winter3.jpg");
		this.comments = comments;
	}
}
