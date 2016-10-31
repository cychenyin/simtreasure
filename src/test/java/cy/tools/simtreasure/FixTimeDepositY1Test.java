
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositY1Test extends FixTimeDepositM3Test {

    @Override
    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.Y1;
        dep.duration = Terms.Y1;
        dep.termRate = Rates.currentY1FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Override
    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2017, 1, 1));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10175.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 * 1 ) * (1 + 0.0030 * 30 / 365 ) " | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10177.5089041095884975").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10353.0625000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0175 * 12 / 12) ^ 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6153099315062962
        // echo "scale=16; 10000 * ((1 + 0.0175 * 12 / 12) ^ 1 ) " | bc
        // echo "scale=16; 10175.0000000000000000 * ((1 + 0.0175 * 12 / 12) ^ 1 ) " | bc
        // echo "scale=16; 10353.06 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10355.6128093150679400
        Assert.assertEquals(withdrawl, new BigDecimal("10355.6128093150679400").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2019, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) ^ 3" | bc
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) ^ 1" | bc
        // echo "scale=16; 10077.50 * (1 + 0.0175 * 12 / 12 ) ^ 1" | bc
        // echo "scale=16; 10155.60 * (1 + 0.0175 * 12 / 12 ) ^ 1" | bc
        // 10234.3059000000000000
        // echo "scale=16; 10234.31 * (1 + 0.0175 * 12 / 12 ) ^ 1" | bc
        // 10313.6259025000000000
        Assert.assertEquals(withdrawl, new BigDecimal("10534.2410937500000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2019, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0175 * 12 / 12) ^ 3) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10234.31 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10234.31
        Assert.assertEquals(withdrawl, new BigDecimal("10536.8385778553076563").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2020, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10718.5903128906250000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2020, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0175 * 12 / 12 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        // echo "scale=16; 10313.63 * ( 1 + 0.0030 * 30 / 365) " | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10721.2332529677755403").setScale(2, RoundingMode.HALF_UP));
    }

}
