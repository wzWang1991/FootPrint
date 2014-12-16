

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class getToken
 */
public class getToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getToken() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println("password:" + password);
		System.out.println("email:" + email);
		TokenContent result=new TokenContent();
		if (password != null) {
			RdsLoader instance = RdsLoader.getInstance();
			instance.init();
			UserInfo userInfo = instance.checkPassword(email, password);
			if (userInfo!=null) {
				result = TokenContent.getNewToken(TokenGenerator.getToken(), userInfo.getUserID(), userInfo.getEmail(), userInfo.getNickname(), userInfo.getFaceBook());
			}
		} else {
			// Facebook login. Check if this email in the database.
		}
		TokenSaver.getInstance().map.put(result.token, "hehe");
		System.out.println(TokenSaver.getInstance().isInMap(result.token));
		System.out.println(TokenSaver.getInstance().map.size());
		System.out.println(result.token);
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(result);
		out.println(ans);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		response.setContentType("application/json"); 

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println("Post!");
		System.out.println("password:" + password);
		System.out.println("email:" + email);
		TokenContent result=new TokenContent();
		if (password != null) {
			RdsLoader instance = RdsLoader.getInstance();
			instance.init();
			UserInfo userInfo = instance.checkPassword(email, password);
			if (userInfo!=null) {
				result = TokenContent.getNewToken("123", userInfo.getUserID(), userInfo.getEmail(), userInfo.getNickname(), userInfo.getFaceBook());
			}
		} else {
			// Facebook login. Check if this email in the database.
		}
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(result);
		out.println(ans);
		out.flush();
	}

}
