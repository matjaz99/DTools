package si.matjazcerkvenik.dtools.io.randomizer;

import java.io.Serializable;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import si.matjazcerkvenik.dtools.context.DMetrics;

public class RandomGenerator implements Serializable {
	
	private static final long serialVersionUID = 9160875147783887518L;
	
	private int min = 0;
	private int max = 100;
	private int dev = 20;
	private String wave = "9999d";
	private Double value = 20.0;
	private Double currentValue = 10.0;
	

	/**
	 * Generate next value based on current value +/- delta. Delta is random
	 * value, but not bigger than maxDeviation. Value cannot be bigger than
	 * maxValue and not less than 0.
	 * 
	 * @param currentValue
	 * @param max
	 * @param dev
	 * @return
	 */
	public double getNextInt() {

		if (min >= max) {
			return 0;
		}

		Random rand = new Random();

		int d = rand.nextInt(dev);
		
		currentValue = currentValue * getCosinusFactor();

		if (rand.nextBoolean()) {
			currentValue = currentValue + d;
		} else {
			currentValue = currentValue - d;
		}

		if (currentValue > max) {
			currentValue = (double) max;
		}
		if (currentValue < min) {
			currentValue = (double) min;
		}
		
		return currentValue;

	}
	
	private double getCosinusFactor() {
		
		double waveMillis = 5 * 60 * 1000;
		long time = System.currentTimeMillis() - DMetrics.startTimestamp;
		
		if (wave.endsWith("d")) {
			waveMillis = new Double(wave.split("d")[0]);
			waveMillis = waveMillis * 24 * 60 * 60 * 1000;
		} else if (wave.endsWith("h")) {
			waveMillis = new Double(wave.split("h")[0]);
			waveMillis = waveMillis * 60 * 60 * 1000;
		} else if (wave.endsWith("m")) {
			waveMillis = new Double(wave.split("m")[0]);
			waveMillis = waveMillis * 60 * 1000;
		}
		
		double d = 2 * Math.PI * time / waveMillis;
		System.out.format("The cosine for " + wave + " of %.4f is %.4f%n", d, Math.cos(d));
		return Math.abs(Math.cos(d));
		
//		double degrees = 45.0;
//	      double radians = Math.toRadians(degrees);

//	      System.out.format("The value of pi is %.4f%n", Math.PI);
//	      System.out.format("The cosine of %.1f degrees is %.4f%n", degrees, Math.cos(radians));
		
	}
	
	public Double getValue() {
		return value;
	}

	@XmlAttribute
	public void setValue(Double value) {
		this.value = value;
		this.currentValue = value;
	}

	public int getMin() {
		return min;
	}

	@XmlAttribute
	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	@XmlAttribute
	public void setMax(int max) {
		this.max = max;
	}

	public int getDev() {
		return dev;
	}

	@XmlAttribute
	public void setDev(int dev) {
		this.dev = dev;
	}

	public String getWave() {
		return wave;
	}

	@XmlAttribute
	public void setWave(String wave) {
		this.wave = wave;
	}
	
	public static Double abs(double d) {
		return Math.abs(d);
	}

}
