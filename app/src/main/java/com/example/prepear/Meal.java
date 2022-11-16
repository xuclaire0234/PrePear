
package com.example.prepear;

import java.io.Serializable;

/*
* This class creates Meal objects and allows to initialize, set and access the attributes
* */
public class Meal implements Serializable {
    // On below part: initialize Meal class attributes
    private String mealType; // represents the meal type, either an in-storage ingredient or a dish cooked based on a recipe
    private String documentID; // contains the same Document ID as its corresponding in-storage ingredient / recipe has inside database
    private Integer customizedScalingNumber; // represents user-entered number of servings / customized amount

    /**
     * @param mealType passed-in String represents this meal's type
     * @param documentID passed-in String contains the document id
     * @param customizedScalingNumber passed-in integer value represents the number of servings / amount of this meal
     */
    public Meal(String mealType, String documentID, Integer customizedScalingNumber) {
        this.mealType = mealType;
        this.documentID = documentID;
        this.customizedScalingNumber = customizedScalingNumber;
    }

    // On below part: Getter and Setter methods

    /**
     * @return
     */
    public String getMealType() {
        return this.mealType;
    }

    /**
     * @param mealType
     */
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    /**
     * @return
     */
    public String getDocumentID() {
        return this.documentID;
    }

    /**
     * @param documentID
     */
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    /**
     * @return
     */
    public Integer getCustomizedScalingNumber() {
        return this.customizedScalingNumber;
    }

    /**
     * @param customizedScalingNumber
     */
    public void setCustomizedScalingNumber(Integer customizedScalingNumber) {
        this.customizedScalingNumber = customizedScalingNumber;
    }


}
