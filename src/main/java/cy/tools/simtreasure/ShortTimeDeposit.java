
package cy.tools.simtreasure;

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
public final class ShortTimeDeposit extends Deposit {

    // constructor
    public ShortTimeDeposit() {
        this.autoResave = false;
        this.termRate = Rates.currentShortTermRate;
        this.autoResaveDuration = this.duration = Terms.Short;
    }

    /**
     *
     */
    private static final long serialVersionUID = 4238451849493449310L;

    /**
     * withdrawal
     */
    @Override
    public BigDecimal withdrawl(LocalDate date) {
        if (date.toInterval().getEndMillis() < this.durationStart.toInterval().getEndMillis()) {
            return this.principalAmount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal ret = BigDecimal.ZERO;
        LocalDate start = this.getDurationStart();
        int year = start.getYear();
        LocalDate pieceEnd = null;
        while (year <= date.getYear()) {
            pieceEnd = year < date.getYear() ? Deposit.getFirstDayOfNextYear(year) : date;

            ret = ret.add(this.principalAmount.multiply(this.termRate)
                            .multiply(BigDecimal.valueOf(new Period(start, pieceEnd, PeriodType.days()).getDays()))
                            .divide(BigDecimal.valueOf(Deposit.getDaysOfYear(year)), MathContext.DECIMAL64));
            start = Deposit.getFirstDayOfNextYear(year);
            year++;
        }
        ret = this.principalAmount.add(ret).setScale(2, RoundingMode.HALF_UP);
        return ret;
    }

    /**
     * get end day of duration; but you know, NO end of short term but now.
     */
    @Override
    public LocalDate getDurationEnd() {
        return getDurationEnd(LocalDate.now());
    }

    /**
     * get end day of duration; but you know, NO end of short term but now.
     */
    public LocalDate getDurationEnd(LocalDate date) {
        return date;
    }

    /**
     * get lost by specfied date
     */
    @Override
    public BigDecimal lost(LocalDate date) {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * get days between period.
     */
    public int periodDays(LocalDate date) {
        return (new Period(this.durationStart, this.getDurationEnd(date), PeriodType.days())).getDays();
    }
}
