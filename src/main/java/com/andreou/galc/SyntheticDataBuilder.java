package com.andreou.galc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;



public class SyntheticDataBuilder {
	
	private Data 		data;
	private String		dist;
	
	private Boolean		verbose;
	private String		file;

	public SyntheticDataBuilder(Boolean verbose, String file) {

		dist = loadDistributionOption(file);
		this.verbose = verbose;
		this.file = file;		
	}
	public void build(){
		if(dist.equals("joinlynormal")) {
			SyntheticJoinlyNormalData jdata = new SyntheticJoinlyNormalData(verbose,file);
			jdata.initDataParameters();
			jdata.initWorkerParameters();
			jdata.build();

			data = jdata;
		} else if(dist.equals("singlerho")){
			SyntheticSingleRhoData srdata = new SyntheticSingleRhoData(verbose,file);
			srdata.initDataParameters();
			srdata.initWorkerParameters();
			srdata.build();
			data = srdata;
		} else {
			SyntheticData sdata = new SyntheticData(verbose,file);
			sdata.initDataParameters();
			sdata.initWorkerParameters();
			sdata.build();
			data = sdata;
		}
		
	}
	
	public Data getData() {
		return data;
	}

	public void writeLabelsToFile(String filename) {

		try {
			File outfile = new File(filename);

			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			for (AssignedLabel al : data.getLabels()) {
				String line = al.getWorker() + "\t" + al.getDatum() + "\t" + al.getLabel() + "\n";
				bw.write(line);
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeTrueObjectDataToFile(String filename) {

		try {
			File outfile = new File(filename);

			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			for (DatumCont d: data.getObjects()) {
				String line = d.getName() + "\t" + d.getTrueValue() + "\t" + d.getTrueZeta() + "\n";
				bw.write(line);
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeTrueWorkerDataToFile(String filename) {

		try {
			File outfile = new File(filename);

			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			for (Worker w : data.getWorkers()) {
				String line = w.getName() + "\t" + w.getTrueRho() + "\t" + w.getTrueMu() + "\t" + w.getTrueSigma() + "\t"
						+ "\n";
				bw.write(line);
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeGoldObjectDataToFile(String filename) {

		try {
			File outfile = new File(filename);

			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			for (DatumCont d: data.getObjects()) {
				if(d.isGold()) {
					String line = d.getName() + "\t" + d.getTrueValue() + "\t" + d.getTrueZeta() + "\n";
					bw.write(line);
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String loadDistributionOption(String filename) {
		String[] lines = Utils.getFile(filename).split("\n");
		for(String line : lines) {
			String[] entries = line.split("=");
			if (entries.length != 2) {
				throw new IllegalArgumentException("Error while loading from options file");
			} else if(entries[0].equals("dist")) {
				return new String(entries[1]);
			} else {
				continue;
			}
		}
		return "";
	}	

}
