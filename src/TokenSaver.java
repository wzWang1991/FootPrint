import java.util.HashMap;


public class TokenSaver {
	static TokenSaver instance = new TokenSaver();
	HashMap<String, String> map = new HashMap<>();
	
	private TokenSaver() {
		
	}
	
	public static TokenSaver getInstance() {
		return instance;
	}
	
	public boolean isInMap(String token) {
		return map.containsKey(token);
	}
}
