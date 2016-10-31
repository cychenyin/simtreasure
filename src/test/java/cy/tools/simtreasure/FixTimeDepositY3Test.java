
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositY3Test extends FixTimeDepositM3Test {

    @Override
    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.Y3;
        dep.duration = Terms.Y3;
        dep.termRate = Rates.currentY3FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Override
    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2019, 1, 1));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2019, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10825.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2019, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3 ) * (1 + 0.0030 * 30 / 365 ) " | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10827.6691780821912025").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2022, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("11718.0625000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2022, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 3 ) ^ 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6153099315062962
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 3 ) ^ 1 ) " | bc
        // echo "scale=16; 10175.0000000000000000 * ((1 + 0.0275 * 12 / 12 * 3 ) ^ 1 ) " | bc
        // echo "scale=16; 10353.06 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6128093150679400
        Assert.assertEquals(withdrawl, new BigDecimal("11720.9518852739719767").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2025, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 3" | bc
        // 10234.3059000000000000
        // echo "scale=16; 10234.31 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 1" | bc
        // 10313.6259025000000000
        Assert.assertEquals(withdrawl, new BigDecimal("12684.8026562500000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2025, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 3 ) ^ 3) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 1" | bc
        // echo "scale=16; 10450.00 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 1" | bc
        // echo "scale=16; 10920.25 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 1" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 11411.66 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 1" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("12687.930415809074664").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2028, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0275 * 12 / 12 * 3 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("13731.2988753906250000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2028, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0275 * 12 / 12 * 3 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        // echo "scale=16; 13731.30 * (1 + 0.0030 * 30 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("13734.68").setScale(2, RoundingMode.HALF_UP));
    }

}
