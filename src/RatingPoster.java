

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RatingPoster
 */
public class RatingPoster extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RatingPoster() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		int rank = Integer.parseInt(request.getParameter("rank"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertRatingsTable(userId, photoId, rank);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		int rank = Integer.parseInt(request.getParameter("rank"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertRatingsTable(userId, photoId, rank);
	}

}
