/**
 * ScoredDocument object links a document id to a score
 *
 */
public class ScoredDocument implements Comparable<ScoredDocument> {

	private final String id;   
	private final Double score;
	
	public ScoredDocument() {
		id = StdRandom.uniform(Integer.MAX_VALUE)+"_"+StdRandom.uniform(Integer.MAX_VALUE);
		score = Math.floor((StdRandom.uniform()*10000+.5))/10000;
	}
	
	public String id() { return id; }
	
	public double score() { return score; }

	@Override
	public int compareTo(ScoredDocument o) {
		return this.score.compareTo(o.score);
	}

	@Override
	public String toString() {
		return score+"_"+id;
	}
}
