import java.util.LinkedList;
import java.util.List;


public class Photo {
	public String url;
	public String date;
	public String dateFormat;
	public String title;
	public String content;
	public List<SimilarPhoto> similarPhotoes;
	public List<Comment> comments;
	
	public Photo(String date, String title, String content, String url, List<String> urls, List<Comment> comments) {
		this.url = url;
		this.date = date;
		this.dateFormat = "D/MM/YY h:mm A";
		this.title = title;
		this.content = content;
		this.similarPhotoes = new LinkedList<SimilarPhoto>();
		this.similarPhotoes.add(new SimilarPhoto(8,"https://s3.amazonaws.com/footprint.linhuang/winter.jpg"));
		this.similarPhotoes.add(new SimilarPhoto(5,"https://s3.amazonaws.com/footprint.linhuang/winter1.jpg"));
		this.similarPhotoes.add(new SimilarPhoto(6,"https://s3.amazonaws.com/footprint.linhuang/winter2.jpg"));
		this.similarPhotoes.add(new SimilarPhoto(7,"https://s3.amazonaws.com/footprint.linhuang/winter3.jpg"));
		this.comments = comments;
	}
}
