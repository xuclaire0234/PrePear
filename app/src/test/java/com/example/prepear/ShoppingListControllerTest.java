package com.example.prepear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.prepear.ui.ShoppingList.ShoppingListController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ShoppingListControllerTest {
    private ShoppingListController shoppingListController;

    @BeforeEach
    private void mockShoppingList() {
        shoppingListController = new ShoppingListController();
    }

    private IngredientInRecipe mockIngredient1() {
        return new IngredientInRecipe("ingredient1","10.0","kg","milk");
    }

    private IngredientInRecipe mockIngredient2() {
        return new IngredientInRecipe("ingredient2","10.0","kg","egg");
    }

    @Test
    @DisplayName("This function is for testing add")
    void addIngredientTest() {
        assertEquals(0, shoppingListController.getIngredients().size());

        IngredientInRecipe ingredient1 = mockIngredient1();
        shoppingListController.add(ingredient1);
        assertEquals(1, shoppingListController.getIngredients().size());

        IngredientInRecipe ingredient2 = mockIngredient1();
        shoppingListController.add(ingredient2);
        assertEquals(2, shoppingListController.getIngredients().size());

        shoppingListController.add(ingredient2);
        assertEquals(2, shoppingListController.getIngredients().size());
    }

    @Test
    @DisplayName("This function is for testing clear")
    void clearTest() {
        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();
        assertEquals(0, shoppingListController.getIngredients().size());

        shoppingListController.clear();
        assertEquals(0, shoppingListController.getIngredients().size());

        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);
        assertEquals(2, shoppingListController.getIngredients().size());

        shoppingListController.clear();
        assertEquals(0, shoppingListController.getIngredients().size());
        assertFalse(shoppingListController.getIngredients().contains(ingredient1));
        assertFalse(shoppingListController.getIngredients().contains(ingredient2));
    }

    @Test
    @DisplayName("This function is for testing count")
    void countTest() {
        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());

        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);

        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());
        shoppingListController.clear();
        assertEquals(shoppingListController.countIngredients(), shoppingListController.getIngredients().size());
    }

    @Test
    @DisplayName("This function is for testing get")
    void getTest() {
        ArrayList<IngredientInRecipe> ingredientList;
        ingredientList = shoppingListController.getIngredients();
        assertEquals(0,ingredientList.size());

        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        ingredientList = shoppingListController.getIngredients();
        assertTrue(ingredientList.contains(ingredient));

        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);
        ingredientList = shoppingListController.getIngredients();
        assertTrue(ingredientList.contains(ingredient));

        shoppingListController.clear();
        ingredientList = shoppingListController.getIngredients();
        assertEquals(0,ingredientList.size());
        assertFalse(ingredientList.contains(ingredient));
    }

    @Test
    @DisplayName("This function is for testing get ingredient by index")
    void getIndexTest() {
        IngredientInRecipe ingredient = mockIngredient1();
        shoppingListController.add(ingredient);
        assertEquals(1,shoppingListController.countIngredients());

        assertTrue(ingredient.equals(shoppingListController.getIngredientAt(0)));

        ingredient = mockIngredient2();
        shoppingListController.add(ingredient);
        assertTrue(ingredient.equals(shoppingListController.getIngredientAt(1)));
        assertEquals(shoppingListController.getIngredientAt(12),null);

        shoppingListController.clear();
        assertFalse(ingredient.equals(shoppingListController.getIngredientAt(0)));
        assertEquals(shoppingListController.getIngredientAt(12),null);
    }

    @Test
    @DisplayName("This function is for testing test delete")
    void deleteTest() {
        assertEquals(0,shoppingListController.countIngredients());

        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient2);
        assertEquals(1,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(0,shoppingListController.countIngredients());

        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);
        assertEquals(2,shoppingListController.countIngredients());

        assertTrue(shoppingListController.getIngredients().contains(ingredient1));
        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());
        assertFalse(shoppingListController.getIngredients().contains(ingredient1));


        shoppingListController.deleteIngredient(ingredient1);
        assertEquals(1,shoppingListController.countIngredients());

        assertTrue(shoppingListController.getIngredients().contains(ingredient2));
        shoppingListController.deleteIngredient(ingredient2);
        assertFalse(shoppingListController.getIngredients().contains(ingredient2));

        assertEquals(0,shoppingListController.countIngredients());
        shoppingListController.deleteIngredient(ingredient2);
        assertEquals(0,shoppingListController.countIngredients());
    }

    @Test
    @DisplayName("This function is for testing test sort")
    void TestSort() {
        assertEquals(0,shoppingListController.countIngredients());
        shoppingListController.sortIngredient(0);

        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        shoppingListController.add(ingredient2);

        shoppingListController.sortIngredient(1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);


        shoppingListController.sortIngredient(0);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);

        shoppingListController.sortIngredient(3);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
    }

    @Test
    @DisplayName("This function is for testing test reverse")
    void testReverse() {
        IngredientInRecipe ingredient1 = mockIngredient1();
        IngredientInRecipe ingredient2 = mockIngredient2();

        shoppingListController.add(ingredient1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);


        shoppingListController.add(ingredient2);

        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);

        shoppingListController.sortIngredient(0);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);
        shoppingListController.reverseOrder();

        shoppingListController.sortIngredient(1);
        assertEquals(shoppingListController.getIngredientAt(0), ingredient2);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient1);
        shoppingListController.reverseOrder();
        assertEquals(shoppingListController.getIngredientAt(0), ingredient1);
        assertEquals(shoppingListController.getIngredientAt(1), ingredient2);

    }

}
