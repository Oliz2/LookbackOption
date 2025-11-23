package it.univr.montecarlo;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

public class LookbackCallFloatingStrike extends AbstractBuildMonitoring {
	
	private double maturity;
	private int underlyingIndex;
	
	
	public LookbackCallFloatingStrike(double maturity, int discretelyTimes) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=0;
	
	}
	
	public LookbackCallFloatingStrike(double maturity) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=0;
	
	}
	
	public LookbackCallFloatingStrike(int underlyingIndex, double maturity) {
		super(0);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
	
	}
	
	public LookbackCallFloatingStrike(double maturity, int underlyingIndex, int discretelyTimes) {
		super(discretelyTimes);
		this.maturity=maturity;
		this.underlyingIndex=underlyingIndex;
	
	}
	
	
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model) throws CalculationException {
		
		
		double[] discretizedTimes = buildMonitoringTimes(model);
	
		RandomVariable minValueForEveryRandomVariable =  getMin(discretizedTimes, maturity,underlyingIndex, model);
		RandomVariable finalValue = model.getAssetValue(model.getTimeIndex(maturity), underlyingIndex);
		RandomVariable values = finalValue.sub(minValueForEveryRandomVariable).floor(0.0);
		
		// Discounting...
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
