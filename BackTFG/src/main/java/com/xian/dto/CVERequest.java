package com.xian.dto;

import java.util.Objects;

public class CVERequest {

	private String id;

	private String meta_data_id;

	// Refer CWE
	private String description_value;

	private String ref_cpe_name;

	private String ref_vulnerable;

	private String ref_cpe23Uri;

	private String ref_versionStartIncluding;

	private String ref_versionEndIncluding;

	public CVERequest() {
		super();
	}

	public CVERequest(String id, String meta_data_id, String description_value, String ref_cpe_name,
			String ref_vulnerable, String ref_cpe23Uri, String ref_versionStartIncluding,
			String ref_versionEndIncluding) {
		super();
		this.id = id;
		this.meta_data_id = meta_data_id;
		this.description_value = description_value;
		this.ref_cpe_name = ref_cpe_name;
		this.ref_vulnerable = ref_vulnerable;
		this.ref_cpe23Uri = ref_cpe23Uri;
		this.ref_versionStartIncluding = ref_versionStartIncluding;
		this.ref_versionEndIncluding = ref_versionEndIncluding;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeta_data_id() {
		return meta_data_id;
	}

	public void setMeta_data_id(String meta_data_id) {
		this.meta_data_id = meta_data_id;
	}

	public String getDescription_value() {
		return description_value;
	}

	public void setDescription_value(String description_value) {
		this.description_value = description_value;
	}

	public String getRef_cpe_name() {
		return ref_cpe_name;
	}

	public void setRef_cpe_name(String ref_cpe_name) {
		this.ref_cpe_name = ref_cpe_name;
	}

	public String getRef_vulnerable() {
		return ref_vulnerable;
	}

	public void setRef_vulnerable(String ref_vulnerable) {
		this.ref_vulnerable = ref_vulnerable;
	}

	public String getRef_cpe23Uri() {
		return ref_cpe23Uri;
	}

	public void setRef_cpe23Uri(String ref_cpe23Uri) {
		this.ref_cpe23Uri = ref_cpe23Uri;
	}

	public String getRef_versionStartIncluding() {
		return ref_versionStartIncluding;
	}

	public void setRef_versionStartIncluding(String ref_versionStartIncluding) {
		this.ref_versionStartIncluding = ref_versionStartIncluding;
	}

	public String getRef_versionEndIncluding() {
		return ref_versionEndIncluding;
	}

	public void setRef_versionEndIncluding(String ref_versionEndIncluding) {
		this.ref_versionEndIncluding = ref_versionEndIncluding;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description_value, id, meta_data_id, ref_cpe23Uri, ref_cpe_name, ref_versionEndIncluding,
				ref_versionStartIncluding, ref_vulnerable);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CVERequest other = (CVERequest) obj;
		return Objects.equals(description_value, other.description_value) && Objects.equals(id, other.id)
				&& Objects.equals(meta_data_id, other.meta_data_id) && Objects.equals(ref_cpe23Uri, other.ref_cpe23Uri)
				&& Objects.equals(ref_cpe_name, other.ref_cpe_name)
				&& Objects.equals(ref_versionEndIncluding, other.ref_versionEndIncluding)
				&& Objects.equals(ref_versionStartIncluding, other.ref_versionStartIncluding)
				&& Objects.equals(ref_vulnerable, other.ref_vulnerable);
	}

	@Override
	public String toString() {
		return "CVERequest [id=" + id + ", meta_data_id=" + meta_data_id + ", description_value=" + description_value
				+ ", ref_cpe_name=" + ref_cpe_name + ", ref_vulnerable=" + ref_vulnerable + ", ref_cpe23Uri="
				+ ref_cpe23Uri + ", ref_versionStartIncluding=" + ref_versionStartIncluding
				+ ", ref_versionEndIncluding=" + ref_versionEndIncluding + "]";
	}

}
