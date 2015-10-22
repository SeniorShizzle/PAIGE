import java.io.IOException;

import static java.lang.Thread.sleep;

public class ScoredResultsPager {

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

    public ScoredResultsPager(){
        System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t\tWelcome To\n\n");
        System.out.println(nameplate);
        System.out.println("\n\t\t\t\t\t\tThe Page-Accessed Information Gateway Experiment\n\n");

        try {
            sleep(1500); // Wait 1.5 seconds
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        System.out.print(paige);
        System.out.printf("\n\n\nHi, I'm Paige, your personal assistant. Would you like to run a simulation? Y/N\n> ");

        char input = 0;
        try {
            input = (char)System.in.read();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (input != 0) {
                switch (input){
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
