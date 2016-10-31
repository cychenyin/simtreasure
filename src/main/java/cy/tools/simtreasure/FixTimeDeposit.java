
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
import org.junit.Assert;

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

public final class FixTimeDeposit extends Deposit {

    /**
     *
     */
    private static final long serialVersionUID = 4238451849493449310L;

    @Override
    public BigDecimal withdrawl(LocalDate date) {
        if (date.toInterval().getEndMillis() < this.durationStart.toInterval().getEndMillis()) {
            return this.principalAmount.setScale(2, RoundingMode.HALF_UP);
        }

        FixTimeDeposit fix = this;
        while (!fix.getDurationEnd().isAfter(date)) {
            fix = fix.nextResavedDeposit();
            if (!this.autoResave) {
                break;
            }
        }
        ShortTimeDeposit rest = this.restShortTimeDeposit(fix, date);
        return rest.withdrawl(date);
    }

    private ShortTimeDeposit restShortTimeDeposit(FixTimeDeposit unclosedDeposit, LocalDate date) {
        ShortTimeDeposit ret = new ShortTimeDeposit();
        ret.setDurationStart(unclosedDeposit.getDurationStart());
        ret.setPrincipalAmount(unclosedDeposit.principalAmount);

        return ret;
    }

    protected FixTimeDeposit nextResavedDeposit() {
        FixTimeDeposit ret = null;
        try {
            ret = (FixTimeDeposit) this.clone();
        } catch (CloneNotSupportedException e) {
            // eat it
        }

        ret.setId(Deposit.nextId());
        ret.setPrincipalAmount(this.principalAmount.add(this.interestOnSingleFixTime()));
        ret.setTermRate(Rates.getRate(this.autoResaveDuration));
        ret.setDurationStart(this.getDurationEnd());
        return ret;
    }

    @Override
    public LocalDate getDurationEnd() {
        {
            switch (this.duration) {
                case M3:
                    return this.durationStart.withFieldAdded(DurationFieldType.months(), 3);
                case M6:
                    return this.durationStart.withFieldAdded(DurationFieldType.months(), 6);
                case Y1:
                    return this.durationStart.withFieldAdded(DurationFieldType.years(), 1);
                case Y2:
                    return this.durationStart.withFieldAdded(DurationFieldType.years(), 2);
                case Y3:
                    return this.durationStart.withFieldAdded(DurationFieldType.years(), 3);
                case Y5:
                    return this.durationStart.withFieldAdded(DurationFieldType.years(), 5);
                default:
                    return LocalDate.now();
            }
        }
    }

    /**
     * do not consider about that days of year do NOT equals 365 all the time.
     */
    @Override
    public BigDecimal lost(LocalDate date) {
        if (date.toInterval().getEndMillis() <= this.durationStart.toInterval().getEndMillis()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        FixTimeDeposit fix = this;
        while (!fix.getDurationEnd().isAfter(date)) {
            fix = fix.nextResavedDeposit();
            if (!this.autoResave) {
                break;
            }
        }
        ShortTimeDeposit rest = this.restShortTimeDeposit(fix, date);
        return this.principalAmount
                        .multiply(this.termRate.subtract(rest.termRate))
                        .multiply(BigDecimal.valueOf(rest.periodDays(date)))
                        .divide(BigDecimal.valueOf(365L), MathContext.DECIMAL64)
                        .setScale(2, RoundingMode.HALF_UP);
    }
}
