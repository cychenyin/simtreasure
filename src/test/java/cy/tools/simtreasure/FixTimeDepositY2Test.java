
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositY2Test extends FixTimeDepositM3Test {

    @Override
    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.Y2;
        dep.duration = Terms.Y2;
        dep.termRate = Rates.currentY2FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Override
    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2018, 1, 1));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12 * 2)" | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10450.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12  * 2 ) * (1 + 0.0030 * 30 / 365 ) " | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10452.5767123287665650").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2020, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10920.2500000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2020, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0225 * 12 / 12 * 2 ) ^ 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6153099315062962
        // echo "scale=16; 10000 * ((1 + 0.0225 * 12 / 12 * 2 ) ^ 1 ) " | bc
        // echo "scale=16; 10175.0000000000000000 * ((1 + 0.0225 * 12 / 12 * 2 ) ^ 1 ) " | bc
        // echo "scale=16; 10353.06 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6128093150679400
        Assert.assertEquals(withdrawl, new BigDecimal("10922.9426643835610604").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2022, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 3" | bc
        // 10234.3059000000000000
        // echo "scale=16; 10234.31 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 1" | bc
        // 10313.6259025000000000
        Assert.assertEquals(withdrawl, new BigDecimal("11411.6612500000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2022, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0225 * 12 / 12 * 2 ) ^ 3) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 1" | bc
        // echo "scale=16; 10450.00 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 1" | bc
        // echo "scale=16; 10920.25 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 1" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 1" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("11414.4738339726021300").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2024, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0225 * 12 / 12 * 2 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("11925.1847000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2024, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0225 * 12 / 12 * 2 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        // echo "scale=16; 11925.18 * (1 + 0.0030 * 30 / 365 )" | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("11928.11").setScale(2, RoundingMode.HALF_UP));
    }

}
