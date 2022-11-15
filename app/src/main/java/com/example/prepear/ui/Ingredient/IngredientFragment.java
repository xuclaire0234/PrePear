package com.example.prepear.ui.Ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.prepear.AddEditIngredientActivity;
import com.example.prepear.IngredientController;
import com.example.prepear.IngredientInStorage;
import com.example.prepear.IngredientStorageCustomList;
import com.example.prepear.R;
import com.example.prepear.databinding.FragmentIngredientBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
/**
 * This class is an Fragment Class for displaying the all in-storage ingredients with its detailed info on a ListView
 */
public class IngredientFragment extends Fragment {

    private FragmentIngredientBinding binding;
    int LAUNCH_ADD_INGREDIENT_ACTIVITY = 1;
    int LAUNCH_EDIT_INGREDIENT_ACTIVITY = 2;
    private ListView ingredientStorageList; // for displaying all added in-storage ingredients
    private ArrayAdapter<IngredientInStorage> ingredientStorageListAdapter;
    private ArrayList<IngredientInStorage> ingredientStorageDataList = new ArrayList<>(); // store in-storage ingredient entries
    // On below: a String array containing all sort by choices used on sort-by Spinner for user selection
    private String[] userSortChoices = {"                 ---- Select  ---- ",
            "description(ascending)", "description(descending)",
            "best before (oldest to newest)", "best before (newest to oldest)",
            "location(ascending)", "category(ascending)",
            "location(descending)", "category(descending)"};
    private String userSelectedSortChoice; // Store the current sort-by selection made by user
    private final String IN_STORAGE_INGREDIENTS_COLLECTION_NAME = "Ingredient Storage";
    private FirebaseFirestore dbForInStorageIngredients = FirebaseFirestore.getInstance();
    private CollectionReference inStorageIngredientCollection = dbForInStorageIngredients.collection("Ingredient Storage");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // On below: Grab the ListView object for use
        ingredientStorageList = view.findViewById(R.id.ingredients_in_storage_listview);
        // On below: initialize the used-defined ArrayAdapter for use
        ingredientStorageListAdapter = new IngredientStorageCustomList(this.getContext(),
                ingredientStorageDataList);
        // On below: build a connection between the in-storage ingredients data list and the ArrayAdapter
        ingredientStorageList.setAdapter(ingredientStorageListAdapter);

        // On below: grab the ingredient addition button for use
        final FloatingActionButton addInStorageIngredientButton = view.findViewById(R.id.add_ingredient_button);
        addInStorageIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below call activity for new in-storage ingredient
                Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
                intent.putExtra("Add or Edit", "1");
                startActivityForResult(intent, LAUNCH_ADD_INGREDIENT_ACTIVITY);
            }
        });

        ingredientStorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Grab the clicked item out of the ListView
                Object clickedItem = ingredientStorageList.getItemAtPosition(position);
                // Casting this clicked item to IngredientInStorage type from Object type
                IngredientInStorage clickedFood = (IngredientInStorage) clickedItem;
                // call activity to edit ingredient
                Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
                intent.putExtra("Add or Edit", "2");
                intent.putExtra("ingredientInStorage", clickedFood);
                intent.putExtra("index", ingredientStorageDataList.indexOf(clickedFood));
                startActivityForResult(intent, LAUNCH_EDIT_INGREDIENT_ACTIVITY);
            }
        });

        // On below: Sort-by Spinner Initialization
        final Spinner sortBySpinner = (Spinner) view.findViewById(R.id.sort_spinner); // grab the Sort-by Spinner
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IngredientController ingredientController = new IngredientController(ingredientStorageDataList);
                userSelectedSortChoice = userSortChoices[position]; // use position index to locate the sort-by selection the user made
                ingredientController.SortInStorageIngredients(userSelectedSortChoice); // based on the selection user just made, sort throughout
                ingredientStorageListAdapter.notifyDataSetChanged(); // notify for updating data after sorting completely
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // on below: initialize the ArrayAdapter
        ArrayAdapter adapterForSpinner = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, userSortChoices);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // On below: set the ArrayAdapter up with the Spinner
        sortBySpinner.setAdapter(adapterForSpinner);
        // On below: Continually updating
        inStorageIngredientCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ingredientStorageDataList.clear();
                        if (task.isSuccessful()) {
                            // On below part: continually updating and retrieve data from Ingredient Storage Collection to local after a update in database
                            // On below line: use for-loop traverse through every document(stores every in-storage ingredient detailed information)
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("description")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("bestBeforeDate")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("location")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("category")));
                                    Log.d(IN_STORAGE_INGREDIENTS_COLLECTION_NAME, String.valueOf(document.getData().get("amount")));

                                    // On below part: grab the stored values inside each document with its corresponding key
                                    String documentID = document.getId();
                                    String description = (String) document.getData().get("description");
                                    String bestBeforeDate = (String) document.getData().get("bestBeforeDate");
                                    String location = (String) document.getData().get("location");
                                    String unit = (String) document.getData().get("unit");
                                    String amount = String.valueOf(document.getData().get("amount"));
                                    String category = (String) document.getData().get("category");
                                    int iconCode = ((Long) document.getData().get("icon code")).intValue();

                                    // On below line: use retrieved data from document and build a new in-storage ingredient entry,
                                    // then add locally into the Ingredient Storage Data List
                                    ingredientStorageDataList.add(new IngredientInStorage(description, category,
                                            bestBeforeDate, location, amount, unit, documentID, iconCode));
                                    // Notifying the adapter to render any new data fetched from the cloud
                                    ingredientStorageListAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("This document", "onComplete: DNE! ");
                                }
                            }
                        }
                    }
                });

        // On below part: After retrieving all existing in-storage ingredients' data from DB to in-storage ingredient list,
        // sort all retrieved ingredients based on user's picked sort-by choice
        IngredientController ingredientController = new IngredientController(ingredientStorageDataList);
        ingredientController.SortInStorageIngredients(userSelectedSortChoice);
        ingredientStorageListAdapter.notifyDataSetChanged(); // for updating data in the ArrayAdapter
    }

    @Override
    /**
     * This method updates the ingredient list view after the AddEditIngredientActivity concludes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_INGREDIENT_ACTIVITY) { // if user adds a new ingredient
            if(resultCode == Activity.RESULT_OK){
                /** get the ingredient object from the AddEditIngredient activity and add it to the
                 *  ingredientStorageListAdapter
                 */
                IngredientInStorage ingredientToAdd = (IngredientInStorage) data.getSerializableExtra("IngredientToAdd");
                IngredientController ingredientController = new IngredientController(ingredientStorageDataList);
                ingredientController.addIngredient(ingredientToAdd);
                ingredientStorageListAdapter.notifyDataSetChanged();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // do nothing
            }
        }
        else if (requestCode == LAUNCH_EDIT_INGREDIENT_ACTIVITY){ // if user edits or deletes an ingredient
            if (resultCode == Activity.RESULT_OK){
                /* get the ingredient and its index in the ingredientStorageDataList */
                int index = (int) data.getSerializableExtra("index");
                IngredientInStorage ingredientToEdit = (IngredientInStorage) data.getSerializableExtra("IngredientToEdit");
                IngredientInStorage ingredientToDelete = (IngredientInStorage) data.getSerializableExtra("IngredientToDelete");
                IngredientController ingredientController = new IngredientController(ingredientStorageDataList);
                if (ingredientToEdit != null){
                    /* replace the edited ingredient with the old ingredient */
                    ingredientController.replaceIngredient(index, ingredientToEdit);
                    ingredientStorageListAdapter.notifyDataSetChanged();
                } else if (ingredientToDelete != null){
                    /* replace the ingredient to delete with the old ingredient and then remove it */
                    ingredientController.replaceIngredient(index, ingredientToDelete);
                    ingredientController.removeIngredient(ingredientToDelete);
                    ingredientStorageListAdapter.notifyDataSetChanged();

                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // do nothing
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}