package it.univr.montecarlo;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.stochastic.RandomVariable;

public class LookbackPutFloatingStrike extends AbstractBuildMonitoring  {
	
	private double maturity;
	private int underlyingIndex;
	
	
	
	public LookbackPutFloatingStrike (double maturity, int discretelyTimes) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=0;
 
	}
	
	public LookbackPutFloatingStrike (double maturity) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=0;

	}
	
	public LookbackPutFloatingStrike (int underlyingIndex, double maturity) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
		 
	}
	
	public LookbackPutFloatingStrike (double maturity, int underlyingIndex, int discretelyTimes) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
		
	}

	
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		
		
		double[] discretizedTimes = buildMonitoringTimes(model);	
	
		RandomVariable maxvaluesForRandomVariable =  getMax(discretizedTimes, maturity, underlyingIndex, model);
		RandomVariable finalValue = model.getAssetValue(model.getTimeIndex(maturity), underlyingIndex);
		RandomVariable values =  maxvaluesForRandomVariable.sub(finalValue).floor(0.0);
		
		
		
		
		final RandomVariable numeraireAtMaturity = model.getNumeraire(maturity);
		final RandomVariable monteCarloWeights = model.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);

		// ...to evaluation time.
		final RandomVariable numeraireAtEvalTime = model.getNumeraire(evaluationTime);
		final RandomVariable monteCarloWeightsAtEvalTime = model.getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloWeightsAtEvalTime);
	
		return values;
	
	}

}



