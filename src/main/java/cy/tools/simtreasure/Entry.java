
package cy.tools.simtreasure;

import org.joda.time.LocalDate;

import com.google.common.base.Stopwatch;

public class Entry {

    public static void main(String[] args) {

        Stopwatch sw = Stopwatch.createStarted();
        LocalDate date = LocalDate.now().plusDays(10);
        double dest = 124000d;
        Account ac = new Account();
        ac.load("d:\b.txt");

        System.out.println("i for 1day " + ac.interestOfOneDay().toPlainString() + "of =" + ac.amount().toPlainString()) ;

        WithdrawlStrategy withdrawal = new DynamicStrategy(ac, dest);
        Knapsack kn = withdrawal.beforeDate(date);
        print(kn);
        sw.stop();
        System.out.println(sw.toString());

        sw = Stopwatch.createStarted();
        withdrawal = new GreedyStrategy(ac, dest);
        kn = withdrawal.beforeDate(date);
        print(kn);

        sw.stop();
        System.out.println(sw.toString());
    }

    private static void print(Knapsack min) {
        if (min != null)
            min.print();
        else
            System.out.println("no solvation.");
    }
}
