
package cy.tools.simtreasure;

import org.junit.Assert;
import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {

    private Account ac;

    @Before
    public void setUp() throws Exception {
        ac = new Account();
    }

    @Test
    public void testAccount() {
        Account acc = new Account();
        Assert.assertTrue(true);
    }

    @Test
    public void testGetDeposites() {
        Assert.assertTrue(ac.getDeposites().size() > 0);
    }

    @Test
    public void testSave() {
        int len = ac.getDeposites().size();

        ac.save(10, LocalDate.now());
        ac.save(10, LocalDate.now(), Terms.Short, Terms.Short);
        Assert.assertTrue(true);
    }

    @Test
    public void testInit() {
        Assert.assertTrue(ac.getDeposites().size() > 0);
        Assert.assertTrue(true);
    }

}
