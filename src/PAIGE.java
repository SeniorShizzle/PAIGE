import java.io.IOException;
import java.lang.System;

public class PAIGE {

    public static void main(String [] args){
        //ScoredResultsPager paige = new ScoredResultsPager();

//        System.out.printf("\n\n\nHi, I'm Paige, your personal assistant. Would you like to run a simulation? Y/N\n> ");
//
//        char input = 0;
//        try {
//            input = (char) System.in.read();
//        } catch (IOException e) {
//            System.out.println(e.getLocalizedMessage());
//        } finally {
//            if (input != 0) {
//                switch (input) {
//                    case 'y':
//                    case 'Y':
//                        // YES
//                        break;
//                    case 'n':
//                    case 'N':
//                    case 'q':
//                    case 'Q':
//                        // NO
//                        System.out.println("Okay! Please come back again!");
//                        System.exit(0);
//                }
//            }
//        }


        int testSize = 100;
        int pageSize = 100;

        System.out.println("Test Size, Java Run Time, Custom Run Time");

        for (int i = 0; i < 10; i++) {


            ScoredDocument[] javaSortedTestArray = Lab2.generateScoredDocuments(testSize);
            ScoredDocument[] selfSortedTestArray = new ScoredDocument[testSize];

            System.arraycopy(javaSortedTestArray, 0, selfSortedTestArray, 0, testSize); // Start with two identical arrays

            assert javaSortedTestArray != selfSortedTestArray;

            long startJavaSorted = System.nanoTime();
            JavaSortedPager<ScoredDocument> javaSortedPager = new JavaSortedPager<>(javaSortedTestArray, pageSize);
            javaSortedPager.setShouldCache(false);
            javaSortedPager.page(0);
            long stopJavaSorted = System.nanoTime();
            ScoredResultsPager<ScoredDocument> scoredResultsPager =
                    new ScoredResultsPager<>(
                            selfSortedTestArray,
                            pageSize,
                            ScoredResultsPager.SortType.FUCKIN_FAST_FLASH_SORT,
                            false, true, false);
            scoredResultsPager.page(0);
            long stopSelfSorted = System.nanoTime();

            long javaSortedTime = stopJavaSorted - startJavaSorted;
            long selfSortedTime = stopSelfSorted - stopJavaSorted;
            System.out.println(testSize + ", " + javaSortedTime + ", " + selfSortedTime);
            //System.out.println("Java ran in " + javaSortedTime + ", your sort ran in " + selfSortedTime +
            //        " meaning your sort was faster for " + testSize + " objects by " + ((javaSortedTime - selfSortedTime) / 1000000.0) + " milliseconds. Good work!");

            testSize *= 2;
        }


    }
}
