import java.util.HashMap;
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
    private boolean verbose = true;

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

        if (verbose) System.out.println("Unsorted " + this);

        sortItemsArray(); // Delegate method, in this class this calls Java.util.Arrays.sort()

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
     * Finds and returns the minimum (smallest) item in the list. Equivalent to calling get(0)
     *
     * @return the smallest item in the collection
     */
    public T min() {
        return get(0);
    }

    /**
     * Finds and returns the maximum item in the list. Equivalent to calling get(size() - 1)
     *
     * @return the largest item in the collection
     */
    public T max() {
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

    /**
     * The delegate sorting method. Sorts the objects in descending order.
     * This method is optomized for ScoredDocument objects.
     */
    private void sortItemsArray() {
        java.util.Arrays.sort(this.items); // Use the built in sorting methods to sort this array descending

    }

    private String nameplate = "__/\\\\\\\\\\\\\\\\\\\\\\\\\\_______/\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_TM        \n" +
            " _\\/\\\\\\/////////\\\\\\___/\\\\\\\\\\\\\\\\\\\\\\\\\\__\\/////\\\\\\///____/\\\\\\//////////__\\/\\\\\\///////////__       \n" +
            "  _\\/\\\\\\_______\\/\\\\\\__/\\\\\\/////////\\\\\\_____\\/\\\\\\______/\\\\\\_____________\\/\\\\\\_____________      \n" +
            "   _\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/__\\/\\\\\\_______\\/\\\\\\_____\\/\\\\\\_____\\/\\\\\\____/\\\\\\\\\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\_____     \n" +
            "    _\\/\\\\\\/////////____\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_____\\/\\\\\\_____\\/\\\\\\___\\/////\\\\\\_\\/\\\\\\///////______    \n" +
            "     _\\/\\\\\\_____________\\/\\\\\\/////////\\\\\\_____\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_____________   \n" +
            "      _\\/\\\\\\_____________\\/\\\\\\_______\\/\\\\\\_____\\/\\\\\\_____\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_____________  \n" +
            "       _\\/\\\\\\_____________\\/\\\\\\_______\\/\\\\\\__/\\\\\\\\\\\\\\\\\\\\\\_\\//\\\\\\\\\\\\\\\\\\\\\\\\/__\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\_ \n" +
            "        _\\///______________\\///________\\///__\\///////////___\\////////////____\\///////////////__\n";


    private String paige =
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
                    "\t\t\t  E!!!                      !>             `!!            'X!!\\*$\n";


}

