package com.example.prepear.ui.MealPlan;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.prepear.AddEditIngredientFragment;
import com.example.prepear.AddMealPlanActivity;
import com.example.prepear.DailyMealPlan;
import com.example.prepear.IngredientInStorage;
import com.example.prepear.Meal;
import com.example.prepear.MealPlanCustomList;
import com.example.prepear.R;
import com.example.prepear.ViewDailyMealPlanActivity;
import com.example.prepear.ViewMealPlanActivity;
import com.example.prepear.databinding.FragmentMealPlanBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MealPlanFragment extends Fragment {

    private MealPlanViewModel mViewModel;
    private FragmentMealPlanBinding binding;


    private ListView mealPlanList; // for displaying all added meal plans
    private ArrayAdapter<DailyMealPlan> mealPlanAdapter;
    private ArrayList<DailyMealPlan> mealPlanDataList = new ArrayList<DailyMealPlan>(); // store meal plan entries
    private int LAUNCH_ADD_MEAL_PLAN_ACTIVITY = 1;

    public static MealPlanFragment newInstance() {
        return new MealPlanFragment();
    }

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
                DailyMealPlan clickedFood= (DailyMealPlan) clickedItem;
                // call activity to edit ingredient
                Intent intent = new Intent(getActivity(), ViewDailyMealPlanActivity.class);
                startActivity(intent);
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
            if (resultCode == Activity.RESULT_OK){
                Integer counter = (Integer) Integer.valueOf(data.getSerializableExtra("counter").toString());
                for (int i = 1; i <= counter; i++){
                    DailyMealPlan mealToAdd = (DailyMealPlan) data.getSerializableExtra("meal"+i);
                    mealPlanAdapter.add(mealToAdd);
                }
            }else{
                // do nothing
            }

        }

        }
    }
