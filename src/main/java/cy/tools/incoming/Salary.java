package cy.tools.incoming;

import java.util.List;

import com.google.common.collect.Lists;

public class Salary {

	List<Increase> increases = Lists.<Increase> newArrayList();

	private void initIncreases() {
		this.increases.add(Increase.valueof(1980, 70.67));
		this.increases.add(Increase.valueof(1981, 69.75));
		this.increases.add(Increase.valueof(1982, 71.91));
		this.increases.add(Increase.valueof(1983, 77.58));
		this.increases.add(Increase.valueof(1984, 90.5));
		this.increases.add(Increase.valueof(1985, 118.33));
		this.increases.add(Increase.valueof(1986, 113.42));
		this.increases.add(Increase.valueof(1987, 148.92));
		this.increases.add(Increase.valueof(1988, 175.58));
		this.increases.add(Increase.valueof(1989, 192.67));
		
		this.increases.add(Increase.valueof(1990, 221.08));
		this.increases.add(Increase.valueof(1991, 240));
		this.increases.add(Increase.valueof(1992, 284));
		this.increases.add(Increase.valueof(1993, 377));
		this.increases.add(Increase.valueof(1994, 545));
		this.increases.add(Increase.valueof(1995, 679));
		this.increases.add(Increase.valueof(1996, 789));
		this.increases.add(Increase.valueof(1997, 918));
		this.increases.add(Increase.valueof(1998, 1024));
		this.increases.add(Increase.valueof(1999, 1148));
		this.increases.add(Increase.valueof(2000, 1311));
		this.increases.add(Increase.valueof(2001, 1508));
		this.increases.add(Increase.valueof(2002, 1727));
		this.increases.add(Increase.valueof(2003, 2004));
		this.increases.add(Increase.valueof(2004, 2362));
		this.increases.add(Increase.valueof(2005, 2734));
		this.increases.add(Increase.valueof(2006, 3008));
		this.increases.add(Increase.valueof(2007, 3322));
		this.increases.add(Increase.valueof(2008, 3726));
		this.increases.add(Increase.valueof(2009, 4037));
		this.increases.add(Increase.valueof(2010, 4201));
		this.increases.add(Increase.valueof(2011, 4672));
		this.increases.add(Increase.valueof(2012, 5223));
		this.increases.add(Increase.valueof(2013, 5793));
		this.increases.add(Increase.valueof(2014, 6463));
		this.increases.add(Increase.valueof(2015, 7086));

		for (int i = 1; i < this.increases.size(); i++) {
			if (this.increases.get(i).increaseRatio == 0) {
				this.increases.get(i).increaseRatio = (this.increases.get(i).averageSalaryMonthly
						- this.increases.get(i - 1).averageSalaryMonthly)
						/ this.increases.get(i - 1).averageSalaryMonthly;
				this.increases.get(i).increaseRatioCommendeed = this.increases.get(i).increaseRatio;
				this.increases.get(i).increaseRatioAccumulative = this.increases.get(i).increaseRatio 
						+ this.increases.get(i - 1).increaseRatioAccumulative;
			}
		}
	}

	public void printIncreases() {
		for(Increase in : this.increases) {
			System.out.println(in.toString());
		}
	}
	public void printIncreasesPlaning() {
		System.out.println("year\tsalary\tratio\n");
		for(Increase in : this.increases) {
			System.out.println(String.format("%d %4.2f %4.2f", in.year, in.averageSalaryMonthly, in.increaseRatio));
		}
	}
	public static class Increases{
		
	}
 	public static class Increase {
 		int year;
		double averageSalaryMonthly;
		double increaseRatio;
		double increaseRatioCommendeed;
		double increaseRatioAccumulative;
		
		public Increase() {
		}

		public String toString() {
			return String.format("year: %d salary: %.0f ratio: %4.2f", this.year, this.averageSalaryMonthly,
					this.increaseRatio * 100d);
		}

		public static Increase valueof(int year, double averageSalary) {
			return new Increase(year, averageSalary);
		}

		public Increase(int year, double averageSalary) {
			super();
			this.init(year, averageSalary);
		}

		private void init(int year, double averageSalary) {
			this.year = year;
			this.averageSalaryMonthly = averageSalary;
			this.increaseRatio = 0d;
			this.increaseRatioCommendeed = 0d;
			this.increaseRatioAccumulative = 0d;
		}

		
	}

	public static void main(String[] args) {
		Salary sa = new Salary();
		sa.initIncreases();
		sa.printIncreasesPlaning();
	}
}
