
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositY5Test extends FixTimeDepositM3Test {

    @Override
    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.Y5;
        dep.duration = Terms.Y5;
        dep.termRate = Rates.currentY5FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Override
    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2021, 1, 1));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2021, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("11375.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2021, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5 ) * (1 + 0.0030 * 30 / 365 ) " | bc
        Assert.assertEquals(withdrawl, new BigDecimal("11377.8047945205473375").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2026, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("12939.0625000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2026, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 5 ) ^ 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("12942.2529537671225964").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2031, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 3" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("14718.1835937500000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2031, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 5 ) ^ 3) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 1" | bc
        // echo "scale=16; 10450.00 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 1" | bc
        // echo "scale=16; 10920.25 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 1" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 1" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("14721.8127349101019534").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2036, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 5 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("16741.9338378906250000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2036, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 5 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        Assert.assertEquals(withdrawl, new BigDecimal("16746.05").setScale(2, RoundingMode.HALF_UP));
    }

}
