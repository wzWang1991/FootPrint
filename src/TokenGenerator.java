import java.util.UUID; 

public class TokenGenerator {
	public static String getToken() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
	
	public static void main(String[] args) {
		System.out.println(getToken());
	}
}
