import java.io.IOException;

public class PAIGE {

    public static void main(String [] args){
        //ScoredResultsPager paige = new ScoredResultsPager();

        System.out.printf("\n\n\nHi, I'm Paige, your personal assistant. Would you like to run a simulation? Y/N\n> ");

        char input = 0;
        try {
            input = (char) System.in.read();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (input != 0) {
                switch (input) {
                    case 'y':
                    case 'Y':
                        // YES
                        break;
                    case 'n':
                    case 'N':
                    case 'q':
                    case 'Q':
                        // NO
                        System.out.println("Okay! Please come back again!");
                        return;
                }
            }
        }


    }
}
