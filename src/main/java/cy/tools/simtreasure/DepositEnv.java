
package cy.tools.simtreasure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.joda.time.LocalDate;
import com.google.common.collect.Lists;

public class DepositEnv {
    LocalDate date;
    Deposit dep;
    double lost;
    double amount;

    public DepositEnv(Deposit dep, LocalDate date) {
        this.date = date;
        this.dep = dep;
        this.lost = dep.lost(date).doubleValue();
        this.amount = dep.getPrincipalAmount().doubleValue();
    }

    @Override
    public String toString() {
        return "[weight: " + getWeight() + " " + "value: " + getValue() + "]";
    }

    /** weight of package
     */
    public double getWeight() {
        return this.amount;
    }

    /** value of package
    */
    public double getValue() {
        return this.lost;
    }
}
