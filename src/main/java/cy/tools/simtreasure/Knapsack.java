
package cy.tools.simtreasure;

import java.util.List;

import org.joda.time.LocalDate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class Knapsack implements Comparable<Knapsack> {
    /**
     * Knapsack, package
     */
    public Knapsack(LocalDate date, double destination) {
        this.destination = destination;
        this.date = date;
    }

    /**
     * Knapsack, package
     */
    public Knapsack(Knapsack from) {
        this.destination = from.destination;
        this.date = from.date;
        this.weight = from.weight;
        this.value = from.value;

        for (DepositEnv se : from.pis) {
            this.pis.add(se);
        }
    }

    double destination; // dest amount
    LocalDate date;
    double weight = 0d; // amount
    double value = 0d; // lost
    List<DepositEnv> pis = Lists.<DepositEnv>newArrayList();

    /**
     * count of stuffs
     *
     * @return
     */
    public int size() {
        return this.pis.size();
    }

    /**
     * return will be full if add new one dep into package
     */
    public boolean willFull(DepositEnv dep) {
        return this.weight + dep.getWeight() >= this.destination;
    }

    /**
     * add new one dep into package
     */
    public Knapsack add(DepositEnv dep) {
        this.pis.add(dep);
        this.weight += dep.getWeight();
        this.value += dep.getValue();
        return this;
    }

    /**
     * return whether package is empty or not
     */
    public boolean isEmpty() {
        return this.pis.isEmpty();
    }

    /**
     * return whether package is full or not
     */
    public boolean isFull() {
        return this.weight >= this.destination;
    }

    /**
     * get value / lost
     */
    public double getLost() {
        if (this.isFull()) {
            return this.value;
        }
        return this.destination;
    }

    /**
     * implement method of compare interface
     */
    public int compareTo(Knapsack kn) {
        if (this.value == kn.value)
            return 0;
        return this.value > kn.value ? 1 : -1;
    }

    /**
     * print package to console
     */
    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("date:").append(date).append("\t");
        sb.append("lost:").append(this.getLost()).append("\t");
        sb.append("volume:").append(this.weight).append("\t");
        sb.append("destination:").append(this.destination).append("\t");
        sb.append("\n");
        for (DepositEnv dep : this.pis) {
            sb.append("\t").append(dep.dep.toString()).append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * to string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("date:").append(date).append("\t");
        sb.append("lost:").append(String.format("%8.2f", this.value)).append("\t");
        sb.append("volume:").append(String.format("%.2f", this.weight)).append("\t");
        sb.append("deposites:");
        for (DepositEnv dep : this.pis) {
            sb.append(dep.dep.getId()).append("\t");
        }
        return sb.toString();
    }
}
