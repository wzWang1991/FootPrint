import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class Recommender {
	public static List<SimilarPhoto> recomender(int userId) throws TasteException, IOException {
		NumberFormat formatter = new DecimalFormat("#0.0"); 
		RdsLoader instance = RdsLoader.getInstance();
		List<SimilarPhoto> res = new ArrayList<SimilarPhoto>();
		DataModel model=new FileDataModel(new File("ratings.csv"));
        UserSimilarity similarity =new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood=new ThresholdUserNeighborhood(0,similarity,model);
        UserBasedRecommender recommender=new GenericUserBasedRecommender(model,neighborhood,similarity);
        List<RecommendedItem> recommendations=recommender.recommend(userId,10);
        for(RecommendedItem recommendation:recommendations) {
        	int photoId = (int) recommendation.getItemID();
        	String recommendValue = formatter.format(recommendation.getValue());
        	instance.init();
        	String[] photoInfo = instance.selectOnePhoto(photoId);
        	if (photoInfo != null)
        		res.add(new SimilarPhoto(photoId, photoInfo[0], recommendValue, photoInfo[1], photoInfo[2], photoInfo[3], photoInfo[4]));
        }
		return res;
	}
	
	public static List<Cluster> placeCluster(List<SimilarPhoto> similarPhoto) throws SQLException {
		RdsLoader instance = RdsLoader.getInstance();
		instance.init();
		List<Integer> photoId = new ArrayList<Integer>();
		for (int i = 0; i < similarPhoto.size(); i++) {
			photoId.add(similarPhoto.get(i).photoId);
		}
		List<Place> places = instance.searchPlaces(photoId);
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (int i = 0; i < places.size(); i++) {
			String placeName = places.get(i).placeName;
			int placeId = places.get(i).placeId;
			clusters.add(new Cluster(placeName, instance.clusterPhoto(placeId)));
		}
		instance.closeConn();
		return clusters;
	}
}
