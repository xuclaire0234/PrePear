package com.example.prepear;

import java.io.Serializable;

public class Meal implements Serializable {
    private String documentID;
    private Integer customizedScalingNumber;
    private String mealType;

    public Meal(String documentID, Integer customizedScalingNumber, String mealType) {
        this.documentID = documentID;
        this.customizedScalingNumber = customizedScalingNumber;
        this.mealType = mealType;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Integer getCustomizedScalingNumber() {
        return customizedScalingNumber;
    }

    public void setCustomizedScalingNumber(Integer customizedScalingNumber) {
        this.customizedScalingNumber = customizedScalingNumber;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
