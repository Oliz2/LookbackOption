package it.univr.montecarlo;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

public class LookbackPutFixedStrike extends AbstractBuildMonitoring {

		private double maturity;
		private int underlyingIndex;
		private double strike;
		
		
		public LookbackPutFixedStrike (double maturity, int discretelyTimes, double strike) {
			super(discretelyTimes);
			this.maturity=maturity;
			this.underlyingIndex=0;
			this.strike = strike; 
		}
		
		public LookbackPutFixedStrike (double maturity, double strike) {
			super(0);
			this.maturity=maturity;
			this.underlyingIndex=0;
			this.strike = strike; 
		}
		
		public LookbackPutFixedStrike (int underlyingIndex, double maturity, double strike) {
			super(0);
			this.maturity=maturity;
			this.underlyingIndex=underlyingIndex;
			this.strike = strike; 
		}
		
		public LookbackPutFixedStrike (double maturity, int underlyingIndex, int discretelyTimes, double strike) {
			super(discretelyTimes);
			this.maturity=maturity;
			this.underlyingIndex=underlyingIndex;
			this.strike = strike; 
		}
		

		
		@Override
		public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
				throws CalculationException {
			
			
			double[] discretizedTimes = buildMonitoringTimes(model);	
			RandomVariable minValueForEveryRandomVariable =  getMin(discretizedTimes, maturity, underlyingIndex, model);
			
			RandomVariable strikeRandom = model.getRandomVariableForConstant(this.strike);
			RandomVariable values = strikeRandom.sub(minValueForEveryRandomVariable);
			

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


