package com.andreou.galc;


public class AssignedLabel {

		private String worker_id;
		private String object_id;
		private Double label;

		public AssignedLabel(String wid, String oid, Double label) {
			this.worker_id=wid;
			this.object_id=oid;
		this.label=label;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
			result = prime * result + ((object_id == null) ? 0 : object_id.hashCode());
			result = prime * result + ((worker_id == null) ? 0 : worker_id.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof AssignedLabel))
				return false;
			AssignedLabel other = (AssignedLabel) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			if (object_id == null) {
				if (other.object_id != null)
					return false;
			} else if (!object_id.equals(other.object_id))
				return false;
			if (worker_id == null) {
				if (other.worker_id != null)
					return false;
			} else if (!worker_id.equals(other.worker_id))
				return false;
			return true;
		}

		
		/**
		 * @return the worker_id
		 */
		public String getWorker() {
		
			return worker_id;
		}

		
		/**
		 * @param worker_id the worker_id to set
		 */
		public void setWorker(String worker_id) {
		
			this.worker_id = worker_id;
		}

		
		/**
		 * @return the object_id
		 */
		public String getDatum() {
		
			return object_id;
		}

		
		/**
		 * @param object_id the object_id to set
		 */
		public void setDatum(String object_id) {
		
			this.object_id = object_id;
		}

		
		/**
		 * @return the label
		 */
		public Double getLabel() {
		
			return label;
		}

		
		/**
		 * @param label the label to set
		 */
		public void setLabel(Double label) {
		
			this.label = label;
		}



}
