package si.matjazcerkvenik.dtools.io.randomizer;

import java.io.Serializable;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import si.matjazcerkvenik.dtools.context.DMetrics;

public class RandomGenerator implements Serializable {
	
	private static final long serialVersionUID = 9160875147783887518L;
	
	private int minValue = 0;
	private int maxValue = 100;
	private int maxDeviation = 20;
	private String wavePeriod = "5m";
	private int currentValue = 10;
	

	/**
	 * Generate next value based on current value +/- delta. Delta is random
	 * value, but not bigger than maxDeviation. Value cannot be bigger than
	 * maxValue and not less than 0.
	 * 
	 * @param currentValue
	 * @param maxValue
	 * @param maxDeviation
	 * @return
	 */
	public int getNextInt() {

		if (minValue >= maxValue) {
			return 0;
		}

		Random rand = new Random();

		int dev = rand.nextInt(maxDeviation);
		
		currentValue = (int) (currentValue * getCosinusFactor());

		if (rand.nextBoolean()) {
			currentValue = currentValue + dev;
		} else {
			currentValue = currentValue - dev;
		}

		if (currentValue > maxValue) {
			currentValue = maxValue;
		}
		if (currentValue < minValue) {
			currentValue = minValue;
		}
		
		return currentValue;

	}
	
	private double getCosinusFactor() {
		
		double waveMillis = 5 * 60 * 1000;
		long time = System.currentTimeMillis() - DMetrics.startTimestamp;
		
		if (wavePeriod.endsWith("d")) {
			waveMillis = new Double(wavePeriod.split("d")[0]);
			waveMillis = waveMillis * 24 * 60 * 60 * 1000;
		} else if (wavePeriod.endsWith("h")) {
			waveMillis = new Double(wavePeriod.split("h")[0]);
			waveMillis = waveMillis * 60 * 60 * 1000;
		} else if (wavePeriod.endsWith("m")) {
			waveMillis = new Double(wavePeriod.split("m")[0]);
			waveMillis = waveMillis * 60 * 1000;
		}
		
		double d = 2 * Math.PI * time / waveMillis;
		System.out.format("The cosine for " + wavePeriod + " of %.4f is %.4f%n", d, Math.cos(d));
		return Math.abs(Math.cos(d));
		
//		double degrees = 45.0;
//	      double radians = Math.toRadians(degrees);

//	      System.out.format("The value of pi is %.4f%n", Math.PI);
//	      System.out.format("The cosine of %.1f degrees is %.4f%n", degrees, Math.cos(radians));
		
	}

	public int getMinValue() {
		return minValue;
	}

	@XmlAttribute
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	@XmlAttribute
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMaxDeviation() {
		return maxDeviation;
	}

	@XmlAttribute
	public void setMaxDeviation(int maxDeviation) {
		this.maxDeviation = maxDeviation;
	}

	public String getWavePeriod() {
		return wavePeriod;
	}

	@XmlAttribute
	public void setWavePeriod(String wavePeriod) {
		this.wavePeriod = wavePeriod;
	}

}
