package com.xian.dto;

public class ResultPair {
    public String cveID;
    public Double score;
    
    public ResultPair() {
    }
    
    public ResultPair(String cveID, Double score) {
        this.cveID = cveID;
        this.score = score;
    }

    public String getCveID() {
        return cveID;
    }

    public void setCveID(String cveID) {
        this.cveID = cveID;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ResultPair [cveID=" + cveID + ", score=" + score + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cveID == null) ? 0 : cveID.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        return result;
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
        if (cveID == null) {
            if (other.cveID != null)
                return false;
        } else if (!cveID.equals(other.cveID))
            return false;
        if (score == null) {
            if (other.score != null)
                return false;
        } else if (!score.equals(other.score))
            return false;
        return true;
    }

    
}
