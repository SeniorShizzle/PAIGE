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
            return o.value - this.value; // Follows all conventions for Comparable
        }

        @Override
        public String toString(){
            return "TestObject: " + this.value;
        }
    }



    public static final int TEST_SIZE = 10000;
    public static final int PAGE_SIZE = 45;
    public static final boolean RANDOM = false;

    JavaSortedPager<TestObject> pager;


    @Before
    public void setUp() throws Exception {
        TestObject[] testArray = new TestObject[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            if (RANDOM) testArray[i] = new TestObject((int)(Math.random()*TEST_SIZE)); // Generates TestObjects in random order
            else testArray[i] = new TestObject(TEST_SIZE - i); // Generates TestObjects in reverse order

        }

        pager = new JavaSortedPager<>(testArray, PAGE_SIZE);

    }

    @Test
    public void testGet() throws Exception {
        if (RANDOM){

        }

    }

    @Test
    public void testMin() throws Exception {

    }

    @Test
    public void testMax() throws Exception {

    }

    @Test
    public void testSize() throws Exception {

    }

    @Test
    public void testPageSize() throws Exception {

    }

    @Test
    public void testPages() throws Exception {

    }

    @Test
    public void testPage() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }
}