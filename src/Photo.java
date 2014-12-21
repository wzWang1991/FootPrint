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
			int rating, String avgRank, int photoId) {
		this.url = url;
		this.date = date;
		this.dateFormat = "D/MM/YY h:mm A";
		this.title = title;
		this.content = content;
		hashTreeFP hashTree = hashTreeFP.getInstance();
		hashTree.initDB();
		hashTreeFP.SimilarPhotoWithDiff[] similarphotos = hashTree.findFourClosest(photoId);
		this.similarPhotos = new LinkedList<SimilarPhoto>();
		for (int i = 0; i < similarphotos.length; i++) {
			this.similarPhotos.add(new SimilarPhoto(similarphotos[i].id, similarphotos[i].url,"1","1","1","1"));
		}

		this.comments = comments;
		this.rating = rating;
		this.avgRank = avgRank;
	}
}
