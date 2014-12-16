
public class Comment {
	public String date;
	public String title;
	public String content;
	public String dateFormat;
	
	public Comment (String title, String content, String date) {
		this.date = date;
		this.title = title;
		this.content = content;
		this.dateFormat = "D/MM/YY h:mm A";
	}
}
