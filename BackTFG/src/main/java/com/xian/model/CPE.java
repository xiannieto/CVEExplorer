package com.xian.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CPE {

    public final String[] ATTRIBUTE_NAMES = {"part", "vendor", "product", "version",
        "update", "edition", "language", "sw_edition", "target_sw",
        "target_hw", "other"};

    private String cpe23;
    private String title;

    private String vendor;
    private String product;
    private String version;
    private String language;
    private String targetSW;
    private String targetHW;

    public CPE() {
    }

    public CPE(String cpe23) {
        this.cpe23 = cpe23;
        loadCPE23Attributes(cpe23);
    }

    public CPE(String cpe23, String title) {
        this.cpe23 = cpe23;
        loadCPE23Attributes(cpe23);
        this.title = title;
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

        //  cpe:2.3:a:microsoft:internet_explorer:8.0.6001:beta:*:*:*:*:*:*
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCpe23() {
        return cpe23;
    }

    public String getTitle() {
        return title;
    }

    public String getVendor() {
        return vendor;
    }

    public String getProduct() {
        return product;
    }

    public String getLanguage() {
        return language;
    }

    public String getTargetSW() {
        return targetSW;
    }

    public String getTargetHW() {
        return targetHW;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.cpe23);
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
        final CPE other = (CPE) obj;
        if (!Objects.equals(this.cpe23, other.cpe23)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CPE{" + "cpe23=" + cpe23 + ", title=" + title + ", vendor=" + vendor + ", product=" + product + ", version=" + version + ", language=" + language + ", targetSW=" + targetSW + ", targetHW=" + targetHW + '}';
    }

    
    
}