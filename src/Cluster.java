import java.util.List;


public class Cluster {
	String topTerm;
	List<ClusterPhoto> clusterPhoto;
	
	public Cluster() {}
	
	public Cluster(String topTerm, List<ClusterPhoto> clusterPhoto) {
		this.topTerm = topTerm;
		this.clusterPhoto = clusterPhoto;
	}
}