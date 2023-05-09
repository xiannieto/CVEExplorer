package com.xian.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cve_index")
public class CVE {

	@Id
	private String cveID;
	
	@Field(type = FieldType.Keyword)
	private String assigner;
	private String description;
	
	@Field(type = FieldType.Keyword)
	private List<String> cwes = new ArrayList<>();
	
	
	private List<String> cwesWithAncestors = new ArrayList<>(); // Para indexar jerarquía en ElasticSearch
	private List<String> cpes = new ArrayList<>(); // Igual sobra
	
	@Field(type = FieldType.Keyword)
	private List<String> vendorProductPairs = new ArrayList<>(); // Para indexar productos afectados en ElasticSearch
	private List<Configuration> configurations = new ArrayList<>();
	private List<Reference> references = new ArrayList<>();
	private CVSS3Impact impact;

	public CVE() {
	}

	public CVE(String cveID, String assigner, String description) {
		this.cveID = cveID;
		this.assigner = assigner;
		this.description = description;
	}

	public String getCveID() {
		return cveID;
	}

	public void setCveID(String cveID) {
		this.cveID = cveID;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCwes() {
		return cwes;
	}

	public void setCwes(List<String> cwes) {
		this.cwes = cwes;
	}

	public List<String> getCpes() {
		return cpes;
	}

	public void setCpes(List<String> cpes) {
		this.cpes = cpes;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public List<String> getCwesWithAncestors() {
		return cwesWithAncestors;
	}

	public void setCwesWithAncestors(List<String> cwesWithAncestors) {
		this.cwesWithAncestors = cwesWithAncestors;
	}

	public List<String> getVendorProductPairs() {
		return vendorProductPairs;
	}

	public void setVendorProductPairs(List<String> vendorProductPairs) {
		this.vendorProductPairs = vendorProductPairs;
	}

	public CVSS3Impact getImpact() {
		return impact;
	}

	public void setImpact(CVSS3Impact impact) {
		this.impact = impact;
	}

	public void addCWE(String cwe) {
		this.cwes.add(cwe);
	}

	public void addCPE(String cpe23) {
		this.cpes.add(cpe23);
		Configuration configuration = new Configuration(cpe23);
		this.configurations.add(configuration);
	}

	public void addConfiguration(Configuration c) {
		this.configurations.add(c);
		this.cpes.add(c.cpe23);
	}

	public void addReference(Reference r) {
		this.references.add(r);
	}

	@Override
	public String toString() {
		return "CVE{" + "cveID=" + cveID + ", description=" + description + ", cwes=" + cwes + ", configurations="
				+ configurations + ", references=" + references + ", impact=" + impact + '}';
	}

	public static class Configuration {

		public final static String[] ATTRIBUTE_NAMES = { "part", "vendor", "product", "version", "update", "edition",
				"language", "sw_edition", "target_sw", "target_hw", "other" };

		public String cpe23;
		public String vendor;
		public String product;

		public String version;
		public String language;
		public String targetSW;
		public String targetHW;

		public Configuration() {
		}

		public Configuration(String cpe23) {
			this.cpe23 = cpe23;
			loadCPE23Attributes(cpe23);
		}

		private void loadCPE23Attributes(String cpe23) {
			Map<String, String> attributes = parseCPE23(cpe23);
			this.vendor = attributes.get(ATTRIBUTE_NAMES[1]);
			this.product = attributes.get(ATTRIBUTE_NAMES[2]);
			this.version = attributes.get(ATTRIBUTE_NAMES[3]);
			this.language = attributes.get(ATTRIBUTE_NAMES[6]);
			this.targetSW = attributes.get(ATTRIBUTE_NAMES[8]);
			this.targetHW = attributes.get(ATTRIBUTE_NAMES[9]);
		}

		public Map<String, String> parseCPE23(String cpe23) {

			// cpe:2.3:a:microsoft:internet_explorer:8.0.6001:beta:*:*:*:*:*:*
			if (cpe23.startsWith("cpe:2.3")) {
				String parts[] = cpe23.split(":");
				if (parts.length == 13) {
					Map<String, String> attributes = new HashMap<>();
					for (int i = 2; i < 13; i++) {
						attributes.put(ATTRIBUTE_NAMES[i - 2], parts[i]);
					}
					return attributes;
				}
			}
			// Formato incorrecto => hashmap vacio
			return Collections.emptyMap();
		}

		public void setCpe23(String cpe23) {
			this.cpe23 = cpe23;
			loadCPE23Attributes(cpe23);
		}

		@Override
		public String toString() {
			return "Configuration{" + "vendor=" + vendor + ", product=" + product + '}';
		}

	}

	public static class Reference {
		public String url;
		public String source;
		public List<String> tags = new ArrayList<>();

		public Reference() {
		}

		public Reference(String url) {
			this.url = url;
		}

		public Reference(String url, String source) {
			this.url = url;
			this.source = source;
		}

		public void addTag(String tag) {
			this.tags.add(tag);
		}

		@Override
		public String toString() {
			return "Reference{" + "url=" + url + ", source=" + source + ", tags=" + tags + '}';
		}

	}

	public static class CVSS3Impact {
		public Double exploitabilityScore = 0.0;
		public Double impactScore = 0.0;
		public String attackVector = "UNKNOWN";
		public String attackComplexity = "UNKNOWN";
		public String privilegesRequired = "UNKNOWN";
		public String userInteraction = "UNKNOWN";
		public String scope = "UNKNOWN";
		public String confidentialityImpact = "UNKNOWN";
		public String integrityImpact = "UNKNOWN";
		public String availabilityImpact = "UNKNOWN";
		public Double baseScore = 0.0;
		public String baseSeverity = "UNKNOWN";

		public CVSS3Impact() {
		}

		@Override
		public String toString() {
			return "CVSS3Impact{" + "exploitabilityScore=" + exploitabilityScore + ", impactScore=" + impactScore
					+ ", attackVector=" + attackVector + '}';
		}

	}

}
