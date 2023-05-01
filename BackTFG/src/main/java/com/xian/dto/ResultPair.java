package com.xian.dto;

import java.util.Objects;

public class ResultPair {
    public String cveID;
    public float score;
    
    public ResultPair() {
    }
    
    public ResultPair(String cveID, float score) {
        this.cveID = cveID;
        this.score = score;
    }

    public String getCveID() {
        return cveID;
    }

    public void setCveID(String cveID) {
        this.cveID = cveID;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ResultPair [cveID=" + cveID + ", score=" + score + "]";
    }

	@Override
	public int hashCode() {
		return Objects.hash(cveID, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultPair other = (ResultPair) obj;
		return Objects.equals(cveID, other.cveID) && Float.floatToIntBits(score) == Float.floatToIntBits(other.score);
	}

    
}
