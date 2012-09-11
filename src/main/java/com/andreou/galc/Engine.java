package com.andreou.galc;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Engine {

	private Set<DatumCont> objects;
	private Map<String, DatumCont> objects_index;
	private Set<Worker>	workers;
	private Map<String, Worker> workers_index;
	private Set<AssignedLabel>	labels;
	
	// private Set<Category> current_categories = new HashSet<Category>();
	// private Set<Worker> current_workers = new HashSet<Worker>();

	//private Ipeirotis												ip;

	public Engine() {

		SyntheticData data = new SyntheticData();
		Double data_mu=0.0;
		Double data_sigma=1.0;
		data.setDataParameters(data_mu, data_sigma);
		
		Double worker_mu_down=-10.0;
		Double worker_mu_up=10.0;
		Double worker_sigma_down=0.0;
		Double worker_sigma_up=1.0;
		data.setWorkerParameters(worker_mu_down, worker_mu_up, worker_sigma_down, worker_sigma_up);

		int data_points = 5;
		int workers = 5;
		data.build(data_points, workers);

		this.objects = data.getObjects();
		this.objects_index = new TreeMap<String, DatumCont>();
		for (DatumCont d : this.objects) {
			objects_index.put(d.getName(), d);
		}
		
		this.workers = data.getWorkers();
		this.workers_index = new TreeMap<String, Worker>();
		for (Worker w : this.workers) {
			workers_index.put(w.getName(), w);
		}
		
		this.labels = data.getLabels();

		
		initWorkers();
		estimateObjectZetas();
		estimateWorkerRho();

	}
		/*
		Double diffq, diffz;
		for (int i = 0; i < 10; i++) {
			diffz = recaclZeta();
			diffq = recaclQuality();
			refineWorkers();
			if (i % 1 == 0) {
				// utils.printMeans(categories);
				// utils.printStds(categories);
				System.out.println("\n___" + i + " th loop __");
			}
		}

		finalizeCategories();
		*/



	/*
	private void finalizeCategories() {

		Double zeta = 0.0;
		Double mean_num = 0.0;
		Double std_num = 0.0;
		Double denum = 0.0;
		AssignedLabel aux;
		for (DatumCont c : this.objects) {
			for (Worker w : this.workers) {
				aux = new AssignedLabel(c, w);
				zeta = zetas.get(aux);
				mean_num = mean_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * c.getMean();
				std_num = std_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * c.getStd();
				denum = denum + w.getBeta();
			}
			if (denum != 0) {
				c.setMean(mean_num / denum);
				c.setStd(std_num / denum);
				c.setValue((std_num * zeta + mean_num) / denum);
			} else {// it should Never happen
			}
			mean_num = std_num = denum = 0.0;
		}
	}

	private Double recaclQuality() {

		Double zeta = 0.0;
		Double p_num = 0.0;
		Double p_denum_i = 0.0;
		Double p_denum_ij = 0.0;
		Double q = 0.0;
		Double diff = 0.0;
		for (Worker w : this.workers) {
			for (DatumCont c : this.objects) {
				AssignedLabel aux = new AssignedLabel(c, w);
				zeta = zetas.get(aux);
				p_num = p_num + c.getZeta() * zeta;
				p_denum_i = p_denum_i + c.getZeta() * c.getZeta();
				p_denum_ij = p_denum_ij + zeta * zeta;
			}
			q = p_num / Math.sqrt(p_denum_i * p_denum_ij);
			diff = w.getQuality() - q;
			System.out.print("diffQ: " + diff + " ");
			w.setQuality(q);
		}

		return diff;
	}

	private Double recaclZeta() {

		Double zeta = 0.0;
		Double z_num = 0.0;
		Double z_denum = 0.0;
		AssignedLabel aux;
		Double z = 0.0;
		Double diff = 0.0;
		for (DatumCont c : this.objects) {
			for (Worker w : this.workers) {
				aux = new AssignedLabel(c, w);
				zeta = zetas.get(aux);
				z_num = z_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * zeta;
				z_denum = z_denum + w.getBeta();
			}
			if (z_denum != 0) {
				z = z_num / z_denum;
				diff = c.getZeta() - z;
				System.out.print("diffZ: " + diff + " ");
				c.setZeta(z);
			} else {// it would Never happen 
			}
			z_num = z_denum = 0.0;
		}
		return diff;
	}

	private void caclZeta() {

		Double y = 0.0;
		Double z = 0.0;
		for (DatumCont c : this.objects)
			for (Worker w : this.workers) {
				AssignedLabel aux = new AssignedLabel(c, w);
				y = labels.get(aux);
				z = (y - c.getMean()) / c.getStd();
				zetas.put(aux, z);
			}
	}

	private void refineWorkers() {

		Double beta = 0.0;
		for (Worker w : this.workers) {
			beta = 1 / (1 - w.getQuality() * w.getQuality());
			w.setBeta(beta);
		}
	}
*/
	private void initWorkers() {

		Generator rhoGenerator =  new Generator(Generator.Distribution.UNIFORM);
		rhoGenerator.setUniformParameters(-1.0, 1.0);
		
		for (Worker w : this.workers) {
			Double rho_init = rhoGenerator.nextData();
			w.setRho(rho_init);
		}
	}

	private void estimateObjectZetas() {
  // See equation 9
		for (DatumCont d : this.objects) {
			
			Double zeta = 0.0;
			Double betasum = 0.0;
			for (AssignedLabel al : d.getAssignedLabels() ) {
				String wid = al.getWorker();
				Worker w = this.workers_index.get(wid);
				zeta += w.getBeta() * Math.sqrt(1 - 1/w.getBeta()) * w.getZeta(al.getLabel());
				betasum += w.getBeta();
			}
			
			d.setZeta(zeta/betasum);
		}
		
		
	}

	private void estimateWorkerRho() {
	  // See equation 10
			for (Worker w : this.workers) {
				for (AssignedLabel al : w.getZetaValues() ) {
					String oid = al.getDatum(); //zij
					DatumCont d = objects_index.get(oid);
					d.getZeta();
					
				}
		}
	}
	
	/*
	private void loadLebels() {

		int i = this.objects.size() * this.workers.size();
		for (DatumCont c : this.objects)
			for (Worker w : this.workers) {
				labels.put(new AssignedLabel(c, w), new Double(i--));
				System.out.printf("(%s,%s): %s", c.getName(), w.getName(), i + 1);
				String nl = (i % workers.size() == 0) ? "%n" : "\t\t";
				System.out.printf(nl);
			}

	}
*/
	public Set<DatumCont> getObjects() {

		return objects;
	}

	public void setObjects(Set<DatumCont> objects) {

		this.objects = objects;
	}


}


class WorkerCategories {

	private Worker				w;
	private Set<DatumCont>	c;

	public WorkerCategories(Worker w, Set<DatumCont> c) {

		if (!(w.equals(null) && c.equals(null))) {
			this.w = w;
			this.c = c;
		}
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkerCategories other = (WorkerCategories) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (w == null) {
			if (other.w != null)
				return false;
		} else if (!w.equals(other.w))
			return false;
		return true;
	}
}

class CategorizedWorkers {

	private DatumCont		c;
	private Set<Worker>	w;

	public CategorizedWorkers(DatumCont c, Set<Worker> w) {

		if (!(w.equals(null) && c.equals(null))) {
			this.c = c;
			this.w = w;
		}
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategorizedWorkers other = (CategorizedWorkers) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (w == null) {
			if (other.w != null)
				return false;
		} else if (!w.equals(other.w))
			return false;
		return true;
	}

}