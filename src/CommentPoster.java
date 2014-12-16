

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class CommentPoster
 */
public class CommentPoster extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentPoster() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		String comments = request.getParameter("comments");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date()); 
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertCommentsTable(userId, photoId, comments, date);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		String comments = request.getParameter("comments");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date()); 
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertCommentsTable(userId, photoId, comments, date);
	}

}
