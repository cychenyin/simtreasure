
package cy.tools.simtreasure;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

/**
 * Withdrawal using greedy arithmetic
 * @author cheny-ab
 */
public class GreedyStrategy implements WithdrawlStrategy {

    public GreedyStrategy(Account ac, double destination) {
        super();
        this.ac = ac;
        this.destination = destination;
    }

    private Account ac = new Account();
    private double destination = 125000d;
    List<GreedyEnv> envs;

    /**
     * env just; and change imple of value by coefficient.
     */
    public static class GreedyEnv extends DepositEnv {
        public GreedyEnv(Deposit dep, LocalDate date) {
            super(dep, date);
            this.coefficient = this.lost * 10000d / amount;
        }

        double coefficient;
    }

    private void init(LocalDate date) {
        List<Deposit> sortedDeposits = ac.sortedDeposits();
        envs = Lists.<GreedyEnv>newArrayList();
        for (Deposit dep : sortedDeposits) {
            envs.add(new GreedyEnv(dep, date));
        }

        // sort by lost
        Collections.sort(envs, new Comparator<GreedyEnv>() {

            public int compare(GreedyEnv o1, GreedyEnv o2) {
                return Double.valueOf(o1.coefficient).compareTo(Double.valueOf(o2.coefficient));
            }
        });
    }

    /*
     * imple
     * @see cy.tools.simtreasure.WithdrawlStategy#pi1(org.joda.time.LocalDate)
     */
    public Knapsack onDate(LocalDate date) {
        LocalDate ld = LocalDate.now();
        List<Knapsack> list = Lists.<Knapsack>newArrayList();
        while (!ld.isAfter(date)) {
            this.init(ld);

            Knapsack pi = new Knapsack(ld, this.destination);
            for (GreedyEnv dep : this.envs) {
                if (!pi.willFull(dep)) {
                    pi.add(dep);
                } else {
                    pi.add(dep);
                    break;
                }
            }
            list.add(pi);
            // pi.print();
            ld = ld.plusDays(1);
        }
        Collections.sort(list, new Comparator<Knapsack>() {

            public int compare(Knapsack o1, Knapsack o2) {
                return Double.valueOf(o1.getLost()).compareTo(Double.valueOf(o2.getLost()));
            }
        });

        // System.out.println("the minimux lost before date:" + date.toString());
        // if (list.size() > 0) {
        // list.get(0).print();
        // }
        // if (list.size() > 1) {
        // list.get(list.size() - 1).print();
        // }
        return list.size() > 0 ? list.get(0) : null;
    }

    public Knapsack beforeDate(LocalDate date) {
        LocalDate ld = LocalDate.now();
        Knapsack min = null;
        while(!ld.isAfter(date)) {
            min = leftOrRight(min, this.onDate(ld));
            ld = ld.plusDays(1);
        }
        return min;
    }

    private Knapsack leftOrRight(Knapsack left, Knapsack right) {
        return (left == null || (right != null && right.getLost() < left.getLost())) ? right : left;
    }

    /**
     * test entry
     */
    public static void main(String[] args) {
        System.out.println("greedy ");
        Account ac = new Account();
        ac.load("d:\b.txt");

        WithdrawlStrategy judge = new GreedyStrategy(ac, 125000d);
        Knapsack pi = judge.onDate(LocalDate.now().plusDays(10));
        if (pi != null)
            pi.print();
    }
}
