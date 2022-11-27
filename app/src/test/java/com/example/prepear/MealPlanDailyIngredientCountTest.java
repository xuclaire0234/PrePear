package com.example.prepear;

import com.example.prepear.ui.ShoppingList.MealPlanDailyIngredientCount;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class MealPlanDailyIngredientCountTest {
    private static User theUser;
    private MealPlanDailyIngredientCount mealPlanDailyIngredientCount;
    private FirebaseFirestore db;

    public MealPlanDailyIngredientCountTest (FirebaseFirestore firestore) {
        db = firestore;
        theUser = new User("user");
    }

    @BeforeEach
    private void addOneDayMealPlan() {
    }

    @Test
    @DisplayName("This function is for testing MealPlanDailyIngredientCount")
    void Test() {

        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
//        Mockito.when(mockFirestore.someMethodCallYouWantToMock()).thenReturn(something);

//        DatabaseInteractor interactor = new DatabaseInteractor(mockFirestore);
        HashMap<String, Object> data = new HashMap<>();
        data.put("document id", "0000-00-00");
        data.put("description", "IngredientTest");
        data.put("bestBeforeDate", "0000-00-01");
        data.put("location", "freezer");
        data.put("category", "Eggs, milk, and milk product");
        data.put("amount", 2);
        data.put("unit", "g");
        data.put("icon code",0);

        db
                .collection("Ingredient Storage")
                .document("0000-00-00")
                .set(data);


        HashMap<String, Object> recipeData = new HashMap<>();
        recipeData.put("Id", "0000-00-00");
        recipeData.put("Image URI", null);
        recipeData.put("Title", "RecipeTest");
        recipeData.put("Preparation Time", 12);
        recipeData.put("Number of Servings", 3);
        recipeData.put("Recipe Category", "Lunch");
        recipeData.put("Comments", null);

        db
                .collection("Recipes")
                .document("0000-00-00")
                .set(data);

        data = new HashMap<>();
        data.put("document id", "0000-00-00");
        data.put("description", "IngredientTestInRecipe");
        data.put("category", "Eggs, milk, and milk product");
        data.put("amount", 2);
        data.put("unit", "g");

        db
                .collection("Recipes")
                .document("0000-00-00")
                .collection("Ingredient")
                .document("0000-00-00")
                .set(data);

//        mealPlanDailyIngredientCount = new MealPlanDailyIngredientCount("0000-00-00");
    }
}
