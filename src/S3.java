import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class S3 {
	private static String bucketName = "edu.columbia.cloud.footprint";
	
	
	public static void uploadFile(File file, String fileName) throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
                S3.class.getResourceAsStream("AwsCredentials.properties"));
		AmazonS3 s3Client = new AmazonS3Client(credentials);
		s3Client.putObject(new PutObjectRequest(
                bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}
}
