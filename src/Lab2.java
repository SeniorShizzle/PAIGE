/**
 * 
 * Lab2 provides some static methods to help you develop and test
 *
 */
public class Lab2 {

	
	
	/**
	 * iterates and prints each page of Pageable object
	 * 
	 * @param pagedObject
	 */
	public static <T> void printPagedResults(Pageable<T> pagedObject) {
		
		if (pagedObject == null) return;
		
		StdOut.println("Elements: " + pagedObject.size() + " Pages: "+pagedObject.pages()+"\n");
		for(int i=0;i<pagedObject.pages();i++) {
			T[] vals = pagedObject.page(i);
			StdOut.println("Page: " + i);
			for(T v : vals) {
				StdOut.println("  >"+v);
			}
		}
	}
	
	/**
	 * generates an array of doubles of uniform distribution
	 * @param N size of the array
	 * @return
	 */
	public static Double [] randomDoubles(int N) {
		if (N <= 0) throw new IllegalArgumentException("input must be >= to 1");
		
		Double [] d = new Double[N];
		for(int i=0;i<N;i++) {
			d[i] = StdRandom.uniform();
		}
		return d;
	}
	
	/**
	 * generates an array of ScoredDocument objects
	 * 
	 * @param N size of the array
	 * @return
	 */
	public static ScoredDocument [] generateScoredDocuments(int N) {
		if (N <= 0) throw new IllegalArgumentException("input must be >= to 1");
		
		ScoredDocument [] docs = new ScoredDocument[N];
		for(int i=0;i<N;i++) {
			docs[i] = new ScoredDocument();
		}
		
		return docs;
	}

}
