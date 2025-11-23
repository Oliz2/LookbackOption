package it.univr.montecarlo;

import it.univr.analyticprices.AnalyticPrices;
import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class SecondTest {

	public static void main(String[] args) throws CalculationException {
		//model parameters
		double spotPrice = 100.0;
		double riskFreeRate = 0.1;
		double volatility = 0.3;

		//option parameters
		double maturity = 1.0;		
		double strike = 100.0;
		int numberOfFixingsForDiscretelyMonitoredLookbacks = 100;

		//time discretization parameters
		int numberOfTimeSteps = 1000;
		double initialTime = 0.0;
		double timeStep = maturity / numberOfTimeSteps;
				
		TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);
		AbstractAssetMonteCarloProduct continuouslyMonitoredCallFixedStrike = new LookbackCallFixedStrike(maturity, strike);
		AbstractAssetMonteCarloProduct discretelyMonitoredCallFixedStrike = new LookbackCallFixedStrike(maturity, numberOfFixingsForDiscretelyMonitoredLookbacks, strike);
		
		//simulation parameters
		int numberOfPaths = 10000;
		int seed = 1897;
		BrownianMotion ourDriver = new BrownianMotionFromMersenneRandomNumbers(times, 1, numberOfPaths, seed);
		
		MonteCarloBlackScholesModel blackScholesProcess = new MonteCarloBlackScholesModel(spotPrice, riskFreeRate, volatility, ourDriver);
		double mCPriceContinuouslyMonitoredCallFixed = continuouslyMonitoredCallFixedStrike.getValue(blackScholesProcess);
		double mCPriceDiscretelyMonitoredCallFixed = discretelyMonitoredCallFixedStrike.getValue(blackScholesProcess);
		
		System.out.println("Continous fixedCall:" + mCPriceContinuouslyMonitoredCallFixed);
		System.out.println("Discrete fixedCall:" + mCPriceDiscretelyMonitoredCallFixed);
		
		
		System.out.println(" ");
		
		AbstractAssetMonteCarloProduct continuouslyMonitoredCallFloatingStrike = new LookbackCallFloatingStrike(maturity);
		AbstractAssetMonteCarloProduct discretelyMonitoredCallFloatingStrike = new LookbackCallFloatingStrike(maturity, numberOfFixingsForDiscretelyMonitoredLookbacks);
		
		double mCPriceContinuouslyMonitoredCallFloat = continuouslyMonitoredCallFloatingStrike.getValue(blackScholesProcess);
		double mCPriceDiscretelyMonitoredCallFloat = discretelyMonitoredCallFloatingStrike.getValue(blackScholesProcess);
		
		System.out.println("Continous floatingCall:" + mCPriceContinuouslyMonitoredCallFloat);
		System.out.println("Discrete floatinfCall:" + mCPriceDiscretelyMonitoredCallFloat);
		
		
		AbstractAssetMonteCarloProduct continuosLookbackPutFixedStrike = new LookbackPutFixedStrike(maturity, strike);
		AbstractAssetMonteCarloProduct discreteLookbackPutFixedStrike = new LookbackPutFixedStrike(maturity, numberOfFixingsForDiscretelyMonitoredLookbacks, strike);
		double mCPriceContinuouslyMonitoredPutFixed = continuosLookbackPutFixedStrike.getValue(blackScholesProcess);
		double mCPriceDiscretelyMonitoredPutFixed =  discreteLookbackPutFixedStrike.getValue(blackScholesProcess);
		
		System.out.println("");
		
		System.out.println("Continue fixedPut: " + mCPriceContinuouslyMonitoredPutFixed);
		System.out.println("discrete fixedPut: " + mCPriceDiscretelyMonitoredPutFixed);
		
		AbstractAssetMonteCarloProduct continuouslyMonitoredPutFloatingStrike = new LookbackPutFloatingStrike(maturity);
		AbstractAssetMonteCarloProduct discretelyMonitoredCPutFloatingStrike = new LookbackPutFloatingStrike(maturity, numberOfFixingsForDiscretelyMonitoredLookbacks);
		
		double mCPriceContinuouslyMonitoredPutFloat = continuouslyMonitoredPutFloatingStrike.getValue(blackScholesProcess);
		double mCPriceDiscretelyMonitoredPutFloat = discretelyMonitoredCPutFloatingStrike.getValue(blackScholesProcess);
		
		System.out.println("");
		
		System.out.println("Continue floatingdPut: " + mCPriceContinuouslyMonitoredPutFloat);
		System.out.println("Discrete floatingPut: " + mCPriceDiscretelyMonitoredPutFloat);
		
		
		
		
		
		//Analytic Result continuos
	
		System.out.println("\n--- Analytic Result for Continuos Time---");

		
		System.out.println("Analytic formula (Call Fixed, Continuous): " + AnalyticPrices.continuouslyMonitoredLookbackCallFixedStrike(spotPrice, riskFreeRate, volatility, maturity,strike));

	
		System.out.println("Analytic formula (Call Floating, Continuous): " + AnalyticPrices.continuouslyMonitoredLookbackCallFloatingStrike(spotPrice, riskFreeRate, volatility, maturity));

		
		System.out.println("Analytic formula (Put Fixed, Continuous): " + AnalyticPrices.continuouslyMonitoredLookbackPutFixedStrike(spotPrice, riskFreeRate, volatility, maturity,strike));

		
		System.out.println("Analytic formula (Put Floating, Continuous): " + AnalyticPrices.continuouslyMonitoredLookbackPutFloatingStrike(spotPrice, riskFreeRate, volatility, maturity));

		
		//Analytic Result Discrete
	
		System.out.println("\n--- Analytic Value fo discrete Times ---");
		
		
		System.out.println("Analytic formula (Call Fixed, Discrete): " + AnalyticPrices.discretelyMonitoredLookbackPutFloatingStrike(spotPrice, riskFreeRate, volatility, maturity,numberOfFixingsForDiscretelyMonitoredLookbacks));
		System.out.println("Analytic formula (Call Fixed, Discrete): " + AnalyticPrices.discretelyMonitoredLookbackCallFloatingStrike(spotPrice, riskFreeRate, volatility, maturity,numberOfFixingsForDiscretelyMonitoredLookbacks));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	}

}
