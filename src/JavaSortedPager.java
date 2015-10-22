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

        java.util.Arrays.sort(this.items); // Use the built in sorting methods to sort this array descending


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
        return items[0];
    }

    /**
     * Finds and returns the maximum item in the list. Equivalent to calling get(size() - 1)
     * @return the largest item in the collection
     */
    public T max(){
        return items[size() - 1];
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
    /**
     * Returns the data elements contained on the i-th page.
     * This method will always return an array of * up to * pageSize() elements;
     * DO NOT assume this method to return an array of size pageSize()!
     */
    public T[] page(int i) {

        return items;
    }

    @Override
    public String toString(){
        String ret = "JavaSortedPager, " + size() + " objects: ";
        for (int i = 0; i < size(); i++) {
            ret += get(i) + ", ";
        }

        return ret;
    }
}
