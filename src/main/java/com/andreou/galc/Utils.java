package com.andreou.galc;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Utils {

	public Utils() {

	}

	public Double getRandQuality() {

		Random rn = new Random();
		Double d = 0.0;
		while (d == 0.0)
			d = rn.nextDouble();
		return d;
	}

	public void printRealValues(Set<DatumCont> c) {

		System.out.print("\n--Categories Real Values List--");
		for (DatumCont i : c)
			System.out.print("(" + i.getName() + ")" + i.getTrueValue() + "\t");
		System.out.println();
	}

	public void printValues(Set<DatumCont> c) {

		System.out.print("\n--Categories Real Values List--");
		for (DatumCont i : c)
			System.out.print("(" + i.getName() + ")" + i.getValue() + "\t");
		System.out.println();
	}

	public void printMeans(Set<DatumCont> c) {

		System.out.print("\n--Categories Mean Values List--");
		for (DatumCont i : c)
			System.out.print(i.getMean() + "\t");
		System.out.println();
	}

	public void printStds(Set<DatumCont> c) {

		System.out.print("--Categories  Std Values List--");
		for (DatumCont i : c)
			System.out.print(i.getStd() + "\t");
	}

	public void printCategories(Set<DatumCont> c) {

		System.out.println("--Categories List--");
		for (DatumCont i : c)
			System.out.print(i.getName() + ":" + i.getTrueValue() + "\t");
		System.out.println();
	}

	public void printWorkers(Set<Worker> w) {

		System.out.println("--Workers List--");
		for (Worker i : w)
			System.out.print(i.getName() + "\t");
		System.out.println();

	}

	public void printLabels(Set<DatumCont> categories, Set<Worker> workers, HashMap<CategoryWorker, Double> m) {

		System.out.print("Labels ");
		printMap(categories, workers, m);
	}

	public void printTrasposedLabels(Set<DatumCont> categories, Set<Worker> workers, HashMap<CategoryWorker, Double> m) {

		System.out.print("Labels ");
		printTrasposedMap(categories, workers, m);
	}

	public void printMap(Set<DatumCont> categories, Set<Worker> workers, HashMap<CategoryWorker, Double> m) {

		System.out.println("Map");
		// System.out.printf("%s: (%s,%s)%n", key, c.getName(),w.getName());
		Double value = 0.0;
		int i = categories.size() * workers.size();
		for (DatumCont c : categories)
			for (Worker w : workers) {
				CategoryWorker aux = new CategoryWorker(c, w);
				value = m.get(aux);
				System.out.printf("(%s,%s): %s", c.getName(), w.getName(), value.toString());
				String nl = (--i % workers.size() == 0) ? "%n" : "\t\t";
				System.out.printf(nl);
			}

	}

	public void printTrasposedMap(Set<DatumCont> categories, Set<Worker> workers, HashMap<CategoryWorker, Double> m) {

		System.out.println("--Transposed map--");
		// System.out.printf("%s: (%s,%s)%n", key, c.getName(),w.getName());
		Double value = 0.0;
		int i = categories.size() * workers.size();
		for (Worker w : workers)
			for (DatumCont c : categories) {
				CategoryWorker aux = new CategoryWorker(c, w);
				value = m.get(aux);
				System.out.printf("(%s,%s): %s", c.getName(), w.getName(), value.toString());
				String nl = (--i % categories.size() == 0) ? "%n" : "\t\t";
				System.out.printf(nl);
			}

	}

}
