package com.andreou.galc;

import java.util.Map;
import java.util.TreeMap;

public class EmpiricalData extends Data {

	
	private Map<String, DatumCont>	objects_index = new TreeMap<String, DatumCont>();
	private Map<String, Worker>			workers_index = new TreeMap<String, Worker>();
	
	public EmpiricalData() {
		super();
	}


	public void loadFile(String filename) {

		String[] lines = Utils.getFile(filename).split("\n");
		
		for (String line : lines) {
			String[] entries = line.split("\t");
			if (entries.length != 3) {
				throw new IllegalArgumentException("Error while loading from assigned labels file");
			}

			String workername = entries[0];
			Worker w = this.workers_index.get(workername);
			if (w == null) {
				w = new Worker(workername);
				this.workers.add(w);
			}
			
			String objectname = entries[1]; 
			DatumCont d = this.objects_index.get(objectname);
			if (d == null) {
				d = new DatumCont(objectname);
				this.objects.add(d);
			}
			
			
			Double value = Double.parseDouble(entries[2]);
			AssignedLabel al = new AssignedLabel(workername, objectname, value);
			
			this.labels.add(al);
			d.addAssignedLabel(al);
			w.addAssignedLabel(al);
		}
		//System.out.println("Loaded "+lines.length + " lines");
	}


}
