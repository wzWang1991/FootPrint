
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.auth.PropertiesCredentials;

/**
 * Servlet implementation class upload
 */
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public upload() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if ( ServletFileUpload.isMultipartContent( request ))
		{
			File directory = new File(".");
		    List<FileItem> fileItems = null;
			try {
				fileItems = new ServletFileUpload( new DiskFileItemFactory( 1024 * 1024, directory )).parseRequest( request );
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			InputStream fileInput = null;
			HashMap<String, String> parameters = new HashMap<>();
		    for ( FileItem item : fileItems )
		    {
		        String fieldName = item.getFieldName();
		        if ( item.isFormField()) {
		        	parameters.put(fieldName, item.getString());
		        } // Form's input field
		        else { 
		        	fileInput = item.getInputStream();
		        	
		        } // File uploaded
		    }
		    String fileName = parameters.get("userId") + "-" + UUID.randomUUID() + "." + parameters.get("format");
		    File f = new File("/tmp/" + fileName);
		    f.delete();
		    if (fileInput != null) {
		    	Files.copy(fileInput, f.toPath());
		    	S3.uploadFile(f, fileName);
	        	System.out.println(f.getAbsolutePath());
	        	
	        	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        	String date = sDateFormat.format(new java.util.Date()); 
	    	
	    		RdsLoader instance = RdsLoader.getInstance();
	    		instance.init();
	    		String url = "https://s3.amazonaws.com/edu.columbia.cloud.footprint/" + fileName;
	    		int id = instance.insertPhotoTable(Integer.parseInt(parameters.get("userId")), date, parameters.get("description"), Double.parseDouble(parameters.get("latitude")), Double.parseDouble(parameters.get("longitude")), url);
	        	System.out.println(id);
	        	f.delete();
	        	
	        	
	        	PropertiesCredentials propertiesCredentials = new PropertiesCredentials(Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties"));
	        	Sqs sqs = new Sqs(propertiesCredentials);
	        	String queueUrl = "https://sqs.us-east-1.amazonaws.com/846524277299/FootPrint";
	        	String message = "{\"photoId\": " + id + ", \"url\": \""+ url +"\"}";
	        	sqs.sendMessage(queueUrl, message);
	        	out.println("success");
		    } else
		    	out.println("fail");
		} else {
			out.println("fail");
		}
		out.flush();
	}
}
