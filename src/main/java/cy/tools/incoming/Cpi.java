
package cy.tools.incoming;

import java.util.List;

import com.google.common.collect.Lists;

public class Cpi {
	
	public int year;
	double index;
	double indexAccumulative;
	double inflationRate;
	
	public Cpi() {}
	public Cpi(int year, double index) {
		this.year = year;
		this.index = index;
		this.indexAccumulative = 0;
		this.inflationRate = 0;
	}
	public static Cpi valueof(int year, double index) {
		return new Cpi(year, index);
	}
	// http://it.zhaozhao.info/archives/41667
	// http://www.developer.com/java/other/article.php/762441
	// http://math.nist.gov/javanumerics/jama/
	public static class CpiHistory {
		List<Cpi> history = Lists.<Cpi>newArrayList();
		
		public void initHistory() {
			this.history.add(Cpi.valueof(1980, 0.06d));
			this.history.add(Cpi.valueof(1981, 0.024d));
			this.history.add(Cpi.valueof(1982, 0.019d));
			this.history.add(Cpi.valueof(1983, 0.015d));
			this.history.add(Cpi.valueof(1984, 0.028d));
			this.history.add(Cpi.valueof(1985, 0.093d));
			this.history.add(Cpi.valueof(1986, 0.065d));
			this.history.add(Cpi.valueof(1987, 0.073d));
			this.history.add(Cpi.valueof(1988, 0.188d));
			this.history.add(Cpi.valueof(1989, 0.180d));
			this.history.add(Cpi.valueof(1990, 0.0310));
			this.history.add(Cpi.valueof(1991, 0.034));
			this.history.add(Cpi.valueof(1992, 0.064d));
			this.history.add(Cpi.valueof(1993, 0.147d));
			this.history.add(Cpi.valueof(1994, 0.241d));
			this.history.add(Cpi.valueof(1995, 0.171d));
			this.history.add(Cpi.valueof(1996, 0.083d));
			this.history.add(Cpi.valueof(1997, 0.028d));
			this.history.add(Cpi.valueof(1998, -0.008d));
			this.history.add(Cpi.valueof(1999, -0.014d));
			this.history.add(Cpi.valueof(2000, 0.004d));
			this.history.add(Cpi.valueof(2001, 0.007d));
			this.history.add(Cpi.valueof(2002, -0.008d));
			this.history.add(Cpi.valueof(2003, 0.012d));
			this.history.add(Cpi.valueof(2004, 0.039d));
			this.history.add(Cpi.valueof(2005, 0.018d));
			this.history.add(Cpi.valueof(2006, 0.015d));
			this.history.add(Cpi.valueof(2007, 0.048d));
			this.history.add(Cpi.valueof(2008, 0.059d));
			this.history.add(Cpi.valueof(2009, -0.007d));
			this.history.add(Cpi.valueof(2010, 0.0333d));
			this.history.add(Cpi.valueof(2011, 0.054d));
			this.history.add(Cpi.valueof(2012, 0.0263d));
			this.history.add(Cpi.valueof(2013, 0.0265d));
			this.history.add(Cpi.valueof(2014, 0.020d));
				
		}
	}
}
