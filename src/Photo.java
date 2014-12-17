import java.util.LinkedList;
import java.util.List;


public class Photo {
	public String url;
	public String date;
	public String dateFormat;
	public String title;
	public String content;
	public List<SimilarPhoto> similarPhotos;
	public List<Comment> comments;
	public int rating;
	public String avgRank;
	
	public Photo(String date, String title, String content, String url, List<String> urls, List<Comment> comments,
			int rating, String avgRank) {
		this.url = url;
		this.date = date;
		this.dateFormat = "D/MM/YY h:mm A";
		this.title = title;
		this.content = content;
		this.similarPhotos = new LinkedList<SimilarPhoto>();
		this.similarPhotos.add(new SimilarPhoto(8,"https://s3.amazonaws.com/footprint.linhuang/winter.jpg"));
		this.similarPhotos.add(new SimilarPhoto(5,"https://s3.amazonaws.com/footprint.linhuang/winter1.jpg"));
		this.similarPhotos.add(new SimilarPhoto(6,"https://s3.amazonaws.com/footprint.linhuang/winter2.jpg"));
		this.similarPhotos.add(new SimilarPhoto(7,"https://s3.amazonaws.com/footprint.linhuang/winter3.jpg"));
		this.comments = comments;
		this.rating = rating;
		this.avgRank = avgRank;
	}
}
