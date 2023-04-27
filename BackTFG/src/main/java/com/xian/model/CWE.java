package com.xian.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "cwe_index")
public class CWE {
	@Id
	private String cweID;
	private String name;
	private String description;
	private String extendedDescription;

	private Set<String> children = new HashSet<>();
	private Set<String> parents = new HashSet<>();

	private Set<String> languages = new HashSet<>();
	private Set<String> technologies = new HashSet<>();
	private Set<String> operatingSystems = new HashSet<>();
	private Set<String> architectures = new HashSet<>();

	public CWE() {
	}

	public CWE(String cweID, String name, String description, String extendedDescription, Set<String> children,
			Set<String> parents, Set<String> languages, Set<String> technologies, Set<String> operatingSystems,
			Set<String> architectures) {
		super();
		this.cweID = cweID;
		this.name = name;
		this.description = description;
		this.extendedDescription = extendedDescription;
		this.children = children;
		this.parents = parents;
		this.languages = languages;
		this.technologies = technologies;
		this.operatingSystems = operatingSystems;
		this.architectures = architectures;
	}

	public CWE(String cweID, String name, String description, String extendedDescription) {
		super();
		this.cweID = cweID;
		this.name = name;
		this.description = description;
		this.extendedDescription = extendedDescription;
	}

	public Set<String> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<String> languages) {
		this.languages = languages;
	}

	public Set<String> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(Set<String> technologies) {
		this.technologies = technologies;
	}

	public Set<String> getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(Set<String> operatingSystems) {
		this.operatingSystems = operatingSystems;
	}

	public Set<String> getArchitectures() {
		return architectures;
	}

	public void setArchitectures(Set<String> architectures) {
		this.architectures = architectures;
	}

	public void addChild(String childCWEID) {
		this.children.add(childCWEID);
	}

	public void addParent(String parentCWEID) {
		this.parents.add(parentCWEID);
	}

	public void addChild(CWE child) {
		this.children.add(child.getCweID());
		child.addParent(this.cweID);
	}

	public void addParent(CWE parent) {
		this.parents.add(parent.getCweID());
		parent.addChild(this.cweID);
	}

	public String getCweID() {
		return cweID;
	}

	public void setCweID(String cweID) {
		this.cweID = cweID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtendedDescription() {
		return extendedDescription;
	}

	public void setExtendedDescription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}

	public Set<String> getChildren() {
		return children;
	}

	public void setChildren(Set<String> children) {
		this.children = children;
	}

	public Set<String> getParents() {
		return parents;
	}

	public void setParents(Set<String> parents) {
		this.parents = parents;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + Objects.hashCode(this.cweID);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CWE other = (CWE) obj;
		if (!Objects.equals(this.cweID, other.cweID)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CWE [cweID=" + cweID + ", name=" + name + ", description=" + description + ", extendedDescription="
				+ extendedDescription + ", children=" + children + ", parents=" + parents + ", languages=" + languages
				+ ", technologies=" + technologies + ", operatingSystems=" + operatingSystems + ", architectures="
				+ architectures + "]";
	}



}