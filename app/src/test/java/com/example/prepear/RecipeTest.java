package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecipeTest {
    private CustomRecipeList recipeList;
    @BeforeEach
    private void mockCustomRecipeList() {
        // recipeList = new CustomRecipeList(".", mockRecipe());
        recipeList.add(mockRecipe());
    }
    @BeforeEach
    private Recipe mockRecipe() {
        Recipe recipe = new Recipe(".", "Tomato Soup", 30, 3, "food", "none");
        return recipe;
    }
    @Test
    @DisplayName("This function is for testing add")
    void testAdd() {
        assertEquals(1, recipeList.size());
        Recipe recipe = new Recipe(".", "Tomato Soup", 30, 3, "food", "none");
        recipeList.add(recipe);
        assertAll("Testing additions of second city",
                ()->assertEquals(2, recipeList.getCities().size()),
                ()->assertTrue(recipeList.getCities().contains(recipe))
        );
    }
}
