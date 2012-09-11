package com.andreou.galc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Engine {

	private Set<DatumCont>										categories	= new HashSet<DatumCont>();
	private Set<Worker>											workers			= new HashSet<Worker>();
	private HashMap<CategoryWorker, Double>	labels			= new HashMap<CategoryWorker, Double>();
	private HashMap<CategoryWorker, Double>	zetas				= new HashMap<CategoryWorker, Double>();

	// private Set<Category> current_categories = new HashSet<Category>();
	// private Set<Worker> current_workers = new HashSet<Worker>();

	private Ipeirotis												ip;

	public Engine() {

		SyntheticData data = new SyntheticData(5, 4);

		// True Values initialization initData(mu,sigma)
		data.initData(0.0, 1.0);

		this.categories = data.getCategories();
		this.workers = data.getWorkers();
		data.build();
		this.labels = data.getLabels();

		this.ip = new Ipeirotis(this);

		initWorkers();
		initCategories();
		caclZeta();

		Utils utils = new Utils();
		utils.printCategories(categories);
		utils.printWorkers(workers);
		utils.printLabels(categories, workers, labels);
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
		utils.printRealValues(categories);

		// utils.printMeans(categories);
		// utils.printStds(categories);
	}

	private void finalizeCategories() {

		Double zeta = 0.0;
		Double mean_num = 0.0;
		Double std_num = 0.0;
		Double denum = 0.0;
		CategoryWorker aux;
		for (DatumCont c : this.categories) {
			for (Worker w : this.workers) {
				aux = new CategoryWorker(c, w);
				zeta = zetas.get(aux);
				mean_num = mean_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * c.getMean();
				std_num = std_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * c.getStd();
				denum = denum + w.getBeta();
			}
			if (denum != 0) {
				c.setMean(mean_num / denum);
				c.setStd(std_num / denum);
				c.setValue((std_num * zeta + mean_num) / denum);
			} else {/* it would Never happen */
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
			for (DatumCont c : this.categories) {
				CategoryWorker aux = new CategoryWorker(c, w);
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
		CategoryWorker aux;
		Double z = 0.0;
		Double diff = 0.0;
		for (DatumCont c : this.categories) {
			for (Worker w : this.workers) {
				aux = new CategoryWorker(c, w);
				zeta = zetas.get(aux);
				z_num = z_num + w.getBeta() * Math.sqrt(1 - 1 / w.getBeta()) * zeta;
				z_denum = z_denum + w.getBeta();
			}
			if (z_denum != 0) {
				z = z_num / z_denum;
				diff = c.getZeta() - z;
				System.out.print("diffZ: " + diff + " ");
				c.setZeta(z);
			} else {/* it would Never happen */
			}
			z_num = z_denum = 0.0;
		}
		return diff;
	}

	private void caclZeta() {

		Double y = 0.0;
		Double z = 0.0;
		for (DatumCont c : this.categories)
			for (Worker w : this.workers) {
				CategoryWorker aux = new CategoryWorker(c, w);
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

	private void initWorkers() {

		Double beta = 0.0;
		for (Worker w : this.workers) {
			w.setQuality(new Utils().getRandQuality());
			beta = 1 / (1 - w.getQuality() * w.getQuality());
			w.setBeta(beta);
		}
	}

	private void initCategories() {

		Double y = 0.0;
		Double mean = 0.0;
		Double std = 0.0;
		for (DatumCont c : this.categories) {
			// mean loop
			for (Worker w : this.workers) {
				CategoryWorker aux = new CategoryWorker(c, w);
				y = labels.get(aux);
				mean = mean + y;
			}
			mean = mean / workers.size();
			c.setMean(mean);

			// std loop
			for (Worker w : this.workers) {
				CategoryWorker aux = new CategoryWorker(c, w);
				y = labels.get(aux);
				std = (y - mean) * (y - mean);
			}
			std = std / workers.size();
			c.setStd(std);
		}

	}

	private void loadLebels() {

		int i = this.categories.size() * this.workers.size();
		for (DatumCont c : this.categories)
			for (Worker w : this.workers) {
				labels.put(new CategoryWorker(c, w), new Double(i--));
				System.out.printf("(%s,%s): %s", c.getName(), w.getName(), i + 1);
				String nl = (i % workers.size() == 0) ? "%n" : "\t\t";
				System.out.printf(nl);
			}

	}

	private void buildCategories() {

		this.categories.add(new DatumCont("cat1"));
		this.categories.add(new DatumCont("cat2"));
		this.categories.add(new DatumCont("cat3"));
	}

	private void buildWorkers() {

		this.workers.add(new Worker("work1"));
		this.workers.add(new Worker("work2"));
		this.workers.add(new Worker("work3"));
		this.workers.add(new Worker("work4"));
	}

	public Set<DatumCont> getCategories() {

		return categories;
	}

	public void setCategories(Set<DatumCont> categories) {

		this.categories = categories;
	}

	public Set<Worker> getWorker() {

		return worker;
	}

	public void setWorker(Set<Worker> worker) {

		this.worker = worker;
	}

	private Set<Worker>	worker;

}

class CategoryWorker {

	private Worker		w;
	private DatumCont	c;

	public CategoryWorker(DatumCont c, Worker w) {

		if (!(w.equals(null) || c.equals(null))) {
			this.w = w;
			this.c = c;
		}
	}

	public Worker getW() {

		return w;
	}

	public void setW(Worker w) {

		this.w = w;
	}

	public DatumCont getC() {

		return c;
	}

	public void setC(DatumCont c) {

		this.c = c;
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
		if (!(obj instanceof CategoryWorker))
			return false;
		CategoryWorker other = (CategoryWorker) obj;
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