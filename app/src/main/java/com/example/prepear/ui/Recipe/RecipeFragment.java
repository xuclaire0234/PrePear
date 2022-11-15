package com.example.prepear.ui.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prepear.AddEditRecipeActivity;
import com.example.prepear.CustomRecipeList;
import com.example.prepear.IngredientInRecipe;
import com.example.prepear.R;
import com.example.prepear.Recipe;
import com.example.prepear.RecipeController;
import com.example.prepear.ViewRecipeActivity;
import com.example.prepear.databinding.FragmentRecipeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    ListView recipeList;
    CustomRecipeList recipeAdapter;
    RecipeController recipeDataList;
    Integer sortItemRecipe = 0;
    Integer recipePosition = -1;
    int LAUNCH_ADD_RECIPE_ACTIVITY = 1;
    int LAUNCH_VIEW_RECIPE_ACTIVITY = 2;
    final String[] sortItemSpinnerContent = {"  ","Title", "Preparation Time", "Number Of Serving", "Recipe Category"};
    Recipe newRecipe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * All variables to link to the layout elements are defined here below
         */
        final Spinner sortItemSpinner;
        final FloatingActionButton addRecipe;
        final ImageButton sortSequence;

        /*
         * All variables to link to the layout elements are linked to layout by id here below
         */
        sortItemSpinner = view.findViewById(R.id.sort_spinner);
        addRecipe = view.findViewById(R.id.add_recipe_button);
        recipeList = view.findViewById(R.id.recipe_listview);
        sortSequence = view.findViewById(R.id.sort_button);

        /*
         * Database are defined and connected to collection with id "Recipes" here below
         */
        final String TAG = "Recipes";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Recipes");

        /*
         * The arraylist and adapter for the recipes are assigned and linked to each other here
         * below
         */
        recipeDataList = new RecipeController();
        recipeAdapter = new CustomRecipeList(this.getContext(), recipeDataList);
        recipeList.setAdapter(recipeAdapter); /* Link the arraylist and adapter(controller) */

        /*
         * When the sort order button were pressed, the sort order should reverse
         */
        sortSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeDataList.reverseOrder();
                recipeAdapter.notifyDataSetChanged();
            }
        });

        /*
         * When the addRecipe button is clicked, a new activity will start, which is activity to add
         * recipe.
         */
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipePosition = -1;
                Intent intent = new Intent(getActivity(), AddEditRecipeActivity.class);
                intent.putExtra("calling activity", "1");
                startActivityForResult(intent, LAUNCH_ADD_RECIPE_ACTIVITY);
            }
        });

        /*
         * When item on the listview was clicked, a new activity will start, which is activity to
         * view the details of that Recipe
         */
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recipePosition = i;
                Recipe viewedRecipe = recipeAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), ViewRecipeActivity.class);
                intent.putExtra("viewed recipe", viewedRecipe);
                startActivityForResult(intent, LAUNCH_VIEW_RECIPE_ACTIVITY);
            }
        });

        /*
         * The spinner in the activity is set and linked to the string array which contains all the
         * possible sort values
         */
        sortItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    /* If it is not the first item in the spinner, which is a blank, is selected, the recipe
                     * will be sorted by the item selected */
                    sortItemRecipe = i - 1; /* get the index of the item the recipes should be sorted by */
                    recipeDataList.sortRecipe(sortItemRecipe); /* sort the recipes */
                    recipeAdapter.notifyDataSetChanged(); /* Notifying the adapter to render any new data fetched from the cloud */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, sortItemSpinnerContent);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortItemSpinner.setAdapter(ad);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                recipeDataList.clearAllRecipes(); /* Clear the old list */

                /*
                 * Loop through all the documents in the collection named "Recipes"
                 */
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Preparation Time"))); /* Set an
                    error message */

                    /*
                     * get the id of the document
                     */
                    String id = doc.getId();

                    /*
                     * Get all the attributes in each document
                     */
                    String title = (String) doc.getData().get("Title");
                    Number preparationTime = (Number) doc.getData().get("Preparation Time");
                    Number numberOfServings = (Number) doc.getData().get("Number of Servings");
                    String recipeCategory = (String) doc.getData().get("Recipe Category");
                    String comments = (String) doc.getData().get("Comments");
                    String imageURI = (String) doc.getData().get("Image URI");

                    /*
                     * Create a new object of type {Recipe} with all the attributes added
                     */
                    newRecipe = new Recipe(imageURI, title, preparationTime.intValue(),
                            numberOfServings.intValue(), recipeCategory, comments); /* initialize
                                                                                 a recipe object */
                    newRecipe.setId(id); /* set id of the recipe in database */

                    /*
                     * add the newly generated recipe item to the recipeDataList
                     */
                    recipeDataList.addRecipe(newRecipe);
                }

                /*
                 * Loop through all the documents and get collections named "Ingredient" which
                 * and get information of all the ingredients needed by one Recipe.
                 */
                for (int i = 0; i < recipeDataList.countRecipes(); i++) {
                    int indexOfRecipe = i;

                    /*
                     * Get information of all the ingredients needed by the recipe at certain index
                     */
                    db.collection("Recipes").document(recipeDataList.getRecipeAt(indexOfRecipe).getId()).collection("Ingredient")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    recipeDataList.getRecipeAt(indexOfRecipe).deleteAllIngredients();

                                    /*
                                     * Loop through all the documents in Ingredient collection
                                     */
                                    for (QueryDocumentSnapshot doc : value) {
                                        /*
                                         * get the id of the document
                                         */
                                        String id = doc.getId();

                                        /*
                                         * Get all the attributes in each document
                                         */
                                        String briefDescription = (String) doc.getData().get("Brief Description");
                                        Number amount = (Number) doc.getData().get("Amount");
                                        String unit = (String) doc.getData().get("Unit");
                                        String ingredientCategory = (String) doc.getData().get("Ingredient Category");

                                        /*
                                         * Create a new object of type {Ingredient} with all the attributes added
                                         */
                                        IngredientInRecipe NewIngredient = new IngredientInRecipe(briefDescription,amount.toString(),unit,ingredientCategory); /* initialize
                                                                                 a ingredient object */
                                        NewIngredient.setId(id); /* set id of the ingredient in database */

                                        /*
                                         * add the newly generated ingredient item to the recipe in the recipeDataList
                                         */
                                        recipeDataList.getRecipeAt(indexOfRecipe).addIngredientToRecipe(NewIngredient);
                                    }
                                }
                            });
                }

                recipeAdapter.notifyDataSetChanged(); /* Notifying the adapter to render any new data fetched from the cloud */
            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}