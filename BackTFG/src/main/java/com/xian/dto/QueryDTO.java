package com.xian.dto;

import java.io.Serializable;
import java.util.List;

public class QueryDTO implements Serializable {
	private String description; // Cadena/s a buscar en el campo descripcion
	private String assigner; // Cadena/s a buscar en el campo assigner
	private List<String> cwes; // Lista de IDs de CWEs a buscar (tanto en el campo cwes como en el
								// cwesWithAncestor)
	private List<String> vendors; // Lista de vendors a buscar en el campo vendorProductPairs con la clave
									// "{vendor}/*" (buscar solo por vendor)
	private List<String> vendorProductPairs; // Lista de pares {vendor}/{product} a buscar en el campo
												// vendorProductPairs (buscar por vendor+product)
	private List<String> attackVectors; // Lista de vectores de ataque a buscar en CVE.impact.attackVector

	public QueryDTO() {
		super();
	}

	public QueryDTO(String description, String assigner, List<String> cwes, List<String> vendors,
			List<String> vendorProductPairs, List<String> attackVectors) {
		super();
		this.description = description;
		this.assigner = assigner;
		this.cwes = cwes;
		this.vendors = vendors;
		this.vendorProductPairs = vendorProductPairs;
		this.attackVectors = attackVectors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public List<String> getCwes() {
		return cwes;
	}

	public void setCwes(List<String> cwes) {
		this.cwes = cwes;
	}

	public List<String> getVendors() {
		return vendors;
	}

	public void setVendors(List<String> vendors) {
		this.vendors = vendors;
	}

	public List<String> getVendorProductPairs() {
		return vendorProductPairs;
	}

	public void setVendorProductPairs(List<String> vendorProductPairs) {
		this.vendorProductPairs = vendorProductPairs;
	}

	public List<String> getAttackVectors() {
		return attackVectors;
	}

	public void setAttackVectors(List<String> attackVectors) {
		this.attackVectors = attackVectors;
	}

	@Override
	public String toString() {
		return "QueryDTO [description=" + description + ", assigner=" + assigner + ", cwes=" + cwes + ", vendors="
				+ vendors + ", vendorProductPairs=" + vendorProductPairs + ", attackVectors=" + attackVectors + "]";
	}

}