

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class ClusterGetter
 */
public class ClusterGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClusterGetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] topTerms = {"sunset", "rainbow", "park"};
		Gson gson = new Gson();
		response.setContentType("application/json");
		List<Cluster> res = new ArrayList<Cluster>();
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		for (int i = 0; i < topTerms.length; i++) {
			res.add(new Cluster(topTerms[i], instance.clusterPhoto(topTerms[i])));
		}
		try {
			instance.closeConn();
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
		String[] topTerms = {"sunset", "rainbow", "park"};
		Gson gson = new Gson();
		response.setContentType("application/json");
		List<Cluster> res = new ArrayList<Cluster>();
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		for (int i = 0; i < topTerms.length; i++) {
			res.add(new Cluster(topTerms[i], instance.clusterPhoto(topTerms[i])));
		}
		try {
			instance.closeConn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

}
