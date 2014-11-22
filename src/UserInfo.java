public class UserInfo {
	private int userID;
	private String email;
	private String password;
	private boolean faceBook;
	private String nickname;
	public UserInfo(int userID, String email, String password, boolean faceBook, String nickname) {
		this.userID=userID;
		this.email=email;
		this.password=password;
		this.faceBook=faceBook;
		this.nickname=nickname;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getFaceBook() {
		return faceBook;
	}
	public void setFaceBook(Boolean faceBook) {
		this.faceBook = faceBook;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
