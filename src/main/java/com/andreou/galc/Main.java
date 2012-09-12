package com.andreou.galc;

class ReportGenerator {
	
	Engine e;
	public ReportGenerator(Engine e) {
		this.e = e;
	}
	
	/**
	 * 
	 */
	  double getCorrelationRelativeError() {

		Double relRhoError = 0.0;
		int n = e.getWorkers().size();
		for (Worker w : e.getWorkers()) {
			double estRho = w.getEst_rho();
			double realRho = w.getTrueRho();
			double absDiff = Math.abs(realRho - estRho);
			double relDiff = Math.abs(absDiff / realRho);
			relRhoError += relDiff / n;
		}
		return relRhoError;
	}
	
	/**
	 * 
	 */
	  double getCorrelationAbsoluteError() {

		Double avgRhoError = 0.0;
		int n = e.getWorkers().size();
		for (Worker w : e.getWorkers()) {
			double estRho = w.getEst_rho();
			double realRho = w.getTrueRho();
			double absDiff = Math.abs(realRho - estRho);
		
			avgRhoError += absDiff / n;
		}
		return avgRhoError;
	}

	
	/**
	 * 
	 */
	  void generateWorkerReport() {

		System.out.println("Average absolute estimation error for correlation values: " + getCorrelationAbsoluteError());
		System.out.println("Average relative estimation error for correlation values: " + getCorrelationRelativeError());
	}

	/**
	 * @return
	 */
	  Double estimateDistributionSigma() {

		Double nominator_sigma = 0.0;
		Double denominator_sigma = 0.0;
		for (Worker w : e.getWorkers()) {
			Double b = w.getBeta();
			Double coef = Math.sqrt(b * b - b);
			Double s = w.getEst_sigma();
			nominator_sigma += coef * s;
			denominator_sigma += b;
		}
		Double est_sigma = nominator_sigma / denominator_sigma;
		return est_sigma;
	}

	/**
	 * @return
	 */
	  Double estimateDistributionMu() {

		// Estimate mu and sigma of distribution
		Double nominator_mu = 0.0;
		Double denominator_mu = 0.0;
		for (Worker w : e.getWorkers()) {
			Double b = w.getBeta();
			Double coef = Math.sqrt(b * b - b);
			Double m = w.getEst_mu();
			nominator_mu += coef * m;
			denominator_mu += b;
		}
		Double est_mu = nominator_mu / denominator_mu;
		return est_mu;
	}

	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  void generateDistributionReport() {

		System.out.println("Estimated mu = " + estimateDistributionMu());
		System.out.println("Estimated sigma = " + estimateDistributionSigma());
	}

	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  double getZetaAbsoluteErrorObject() {

		Double avgAbsError = 0.0;
		int n = e.getObjects().size();
		for (DatumCont d : e.getObjects()) {
			double estZ = d.getEst_zeta();
			double realZ = d.getTrueZeta();
			double absDiff = Math.abs(realZ - estZ);
			avgAbsError += absDiff / n;
		}
		return avgAbsError;
	}
	
	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  double getZetaRelativeErrorObject() {

		Double avgRelError = 0.0;
		int n = e.getObjects().size();
		for (DatumCont d : e.getObjects()) {
			double estZ = d.getEst_zeta();
			double realZ = d.getTrueZeta();
			double absDiff = Math.abs(realZ - estZ);
			double relDiff = Math.abs(absDiff / realZ);

			avgRelError += relDiff / n;
		}
		return avgRelError;
	}
	
	void generateObjectReport() {

		System.out.println("Average absolute estimation error for z-values: " + getZetaAbsoluteErrorObject());
		System.out.println("Average relative estimation error for z-values: " + getZetaRelativeErrorObject());
	}
	
}

public class Main {



	 static SyntheticData createDataSet(int data_points, Double data_mu, Double data_sigma, int workers,
			Double worker_mu_down, Double worker_mu_up, Double worker_sigma_down, Double worker_sigma_up,
			Double worker_rho_down, Double worker_rho_up) {

		SyntheticData data = new SyntheticData();

		data.setDataParameters(data_mu, data_sigma);

		data.setWorkerParameters(worker_mu_down, worker_mu_up, worker_sigma_down, worker_sigma_up, worker_rho_down,
				worker_rho_up);

		data.build(data_points, workers);
		return data;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Data data = createSyntheticDataSet();
		
		// PANOS: not verified that it works...
		EmpiricalData data = new EmpiricalData();
		data.loadFile(args[0]);

		Engine eng = new Engine(data);
		
		ReportGenerator rpt = new ReportGenerator(eng);

		// Report about distributional estimates
		rpt.generateDistributionReport();

		// Give report for objects
		rpt.generateObjectReport();

		// Give report for workers
		rpt.generateWorkerReport();

	}

	/**
	 * @return
	 */
	private static SyntheticData createSyntheticDataSet() {

		int data_points = 100;
		Double data_mu = 0.0;
		Double data_sigma = 1.0;

		int workers = 1000;
		Double worker_mu_down = -5.0;
		Double worker_mu_up = 5.0;
		Double worker_sigma_down = 0.5;
		Double worker_sigma_up = 1.5;
		Double worker_rho_down = 0.999;
		Double worker_rho_up = 0.9999;

		System.out.println("Data points: " + data_points);
		System.out.println("Workers: " + workers);

		System.out.println("Low rho: " + worker_rho_down);
		System.out.println("High rho: " + worker_rho_up);

		SyntheticData data = createDataSet(data_points, data_mu, data_sigma, workers, worker_mu_down, worker_mu_up,
				worker_sigma_down, worker_sigma_up, worker_rho_down, worker_rho_up);
		return data;
	}

}
