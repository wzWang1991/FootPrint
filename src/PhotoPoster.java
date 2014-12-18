

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PhotoPoster
 */
public class PhotoPoster extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoPoster() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date()); 
		String description = request.getParameter("description");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longtitude = Double.parseDouble(request.getParameter("longtitude"));
		String url = request.getParameter("url");
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertPhotoTable(userId, date, description, latitude, longtitude, url);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date()); 
		String description = request.getParameter("description");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longtitude = Double.parseDouble(request.getParameter("longtitude"));
		String url = request.getParameter("url");
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.insertPhotoTable(userId, date, description, latitude, longtitude, url);
	}

}
