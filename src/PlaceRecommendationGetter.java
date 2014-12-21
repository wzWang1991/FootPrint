

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mahout.cf.taste.common.TasteException;

import com.google.gson.Gson;

/**
 * Servlet implementation class PlaceRecommendationGetter
 */
public class PlaceRecommendationGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaceRecommendationGetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		int userId = Integer.parseInt(request.getParameter("userId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.generateCsvForRatings();
		List<SimilarPhoto> similarPhoto = new ArrayList<SimilarPhoto>();
		try {
			similarPhoto = Recommender.recomender(userId);
		} catch (TasteException e) {
			e.printStackTrace();
		}
		instance.init();
		List<Cluster> res = new ArrayList<Cluster>();
		try {
			res = Recommender.placeCluster(similarPhoto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		int userId = Integer.parseInt(request.getParameter("userId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		instance.generateCsvForRatings();
		List<SimilarPhoto> similarPhoto = new ArrayList<SimilarPhoto>();
		try {
			similarPhoto = Recommender.recomender(userId);
		} catch (TasteException e) {
			e.printStackTrace();
		}
		instance.init();
		List<Cluster> res = new ArrayList<Cluster>();
		try {
			res = Recommender.placeCluster(similarPhoto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

}
