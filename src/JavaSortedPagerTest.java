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
public class JavaSortedPagerTest {

    /** An object for testing. Basically a simple Integer object */
    public class TestObject implements Comparable<TestObject>{
        public int value;

        public TestObject(int value){
            this.value = value;
        }

        @Override
        public int compareTo(TestObject o) {
            return this.value - o.value; // Follows all conventions for Comparable
        }

        @Override
        public String toString(){
            return "TestObject: " + this.value;
        }
    }



    public static final int TEST_SIZE = 100;
    public static final int PAGE_SIZE = 45;
    public static final boolean RANDOM = false;

    JavaSortedPager<TestObject> pager;


    @Before
    public void setUp() throws Exception {
        TestObject[] testArray = new TestObject[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            if (RANDOM) testArray[i] = new TestObject((int)(Math.random()*TEST_SIZE)); // Generates TestObjects in random order
            else testArray[i] = new TestObject(TEST_SIZE - (i + 1)); // Generates TestObjects in reverse order, so they can be sorted
        }

        pager = new JavaSortedPager<>(testArray, PAGE_SIZE);
        pager.setVerbose(true);

        //System.out.println(pager);

    }




    @Test
    public void testGet() throws Exception {
        if (!RANDOM){
            for (int test = 0; test < TEST_SIZE; test += Math.random() * (TEST_SIZE / 50)) { // Test some number of random values
                assertEquals(test, pager.get(test).value); // The index should be equal to the value stored there, if it's not random
            }
        }

    }

    @Test
    /** Tests whether the collection is sorted properly after instantiation */
    public void testSort() {
        for (int test = 0; test < TEST_SIZE - 1; test += 1) { // Test some number of random values
            // Test whether each value is less than the next value
            assert (pager.get(test).compareTo(pager.get(test + 1)) < 0); // compareTo should return < 0 if a < b
        }
    }


    @Test
    public void testMin() throws Exception {
        if (!RANDOM){
            assertEquals(0, pager.min().value);
        } else {
           // Go at it, friend.
        }

    }

    @Test
    public void testMax() throws Exception {
        if (!RANDOM) {
            assertEquals(TEST_SIZE - 1, pager.max().value);
        } else {
            // Good luck, pal
        }

    }

    @Test
    public void testPages() throws Exception {
        double pageCount = (double)TEST_SIZE / (double)PAGE_SIZE; // We should always round up page count

        assertEquals((int)Math.ceil(pageCount), pager.pages());

    }

    @Test
    public void testPage() throws Exception {
        for (int section = 0; section < pager.pages(); section++) {
            Object[] page = pager.page(section); // Generic arrays are returned as Object[]
            assertTrue(page.length <= PAGE_SIZE);

            for (int i = 0; i < page.length; i++) {
                assertSame(page[i], pager.get((section * PAGE_SIZE) + i));
            }
        }

    }

}