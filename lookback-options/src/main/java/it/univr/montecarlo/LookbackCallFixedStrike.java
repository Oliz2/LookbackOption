package it.univr.montecarlo;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import java.util.function.DoubleUnaryOperator;


public class LookbackCallFixedStrike extends AbstractBuildMonitoring {
	
	
	
	
	private double maturity;
	private int underlyingIndex;
	private double strike;
	
	
	public LookbackCallFixedStrike (double maturity, int discretelyTimes, double strike) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=0;
		this.strike = strike; 
	}
	
	public LookbackCallFixedStrike (double maturity, double strike) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=0;
		this.strike = strike; 
	}
	
	public LookbackCallFixedStrike (int underlyingIndex, double maturity, double strike) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
		this.strike = strike; 
	}
	
	public LookbackCallFixedStrike (double maturity, int underlyingIndex, int discretelyTimes, double strike) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
		this.strike = strike; 
	}
	
	

	
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		
		
		double[] discretizedTimes = buildMonitoringTimes(model);
		
		RandomVariable maxvaluesForRandomVariable =  getMax(discretizedTimes, maturity, underlyingIndex, model);
		
		RandomVariable strikeRandom = model.getRandomVariableForConstant(this.strike);
		RandomVariable values =  maxvaluesForRandomVariable.sub(strikeRandom).floor(0.0);
		
		
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












/*
for(double currentTime : discretizedTimes) {


	currentTime = Math.min(currentTime, maturity);
	RandomVariable realizationAtCurrentTime = model.getAssetValue(currentTime, underlyingIndex);
	RandomVariable maxvalueOfRandomVarible = maxValuesForEveryFixedStrike.floor(realizationAtCurrentTime);
	
	
	maxValuesForEveryFixedStrike = maxvalueOfRandomVarible;

}
*/

