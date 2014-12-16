
public class TokenContent {
	public String token;
	// Unix timestamp.
	public long expireTime;
	public String email;
	public String nickName;
	public boolean fbConnect;
	public int userId;
	
	public TokenContent() {
		
	}
	
	// Return a token with 7 day expire time.
	public static TokenContent getNewToken(String token, int userId, String email, String nickName, boolean fbConnect) {
		TokenContent tokenContent = new TokenContent();
		tokenContent.token = token;
		tokenContent.userId = userId;
		tokenContent.email = email;
		tokenContent.nickName = nickName;
		tokenContent.fbConnect = fbConnect;
		long unixTime = System.currentTimeMillis() / 1000L;
		unixTime += 604800; // 7 days.
		tokenContent.expireTime = unixTime;
		return tokenContent;
	}
}
