
package cy.tools.simtreasure;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Deposit with auto resaving imple
 * duration = [start, end)
 *
 * @author cheny-ab
 *
 */

@SuppressWarnings("serial")
@Data
public abstract class Deposit implements Cloneable, Serializable {

    /**
     * deposit
     */
    public Deposit() {
        this.id = Deposit.nextId();
    }

    private static int seed = 0;
    private long id = 1;

    /**
     * auto id generator, for debug
     *
     * @return
     */
    protected static long nextId() {
        synchronized (Deposit.class) {
            seed++;
        }
        return seed;
    }

    // amount
    @NonNull
    protected BigDecimal principalAmount;
    // term
    @NonNull
    protected Terms duration;
    // from date
    @NonNull
    protected LocalDate durationStart;
    // rate
    @NonNull
    protected BigDecimal termRate;
    // auto save status
    protected Boolean autoResave = false;
    // auto save term
    protected Terms autoResaveDuration = Terms.Short;

    // final protected BigDecimal shortTermRate = new BigDecimal("0.0030").setScale(4, RoundingMode.DOWN);

    /**
     * get amount
     *
     * @return
     */
    public BigDecimal getPrincipalAmount() {
        return this.principalAmount == null ? BigDecimal.ZERO : this.principalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * get rate
     *
     * @param rate double value, scale 4, round down directly
     */
    public void setTermRate(BigDecimal rate) {
        this.termRate = rate.setScale(4, RoundingMode.DOWN);
    }

    /**
     * get days of durations
     */
    public int periodDays() {
        return (new Period(this.durationStart, this.getDurationEnd(), PeriodType.days())).getDays();
    }

    /**
     * get end date of duration
     */
    public abstract LocalDate getDurationEnd();

    /**
     *
     * @param date
     * @return
     */
    public abstract BigDecimal withdrawl(LocalDate date);

    /**
     * get value / lost
     */
    public abstract BigDecimal lost(LocalDate date);

    /**
     * get interest of one day
     * @return
     */
    protected BigDecimal interestOneDay() {
        return this.getPrincipalAmount()
                        .multiply(this.getTermRate())
                        .divide(BigDecimal.valueOf(Deposit.getDaysOfYear(LocalDate.now().getYear())), MathContext.DECIMAL64)
                        .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * get interest when single period end of ONE duration.
     *
     * @return
     */
    protected BigDecimal interestOnSingleFixTime() {
        BigDecimal ret = this.principalAmount
                        .multiply(this.termRate);
        switch (this.duration) {
            case M3:
                ret = ret.divide(BigDecimal.valueOf(4D), MathContext.DECIMAL64);
                break;
            case M6:
                ret = ret.divide(BigDecimal.valueOf(2D), MathContext.DECIMAL64);
                break;
            case Y2:
                ret = ret.multiply(BigDecimal.valueOf(2D), MathContext.DECIMAL64);
                break;
            case Y3:
                ret = ret.multiply(BigDecimal.valueOf(3D), MathContext.DECIMAL64);
                break;
            case Y5:
                ret = ret.multiply(BigDecimal.valueOf(5D), MathContext.DECIMAL64);
                break;
            default:
                break;
        }
        ret = ret.setScale(2, RoundingMode.HALF_UP);
        return ret;
    }

    /*
     * implement with, cause of not every year have 365 days.
     * private static BigDecimal interestOnShortTime(BigDecimal principal, BigDecimal rate, int days) {
     * return principal.multiply(BigDecimal.valueOf(days))
     * .divide(DAYS_WHOLE_YEAR, MathContext.DECIMAL64)
     * .multiply(rate)
     * .setScale(2, RoundingMode.HALF_UP);
     * }
     */

    /**
     * get first day of next year
     *
     * @param year
     * @return
     */
    public static LocalDate getFirstDayOfNextYear(int year) {
        return new LocalDate(year + 1, 1, 1);
    }

    /**
     * get first day of specified year
     *
     * @param year
     * @return
     */
    public static LocalDate getFirstDayOfYear(int year) {
        return new LocalDate(year, 1, 1);
    }

    /**
     * get end day of year
     */
    public static LocalDate getEndDayOfYear(int year) {
        return new LocalDate(year + 1, 1, 1).minusDays(1);
    }

    /**
     * get days of the whole year.
     */
    public static int getDaysOfYear(int year) {

        LocalDate date = new LocalDate(year, 1, 1);
        LocalDate end = new LocalDate(year + 1, 1, 1);
        return new Period(date, end, PeriodType.days()).getDays();
    }
}
