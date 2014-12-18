

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
		double lat1 = Double.parseDouble(request.getParameter("lat1"));
		double lat2 = Double.parseDouble(request.getParameter("lat2"));
		double lng1 = Double.parseDouble(request.getParameter("lng1"));
		double lng2 = Double.parseDouble(request.getParameter("lng2"));
		double latBegin = Math.min(lat1, lat2);
		double latEnd = Math.max(lat1, lat2);
		double lngBegin = Math.min(lng1, lng2);
		double lngEnd = Math.max(lng1, lng2);
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation(season, latBegin, latEnd, lngBegin, lngEnd);
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
		double lat1 = Double.parseDouble(request.getParameter("lat1"));
		double lat2 = Double.parseDouble(request.getParameter("lat2"));
		double lng1 = Double.parseDouble(request.getParameter("lng1"));
		double lng2 = Double.parseDouble(request.getParameter("lng2"));
		double latBegin = Math.min(lat1, lat2);
		double latEnd = Math.max(lat1, lat2);
		double lngBegin = Math.min(lng1, lng2);
		double lngEnd = Math.max(lng1, lng2);
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		List<PhotoInfo> res = instance.filterPhotoByTimeAndLocation(season, latBegin, latEnd, lngBegin, lngEnd);
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

}
