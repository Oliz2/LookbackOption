package it.univr.montecarlo;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

public abstract class AbstractBuildMonitoring extends AbstractAssetMonteCarloProduct {
	
	
	private int discretelyTimes;

	
	public AbstractBuildMonitoring(int discretelyTimes) {
		
		this.discretelyTimes = discretelyTimes;
	
	}
	
	
	protected double[] buildMonitoringTimes(AssetModelMonteCarloSimulationModel model) {
		
		
		
		TimeDiscretization td = model.getTimeDiscretization();
        if (discretelyTimes == 0) {
            double[] times = new double[td.getNumberOfTimes()];
            for (int i = 0; i < td.getNumberOfTimes(); i++) {
                times[i] = td.getTime(i);
            }
            return times;
        }
        double[] times = new double[discretelyTimes + 1];
        for (int i = 0; i <= discretelyTimes; i++) {
            int index = (int) Math.round(i * (td.getNumberOfTimes() - 1) / (double) discretelyTimes);
            times[i] = td.getTime(index);
            }
		return times;
        }
	
	
	
	protected RandomVariable getMax(double[] discretizedTimes, double maturity, int underlyingIndex, AssetModelMonteCarloSimulationModel model) 
			throws CalculationException {
		
		RandomVariable maxValue = model.getRandomVariableForConstant(0.0);
		for(double currentTime : discretizedTimes) {
			
			currentTime = Math.min(currentTime, maturity);
			RandomVariable realizationAtCurrentTime = model.getAssetValue(currentTime, underlyingIndex);
			maxValue = maxValue.floor(realizationAtCurrentTime);
			
		}
		return maxValue;
		
	}
	
	protected RandomVariable getMin(double[] discretizedTimes, double maturity, int underlyingIndex, AssetModelMonteCarloSimulationModel model) 
			throws CalculationException{
		
		RandomVariable minValue = model.getRandomVariableForConstant(Integer.MAX_VALUE);
		for(double currentTime : discretizedTimes) {
			
			currentTime = Math.min(currentTime, maturity);
			RandomVariable realizationAtCurrentTime = model.getAssetValue(currentTime, underlyingIndex);
			minValue = minValue.cap(realizationAtCurrentTime);
		}
		return minValue;
	
	}
	
		
		
	}

		
	

	
		




	
	
	
	
	
	
	
	
	
	
	



