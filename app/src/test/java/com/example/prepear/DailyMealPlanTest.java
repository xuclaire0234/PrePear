/**
 * Class Name: DailyMealPlanTest
 * Version Information: Version 1.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */
package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class tests DailyMealPlan class
 */
public class DailyMealPlanTest {

    /**
     * This method creates an {@link DailyMealPlan} object to use it in the test methods
     */
    private DailyMealPlan createDailyMealPlan() {
        return new DailyMealPlan("2022-11-03");
    }

    private DailyMealPlan createDifferentMealPlan(){
        Meal mockMeal =  new Meal("Recipe",null,null);
        return new DailyMealPlan("2022-11-04", mockMeal);

    }
    private  Meal mockMeal(){
        return new Meal("Recipe",null,null);
    }

    @Test
    @DisplayName("This function is for testing Daily Meal Plan getters")
    /**
     * This method tests the DailyMealPlan Class getters
     */
    void testGetters() {
        /* check that daily meal plan getters return the correct attributes */
        DailyMealPlan dailyMealPlan = createDailyMealPlan();
        assertEquals(dailyMealPlan.getCurrentDailyMealPlanDate(), "2022-11-03");
        assertEquals(dailyMealPlan.getDailyMealDataList().size(), 0);
        DailyMealPlan dailyMealPlan2 = createDifferentMealPlan();
        assertEquals(dailyMealPlan2.getDailyMealDataList().size(),1);
    }

    @Test
    @DisplayName("This function is for testing Daily Meal Plan setters")
    /**
     * This method tests the DailyMealPlan Class setters
     */
    void testSetters() {
        /* set attributes of daily meal plan to new values */
        DailyMealPlan dailyMealPlan = createDailyMealPlan();
        dailyMealPlan.setCurrentDailyMealPlanDate("2000-10-01");
        ArrayList<Meal> mockDataList = new ArrayList<>();
        Meal mockMeal = mockMeal();
        mockDataList.add(mockMeal);
        dailyMealPlan.setDailyMealDataList(mockDataList);
        /* check that the attributes of the daily meal plan got updated*/
        assertEquals(dailyMealPlan.getCurrentDailyMealPlanDate(), "2000-10-01");
        assertEquals(dailyMealPlan.getDailyMealDataList().size(), 1);
    }
}