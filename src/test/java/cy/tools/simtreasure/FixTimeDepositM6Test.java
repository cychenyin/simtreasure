
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixTimeDepositM6Test extends FixTimeDepositM3Test {

    @Override
    @Before
    public void setUp() throws Exception {
        dep = new FixTimeDeposit();
        dep.autoResave = true;
        dep.autoResaveDuration = Terms.M6;
        dep.duration = Terms.M6;
        dep.termRate = Rates.currentM6FixTermRate;
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
    }

    @Override
    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2016, 7, 1));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 7, 1));
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10077.5000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn1Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 7, 1).plusDays(30));
        // 10000 * (1 + 0.0155 * 6 /12 * 1 ) * (1 + 0.0030 * 30 / 365 )
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 * 1 ) * (1 + 0.0030 * 30 / 365 ) " | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10079.9848630136980917").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 ) ^ 2" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10155.6006250000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn2Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0155 * 6 /12) ^ 2 ) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10155.60 * ( 1 + 0.0030 * 30 / 365 )" | bc
        // 10158.1041205479446629

        Assert.assertEquals(withdrawl, new BigDecimal("10158.1041205479446629").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 7, 1));
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 ) ^ 3" | bc
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 ) ^ 1" | bc
        // echo "scale=16; 10077.50 * (1 + 0.0155 * 6 /12 ) ^ 1" | bc
        // echo "scale=16; 10155.60 * (1 + 0.0155 * 6 /12 ) ^ 1" | bc
        // 10234.3059000000000000
        // echo "scale=16; 10234.31 * (1 + 0.0155 * 6 /12 ) ^ 1" | bc
        // 10313.6259025000000000
        Assert.assertEquals(withdrawl, new BigDecimal("10234.3059000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn3Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 7, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0155 * 6 /12) ^ 3) * (1 + 0.0030 * 30 / 365 )" | bc
        // echo "scale=16; 10234.31 * (1 + 0.0030 * 30 / 365 )" | bc
        // 10234.31
        Assert.assertEquals(withdrawl, new BigDecimal("10236.8335284931501381").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testWithdrawlOn4Resave() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0155 * 6 /12 ) ^ 4" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10313.6259025000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Test
    public void testWithdrawlOn4Resave30Days() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2018, 1, 1).plusDays(30));
        // echo "scale=16; 10000 * ((1 + 0.0155 * 6 /12 ) ^ 4 ) * ( 1 + 0.0030 * 30 / 365) " | bc
        // echo "scale=16; 10313.63 * ( 1 + 0.0030 * 30 / 365) " | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10316.1730868493145174").setScale(2, RoundingMode.HALF_UP));
    }

}
