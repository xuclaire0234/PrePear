/**
 * Classname: RecipeAddIngredientFragment
 * Version Information: 1.0.0
 * Date: 11/1/2022
 * Author: Jamie Lee
 * Copyright notice:
 */
package com.example.prepear;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * This class creates a fragment called RecipeAddIngredientFragment. This fragment allow user to
 * add the ingredient to certain recipe with its attributes: description, amount, unit, category.
 * This fragment could be directed from AddEditRecipeActivity.
 */
public class RecipeAddEditIngredientFragment extends DialogFragment {
    /* declare variables */
    private ArrayAdapter<CharSequence> unitSpinnerAdapter;
    private ArrayAdapter<CharSequence> categorySpinnerAdapter;
    private EditText descriptionText;
    private EditText amountText;
    private Spinner unitSpinner;
    private Spinner categorySpinner;
    private OnFragmentInteractionListener listener;

    // private final String IN_STORAGE_INGREDIENTS_COLLECTION_NAME = "Ingredient Storage";
    // private FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();
    // private CollectionReference inStorageIngredientCollection = dbForInStorageIngredients.collection("Ingredient Storage");

    /*
     * Database are defined and connected to collection with id "Recipes" here below
     */
    final String TAG = "Ingredient Storage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Ingredient Storage");

    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList;

    /**
     * This method defines an interface of methods that the AddEditRecipeActivity needs to implement
     * in order to respond to the user clicking Confirm buttons.
     *
     * @see AddEditRecipeActivity
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(IngredientInRecipe ingredient);

        void onDeletePressed(IngredientInRecipe ingredient);

        void onOkPressed(IngredientInRecipe ingredient);
    }

    /**
     * This method creates a new instance of RecipeAddIngredientFragment so user can add
     * the ingredient to certain recipe by clicking on it in the AddEditRecipe activity
     *
     * @param ingredient {@link IngredientInRecipe} that the user clicked on
     * @return fragment the newly created fragment
     */
    static RecipeAddEditIngredientFragment newInstance(IngredientInRecipe ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);
        RecipeAddEditIngredientFragment fragment = new RecipeAddEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method receives the context from AddEditRecipeActivity, checks if the context is of type
     * {@link OnFragmentInteractionListener} and if it is, it assigns
     * the variable listener to the context, otherwise it raises a runtime error
     *
     * @param context information about the current state of the app received from AddEditRecipeActivity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method creates the add ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     *
     * @param savedInstanceState {@link Bundle} that stores an ingredient {@link IngredientInRecipe} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /* connects views to its layout */
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredient_fragment, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_ingredient_fragments_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading);
        descriptionText = view.findViewById(R.id.description_edit_text);
        amountText = view.findViewById(R.id.ingredient_amount_edit_text);
        unitSpinner = view.findViewById(R.id.ingredient_unit_edit_text);
        categorySpinner = view.findViewById(R.id.ingredient_category_edit_text);

        /* set up the unit spinner */
        unitSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.units,
                android.R.layout.simple_spinner_item);
        unitSpinner.setAdapter(unitSpinnerAdapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* set up the category spinner */
        categorySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ingredient_categories,
                android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* return the added ingredient back to AddEditRecipeActivity */
        Bundle bundle = getArguments();
        if (bundle != null) {
            /* Editing existing ingredient */
            IngredientInRecipe ingredient = (IngredientInRecipe) bundle.getSerializable("ingredient");
            descriptionText.setText(ingredient.getBriefDescription());
            amountText.setText(String.valueOf(ingredient.getAmountString()));
            unitSpinner.setSelection(unitSpinnerAdapter.getPosition(ingredient.getUnit()));
            categorySpinner.setSelection(categorySpinnerAdapter.getPosition(ingredient.getIngredientCategory()));

            /* return the edited ingredient back to AddEditRecipeActivity */
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("Edit Ingredient");
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        /* set listener for the Delete button, delete the clicked Ingredient
                        and call onDeletePressed method to delete ingredient item
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String description = descriptionText.getText().toString();
                            String amount = amountText.getText().toString();
                            String unit = unitSpinner.getSelectedItem().toString();
                            String category = categorySpinner.getSelectedItem().toString();
                            listener.onDeletePressed(new IngredientInRecipe(description, amount, unit, category));
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /* set listener for the OK button, get user input,
                        and call onOkPressed method to edit ingredient item
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String description;
                            String amount;
                            String unit;
                            String category;
                            if (descriptionText.getText().toString().equals("")
                                    || amountText.getText().toString().equals("")
                                    || unitSpinner.getSelectedItem().toString().equals("")
                                    || categorySpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                description = descriptionText.getText().toString();
                                amount = amountText.getText().toString();
                                unit = unitSpinner.getSelectedItem().toString();
                                category = categorySpinner.getSelectedItem().toString();
                                ingredient.setBriefDescription(description);
                                ingredient.setAmountValue(Double.parseDouble(amount));
                                ingredient.setUnit(unit);
                                ingredient.setIngredientCategory(category);
                                listener.onOkPressed(new IngredientInRecipe(description, amount, unit, category));
                            }
                        }
                    }).create();
        } else {
            /* Adding new ingredient */
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
            title.setText("Add Ingredient");
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        /* set listener for the Confirm button, get user input,
                        and call oncConfirmPressed method to add ingredient item
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /* get user input
                             * and make sure that no text field is left empty
                             * */
                            String description;
                            String amount;
                            String unit;
                            String category;

                            if (descriptionText.getText().toString().equals("")
                                    || amountText.getText().toString().equals("")
                                    || unitSpinner.getSelectedItem().toString().equals("")
                                    || categorySpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getActivity().getApplicationContext(), "You did not enter full information.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                description = descriptionText.getText().toString();
                                
                                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                                            FirebaseFirestoreException error) {
                                        /*
                                         * Loop through all the documents in the collection named "Recipes"
                                         */
                                        String amount = amountText.getText().toString();
                                        String unit = unitSpinner.getSelectedItem().toString();
                                        String category = categorySpinner.getSelectedItem().toString();
                                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                            Log.d(TAG, String.valueOf(doc.getData().get("description"))); /* Set an error message */

                                            /*
                                             * Get description and category attributes
                                             */
                                            String descriptionIngredientInStorage = (String) doc.getData().get("description");
                                            String categoryIngredientInStorage = (String) doc.getData().get("category");

                                            /* if ingredient is also in Ingredient Storage, auto-fill category with existing attribute */
                                            if (descriptionIngredientInStorage.equals(description)) {
                                                category = categoryIngredientInStorage;
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Category has been auto-filled with information in Ingredient Storage",
                                                        Toast.LENGTH_LONG).show();
                                                listener.onConfirmPressed(new IngredientInRecipe(description, amount, unit, category));
                                                return;
                                            }
                                        }
                                        listener.onConfirmPressed(new IngredientInRecipe(description, amount, unit, category));
                                    }
                                });
                            }
                        }
                    }).create();
        }
    }
}