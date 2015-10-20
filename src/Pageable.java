
public interface Pageable<T> {

	/**
	 * 
	 * @return the number of elements in the collection
	 */
	public int size();
	
	/**
	 * 
	 * @return the number of elements per page
	 */
	public int pageSize();
	
	/**
	 * 
	 * @return the number of pages in the collection
	 */
	public int pages();
	
	/**
	 * returns the elements in the requested page 
	 * @param i - zero-indexed page number
	 * @return set of elements in page i
	 */
	public T [] page(int i);
	
}
