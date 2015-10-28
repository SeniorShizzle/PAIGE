import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*PAIGE, 2015

Created Oct 22, 2015 by Esteban Valle

Copyright © 2015  Esteban Valle. All rights reserved.

+1-775-351-4427
esteban@thevalledesign.com
http://facebook.com/SeniorShizzle
*/
public class ScoredResultsPagerTest {

    /**
     * An object for testing. Basically a simple Integer object
     */
    public class TestObject implements Comparable<TestObject> {
        public int value;

        public TestObject(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(TestObject o) {
            return this.value - o.value; // Follows all conventions for Comparable
        }

        @Override
        public String toString() {
            return "TestObject: " + this.value;
        }
    }


    public static final int TEST_SIZE = 100;
    public static final int PAGE_SIZE = 45;
    public static final boolean RANDOM = false;
    public static final boolean testCaching = true;
    public static final boolean useScoredDocuments = false;

    ScoredResultsPager<TestObject> pager;
    ScoredResultsPager<ScoredDocument> documentPager;

    Comparable[] testArray;


    @Before
    public void setUp() throws Exception {

        if (pager == null) {
            if (useScoredDocuments) {
                this.testArray = Lab2.generateScoredDocuments(TEST_SIZE);
                documentPager = new ScoredResultsPager<>((ScoredDocument[])testArray, PAGE_SIZE);
                documentPager.setVerbose(true);
                documentPager.setShouldCache(testCaching);

            } else {
                this.testArray = new TestObject[TEST_SIZE];
                for (int i = 0; i < TEST_SIZE; i++) {
                    if (RANDOM)
                        testArray[i] = new TestObject((int) (Math.random() * TEST_SIZE)); // Generates TestObjects in random order
                    else
                        testArray[i] = new TestObject(i); // Generates TestObjects in reverse order, so they can be sorted
                }
                pager = new ScoredResultsPager<>((TestObject[])testArray, PAGE_SIZE);
                pager.setVerbose(true);
                pager.setShouldCache(testCaching);

            }
        }

        java.util.Arrays.sort(testArray); // sort the test array for comparison
        // Now we have to reverse the array to get it into reverse order
        for (int i = 0; i < testArray.length / 2; i++) {
            // swap the elements
            Comparable temp = testArray[i];
            testArray[i] = testArray[testArray.length - (i + 1)];
            testArray[testArray.length - (i + 1)] = temp;
        }


        //System.out.println(pager);
    }


    @Test
    public void testGet() throws Exception {
        if (useScoredDocuments){
            for (int test = 0; test < TEST_SIZE; test += Math.random() * (TEST_SIZE / 50)) { // Test some number of random values
                assertEquals(testArray[test], documentPager.get(test)); // Compare the sorted input array to the custom sorted object
            }
        }
        else {
            if (!RANDOM) {
                for (int test = 0; test < TEST_SIZE; test += Math.random() * (TEST_SIZE / 50)) { // Test some number of random values
                    assertEquals(TEST_SIZE - (test + 1), pager.get(test).value); // The index should be equal to the value stored there, if it's not random
                }
            }
        }

    }

    @Test
    /** Tests whether the collection is sorted properly after instantiation */
    public void testSort() {
        if (!useScoredDocuments)
        for (int test = 0; test < TEST_SIZE - 1; test += 1) { // Test some number of random values
            // Test whether each value is less than the next value
            assert (pager.get(test).compareTo(pager.get(test + 1)) > 0); // compareTo should return < 0 if a < b
        }
    }


    @Test
    public void testMin() throws Exception {
        if (!RANDOM && !useScoredDocuments) {
            assertEquals(0, pager.min().value);
        }

    }

    @Test
    public void testMax() throws Exception {
        if (!useScoredDocuments)
        if (!RANDOM) {
            assertEquals(TEST_SIZE - 1, pager.max().value);
        }

    }

    @Test
    public void testPages() throws Exception {
        double pageCount = (double) TEST_SIZE / (double) PAGE_SIZE; // We should always round up page count

        if (useScoredDocuments)
            assertEquals((int) Math.ceil(pageCount), documentPager.pages());

        if (!useScoredDocuments)
            assertEquals((int) Math.ceil(pageCount), pager.pages());


    }

    @Test
    public void testPage() throws Exception {
        if (useScoredDocuments) {
            for (int section = 0; section < documentPager.pages(); section++) {
                Object[] page = documentPager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], documentPager.get((section * PAGE_SIZE) + i));
                }
            }
        } else {
            for (int section = 0; section < pager.pages(); section++) {
                Object[] page = pager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], pager.get((section * PAGE_SIZE) + i));
                }
            }
        }

    }

    @Test
    public void setTestCaching() throws Exception {

        if (useScoredDocuments) {
            documentPager.setShouldCache(true);

            for (int section = 0; section < documentPager.pages(); section++) {
                Object[] page = documentPager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], documentPager.get((section * PAGE_SIZE) + i));
                }
            }

            for (int section = 0; section < documentPager.pages(); section++) {
                Object[] page = documentPager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], documentPager.get((section * PAGE_SIZE) + i));
                }
            }
        } else {
            pager.setShouldCache(true); // if it wasn't set before

            for (int section = 0; section < pager.pages(); section++) {
                Object[] page = pager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], pager.get((section * PAGE_SIZE) + i));
                }
            }

            for (int section = 0; section < pager.pages(); section++) {
                Object[] page = pager.page(section); // Generic arrays are returned as Object[]
                assertTrue(page.length <= PAGE_SIZE);

                for (int i = 0; i < page.length; i++) {
                    assertSame(page[i], pager.get((section * PAGE_SIZE) + i));
                }
            }
        }

    }

}