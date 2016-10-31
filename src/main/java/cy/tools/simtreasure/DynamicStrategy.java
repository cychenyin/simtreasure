
package cy.tools.simtreasure;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

public class DynamicStrategy implements WithdrawlStrategy {

    public DynamicStrategy(Account ac, double destination) {
        super();
        this.ac = ac;
        this.destination = destination;
    }

    private Account ac = new Account();
    private double destination = 125000d;
    List<StuffEnv> stuffs;

    private void sort(LocalDate date) {
        List<Deposit> sortedDeposits = ac.sortedDeposits();
        stuffs = Lists.<StuffEnv>newArrayList();
        for (Deposit dep : sortedDeposits) {
            stuffs.add(new StuffEnv(dep, date));
        }

        // sort by lost
        Collections.sort(stuffs, new Comparator<StuffEnv>() {

            public int compare(StuffEnv o1, StuffEnv o2) {
                return Double.valueOf(o1.getValue()).compareTo(Double.valueOf(o2.getValue()));
            }
        });
    }

    private void reid(List<StuffEnv> stuffs) {
        for (int i = 0; i < stuffs.size(); i++) {
            stuffs.get(i).dep.setId(i + 1);
        }
    }

    /**
     * imple
     */
    public Knapsack onDate(LocalDate date) {
        this.sort(date);
        this.reid(this.stuffs);

        List<StuffEnv> filterStuffs = Lists.<StuffEnv>newArrayList();
        Knapsack min = new Knapsack(date, this.destination);
        min.value = this.destination;

        // process stuff amount > destition
        for (int m = 0; m < this.stuffs.size(); m++) {
            if (this.stuffs.get(m).getWeight() > this.destination) {
                Knapsack kn = new Knapsack(date, this.destination).add(this.stuffs.get(m));
                if (min.compareTo(kn) > 0) {
                    min = kn;
                }
            } else {
                filterStuffs.add(this.stuffs.get(m));
            }
        }

        // small stuff
        Knapsack seed = new Knapsack(date, this.destination);
        min = this.pick(seed, filterStuffs, 0, min);
        return min;
    }

    private Knapsack pick(Knapsack seed, List<StuffEnv> all, int restIndex, Knapsack min) {
        for (int size = 2; size <= all.size(); size++) {
            min = pick(seed, size, all, restIndex, min);
            // min = leftOrRight(min, kn);
        }
        return min;
    }

    private Knapsack pick(Knapsack seed, int size, List<StuffEnv> all, int restIndex, Knapsack min) {

        Knapsack yes = new Knapsack(seed);
        yes.add(all.get(restIndex));
        if (yes.isFull() && yes.size() == size) {
            min = leftOrRight(min, yes);
        } else {
            if (yes.isFull() == false && yes.size() < size && restIndex + 1 < all.size()) {
                Knapsack picked = pick(yes, size, all, restIndex + 1, min);
                if (picked != null && picked.isFull() && picked.size() == size)
                    min = leftOrRight(min, picked);
            }
        }

        return restIndex + 1 < all.size() ? pick(seed, size, all, restIndex + 1, min) : min;
    }

    private Knapsack leftOrRight(Knapsack left, Knapsack right) {
        return (left == null || (right != null && right.getLost() < left.getLost())) ? right : left;
    }

    /**
     * env just, you know.
     */
    public static class StuffEnv extends DepositEnv {
        public StuffEnv(Deposit dep, LocalDate date) {
            super(dep, date);
        }

        @Override
        public String toString() {
            return "StuffEnv weight: " + getWeight() + " " + "value: " + getValue();
        }
    }

    /**
     * test entry
     */
    public static void main(String[] args) {
        /*
         * LocalDate date = LocalDate.now().plusDays(20);
         * System.out.println("the minimux lost before date: " + date);
         * Account ac = new Account();
         * ac.load("d:\b.txt");
         *
         * DynamicStrategy dn = new DynamicStrategy(ac, 125000d);
         *
         * Knapsack min = null;
         * LocalDate ld = LocalDate.now();
         * // while(!ld.isAfter(date)) {
         * Knapsack kn = dn.pi(date);
         *
         * min = (min == null || (kn != null && min.compareTo(kn) > 0)) ? kn : min;
         * // }
         *
         * if (min != null)
         * min.print();
         * else
         * System.out.println("no solvation.");
         */
        LocalDate date = LocalDate.now();
        double dest = 17000d;
        Account ac = new Account();
        ac.load("d:\b.txt");

        WithdrawlStrategy withdrawal = new DynamicStrategy(ac, dest);
        Knapsack kn = withdrawal.onDate(date);
        if (kn != null)
            kn.print();
    }

    public Knapsack beforeDate(LocalDate date) {
        //return this.beforeDateThreadPool(date);
        return beforeDateParallel(date);
        // return beforeDateSimple(date);
    }

    private Knapsack beforeDateSimple(LocalDate date) {
        LocalDate ld = LocalDate.now();
        Knapsack min = null;
        while (!ld.isAfter(date)) {
            min = leftOrRight(min, this.onDate(ld));
            ld = ld.plusDays(1);
        }
        return min;
    }

    private Knapsack beforeDateThreadPool(LocalDate date) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(100);
        DynamicStrategy ds = new DynamicStrategy(this.ac, this.destination);
        LocalDate ld = LocalDate.now();

        List<Task> tasks = Lists.<Task>newArrayList();
        while (!ld.isAfter(date)) {
            Task task = new Task(ds, ld);
            tasks.add(task);
            pool.submit(task);

            ld = ld.plusDays(1);
        }
        pool.shutdown();
        try {
            // wait forever
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Knapsack min = null;
        for (int idx = 0; idx < tasks.size(); idx++) {
            min = leftOrRight(min, tasks.get(idx).result);
        }
        return min;
    }

    private Knapsack beforeDateParallel(LocalDate date) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(200);
        DynamicStrategy ds = new DynamicStrategy(this.ac, this.destination);
        LocalDate ld = LocalDate.now();

        List<Task> tasks = Lists.<Task>newArrayList();

        while (!ld.isAfter(date)) {
            Task task = new Task(ds, ld);
            tasks.add(task);
            forkJoinPool.submit(task);
            ld = ld.plusDays(1);
        }

        forkJoinPool.shutdown();
        try {
            // wait forever
            forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Knapsack min = null;
        for (int idx = 0; idx < tasks.size(); idx++) {
            min = leftOrRight(min, tasks.get(idx).result);
        }
        return min;
    }

    // extends RecursiveAction
    public static class Task implements Callable {
        final DynamicStrategy ds;
        final LocalDate date;

        public Task(DynamicStrategy ds, LocalDate date) {
            this.ds = ds;
            this.date = date;
        }

        Knapsack result;

        // @Override
        protected void compute() {
            this.result = ds.onDate(date);
        }

        public Object call() throws Exception {
            this.result = ds.onDate(date);
            return this.result;
        }

    }

}
