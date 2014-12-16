import java.util.LinkedList;
import java.util.List;


public class PhotoInfo {
	public String type;
	public String date;
	public String dateFormat;
	public String title;
	public String content;
	public List<String> images;
	
	public PhotoInfo(String date, String title, String content, String image) {
		this.type = "blog_post";
		this.date = date;
		this.dateFormat = "D/MM/YY h:mm A";
		this.title = title;
		this.content = content;
		this.images = new LinkedList<>();
		this.images.add(image);
	}
}
