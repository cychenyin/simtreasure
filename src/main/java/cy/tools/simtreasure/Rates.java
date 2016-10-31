
package cy.tools.simtreasure;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Rates {

    static BigDecimal currentShortTermRate = new BigDecimal("0.0030");
    static BigDecimal currentM3FixTermRate = new BigDecimal("0.0135");
    static BigDecimal currentM6FixTermRate = new BigDecimal("0.0155");
    static BigDecimal currentY1FixTermRate = new BigDecimal("0.0175");
    static BigDecimal currentY2FixTermRate = new BigDecimal("0.0225");
    static BigDecimal currentY3FixTermRate = new BigDecimal("0.0275");
    static BigDecimal currentY5FixTermRate = new BigDecimal("0.0275");

    /**
     * get rate by term
     */
    public static BigDecimal getRate(Terms term) {
        return getRate(LocalDate.now(), term);
    }

    /**
     * get rate of short term
     */
    public static BigDecimal shortTermRate() {
        return Rates.currentShortTermRate;
    }

    /**
     * get rate of specified date; NOT yet implement.
     */
    public static BigDecimal getRate(LocalDate date, Terms term) {
        switch (term) {
            case M3:
                return Rates.currentM3FixTermRate;
            case M6:
                return Rates.currentM6FixTermRate;

            case Y1:
                return Rates.currentY1FixTermRate;

            case Y2:
                return Rates.currentY2FixTermRate;
            case Y3:
                return Rates.currentY3FixTermRate;
            case Y5:
                return Rates.currentY5FixTermRate;
            default:
                return Rates.currentShortTermRate;
        }
    }
}
