
package cy.tools.simtreasure;

import org.joda.time.LocalDate;

/**
 * withdrawal strategy
 */
public interface WithdrawlStrategy {

    /**
     * withdrawal on the specified day
     * @param date
     * @return
     */
    Knapsack onDate(LocalDate date);


    /**
     * withdrawal from now on until the specified day
     * @param date
     * @return
     */
    Knapsack beforeDate(LocalDate date);

}
