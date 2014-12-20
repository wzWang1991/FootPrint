
public class SimilarPhoto {
	public int photoId;
	public String url;
	public String recommendValue;
	public String date;
	public String username;
	public String description;
	
	public SimilarPhoto (int photoId, String url, String recommendValue, String date, String username, String description) {
		this.photoId = photoId;
		this.url = url;
		this.recommendValue = recommendValue;
		this.date = date;
		this.username = username;
		this.description = description;
	}
}
