import java.util.ArrayList;
import java.util.List;

public class Recommender {
	public static List<SimilarPhoto> recomender() {
		List<SimilarPhoto> res = new ArrayList<SimilarPhoto>();
		DataModel model=new FileDataModel(new File("music.csv"));
        UserSimilarity similarity =new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood=new ThresholdUserNeighborhood(0,similarity,model);
        UserBasedRecommender recommender=new GenericUserBasedRecommender(model,neighborhood,similarity);
        List<RecommendedItem> recommendations=recommender.recommend(1,10);
        for(RecommendedItem recommendation:recommendations) {
        	System.out.println(recommendation);
        }
		return res;
	}
}
