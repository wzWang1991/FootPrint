

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class SinglePhotoGetter
 */
public class SinglePhotoGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SinglePhotoGetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		response.setContentType("application/json"); 
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		Photo res = instance.selectOnePhoto(photoId);
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
		int photoId = Integer.parseInt(request.getParameter("photoId"));
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		Photo res = instance.selectOnePhoto(photoId);
		PrintWriter out=response.getWriter();
		String ans=gson.toJson(res);
		out.println(ans);
		out.flush();
	}

}
