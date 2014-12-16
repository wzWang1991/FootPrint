

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class PhotoGetter
 */
public class PhotoGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhotoGetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		String season = request.getParameter("season");
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation(season, 0, 0, 0, 0);
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		String season = request.getParameter("season");
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation(season, 0, 0, 0, 0);
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

}
