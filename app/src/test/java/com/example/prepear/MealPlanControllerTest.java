/**
 * Classname: MealPlanControllerTest
 * Version Information: 1.0.0
 * Date: Nov 25, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class test the meal plan controller class
 */
public class MealPlanControllerTest {
    private MealPlanController mealPlanController;
    private ArrayList<DailyMealPlan> mockDataList = new ArrayList<>();

    /**
     * Before each run, initialize an MealPlanController
     */
    @BeforeEach
    private void mockMealPlanController() {
        mealPlanController = new MealPlanController(mockDataList);
    }

    /**
     * This function returns a mock daily meal plan
     * @return the return is of type {@link DailyMealPlan}
     */
    private DailyMealPlan mockDailyMealPlan() {
        return new DailyMealPlan("2022-11-03");
    }


    @Test
    @DisplayName("This function is for testing get meal plan")
    void getMealPlanTest(){
        // check if the controller is empty now
        assertEquals(0, mealPlanController.getSize());
        // add a meal plan
        DailyMealPlan mealPlan = mockDailyMealPlan();
        mealPlanController.addMealPlan(mealPlan);
        // test method
        assertEquals(mealPlanController.getMealPlan(0), mealPlan);
        assertEquals(1, mealPlanController.getSize());
    }

    /**
     * Testing add meal plan to meal plan controller
     */
    @Test
    @DisplayName("This function is for testing add")
    void addMealPlanTest() {
        /* check if the controller is empty now */
        assertEquals(0, mealPlanController.getSize());

        /* check add meal plan to controller */
        DailyMealPlan newMealPlan = mockDailyMealPlan();
        mealPlanController.addMealPlan(newMealPlan);
        assertEquals(1,mealPlanController.getSize()); // check the length of controller
        assertTrue(mealPlanController.getDailyMealPlans().contains(newMealPlan)); // check if the controller contains
    }

    /**
     * Testing remove meal plan in meal plan controller
     */
    @Test
    @DisplayName("This function is for testing delete")
    void removeIngredientTest() {
        // add an meal plan to the controller
        DailyMealPlan newMealPlan = mockDailyMealPlan();
        mealPlanController.addMealPlan(newMealPlan);
        assertEquals(1,mealPlanController.getSize()); // check the length of controller
        assertTrue(mealPlanController.getDailyMealPlans().contains(newMealPlan)); // check if the controller contains

        // test to delete an ingredient from the controller
        mealPlanController.removeMealPlan(0);
        assertEquals(0,mealPlanController.getSize()); // check the length of controller
        assertFalse(mealPlanController.getDailyMealPlans().contains(newMealPlan)); // check if the controller contains
    }

    /**
     * Testing get meal plans from the controller
     */
    @Test
    @DisplayName("This function is for testing getting meal plan list")
    void getMealPlansTest() {
        // add an meal plan to the controller
        DailyMealPlan newMealPlan = mockDailyMealPlan();
        mealPlanController.addMealPlan(newMealPlan);
        assertEquals(1,mealPlanController.getSize()); // check the length of controller
        // check that the returned array list contains the same meal plan
        assertTrue(mealPlanController.getDailyMealPlans().contains(newMealPlan));
        mealPlanController.removeMealPlan(0);
        assertEquals(0, mealPlanController.getDailyMealPlans().size());

    }

    /**
     * Testing get size of the controller
     */
    @Test
    @DisplayName("This function is for testing getting the size of the meal plan ")
    void getSizeTest() {
        // size should be 0 initially
        assertEquals(0,mealPlanController.getSize()); // check the length of controller
        // add a meal plan and check the size
        DailyMealPlan newMealPlan = mockDailyMealPlan();
        mealPlanController.addMealPlan(newMealPlan);
        assertEquals(1,mealPlanController.getSize());
    }

}