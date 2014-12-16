

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RatingGetter
 */
public class RatingGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RatingGetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		int res = instance.selectOneRating(userId, photoId);
		PrintWriter out=response.getWriter();
		out.println(res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		int res = instance.selectOneRating(userId, photoId);
		PrintWriter out=response.getWriter();
		out.println(res);
	}

}
