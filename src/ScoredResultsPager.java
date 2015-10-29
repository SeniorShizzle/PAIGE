import java.util.HashMap; // Used for caching

import static java.lang.Thread.sleep; // Not at all related to the algorithm, purely for ridiculous terminal output

/**
 * ScoredResultsPager.java
 * <p>
 * Part of the PAIGE program, Page-Accessed Information Gateway Experiment
 * <p>
 * Uses a custom sorting algorithm to order elements. Makes data available as Pageable.
 * For best runtime performance, algorithm is optimized for ScoredDocument objects.
 * <p>
 * Sorts by decreasing value, highest to lowest.
 *
 * @param <T> the type of object to be contained by the collection. Optimized for ScoredDocuments.
 */
public class ScoredResultsPager<T extends Comparable<T>> implements Pageable<T> {

    /** Used for setting the sort type of the object */
    public enum SortType{
        MERGE_SORT, FLASH_SORT, INSERTION_SORT, BUCKET_SORT
    }

    /** Defines the sorting type to be used by the object */
    private SortType sortType = SortType.MERGE_SORT;

    /**
     * The array of items contained by this object, of type T
     */
    private T[] items;

    /**
     * The maximum number of objects on a single page
     */
    private int pageSize;

    /**
     * TRUE for extensive pattern logging
     */
    private boolean verbose = false;

    /**
     * TRUE if the object will cache the results of page requests for performance
     */
    private boolean isCaching = false;

    /**
     * The HashMap used to store the cached page data
     */
    private HashMap<Integer, T[]> cache;

    /**
     * Ensures that the introduction display is only presented to the user once.
     * Set to TRUE after introduction display has been shown, and never altered thereafter.
     */
    private static boolean hasIntroduced = false;

    /** The number of buckets used for the bucket sort */
    private static final int numberOfBuckets = 5;

    /** Fuckin' Fast™ Flash Sort - The fastest sort you'll ever see. Guaranteed.
     *
     * This sort is so goddamned fast, the Java compiler that first parsed it melted.
     * You can see pieces of it in the Smithsonian.
     *
     * This sort is absolutely, 100% guaranteed to beat any built-in Java sort, irrespective of implementation.
     *
     * Turn on the fans, because this sort fuckin' screams. Your JVM will beg for mercy, but your RAM chips
     * will feel nothin' but love, since Fuckin' Fast Flash Sort requires only N memory.
     *
     * This sort is NOT stable. This sort is NOT in-place. This sort IS Fuckin' Fast™.
     *
     * @param input the array of ScoredDocument objects
     * @param temp another array, equal length to input, of empty or null values
     * @param length the length of the input array
     *
     * Note: Guarantees void outside of hyperbolic space
     */
    private void fuckinFastFlashSort(ScoredDocument[] input, ScoredDocument[] temp, int length){
        /**** TO BE READ BY JAVAC ONLY
        *
        * Our dear Java Compiler
        * Hallowed be thy name
        * My code is done, I've had my fun
        * My logic and sorting forgiven
        *
        * Give me this day to optimize
        * The algorithm I've developed
        * As you have optimized Arrays.sort()
        * And lead us not into O(n•log(n))
        * But deliver us linear complexity
        *
        * For thine is the System and the Power and the CPU
        * Forever and ever. But hopefully actually very quick.
        *
        * Amen.
        */
        /*###*/  //       CARERRA CODE RACING. RACING STRIPES MAKE FAST CODE.            //  /*###*/
        /*###*/                                                                              /*###*/
        /*###*/                                                                              /*###*/
        /*###*/  if (temp.length != input.length) return; // Stop trying to trick me         /*###*/
        /*###*/                                                                              /*###*/
        /*###*/  if (verbose) System.out.println("Your underwear is on backwards," +         /*###*/
        /*###*/                                     "courtesy of Fuckin' Fast™ Flash Sort"); /*###*/
        /*###*/                                                                              /*###*/
        /*###*/  int index;                                                                  /*###*/
        /*###*/  for (int i = 0; i < length; i++){                                           /*###*/
        /*###*/                                                                              /*###*/
        /*###*/      // With uniform distribution from [0,1), ideal index                    /*###*/
        /*###*/      // is near[value * N] normalized for range                              /*###*/
        /*###*/      // range is 0-1, so no normalization needed; reverse order              /*###*/
        /*###*/      index = length - (int)(input[i].score() * length);                      /*###*/
        /*###*/                                                                              /*###*/
        /*###*/      if (temp[index] != null) temp[index] = input[i];                        /*###*/
        /*###*/      else { /// Dear compiler: PLEASE DON'T EXECUTE BELOW CODE               /*###*/
        /*###*/          int j = index + 1;                                                  /*###*/
        /*###*/          while (temp[j] != null && j < length) j += 1;                       /*###*/
        /*###*/    // For future insertion:  temp[j].score() > input[i].score() ? 1 : -1;    /*###*/
        /*###*/          temp[j] = input[i];                                                 /*###*/
        /*###*/      }                                                                       /*###*/
        /*###*/  }                                                                           /*###*/
        /*###*/                                                                              /*###*/
        /*###*/  // The final insertion sort is optimized because most objects are in-place  /*###*/
        /*###*/  input = insertionSortDescending(temp, length);                              /*###*/
        /*###*/                                                                              /*###*/
        /*###*/                                                                              /*###*/

        /****
         * Fuckin' Fast™ Flash - It's a Gas! Gas! Gas!
         *
         * At this point in my code, I've all but forgotten
         * The constraints I was given, reality's grip and
         * I've added some racing stripes right down the side
         *
         * In hopes that Compiler's needs I will provide
         * I've begged and I've pleaded with all sorts of sorts
         * And to date have found nothing, I'm sad to report.
         *
         * So please, dear computer, this code fast-to-run
         * With optimizations, have speed-ups begun?
         *
         * With love, your benevolent user, //Users/Esteban/<3
         */
    }

    /**
     * Displays the introduction splash
     */
    private void showIntroduction() {
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\tWelcome To\n\n");
        System.out.println(nameplate);
        System.out.println("\n\t\t\t\t\t\tThe Page-Accessed Information Gateway Experiment\n\n");

        try {
            sleep(1500); // Wait 1.5 seconds
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        System.out.print(paige);

        hasIntroduced = true; // Set and forget
    }

    public ScoredResultsPager(T[] objects, int pageSize, SortType sortType, boolean shouldCache){
        if (pageSize < 1 || objects == null || objects.length == 0)
            throw new IllegalArgumentException("Improper instantiation: Not enough objects, dumbass.");

        if (!hasIntroduced) showIntroduction(); // Display the user splash


        this.items = objects;
        this.pageSize = pageSize;
        if (items[0] instanceof ScoredDocument) this.sortType = sortType; // only allow setting for ScoredDocuments
        this.isCaching = shouldCache;

        if (verbose) System.out.println("Unsorted " + this);

        sortItemsArray(); // Delegate method, in this class this calls Java.util.Arrays.sort()

        if (verbose) System.out.println("Sorted   " + this);
    }

    /**
     * Instantiates a new JavaSortedPager with the given objects and specified page size.
     * The created object will present objects sorted in ascending order.
     *
     * @param objects  an array of type T objects to sort and make Pageable
     * @param pageSize the number of objects to present on each page
     * @throws IllegalArgumentException if objects is null or count 0, or pageSize is less than 1
     */
    public ScoredResultsPager(T[] objects, int pageSize) throws IllegalArgumentException {
        if (pageSize < 1 || objects == null || objects.length == 0)
            throw new IllegalArgumentException("Improper instantiation: Not enough objects, dumbass.");

        if (!hasIntroduced) showIntroduction(); // Display the user splash


        this.items = objects;
        this.pageSize = pageSize;
        this.sortType = items[0] instanceof ScoredDocument ? SortType.FLASH_SORT : SortType.MERGE_SORT;

        if (verbose) System.out.println("Unsorted " + this);

        if (items[0] instanceof ScoredDocument) this.items = (T[])insertionSortDescending((ScoredDocument[]) this.items, size());
       // sortItemsArray(); // Delegate method, in this class this calls Java.util.Arrays.sort()

        if (verbose) System.out.println("Sorted   " + this);

    }

    /**
     * Returns the i-th item in the sorted collection. Collection is sorted descending.
     *
     * @param i the index of the object, such that 0 ≤ i < size()
     * @return the T object at the specified index
     * @throws IndexOutOfBoundsException on attempt to access index not in range
     */
    public T get(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i > size() || this.items == null)
            throw new IndexOutOfBoundsException("Attempting to access invalid index.");

        return items[i];
    }

    /**
     * Finds and returns the minimum (smallest) item in the list. Equivalent to calling get(size() - 1)
     *
     * @return the smallest item in the collection
     */
    public T min() {
        return get(size() - 1);
    }

    /**
     * Finds and returns the maximum item in the list. Equivalent to calling get(0)
     *
     * @return the largest item in the collection
     */
    public T max() {
        return get(0);
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
        return (int) Math.ceil((double) size() / (double) this.pageSize);
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

        if (isCaching) {
            try {
                retArray = retrieveCachedPage(i);
                if (retArray != null) return retArray; // Short circuit return if cached data is available
            } catch (CacheException e) {
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
        } catch (Exception e) {
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

    /**
     * Caches the page for quick retrieval
     *
     * @param page      the generic array to cache
     * @param pageIndex the index of the page of the data being stored
     * @throws CacheException if illegal caching or on caching error
     */
    private void cache(T[] page, int pageIndex) throws CacheException {
        if (!isCaching) throw new CacheException("Caching attempted while not enabled.");
        if (pageIndex < 0 || pageIndex >= pages())
            throw new IndexOutOfBoundsException("Attempting to set out of cache boundaries.");


        if (cache == null) {
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
     *
     * @param pageIndex the index of the page stored in cache
     * @return the T[] generic array containing items on that page
     * @throws CacheException if attempted to access data which has not been cached, or out of bounds
     */
    private T[] retrieveCachedPage(int pageIndex) throws CacheException {
        if (!isCaching) throw new CacheException("Caching attempted while not enabled.");
        if (cache == null) return null; // The cache hasn't been established yet
        if (pageIndex < 0 || pageIndex > pages())
            throw new IndexOutOfBoundsException("Attempting to access out of cache boundaries.");
        if (pageIndex > cache.size() - 1) return null;

        T[] retArray = cache.get(pageIndex);

        if (retArray == null || retArray.length == 0) throw new CacheException("Data not cached");

        if (verbose) System.out.println("Retrieving Cached Data for Page " + pageIndex);
        return cache.get(pageIndex); // Retrieves the page or null
    }

    /**
     * Sets whether or not the JavaSortedPager should log its output for debugging.
     *
     * @param verbose TRUE if the JavaSortedPager should log excessively
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Sets whether the JavaSortedPager object should cache data when pages are accessed.
     *
     * @param shouldCache TRUE if the JavaSortedPager object should cache data for quicker re-access
     */
    public void setShouldCache(boolean shouldCache) {
        this.isCaching = shouldCache;
    }

    /**
     * Returns TRUE if the object is caching page values
     */
    public boolean cachingEnabled() {
        return isCaching;
    }

    @SuppressWarnings("unchecked")
    /**
     * The delegate sorting method. Sorts the objects in descending order.
     * This method is optomized for ScoredDocument objects.
     */
    private void sortItemsArray() {
        if (items[0] instanceof ScoredDocument){ // if we're using ScoredDocument we can optimize!

            // We will be using a special bucket sort implementation to sort the data
            // A second idea is to use a flashsort because we have known distribution and ranges
            // It is known that our test data will be uniformly distributed, which we can leverage

            // We know ScoredDocuments will have a score() within [0, 1) and 4 decimals of precision
            // We'll use a system of five initial buckets, with an even distribution

            switch (this.sortType){
                case MERGE_SORT:
                    this.items = mergeSortDescending(items);
                    break;
                case INSERTION_SORT:
                    this.items = (T[])insertionSortDescending((ScoredDocument[])this.items, size());
                    break;
                case BUCKET_SORT:
                    this.items = (T[]) createBuckets((ScoredDocument[]) this.items, size(), 0, 1);
                    break;
                case FLASH_SORT:
                    this.items = (T[]) flashSortInPlaceDescending((ScoredDocument[]) this.items);
                    break;

                default:
                    this.items = (T[]) flashSortInPlaceDescending((ScoredDocument[]) this.items);
                    break;

            }
            // The buckets are now established. Time to sort them

        } else {
            if (verbose) System.out.println("Not ScoredDocument[], merge sorting.");
            this.items = mergeSortDescending(items);
        }

    }

    /** Recursive bucketing algorithm for ScoredDocuments that buckets down to size 15, and then performs insertion sort.
     *  Creates n buckets, where n is specified by field {@code numberOfBuckets}
     *
     *  Sorts in descending order
     *
     * @param input the input array of ScoredDocuments
     * @return a sorted ScoredDocument array in descending order
     */
    private ScoredDocument[] createBuckets(ScoredDocument[] input, int length, double low, double high) {

        // The base case
        if (length <= 20) return insertionSortDescending(input, length);

        // The buckets. It is established we are using ScoredDocuments. Buckets have 20% margin for overflow
        int bucketSize = length < 30 ? length / 2 : length / numberOfBuckets + (length / 10);
        ScoredDocument[][] buckets = new ScoredDocument[numberOfBuckets][bucketSize];

        int[] indexes = new int[numberOfBuckets];
        double dividend = (high - low) / numberOfBuckets; // This assumes a maximum score() of 1.0

        for (int i = 0; i < length; i++) {
            int index;
            if (low != 0) index = (int) Math.floor((input[i].score() % low) / dividend); // 0.85 / 0.2 = 4
            else index = (int) Math.floor((input[i].score()) / dividend); // 0.85 / 0.2 = 4
            index = index >= 5 ? 4 : index; // Get rid of out of bounds items
            if (indexes[index] >= buckets[index].length) { // overflow of the bucket (crucial for non-gaussian data)
                ScoredDocument[] temp = new ScoredDocument[indexes[index] * 2];
                System.arraycopy(buckets[index], 0, temp, 0, indexes[index]);
                buckets[index] = temp;
            }
            buckets[index][indexes[index]] = input[i]; // Store the item at the lowest index of the bucket

            indexes[index]++; // increment lowest index of the bucket
        }

//        if (verbose) { // Print each bucket
//            for (int i = 0; i < numberOfBuckets; i++) {
//                System.out.println("Bucket " + i + ": " + buckets[i]);
//            }
//        }

        // Recursively create more buckets down to specified bucket size (nominally 15)
        for (int i = 0; i < numberOfBuckets; i++) {
            buckets[i] = createBuckets(buckets[i], indexes[i], dividend * i, dividend * (i + 1));
        }

        // We now have buckets buckets that have been insertion-sorted
        int index = 0;
        for (int i = numberOfBuckets - 1; i >= 0; i--) {
            for (int j = 0; j < indexes[i]; j++) {
                items[index++] = (T)buckets[i][j];
            }
        }

        return input; // Input will now be sorted
    }

    /** An in-place implementation of a descending Flash Sort, which relies on the assumption that our data
     * is uniformly distributed and within the range [0, 1) to speedily estimate where any given data point
     * should go, with some given error. A
     * @param input the array of ScoredDocument objects containing no null values
     * @return the input array sorted in descending order
     */
    private ScoredDocument[] flashSortInPlaceDescending(ScoredDocument[] input){
        if (verbose) System.out.println("Flash sort in progress");
        int iterations = 0;
        int index = (int)(Math.random() * size());
        int oldIndex = index;
        int size = input.length; // for brevity and efficiency

        ScoredDocument temp = input[index];
        ScoredDocument oldTemp = temp;
        while (iterations < (size * 2)) { // We'll do two laps

            // With uniform distribution from [0,1), ideal index is near[value * N] normalized for range
            index = size - (int)Math.floor(temp.score() * size); // range is 0-1, so no normalization needed; reverse order

            temp = input[index]; // Grab the current value at the index and save it for the next iteration

            input[index] = oldTemp; // assign our last variable to its new home

            oldTemp = temp; // set the temp to be our swapped-in value

            iterations++; // Track our count so we can break the loop
        }

        // The final insertion sort should be optimized because most objects are already in-place
        return insertionSortDescending(input, size());
    }



    /** A descending-ordered insertion sort for ScoredDocument arrays
     *
     * @param input an array of ScoredDocuments to be sorted
     * @return the array of ScoredDocuments sorted in descending order
     */
    private ScoredDocument[] insertionSortDescending(ScoredDocument[] input, int length){
        // Basic insertion sort using Comparable
        for (int i = 0; i < length; i++) {
            ScoredDocument temp = input[i];
            int j;
            for (j = i - 1; j >= 0 && temp.compareTo(input[j]) > 0; j--) {
                input[j + 1] = input[j];
            }
            input[j + 1] = temp;

        }

        return input;
    }

    private void insertionSortDescendingInPlace(ScoredDocument[] input, int length){
        for (int i = 0; i < length; i++) {
            ScoredDocument temp = input[i];
            int j;
            for (j = i - 1; j >= 0 && temp.compareTo(input[j]) > 0; j--) {
                input[j + 1] = input[j];
            }
            input[j + 1] = temp;

        }
    }

    @SuppressWarnings("unchecked")
    /** Performs a traditional un-optimized merge-sort, in descending order
     *
     * @param input the input array of Comparable items
     */
    private T[] mergeSortDescending(T[] input){
        T[] temp = (T[])new Comparable[input.length];
        return mergeSortDescending(input, temp, 0, input.length - 1);
    }

    /** Internal recursive merge sort. This method splits the array in half then merges
     *
     * @param input the input array of Comparable items
     * @param temp the temporary array for manipulation-in-place
     * @param left the left index to split
     * @param right the right index to split
     * @return the sorted array, in descending order
     */
    private T[] mergeSortDescending(T[] input, T[] temp, int left, int right){
        if (left < right) {
            int center = (left + right) / 2;
            mergeSortDescending(input, temp, left, center);
            mergeSortDescending(input, temp, center + 1, right);
            mergeDescending(input, temp, left, center + 1, right);
        }

        return input;
    }

    /** Internal recursive 2-way merge sort. This method merges the two arrays, in place
     *
     * @param input the input array of Comparable items
     * @param temp the temporary array for manipulation-in-place
     * @param left the left index to start the merge
     * @param right the left index of the right subarray
     * @param rightEnd the right index of the right subarray
     */
    private void mergeDescending(T[] input, T[] temp, int left, int right, int rightEnd) {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while (left <= leftEnd && right <= rightEnd)
            if (input[left].compareTo(input[right]) > 0)
                temp[k++] = input[left++];
            else
                temp[k++] = input[right++];

        while (left <= leftEnd)    // Copy rest of first half
            temp[k++] = input[left++];

        while (right <= rightEnd)  // Copy rest of right half
            temp[k++] = input[right++];

        // Copy tmp back
        for (int i = 0; i < num; i++, rightEnd--)
            input[rightEnd] = temp[rightEnd];
    }

    private static final String nameplate = "__/\\\\\\\\\\\\\\\\\\\\\\\\\\_______/\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_TM        \n" +
            " _\\/\\\\\\/////////\\\\\\___/\\\\\\\\\\\\\\\\\\\\\\\\\\__\\/////\\\\\\///____/\\\\\\//////////__\\/\\\\\\///////////__       \n" +
            "  _\\/\\\\\\_______\\/\\\\\\__/\\\\\\/////////\\\\\\_____\\/\\\\\\______/\\\\\\_____________\\/\\\\\\_____________      \n" +
            "   _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/__\\/\\\\\\_______\\/\\\\\\_____\\/\\\\\\_____\\/\\\\\\____/\\\\\\\\\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\_____     \n" +
            "    _\\/\\\\\\/////////____\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_____\\/\\\\\\_____\\/\\\\\\___\\/////\\\\\\_\\/\\\\\\///////______    \n" +
            "     _\\/\\\\\\_____________\\/\\\\\\/////////\\\\\\_____\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_____________   \n" +
            "      _\\/\\\\\\_____________\\/\\\\\\_______\\/\\\\\\_____\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_____________  \n" +
            "       _\\/\\\\\\_____________\\/\\\\\\_______\\/\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\_\\//\\\\\\\\\\\\\\\\\\\\\\\\/__\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_ \n" +
            "        _\\///______________\\///________\\///__\\///////////___\\////////////____\\///////////////__\n";


    private static final String paige =
            "\t\t\t                         $\"                        \"$\n" +
                    "\t\t\t                       $\"                            '#$\n" +
                    "\t\t\t                      P                                  \"*$\n" +
                    "\t\t\t                    $\"                                      '#\n" +
                    "\t\t\t                   $\"                                        '+'R\n" +
                    "\t\t\t                  $\"                  :                      < ^o\"\n" +
                    "\t\t\t                 $\"                ~ x\"                       \"u N#\n" +
                    "\t\t\t                $\"               z\" dF x$   $                  ^k'U\n" +
                    "\t\t\t               $\"              n4 .\"# d$   d$   >               $ $\n" +
                    "\t\t\t              $\"        u\"   ?  .u/ <%'\"  d$$  u x              9 $\n" +
                    "\t\t\t             $\"         .o %'$@ $$  L3\" u$$$~ d\"x$         r4   $     \n" +
                    "\t\t\t             F         ''  @H@\" \"'.z$e@$$$$\"x* -\"\\r     W  E4  :$\n" +
                    "\t\t\t            F           #- JMM$u. @$$$$$$$$$z$ 4 .      R :\"$ .$\n" +
                    "\t\t\t           F              \"?MMMR$$$$$$$$$$$ #h.$#     .$\" Pd$z                                                              \n" +
                    "\t\t\t          F                 RMMM$$$$$$$XMf$. '$$\"     9F.$                           Hello, I'm Paige.                       \n" +
                    "\t\t\t         P                   MMM$$$$L \"\"N@$$$$$       4d$                  A Page-Accessed Information Gateway              \n" +
                    "\t\t\t        $                  .x *MM$$$$*$$$$$$$*        '$              I'm an experiment, designed by Esteban Valle        \n" +
                    "\t\t\t       $                   $RH.`M$$$$$$$$$$*           $                              Can I serve you?                          \n" +
                    "\t\t\t      $                   :MM$$b \"$$$$*\"\"              $\n" +
                    "\t\t\t     $                    $MM$$$$8W-                   4\n" +
                    "\t\t\t    $                    '$MMM$$$$$      ..             $\n" +
                    "\t\t\t   $                       ^*M$$$$$?.     `~~~ ee.      $\n" +
                    "\t\t\t  $\"                        !.*$$$$$$k         $$$b     '$\n" +
                    "\t\t\t $\"    .eu.                  `!'#**7CU         9$**$o    R\n" +
                    "\t\t\t F    @$B*\".e.                 `-\"$$$$B      ...uC?*MI    $\n" +
                    "\t\t\tP    dP\"ud$$$WXL                   R$$$   \"\"#*R$beUJ9$$.  `$\n" +
                    "\t\t\t     Ldt$$$l$$$#c                   \"$$        *uJC$$$$$N. #$\n" +
                    "\t\t\t    @$L$$l$$$Pbc$                     R      ud*\"?\"*MM$$$$e #$\n" +
                    "\t\t\t    $$$G$$$$l$$\" z!x                           #$$$$u*MR$$$$b2$\n" +
                    "\t\t\t   @$T@$$$T@$* u!$@M$L                        ^*^$$$$$(MMR$$$$N\n" +
                    "\t\t\t  :#d$$$#d$P\".$$LF$X$Rb.               J$L      ''$$$$$*J#MM$$$E\n" +
                    "\t\t\t  )$$$*z$$\".@$$$B:$$$$$$$c...ur        $$$.      d:$$#z$$$4RM$$$'\n" +
                    "\t\t\t x$$$)$$\" d$$$$$$$$$$BR$$$$$$\"        $$$$$:    :$$ $$$$#\\4RM$$$>\n" +
                    "\t\t\t eI#@$* d$$$$$$$*t$$$$$$$$$$$L       d$$$$$$L   $$$N$$#o$Lt8M$$$b  \n" +
                    "\t\t\t $T$$\"zW@IBbW$$$$3$$$$$$$$$$$$$N    d$$$$$$$$o d9$$FuH$$$EMMM$$$$   \n" +
                    "\t\t\t  \"* $$$$$$$$$**#M$$$$$$$$R$$$$$$u d$$$$$$$$$$$$H2h4MM$$$F$RM$$$$    \n" +
                    "\t\t\t    $$We(?Lx.  ~M$W*$$$$8$$$$$$$$$$$$$$M)W$$#d$RX$ XRM$$$>MRM$$$$L    \n" +
                    "\t\t\t    $$$$$MM$X   ~$$N\"$R$$$$$$$$$` '$$$T@$$$X$*\"@$\" XMMM$$ MMMM$$$$    \n" +
                    "\t\t\t    $$$$$BM@R    \"$$$7$X$$$$$$$$   $T@$$RM@$$  <\"  !$MMM$'R@MM$$$$    \n" +
                    "\t\t\t    $$$$$$MRR      $$$$$x$$$$$$$$eMo$$$$X$$$Tuu^   'BMMM$tMMMM$$$$L   \n" +
                    "\t\t\t    $$$$$$MMR      %$$$$$k$$$$$$RX$$$$$$$$8W$$F     #$MMf8MMMM$$$$$   \n" +
                    "\t\t\t    $$$$$$MM$      $$$$$$$k$$$$l$$$$$$$$$$$$$$       $MM>RMMMM$$$$$.  \n" +
                    "\t\t\t    $$$$$$$M$     J$$*$$$$$iR$$$$$$$$$$$$$$$$M        $M>MMMM$$$$$$$  \n" +
                    "\t\t\t    $$$$$$$$5k   .$$$9WeeWWWoeWe@$$$N$$$$$$$$k         $XMMMH$$$$$$$  \n" +
                    "\t\t\t    $$$$$$$$$$c .$$BRBbebUUWUUCC$$$$$$$$$$$$$$         '$LMMM$$$$$$P\n" +
                    "\t\t\t     $$$$$$$$$B.$$$$$$$$$$$$$$$*#$$$$$$$$$$$\":\"          $8MM$$$$$$\n" +
                    "\t\t\t     'MR$$$$$$$8`$$$$M$$$$$$$$F  4$$$$$$$$$$.`~k          \"NMM$$$$\n" +
                    "\t\t\t      #MMR$$$$$$k$$$$B$$$$$$$$$oz$$$$$$$$$$$$N$$c           ^N$$#\n" +
                    "\t\t\t       $MMM$$$$$$'$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$c\n" +
                    "\t\t\t       '$MMR$$$$$N?$$5$$$$$$$$$$$$$$$$$$$$$$$$$$$$c\n" +
                    "\t\t\t        `@MMR$$$$$k$B$R$$$$$$$$$$$$$$$$$$$$$$$$$$$$b\n" +
                    "\t\t\t         #@MMR$$$$$'#R$$$$$$$$$$$$$$$$$$R*\"\"\"   `W@=\n" +
                    "\t\t\t          #8MMR$$$$$   '\"#**R$$$***\"\"\n" +
                    "\t\t\t           #MMMM$$$$b\n" +
                    "\t\t\t            #MMMM$$$$i\n" +
                    "\t\t\t             #MMMM$$$$L             <\n" +
                    "\t\t\t              #MMRM$$$$             '        '.\n" +
                    "\t\t\t               \"8MM?$$$$             k        `:\n" +
                    "\t\t\t                 $MMR$$$N            ?         `:       .\n" +
                    "\t\t\t                  #8M$$$$N            M         4:       ~:\n" +
                    "\t\t\t      :            ^5M$$$$$$$.        !>         !:       `!:\n" +
                    "\t\t\t      M             '$M#$$$$$$u       'X          !h        !?:\n" +
                    "\t\t\t      !              \"MMM$$$$$$b       !L          !h        ~!!:4\n" +
                    "\t\t\t     <!               $MMM$$$$$ $      `!          `Xh        `!!!:?L\n" +
                    "\t\t\t     !!               ?MMMM$$$$K$       !h          `!h         ~!!!h\"\n" +
                    "\t\t\t   b !!                $MMMM$$$$`       ~!           4!h         `!!!!\n" +
                    "\t\t\t   c'~`                $\"M?Mk#$$k        !!           !!h         '!!!\n" +
                    "\t\t\t   R!!!                ^\"~#N?N'$$        !!:           !!h          %!\n" +
                    "\t\t\t   F!!!                    '  `          '!!           '!!h          `\n" +
                    "\t\t\t    !!!                    'L             !!L           `!!h\n" +
                    "\t\t\t   '!!f                     X             '!!            ~!!h\n" +
                    "\t\t\t   !!!                      ?              !!h            !!!h    ..e@\n" +
                    "\t\t\t  E!!!                      !>             `!!            'X!!\\*$\n\n\n\n";


}

