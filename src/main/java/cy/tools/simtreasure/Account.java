
package cy.tools.simtreasure;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Account {

    /**
     * account
     */
    public Account() {
        this.deposits = Lists.<Deposit>newArrayList(); // new ArrayList<Deposit>();
    }

    private List<Deposit> deposits;

    /**
     * get all deposits
     *
     * @return
     */
    public ImmutableList<Deposit> getDeposites() {
        return ImmutableList.copyOf(this.deposits);
        // return Collections.unmodifiableList(this.deposits);
    }

    /**
     * save short term deposit
     */
    public void save(int amount, LocalDate start) {
        this.save(amount, start, Terms.Short, Terms.Short);
    }

    /**
     * save deposit
     */
    public void save(int amount, LocalDate start, Terms term, Terms resaveTerm) {
        Deposit dep = new FixTimeDeposit();
        dep.setPrincipalAmount(BigDecimal.valueOf(amount));
        dep.setDuration(term);
        dep.setTermRate(Rates.getRate(term));
        dep.setDurationStart(start);
        dep.setAutoResave(resaveTerm != null);
        dep.setAutoResaveDuration(resaveTerm);
        this.deposits.add(dep);
    }

    public void load(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("d:\\b.txt"), Charsets.UTF_8);
            for (String line : lines) {
                this.deposits.add(this.parseDeposit(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to load deposits from file cause of " + e.getMessage());
        }
    }

    private Deposit parseDeposit(String line) {

        String[] ary = line.split("\\t");
        if (ary.length == 6) {
            Terms term = Terms.valueOf(ary[0]);
            Deposit dep = null;
            if(term == Terms.Short) {
                dep = new ShortTimeDeposit();
            } else {
                dep = new FixTimeDeposit();
            }

            dep.setDuration(term);
            dep.setPrincipalAmount(new BigDecimal(ary[1]));
            if(ary[2].indexOf('%') > 0) {
            	dep.setTermRate(new BigDecimal(ary[2].substring(0, ary[2].indexOf('%'))).divide(BigDecimal.valueOf(100L), MathContext.DECIMAL64));
            } else {
            	dep.setTermRate(new BigDecimal(ary[2]));
            }
            dep.setDurationStart(LocalDate.parse(ary[3]));
            dep.setDurationStart(LocalDate.parse(ary[3]));
            dep.setAutoResave(Boolean.valueOf(ary[4]));
            dep.setAutoResaveDuration(Terms.valueOf(ary[5]));
            return dep;
        }
        return null;
    }

    /**
     * create deposits for debug
     */
    public void load() {
        // ((Terms\.\w\d,\s){2})(new\sLocalDate\(\d{4},\s\d{1,2},\s\d{1,2}\)\))
        Random rand = new Random(DateTime.now().getMillis());
        this.save(10000, new LocalDate(2014, 1, rand.nextInt(27) + 1), Terms.M3, Terms.M3);
        this.save(10000, new LocalDate(2014, 2, rand.nextInt(27) + 1), Terms.M3, Terms.M3);
        this.save(10000, new LocalDate(2014, 3, rand.nextInt(27) + 1), Terms.M3, Terms.M3);
        this.save(10000, new LocalDate(2014, 4, rand.nextInt(27) + 1), Terms.M3, Terms.M3);

        this.save(11000, new LocalDate(2014, 5, rand.nextInt(27) + 1), Terms.M6, Terms.M6);
        this.save(11000, new LocalDate(2014, 6, rand.nextInt(27) + 1), Terms.M6, Terms.M6);
        this.save(11000, new LocalDate(2014, 7, rand.nextInt(27) + 1), Terms.M6, Terms.M3);
        this.save(11000, new LocalDate(2014, 8, rand.nextInt(27) + 1), Terms.M6, Terms.M3);

        this.save(11000, new LocalDate(2014, 9, rand.nextInt(27) + 1), Terms.Y1, Terms.M3);
        this.save(11000, new LocalDate(2014, 10, rand.nextInt(27) + 1), Terms.Y1, Terms.M3);
        this.save(11000, new LocalDate(2014, 11, rand.nextInt(27) + 1), Terms.Y1, Terms.M6);
        this.save(11000, new LocalDate(2014, 12, rand.nextInt(27) + 1), Terms.Y1, Terms.M6);

        this.save(11000, new LocalDate(2015, 1, rand.nextInt(27) + 1), Terms.Y1, Terms.Y1);
        this.save(11000, new LocalDate(2015, 2, rand.nextInt(27) + 1), Terms.Y1, Terms.Y1);

        this.save(11000, new LocalDate(2015, 3, rand.nextInt(27) + 1), Terms.Y2, Terms.M3);
        this.save(11000, new LocalDate(2015, 4, rand.nextInt(27) + 1), Terms.Y2, Terms.M3);
        this.save(11000, new LocalDate(2015, 5, rand.nextInt(27) + 1), Terms.Y2, Terms.M6);
        this.save(11000, new LocalDate(2015, 6, rand.nextInt(27) + 1), Terms.Y2, Terms.M6);
        this.save(11000, new LocalDate(2015, 7, rand.nextInt(27) + 1), Terms.Y2, Terms.Y1);
        this.save(11000, new LocalDate(2015, 8, rand.nextInt(27) + 1), Terms.Y2, Terms.Y1);
        this.save(11000, new LocalDate(2015, 9, rand.nextInt(27) + 1), Terms.Y2, Terms.Y2);
        this.save(11000, new LocalDate(2015, 10, rand.nextInt(27) + 1), Terms.Y2, Terms.Y2);
        this.save(11000, new LocalDate(2015, 11, rand.nextInt(27) + 1), Terms.Y3, Terms.Y1);
        this.save(11000, new LocalDate(2015, 12, rand.nextInt(27) + 1), Terms.Y3, Terms.Y1);

        this.save(150000, new LocalDate(2014, 2, rand.nextInt(27) + 1), Terms.M3, Terms.M3);
        this.save(200000, new LocalDate(2015, 5, rand.nextInt(27) + 1), Terms.M6, Terms.M6);
        this.save(150000, new LocalDate(2016, 5, rand.nextInt(27) + 1), Terms.M6, Terms.M6);
    }

    /**
     * sort deposit by value
     *
     * @return
     */
    public List<Deposit> sortedDeposits() {
        ArrayList<Deposit> list = Lists.newArrayList(this.deposits);
        Collections.sort(list, new Comparator<Deposit>() {

            public int compare(Deposit o1, Deposit o2) {
                return o1.getPrincipalAmount().compareTo(o2.getPrincipalAmount());
            }

        });

        // Ordering<Deposit> ordering = Ordering.compound(Lists.newArrayList(new Comparator<Deposit>() {
        // public int compare(Deposit o1, Deposit o2) {
        // return o1.getPrincipalAmount().compareTo(o2.getPrincipalAmount());
        // }
        //
        // }));
        // Collections.sort(list, ordering);

        return ImmutableList.copyOf(list);
    }

    /**
     * print deposits
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        for (Deposit dep : this.deposits) {
            sb.append(dep.getDuration().toString()).append("\t");
            sb.append(dep.getPrincipalAmount().toPlainString()).append("\t");
            sb.append(dep.getTermRate().toPlainString()).append("\t");
            sb.append(dep.getDurationStart().toString()).append("\t");
            sb.append(dep.getAutoResave().toString()).append("\t");
            sb.append(dep.getAutoResaveDuration().toString()).append("\t");
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    public BigDecimal amount() {
        BigDecimal ret = BigDecimal.ZERO;
        for(Deposit dep : this.deposits) {
            ret = ret.add(dep.getPrincipalAmount());
        }
        return ret;
    }

    /**
     * get interest of one day
     * @return
     */
    public BigDecimal interestOfOneDay() {
        BigDecimal ret = BigDecimal.ZERO;
        for(Deposit dep : this.deposits) {
            ret = ret.add(dep.interestOneDay());
        }
        return ret;
    }
    
    public static void main(String[] args) {
    	Account ac = new Account();
    	ac.load();
    	ac.print();
    	
    }
}
