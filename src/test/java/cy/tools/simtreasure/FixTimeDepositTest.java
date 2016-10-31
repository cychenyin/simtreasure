
package cy.tools.simtreasure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FixTimeDepositTest {

    private Deposit dep;

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

    @After
    public void teardown() {
        long nextId = Deposit.nextId();
        Assert.assertTrue(nextId < Deposit.nextId());
    }
    @Test
    public void testGetSet2() {
        dep.setAutoResaveDuration(null);
        Assert.assertTrue(true);
    }

    // @Test(expected=NullPointerException.class)
    @Test
    public void testGetSet3() {
        thrown.expect( NullPointerException.class );
        dep.setDuration(null);
        Assert.assertTrue(true);
    }

    @Test()
    public void testGetSet4() {
        thrown.expect( NullPointerException.class );
        dep.setDurationStart(null);
        Assert.assertTrue(true);
    }
    //@Test(expected=NullPointerException.class)
    @Test
    public void testGetSet5() {
        thrown.expect( NullPointerException.class );
        dep.setTermRate(null);
        Assert.assertTrue(true);
    }
    @Test()
    public void testGetSet6() {
        thrown.expect( NullPointerException.class );
        dep.setPrincipalAmount(null);
        Assert.assertTrue(true);
    }


    @Rule public ExpectedException thrown= ExpectedException.none();
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
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2016, 7, 1));
        dep.setPrincipalAmount(new BigDecimal("10000"));
        Assert.assertEquals(dep.getPrincipalAmount(), new BigDecimal("10000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testGetPeriodEnd() {
        Assert.assertEquals(dep.getDurationEnd(), new LocalDate(2017, 1, 1));
    }

    @Test
    public void testNotResaveSameDay() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0030 * 10 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.00").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResave1Day() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 2));
        // echo "scale=16; 10000 * (1 + 0.0030 * 1 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.0821917808210000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveInsideDuration() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 1, 1).plusDays(10));
        // echo "scale=16; 10000 * (1 + 0.0030 * 10 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10000.8219178082190000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveTheDayBeforeSaveDurationEnd() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2016, 12, 31));
        int days = (new Period(dep.getDurationStart(), new LocalDate(2016, 12, 31), PeriodType.days())).getDays();
        // echo "scale=16; 10000 * (1 + 0.0030 * 365 / 366 )" | bc
        // System.out.println(withdrawl.toPlainString());
        Assert.assertEquals(withdrawl, new BigDecimal("10029.9180327868850000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveOnDurationEnd() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10175.0000000000000000").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveTheDayAfterDurationEnd() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 2));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) * ( 1 + 0.0030 * 1 / 365 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10175.0836301369853675").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveAfterDurationEnd10Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(10));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) * ( 1 + 0.0030 * 10 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10175.8363013698628325").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveAfterDurationEnd100Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(100));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) * ( 1 + 0.0030 * 100 / 365 )" | bc
        Assert.assertEquals(withdrawl, new BigDecimal("10183.3630136986293425").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testNotResaveAfterDurationEnd1000Days() {
        dep.setAutoResave(false);
        BigDecimal withdrawl = dep.withdrawl(new LocalDate(2017, 1, 1).plusDays(1000));
        // echo "scale=16; 10000 * (1 + 0.0175 * 12 / 12 ) * ( 1 + 0.0030 * 1000 / 365 )" | bc

        Assert.assertEquals(withdrawl, new BigDecimal("10258.6301369863005475").setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testGetDaysOfYear() {
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2016), 366);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2017), 365);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2018), 365);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2019), 365);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2020), 366);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2021), 365);
        Assert.assertEquals(FixTimeDeposit.getDaysOfYear(2022), 365);
    }

    @Test
    public void testGetEndDayOfYear() {
        int year = 2016;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
        Assert.assertEquals(FixTimeDeposit.getEndDayOfYear(year), new LocalDate(year, 12, 31));
        year++;
    }

    @Test
    public void testGetFirstDayOfYear() {
        int year = 2016;
        // System.out.println(FixTimeDeposit.getFirstDayOfYear(year));
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(FixTimeDeposit.getFirstDayOfYear(year), new LocalDate(year, 1, 1));
            year++;
        }
    }

    @Test
    public void testGetFirstDayOfNextYear() {
        int year = 2016;
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(FixTimeDeposit.getFirstDayOfNextYear(year), new LocalDate(year + 1, 1, 1));
            year++;
        }
    }

    @Test
    public void testPeriodDays() {
        Assert.assertEquals(dep.periodDays(), 366);
        dep.setDurationStart(new LocalDate(2017, 1, 1));
        Assert.assertEquals(dep.periodDays(), 365);


    }

    @Test
    public void testEtc() {
        FixTimeDeposit dep = new FixTimeDeposit();
        String string = dep.toString();
        Assert.assertTrue(string.length() > 40);
        FixTimeDeposit right = new FixTimeDeposit();
        Assert.assertEquals(dep, right);
        Assert.assertTrue(dep.equals(right));
        right.setId(FixTimeDeposit.nextId());
        Assert.assertNotEquals(dep, right);
        Assert.assertFalse(dep.equals(right));
        right.setId(dep.getId());
        right.setPrincipalAmount(BigDecimal.ONE);
        Assert.assertFalse(dep.equals(right));

        Assert.assertTrue(dep.equals(dep));
        Assert.assertFalse(dep.equals(right));
        Assert.assertFalse(dep.equals(null));
        Assert.assertFalse(dep.equals(new Object()));
        int hashCode = dep.hashCode();
        Assert.assertEquals(hashCode, dep.hashCode());
    }
}
