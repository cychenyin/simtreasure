
package cy.tools.simtreasure;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositM3Test {

    Deposit dep;

    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.M3;
        dep.duration = Terms.M3;
        dep.termRate = Rates.currentM3FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2016, 4, 1));
    }

    @Test
    public void testWithdrawlOnErrorDate() {
        BigDecimal withdrawl = dep.withdrawl(dep.durationStart.minusDays(1));
        Assert.assertEquals(withdrawl, dep.getPrincipalAmount());
    }

    @Test
    public void testWithdrawlOnSameDay() {
        BigDecimal withdrawl = dep.withdrawl(dep.durationStart);

        Assert.assertEquals(withdrawl, dep.getPrincipalAmount());
    }

    public void testGetFixedTermRate() {
        Assert.assertEquals(dep.getTermRate(), Rates.currentM3FixTermRate);
    }

    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 4, 1));
        Double expect = 10000D * (1d + 0.0135D * 3d / 12d * 1d);
        // 10033.75
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 * 1 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10033.75").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 4, 1).plusDays(30));
        // 10000 * (1 + 0.0135 * 3 / 12 * 1 ) * (1 + 0.0030 * 30 / 365 ) = 10033.75
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 * 1 + 0.0030 * 30 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10036.2157534246570000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 7, 1));
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10067.6139062500000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 7, 1).plusDays(30));
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 * 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16;10000 * ((1 + 0.0135 * 3 / 12 ) ^ 2 + 0.0030 * 30 / 365 )" | bc
        // 10033.7500000000000000
        // echo "scale=16;10033.75 * ((1 + 0.0135 * 3 / 12 ) + 0.0030 * 30 / 365 )" | bc
        // 10070.0879815924652173

        Assert.assertEquals(withdrawl, new BigDecimal("10070.0879815924652173").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 10, 1));
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 ) ^ 3" | bc
        // 10101.59
        Assert.assertEquals(withdrawl, new BigDecimal("10101.5921031835930000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 10, 1).plusDays(30));
        // echo "scale=16;10000 * ((1 + 0.0135 * 3 / 12) ^ 3) * (1 + 0.0030 * 30 / 366 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10104.07").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1));
        // echo "scale=16;10000 * (1 + 0.0135 * 3 / 12 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10135.6849765318380000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(30));
        // echo "scale=16;10000 * ((1 + 0.0135 * 3 / 12 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        // 10138.1892112328761708

        // echo "scale=16;10000 * ((1 + 0.0135 * 3 / 12 ) ^ 1 )" | bc
        // 10033.7500000000000000
        // echo "scale=16; 10033.75 * ((1 + 0.0135 * 3 / 12 ) ^ 1 )" | bc
        // 10067.6139062500000000
        // echo "scale=16; 10067.61 * ((1 + 0.0135 * 3 / 12 ) ^ 1 )" | bc
        // 10101.5881837500000000
        // echo "scale=16; 10101.59 * ((1 + 0.0135 * 3 / 12 ) ^ 1 )" | bc
        // 10135.6828662500000000
        // echo "scale=16; 10135.68 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10138.1792087671227461

        Assert.assertEquals(withdrawl, new BigDecimal("10138.1792087671227461").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlShortTime1Day() {
        BigDecimal withdrawl = dep.withdrawl(dep.getDurationStart().plusDays(1));
        // echo "scale=16;10000 * (1 + 0.0030 * 1 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.0821917808210000").setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    public void testWithdrawlShortTime10Day() {
        BigDecimal withdrawl = dep.withdrawl(dep.getDurationStart().plusDays(10));
        // echo "scale=16;10000 * (1 + 0.0030 * 10 / 366 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10000.8219178082190000").setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    public void testWithdrawlShortTime30Day() {
        BigDecimal withdrawl = dep.withdrawl(dep.getDurationStart().plusDays(30));
        // echo "scale=16; 10000 * (1 + 0.0030 * 30 / 366 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10002.4590163934420000").setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    public void testWithdrawlShortTime31Day() {
        BigDecimal withdrawl = dep.withdrawl(dep.getDurationStart().plusDays(31));
        // echo "scale=16;10000 * (1 + 0.0030 * 31 / 366 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10002.5409836065570000").setScale(2, RoundingMode.HALF_UP));

    }
}
