import java.util.HashMap;

/**
 * JavaSortedPager.java
 *
 * Part of the PAIGE program, Page-Accessed Information Gateway Experiment
 *
 * Uses java.utils.array.sort() to order elements. Makes data available as Pageable.
 *
 * Sorts by increasing value, lowest to highest.
 *
 * @param <T> the type of object to be contained by the collection
 */
public class JavaSortedPager<T extends Comparable<T>> implements Pageable<T> {

    /** The array of items contained by this object, of type T */
    private T [] items;

    /** The maximum number of objects on a single page */
    private int pageSize;

    /** TRUE for extensive pattern logging */
    private boolean verbose = false;

    /** TRUE if the object will cache the results of page requests for performance */
    private boolean isCaching = false;

    /** The HashMap used to store the cached page data */
    private HashMap<Integer, T[]> cache;


    /**
     * Instantiates a new JavaSortedPager with the given objects and specified page size.
     * The created object will present objects sorted in ascending order.
     * @param objects an array of type T objects to sort and make Pageable
     * @param pageSize the number of objects to present on each page
     * @throws IllegalArgumentException if objects is null or count 0, or pageSize is less than 1
     */
    public JavaSortedPager(T [] objects, int pageSize) throws IllegalArgumentException{
        if (pageSize < 1 || objects == null || objects.length == 0)
            throw new IllegalArgumentException("Improper instantiation: Not enough objects, dumbass.");


        this.items = objects;
        this.pageSize = pageSize;

        if (verbose) System.out.println("Unsorted " + this);

        sortItemsArray(); // Delegate method, in this class this calls Java.util.Arrays.sort()

        if (verbose) System.out.println("Sorted   " + this);

    }

    /**
     * Returns the i-th item in the sorted collection. Collection is sorted descending.
     * @param i the index of the object, such that 0 ≤ i < size()
     * @return the T object at the specified index
     * @throws IndexOutOfBoundsException on attempt to access index not in range
     */
    public T get(int i) throws IndexOutOfBoundsException{
        if (i < 0 || i > size() || this.items == null) throw new IndexOutOfBoundsException("Attempting to access invalid index.");

        return items[i];
    }

    /**
     * Finds and returns the minimum (smallest) item in the list. Equivalent to calling get(0)
     * @return the smallest item in the collection
     */
    public T min(){
        return get(0);
    }

    /**
     * Finds and returns the maximum item in the list. Equivalent to calling get(size() - 1)
     * @return the largest item in the collection
     */
    public T max(){
        return get(size() - 1);
    }


    @Override
    /**
     * Returns the number of data elements in the entire collection
     */
    public int size() {
        return items.length;
    }

    @Override
    /**
     * Returns the maximum number of elements to return on a given page.
     */
    public int pageSize() {
        return this.pageSize;
    }

    @Override
    /**
     * Returns the number of pages contained in the collection.
     */
    public int pages() {
        return (int)Math.ceil((double)size() / (double)this.pageSize);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Returns the data elements contained on the i-th page.
     * This method will always return an array of * up to * pageSize() elements;
     * DO NOT assume this method to return an array of size pageSize()!
     *
     * NOTE: This method returns an Object[] not a T[] due to Java Generics Typing
     *       Method signature is deprecated; call {@code: public Object[] page(int)} instead
     * @returns an Object[] NOT a T[] !!!!!
     */
    public T[] page(int i) {
        T[] retArray;

        if (isCaching){
            try {
                retArray = retrieveCachedPage(i);
                if (retArray != null ) return retArray; // Short circuit return if cached data is available
            } catch (CacheException e){
                // The data isn't cached, or there was another error, lets load it fresh
                if (verbose) System.out.println(e.getMessage());
            }
        }

        //int pageSize = pageSize(); // store for efficiency in following ternary
        int pageSize = i == pages() - 1 ? size() % pageSize() : pageSize();
        retArray = (T[]) new Comparable[pageSize];

        int copyIndex = 0;
        for (int j = i * pageSize(); j < (i * pageSize()) + pageSize; j++) {
            retArray[copyIndex] = get(j);

            copyIndex++;
        }

        try {
            if (isCaching) cache(retArray, i);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return retArray;
    }

    @Override
    public String toString() {
        String ret = "JavaSortedPager, " + size() + " objects: ";
        for (int i = 0; i < size(); i++) {
            ret += get(i) + ", ";
        }

        return ret;
    }

    /** Caches the page for quick retrieval
     *
     * @param page the generic array to cache
     * @param pageIndex the index of the page of the data being stored
     * @throws CacheException if illegal caching or on caching error
     */
    private void cache(T[] page, int pageIndex) throws CacheException{
        if (!isCaching) throw new CacheException("Caching attempted while not enabled.");
        if (pageIndex < 0 || pageIndex >= pages()) throw new IndexOutOfBoundsException("Attempting to set out of cache boundaries.");


        if (cache == null){
            cache = new HashMap<>(); // Creates an ArrayList with capacity for the correct number of pages
        }

        if (verbose) System.out.println("Caching " + page.length + " objects for page " + pageIndex);
        if (cache.containsKey(pageIndex)) { // The data should not be able to change after being written
            System.out.println("Attempting to cache already cached data. Ignoring. ");
            return;
        }
        cache.put(pageIndex, page);
    }

    /**
     * Retrieves the cached data from the cache.
     * @param pageIndex the index of the page stored in cache
     * @return the T[] generic array containing items on that page
     * @throws CacheException if attempted to access data which has not been cached, or out of bounds
     */
    private T[] retrieveCachedPage(int pageIndex) throws CacheException{
        if (!isCaching) throw new CacheException("Caching attempted while not enabled.");
        if (cache == null) return null; // The cache hasn't been established yet
        if (pageIndex < 0 || pageIndex > pages()) throw new IndexOutOfBoundsException("Attempting to access out of cache boundaries.");
        if (pageIndex > cache.size() - 1) return null;

        T[] retArray = cache.get(pageIndex);

        if (retArray == null || retArray.length == 0) throw new CacheException("Data not cached");

        if (verbose) System.out.println("Retrieving Cached Data for Page " + pageIndex);
        return cache.get(pageIndex); // Retrieves the page or null
    }

    /** Sets whether or not the JavaSortedPager should log its output for debugging.
     * @param verbose TRUE if the JavaSortedPager should log excessively
     */
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    /** Sets whether the JavaSortedPager object should cache data when pages are accessed.
     * @param shouldCache TRUE if the JavaSortedPager object should cache data for quicker re-access
     */
    public void setShouldCache(boolean shouldCache){
        this.isCaching = shouldCache;
    }

    /** Returns TRUE if the object is caching page values */
    public boolean cachingEnabled(){
        return isCaching;
    }

    /** The delegate sorting method. Sorts the array in ascending order */
    private void sortItemsArray(){
        java.util.Arrays.sort(this.items); // Use the built in sorting methods to sort this array descending

    }
}
