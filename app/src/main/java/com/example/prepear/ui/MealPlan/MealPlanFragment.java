package com.example.prepear.ui.MealPlan;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.prepear.AddEditIngredientFragment;
import com.example.prepear.AddMealPlanActivity;
import com.example.prepear.ConfirmationDialog;
import com.example.prepear.DailyMealPlan;
import com.example.prepear.DatabaseController;
import com.example.prepear.DeleteMealPlanDialog;
import com.example.prepear.IngredientInStorage;
import com.example.prepear.Meal;
import com.example.prepear.MealPlanController;
import com.example.prepear.MealPlanCustomList;
import com.example.prepear.R;
import com.example.prepear.ViewDailyMealPlanActivity;
import com.example.prepear.ViewMealPlanActivity;
import com.example.prepear.databinding.FragmentMealPlanBinding;
import com.example.prepear.ui.Ingredient.IngredientFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class MealPlanFragment extends Fragment implements DeleteMealPlanDialog.OnFragmentInteractionListener {

    private MealPlanViewModel mViewModel;
    private FragmentMealPlanBinding binding;
    private ListView mealPlanList; // for displaying all added meal plans
    private ArrayAdapter<DailyMealPlan> mealPlanAdapter;
    private ArrayList<DailyMealPlan> mealPlanDataList = new ArrayList<>(); // store meal plan entries
    private final int LAUNCH_ADD_MEAL_PLAN_ACTIVITY = 1;
    private int positionOfPlanToRemove;
    private DatabaseController databaseController;
    // On below: class constructor
    public static MealPlanFragment newInstance() {
        return new MealPlanFragment();
    }
    private FirebaseFirestore dbMealPlanPart = FirebaseFirestore.getInstance();
    private CollectionReference dailyMealPlansCollection = dbMealPlanPart.collection("Daily Meal Plans");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // On below: Grab the ListView object for use
        mealPlanList = view.findViewById(R.id.meal_plan_listview);
        // On below: initialize the used-defined ArrayAdapter for use
        mealPlanAdapter = new MealPlanCustomList(this.getContext(), mealPlanDataList);
        // On below: build a connection between the meal plan data list and the ArrayAdapter
        mealPlanList.setAdapter(mealPlanAdapter);
        dailyMealPlansCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mealPlanDataList.clear();
                        if (task.isSuccessful()) {
                            // On below part: continually updating and retrieve data from "Daily Meal Plans" Collection to local after a update in database
                            // On below line: use for-loop traverse through every document(stores every daily meal plan detailed information)
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    // On below part: grab the stored values inside each document with its corresponding key
                                    String currentDailyMealPlanDate = document.getId();

                                    // On below line: use retrieved data from document and build a new daily meal plan entry,
                                    // then add locally into the Meal Plan Data List
                                    mealPlanDataList.add(new DailyMealPlan(currentDailyMealPlanDate));
                                    // Notifying the adapter to render any new data fetched from the cloud
                                } else {
                                    Log.d("This document", "onComplete: DNE! ");
                                }
                            }
                            for (DailyMealPlan dailyMealPlan: mealPlanDataList) {
                                dailyMealPlansCollection
                                        .document(dailyMealPlan.getCurrentDailyMealPlanDate())
                                        .collection("Meals")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                dailyMealPlan.emptyDailyMealDataList();
                                                for (QueryDocumentSnapshot documentSnapshot: value) {
                                                    String documentID = documentSnapshot.getId();
                                                    String mealType = (String) documentSnapshot.getData().get("Meal Type");
                                                    if (Objects.equals(mealType, "IngredientInStorage")) {
                                                        double customizedMealAmount = (double) documentSnapshot.getData().get("Customized Scaling Number");
                                                        Meal currentMeal = new Meal(mealType,documentID);
                                                        currentMeal.setCustomizedAmount(customizedMealAmount);
                                                        dailyMealPlan.getDailyMealDataList().add(currentMeal);
                                                    } else if (Objects.equals(mealType, "Recipe")) {
                                                        int customizedMealNumberOfServings = ((Long)documentSnapshot.getData().get("Customized Scaling Number")).intValue();
                                                        Meal currentMeal = new Meal(mealType,documentID);
                                                        currentMeal.setCustomizedNumberOfServings(customizedMealNumberOfServings);
                                                        dailyMealPlan.getDailyMealDataList().add(currentMeal);
                                                    }
                                                }
                                                mealPlanAdapter.notifyDataSetChanged();
                                            }

                                        });

                            }
                        }
                    }
                });
//        for (DailyMealPlan dailyMealPlan: mealPlanDataList) {
//            dailyMealPlansCollection
//                    .document(dailyMealPlan.getCurrentDailyMealPlanDate())
//                    .collection("Meals")
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            dailyMealPlan.emptyDailyMealDataList();
//                            for (QueryDocumentSnapshot documentSnapshot: value) {
//                                String documentID = documentSnapshot.getId();
//                                String mealType = (String) documentSnapshot.getData().get("Meal Type");
//                                if (Objects.equals(mealType, "IngredientInStorage")) {
//                                    double customizedMealAmount = (double) documentSnapshot.getData().get("Customized Scaling Number");
//                                    Meal currentMeal = new Meal(mealType,documentID);
//                                    currentMeal.setCustomizedAmount(customizedMealAmount);
//                                    dailyMealPlan.getDailyMealDataList().add(currentMeal);
//                                } else if (Objects.equals(mealType, "Recipe")) {
//                                    int customizedMealNumberOfServings = (int) documentSnapshot.getData().get("Customized Scaling Number");
//                                    Meal currentMeal = new Meal(mealType,documentID);
//                                    currentMeal.setCustomizedNumberOfServings(customizedMealNumberOfServings);
//                                    dailyMealPlan.getDailyMealDataList().add(currentMeal);
//                                }
//                            }
//                        }
//                    });
//
//        }
        mealPlanAdapter.notifyDataSetChanged();
        databaseController = new DatabaseController();
        // On below: grab the ingredient addition button for use
        final FloatingActionButton addMealPlanButton = view.findViewById(R.id.add_meal_plan_button);
        addMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below call activity for new in-storage ingredient
                Intent intent = new Intent(getActivity(), AddMealPlanActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_MEAL_PLAN_ACTIVITY);
            }
        });

        mealPlanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Grab the clicked item out of the ListView
                Object clickedItem = mealPlanList.getItemAtPosition(position);
                // Casting this clicked item to IngredientInStorage type from Object type
                DailyMealPlan clickedDailyMealPlan= (DailyMealPlan) clickedItem;
                // call activity to edit ingredient
                Intent intent = new Intent(getActivity(), ViewDailyMealPlanActivity.class);
                intent.putExtra("selected daily meal plan", clickedDailyMealPlan);
                startActivity(intent);
            }
        });
        mealPlanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positionOfPlanToRemove = position;
                DialogFragment deletePlan = new DeleteMealPlanDialog();
                deletePlan.setTargetFragment(MealPlanFragment.this, 0);
                deletePlan.show(getFragmentManager(), "Attention");
                return false;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MealPlanViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_MEAL_PLAN_ACTIVITY){
            MealPlanController mealPlanController = new MealPlanController(mealPlanDataList);
            if (resultCode == Activity.RESULT_OK){
                int counter =  Integer.parseInt(data.getSerializableExtra("counter").toString()); // counter = num of days = num of meals initially added
                int size = mealPlanController.getSize();
                for (int i = 1; i <= counter; i++){ // for each daily meal plan
                    DailyMealPlan currentDailyMealPlan = (DailyMealPlan) data.getSerializableExtra("meal" + i);
//                    databaseController.addDailyMealPlanToMealPlan(MealPlanFragment.newInstance().getContext(), currentDailyMealPlan);
                    if (mealPlanController.getSize() == 0){
                        databaseController.addDailyMealPlanToMealPlan(getContext(), currentDailyMealPlan);
                        mealPlanController.addMealPlan(currentDailyMealPlan);
                        mealPlanAdapter.notifyDataSetChanged();
                    }else {
                        for (int j = 0; j < size; j++) {// compare the new meal plan date with the existing ones
                            // On below line: // if the new meal date matches any one of the existing meal plan date
                            if (mealPlanController.getMealPlan(j).getCurrentDailyMealPlanDate().matches(currentDailyMealPlan.getCurrentDailyMealPlanDate())) {
                                DailyMealPlan duplicateDay = mealPlanController.getMealPlan(j);
                                for (int k = 0; k < duplicateDay.getDailyMealDataList().size(); k++){
                                    // On below line: if there is a matching date, compare the documentID of each meal plan with the document ID of the new meal plan
                                    if (duplicateDay.getDailyMealDataList().get(k).getDocumentID().matches(currentDailyMealPlan.getDailyMealDataList().get(0).getDocumentID())
                                            && currentDailyMealPlan.getDailyMealDataList().get(0).getMealType().matches("IngredientInStorage")){
                                        // if document ID matches, and the daily meal plan type is ingredient, add the amounts into one
                                        double initialScalingNumber = duplicateDay.getDailyMealDataList().get(k).getCustomizedAmount();
                                        mealPlanController.getMealPlan(j).getDailyMealDataList().get(k).setCustomizedAmount(initialScalingNumber +
                                                currentDailyMealPlan.getDailyMealDataList().get(0).getCustomizedAmount());
                                        databaseController.addEditMealToDailyMealPlan(getContext(),
                                                duplicateDay, duplicateDay.getDailyMealDataList().get(k));
                                        currentDailyMealPlan = null; // set daily meal plan to null (use this to check if its added to the list later)
                                        break;
                                    }
                                }
                                if (currentDailyMealPlan != null){
                                    // if daily meal plan isn't added to the list either because the document ID don't match or because the meal type is recipe
                                    mealPlanController.getMealPlan(j).getDailyMealDataList().add(currentDailyMealPlan.getDailyMealDataList().get(0));
                                    // the meal plan date matches an existing date, so add meal plan to the array list of the existing meal plan
                                    currentDailyMealPlan = null;
                                    break;
                                }
                            }
                        }
                        if (currentDailyMealPlan != null){ // if the meal plan date does not match any date in the list
                            databaseController.addDailyMealPlanToMealPlan(getContext(), currentDailyMealPlan);
                            mealPlanController.addMealPlan(currentDailyMealPlan);
                            mealPlanAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }



    @Override
    public void onYesPressed() {
        MealPlanController mealPlanController = new MealPlanController(mealPlanDataList);
        mealPlanController.removeMealPlan(positionOfPlanToRemove);
        databaseController.deleteDailyMealPlanFromMealPlan(getContext(),mealPlanDataList.get(positionOfPlanToRemove));
        mealPlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoPressed() {
        // do nothing
    }

    @Override
    public void onResume() {
        super.onResume();
        Collections.sort(mealPlanDataList, new Comparator<DailyMealPlan>() {
            @Override
            public int compare(DailyMealPlan mealPlan1, DailyMealPlan mealPlan2) {
                return mealPlan1.getCurrentDailyMealPlanDate().compareTo(mealPlan2.getCurrentDailyMealPlanDate());
            }
        });

    }
}