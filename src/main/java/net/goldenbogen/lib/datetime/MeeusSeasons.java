package net.goldenbogen.lib.datetime;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class to calculate season for a given date.<br>
 * It makes heavy use of Meeus' astronmical algorithms <i>(Title of his book.)</i>.
 * 
 * @author Pierre Goldenbogen <br>
 * email  pierre [at] goldenbogen [dot] net <br>
 * (c) Copyright by Pierre Goldenbogen, 2013
 */
public class MeeusSeasons {

	/**
	 * Returns the value of the current season.
	 * 
	 * @return int = 1: Winter<br>
	 *         int = 2: Spring<br>
	 *         int = 3: Summer<br>
	 *         int = 4: Autumn
	 * @throws Exception description
	 */
	public static int getActualNorthernHemisphereSeason() throws Exception {
		for (int iSeason = 1; iSeason <= 4; iSeason++) {
			if (calcEquSol(iSeason, Calendar.getInstance().get(Calendar.YEAR)).getTime() > new Date().getTime()) {
				return iSeason;
			}
		}
		return 0;
	}

	public static int getNorthernHemisphereSeason(Date date) throws Exception {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		final int year = calendar.get(Calendar.YEAR);
		for (int iSeason = 1; iSeason <= 4; iSeason++) {
			if (calcEquSol(iSeason, year).getTime() > date.getTime()) {
				return iSeason;
			}
		}
		return 0;
	}
	/**
	 * Special implementation of the Math.cos function.
	 * 
	 * @param value a number
	 * @return double
	 */
	private static double specialCos(double value) {
		return Math.cos(value * Math.PI / 180);
	}

	/**
	 * Calculate a single event for a single year (Either a Equiniox or Solstice)<br>
	 * <i>See Meeus Astronmical Algorithms Chapter 27</i>
	 * 
	 * @param iSeason to calculate
	 * @param year for calculation
	 * @throws Exception
	 */
	private static Date calcEquSol(int iSeason, int year) throws Exception {
		int k = iSeason - 1;
		// Initial estimate of date of event
		double ETA = init(k, year);
		double T = (ETA - 2451545.0) / 36525;
		double W = 35999.373 * T - 2.47;
		double dL = 1.0 + 0.0334 * specialCos(W) + 0.0007 * specialCos(2 * W);
		double S = periodic24(T);
		// This is the answer in Julian Emphemeris Days
		double JDE = ETA + ((0.00001 * S) / dL);
		// Convert Julian Days to TDT in a Date Object
		Date TDT = fromJDEToUTC(JDE);
		// Correct TDT to UTC, both as Date Objects
		Date UTC = fromTDTToUTC(TDT);
		return UTC;
	}

	/**
	 * Calculate an initial guess as the ETA of the Equinox or Solstice of a given year.<br>
	 * <b>Valid for years 1000 to 3000!</b><br>
	 * <br>
	 * <i>See Meeus Astronmical Algorithms Chapter 27</i>
	 * 
	 * @param k = season
	 * @param year for calculation
	 * @return
	 */
	private static double init(int k, int year) {
		double tmpYear = ((double) year - 2000) / 1000;
		double result = 0.0;
		switch (k) {
			case 0:
				result = 2451623.80984 + 365242.37404 * tmpYear + 0.05169 * Math.pow(tmpYear, 2) - 0.00411 * Math.pow(tmpYear, 3) - 0.00057 * Math.pow(tmpYear, 4);
				break;
			case 1:
				result = 2451716.56767 + 365241.62603 * tmpYear + 0.00325 * Math.pow(tmpYear, 2) + 0.00888 * Math.pow(tmpYear, 3) - 0.00030 * Math.pow(tmpYear, 4);
				break;
			case 2:
				result = 2451810.21715 + 365242.01767 * tmpYear - 0.11575 * Math.pow(tmpYear, 2) + 0.00337 * Math.pow(tmpYear, 3) + 0.00078 * Math.pow(tmpYear, 4);
				break;
			case 3:
				result = 2451900.05952 + 365242.74049 * tmpYear - 0.06223 * Math.pow(tmpYear, 2) - 0.00823 * Math.pow(tmpYear, 3) + 0.00032 * Math.pow(tmpYear, 4);
				break;
			default:
				result = 0.0;
				break;
		}
		return result;
	}

	/**
	 * Calculate 24 Periodic Terms.<br>
	 * <br>
	 * <i>See Meeus Astronmical Algorithms Chapter 27</i>
	 * 
	 * @param T
	 * @return
	 */
	private static double periodic24(double T) {
		double result = 0.0;
		double[] A = { 485, 203, 199, 182, 156, 136, 77, 74, 70, 58, 52, 50, 45, 44, 29, 18, 17, 16, 14, 12, 12, 12, 9, 8 };
		double[] B = { 324.96, 337.23, 342.08, 27.85, 73.14, 171.52, 222.54, 296.72, 243.58, 119.81, 297.17, 21.02, 247.54, 325.15, 60.93, 155.12, 288.79, 198.04, 199.76, 95.39, 287.11, 320.81, 227.73, 15.45 };
		double[] C = { 1934.136, 32964.467, 20.186, 445267.112, 45036.886, 22518.443, 65928.934, 3034.906, 9037.513, 33718.147, 150.678, 2281.226, 29929.562, 31555.956, 4443.417, 67555.328, 4562.452, 62894.029, 31436.921, 14577.848, 31931.756, 34777.259, 1222.114, 16859.074 };

		for (int i = 0; i < 24; i++) {
			result += A[i] * specialCos(B[i] + (C[i] * T));
		}
		return result;
	}

	/**
	 * Julian Date to UTC Date Object.<br>
	 * <br>
	 * <i>See Meeus Astronmical Algorithms Chapter 7</i>
	 * 
	 * @param JDE = Julian date, possible with fractional days.
	 * @return Date = Output is a UTC date object.
	 */
	private static Date fromJDEToUTC(double JDE) {
		Date result = null;
		int A = 0;
		int alpha = 0;

		// Integer (important) JDE's
		int Z = new Double(Math.floor(JDE + 0.5)).intValue();
		// Fractional JDE's
		double F = (JDE + 0.5) - Z;

		if (Z < 2299161) {
			A = Z;
		} else {
			alpha = new Double(Math.floor((Z - 1867216.25) / 36524.25)).intValue();
			A = Z + 1 + alpha - new Double(Math.floor(alpha / 4)).intValue();
		}
		int B = A + 1524;
		int C = new Double(Math.floor((B - 122.1) / 365.25)).intValue();
		int D = new Double(Math.floor(365.25 * C)).intValue();
		int E = new Double(Math.floor((B - D) / 30.6001)).intValue();

		// Day of month with decimals for time
		double DT = B - D - new Double(Math.floor(30.6001 * E)).intValue() + F;

		// Month Number
		int Mon = E - ((E < 13.5) ? 1 : 13);

		// Year 
		int Yr = C - (((double) Mon > 2.5) ? 4716 : 4715);

		// Day of Month without decimals for time
		int Day = new Double(Math.floor(DT)).intValue();

		// Hours and fractional hours
		double H = 24 * (DT - Day);

		// Integer value of hours
		int Hr = new Double(Math.floor(H)).intValue();

		// Minutes and fractional minutes
		double M = 60 * (H - Hr);

		// Integer value of minutes
		int Min = new Double(Math.floor(M)).intValue();

		// Integer value of seconds (Milliseconds discarded/cut away)
		int Sec = new Double(Math.floor(60 * (M - Min))).intValue();

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR, Yr);
		cal.set(Calendar.MONTH, Mon - 1);
		cal.set(Calendar.DAY_OF_MONTH, Day);
		cal.set(Calendar.HOUR_OF_DAY, Hr);
		cal.set(Calendar.MINUTE, Min);
		cal.set(Calendar.SECOND, Sec);
		result = cal.getTime();

		return result;
	}

	/**
	 * Correct TDT to UTC.<br>
	 * <br>
	 * <i>See Meeus Astronmical Algorithms Chapter 10</i>
	 * 
	 * @param TDT
	 * @return
	 * @throws Exception
	 */
	private static Date fromTDTToUTC(Date TDT) throws Exception {
		Date result = null;
		// Range of years in lookup table.
		int tblFirstValue = 1620;
		int tblLastValue = 2002;

		// @formatter:off
		// Correction lookup table has entry for every even year between tblFirstValue and tblLastValue.
		// Corrections in Seconds!
		double[] table = {
		/*1620*/121, 112, 103, 95, 88, 82, 77, 72, 68, 63, 60, 56, 53, 51, 48, 46, 44, 42, 40, 38,
		/*1660*/35, 33, 31, 29, 26, 24, 22, 20, 18, 16, 14, 12, 11, 10, 9, 8, 7, 7, 7, 7,
		/*1700*/7, 7, 8, 8, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11,
		/*1740*/11, 11, 12, 12, 12, 12, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 15, 16, 16,
		/*1780*/16, 16, 16, 16, 16, 16, 15, 15, 14, 13,
		/*1800*/13.1, 12.5, 12.2, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 11.9, 11.6, 11.0, 10.2, 9.2, 8.2,
		/*1830*/7.1, 6.2, 5.6, 5.4, 5.3, 5.4, 5.6, 5.9, 6.2, 6.5, 6.8, 7.1, 7.3, 7.5, 7.6,
		/*1860*/7.7, 7.3, 6.2, 5.2, 2.7, 1.4, -1.2, -2.8, -3.8, -4.8, -5.5, -5.3, -5.6, -5.7, -5.9,
		/*1890*/-6.0, -6.3, -6.5, -6.2, -4.7, -2.8, -0.1, 2.6, 5.3, 7.7, 10.4, 13.3, 16.0, 18.2, 20.2,
		/*1920*/21.1, 22.4, 23.5, 23.8, 24.3, 24.0, 23.9, 23.9, 23.7, 24.0, 24.3, 25.3, 26.2, 27.3, 28.2,
		/*1950*/29.1, 30.0, 30.7, 31.4, 32.2, 33.1, 34.0, 35.0, 36.5, 38.3, 40.2, 42.2, 44.5, 46.5, 48.5,
		/*1980*/50.5, 52.5, 53.8, 54.9, 55.8, 56.9, 58.3, 60.0, 61.6, 63.0, 63.8, 64.3 };/*2002 last entry*/
		// @formatter:on

		/*
		 *  Values for deltaT for 2000 thru 2002 from NASA.
		 */

		// deltaT = TDT - UTC (in Seconds).
		double deltaT = 0.0;
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(TDT);
		int Year = cal.get(Calendar.YEAR);

		// Centuries from the epoch 2000.0
		double t = (Year - 2000.0) / 100.0;

		/*
		 *  Find correction in table.
		 */
		if (Year >= tblFirstValue && Year <= tblLastValue) {
			// Odd year - interpolate.
			if (Year % 2 != 0) {
				deltaT = table[(Year - tblFirstValue - 1) / 2] + table[(Year - tblLastValue + 1 / 2)] / 2;
			} else {
				// Even year - direct table lookup.
				deltaT = table[(Year - tblFirstValue) / 2];
			}
		} else if (Year < 948) {
			deltaT = 2177 + 497 * t + 44.1 * Math.pow(t, 2);
		} else if (Year >= 948) {
			deltaT = 102.0 + 102.0 * t + 25.3 * Math.pow(t, 2);
			// Special correction to avoid discontinuity in 2000.
			if (Year >= 2000 && Year <= 2100) {
				deltaT += 0.37 * (Year - 2100);
			}
		} else {
			throw new Exception("Error: TDT to UTC correction not computed.");
		}
		cal.setTimeInMillis(TDT.getTime() - (long) (deltaT * 1000));
		result = cal.getTime();

		return result;
	}

	/**
	 * Returns the name of the season specified by an id
	 * @param season id
	 * @return name of season
     */
	public static String name(int season) {
		switch (season) {
			case 1:
				return "Winter";
			case 2:
				return "Spring";
			case 3:
				return "Summer";
			case 4:
				return "Autumn";
			default:
				throw new RuntimeException("unknown season id");
		}
	}

	/**
	 * Test method to print out what season we actually have.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			int season = getActualNorthernHemisphereSeason();
			System.out.println(name(season));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

