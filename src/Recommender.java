import java.io.File;
import java.io.IOException;
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
		instance.init();
		List<SimilarPhoto> res = new ArrayList<SimilarPhoto>();
		DataModel model=new FileDataModel(new File("ratings.csv"));
        UserSimilarity similarity =new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood=new ThresholdUserNeighborhood(0,similarity,model);
        UserBasedRecommender recommender=new GenericUserBasedRecommender(model,neighborhood,similarity);
        List<RecommendedItem> recommendations=recommender.recommend(userId,10);
        for(RecommendedItem recommendation:recommendations) {
        	int photoId = (int) recommendation.getItemID();
        	String recommendValue = formatter.format(recommendation.getValue());
        	String[] photoInfo = instance.selectOnePhoto(photoId);
        	res.add(new SimilarPhoto(photoId, photoInfo[0], recommendValue, photoInfo[1], photoInfo[2], photoInfo[3]));
        }
		return res;
	}
}
