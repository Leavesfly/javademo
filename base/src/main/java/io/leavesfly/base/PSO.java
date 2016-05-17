package io.leavesfly.base;

//y=f(x1,x2)=x1^2+x2^2,-10<=x1,x2<=10;��y����Сֵ
public class PSO {
	private static final int ParticleNum = 3;

	private Particle[] particleArray;
	private float[] globalBestLocation;
	private float globalValue;

	public float getGlobalValue() {
		return globalValue;
	}

	public void setGlobalValue(float globalValue) {
		this.globalValue = globalValue;
	}

	public Particle[] getParticleArray() {
		return particleArray;
	}

	public void setParticleArray(Particle[] particleArray) {
		this.particleArray = particleArray;
	}

	public float[] getGlobalBestLocation() {
		return globalBestLocation;
	}

	public void setGlobalBestLocation(float[] globalBestLocation) {
		this.globalBestLocation = globalBestLocation;
	}

	private float valueOfGloableLocation() {
		float result = 0f;
		for (int i = 0; i < globalBestLocation.length; i++) {
			result += Math.pow(globalBestLocation[i], 2);
		}
		return result;
	}

	public PSO() {
		particleArray = new Particle[ParticleNum];
		for (int i = 0; i < ParticleNum; i++) {
			particleArray[i] = new Particle();
		}
		globalBestLocation = getBestParticleLocation();
		globalValue = valueOfGloableLocation();
	}

	private float[] getBestParticleLocation() {
		Particle par = particleArray[0];
		for (int i = 1; i < ParticleNum; i++) {
			if (par.valueOfBestLocation() > particleArray[i]
					.valueOfBestLocation()) {
				par = particleArray[i];
			}
		}
		return par.getBestLocation().clone();
	}

	private void printBestLocation() {
		for (int i = 0; i < globalBestLocation.length; i++) {
			System.out.print(globalBestLocation[i]);
			System.out.print(' ');
		}
		System.out.println();
	}

	public void bestParticleLocation(){
		Particle par = particleArray[0];
		for (int i = 1; i < ParticleNum; i++) {
			if (par.valueOfBestLocation() > particleArray[i]
					.valueOfBestLocation()) {
				par = particleArray[i];
			}
		}	
		globalBestLocation[0]=par.getBestLocation()[0];
		globalBestLocation[1]=par.getBestLocation()[1];
	}
	
	public void flushGlobalBest() {
		bestParticleLocation();
		globalValue = valueOfGloableLocation();
	}

	public void psOptimization(int num) {
		int count = 0;
		while (true) {
			for (int i = 0; i < ParticleNum; i++) {
				particleArray[i].changeRate(globalBestLocation);
				particleArray[i].changeLocation();
				particleArray[i].changeBestLocation();

			}

			flushGlobalBest();
			count++;
			if (count > num) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		PSO pso = new PSO();
		pso.psOptimization(100);
		System.out.println(pso.getGlobalValue());

	}
}

class Particle {
	private static final float MIN = -10f;
	private static final float MAX = 10f;
	private static final float RateMax = MAX * 0.2f;
	private static final float RateMin = MIN * 0.2f;
	private static final float InertiaWeight = 0.9f;
	private static final float LearnRate = 2.0f;

	private float[] location;
	private float[] rate;
	private float[] bestLocation;
	private float funValue;

	public float getFunValue() {
		return funValue;
	}

	public void setFunValue(float funValue) {
		this.funValue = funValue;
	}

	public Particle() {
		location = new float[2];
		for (int i = 0; i < location.length; i++) {
			location[i] = (float) (MIN + (MAX - MIN) * Math.random());
		}
		rate = new float[2];
		for (int i = 0; i < rate.length; i++) {
			rate[i] = (float) (RateMin + (RateMax - RateMin) * Math.random());
		}
		bestLocation = location.clone();
		flushFunValue();
	}

	public float[] getLocation() {
		return location;
	}

	public void setLocation(float[] location) {
		this.location = location;
	}

	public float[] getRate() {
		return rate;
	}

	public void setRate(float[] rate) {
		this.rate = rate;
	}

	public float[] getBestLocation() {
		return bestLocation;
	}

	public void setBestLocation(float[] bestLocation) {
		this.bestLocation = bestLocation;
	}

	public void flushFunValue() {
		funValue = 0f;
		for (int i = 0; i < location.length; i++) {
			funValue += Math.pow(location[i], 2);
		}
	}

	public void changeRate(float[] globalBestLocation) {
		for (int i = 0; i < globalBestLocation.length; i++) {
			rate[i] = (float) (InertiaWeight * rate[i] + LearnRate
					* Math.random() * (bestLocation[i] - location[i]) + LearnRate
					* Math.random() * (globalBestLocation[i] - location[i]));
		}

	}

	public void changeLocation() {
		for (int i = 0; i < location.length; i++) {
			location[i] += rate[i];

			if (location[i] > MAX) {
				location[i] = MAX;
			}
			if (location[i] < MIN) {
				location[i] = MIN;
			}
		}
		flushFunValue();
	}

	public float valueOfBestLocation() {
		float result = 0f;
		for (int i = 0; i < bestLocation.length; i++) {
			result += Math.pow(bestLocation[i], 2);
		}
		return result;
	}

	public void changeBestLocation() {

		if (funValue < valueOfBestLocation()) {
			for (int i = 0; i < location.length; i++) {
				bestLocation[i] = location[i];

			}

		}
	}
}