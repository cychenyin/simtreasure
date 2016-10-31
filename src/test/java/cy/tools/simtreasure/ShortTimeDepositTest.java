
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShortTimeDepositTest {

    private ShortTimeDeposit dep;

    @Before
    public void setUp() throws Exception {
        dep = new ShortTimeDeposit();
        dep.durationStart = new LocalDate(2016, 1, 1);
        dep.principalAmount = BigDecimal.valueOf(10000D);
        Assert.assertTrue(true);
    }
    @After
    public void teardown() {
    }
    @Test
    public void testGetSet() {
        dep.setAutoResave(false);
        Assert.assertEquals(dep.getAutoResave(), false);
        dep.setAutoResaveDuration(Terms.Short);
        Assert.assertEquals(dep.getAutoResaveDuration(), Terms.Short);
        dep.setDuration(Terms.M6);
        Assert.assertEquals(dep.getDuration(), Terms.M6);

        dep.setTermRate(Rates.currentM6FixTermRate);
        Assert.assertEquals(dep.getTermRate(), Rates.getRate(Terms.M6));
        dep.setDurationStart(new LocalDate(2016, 1, 1));
        Assert.assertEquals(dep.getDurationStart(), new LocalDate(2016, 1, 1));

        dep.setPrincipalAmount(new BigDecimal("10000") );
        Assert.assertEquals(dep.getPrincipalAmount(), new BigDecimal("10000").setScale(2, RoundingMode.HALF_UP));

        Assert.assertEquals(LocalDate.now(), dep.getDurationEnd());
        Assert.assertEquals(LocalDate.now(), dep.getDurationEnd(LocalDate.now()));

    }

    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(new LocalDate(2016, 1, 1)), new LocalDate(2016, 1, 1));
        Assert.assertEquals(dep.getDurationEnd(LocalDate.now()), LocalDate.now());
        Assert.assertEquals(dep.getDurationEnd(), LocalDate.now());
    }

    @Test
    public void testYesteryDay() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).minusDays(1));
        Assert.assertEquals(withdrawl, new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testSameDay() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1));
        Assert.assertEquals(withdrawl, new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test1Day() {
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 2));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 1 / 366 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.0819672131140000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test10Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(10));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 10 / 366 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.8196721311470000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test100Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(100));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 100 / 366 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10008.1967213114750000").setScale(2, RoundingMode.HALF_UP));
    }
    @Test
    public void test365Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(365));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 365 / 366 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10029.9180327868850000").setScale(2, RoundingMode.HALF_UP));
    }
    @Test
    public void test366Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(366));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 366 / 366 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10030.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test367Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(367));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 366 / 366 + 0.0030 * 1 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10030.0821917808210000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test1000Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(1000));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 366 / 366 + 0.0030 * 365 / 365 + 0.0030 * (1000 - 366 - 365) / 365)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10082.1095890410950000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test3000Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(3000));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 8 + 0.0030 * 78 / 366 )" | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10246.3934426229500000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test10Years() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusYears(10));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 10)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10300.0000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test20Years() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusYears(20));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 20)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10600.0000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void test50Years() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusYears(50));
        // echo "scale=16; 10000 * ( 1 + 0.0030 * 50)" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("11500.0000").setScale(2, RoundingMode.HALF_UP));
    }

}
