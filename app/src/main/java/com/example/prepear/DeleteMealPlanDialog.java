package com.example.prepear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteMealPlanDialog extends DialogFragment {
    private DeleteMealPlanDialog.OnFragmentInteractionListener listener;


    /**
     * This method defines an interface of methods that the MainActivity needs to implement
     * in order to respond to the user clicking OK, DELETE, and CANCEL buttons.
     *
     * @see LoginActivity
     */
    public interface OnFragmentInteractionListener {
        /* list of methods implemented in main activity class
         */
        void onYesPressed();
        void onNoPressed();
    }

    /**
     * This method receives the context from MainActivity, checks if the context is of type
     * {@link AddEditIngredientFragment.OnFragmentInteractionListener} and if it is, it assigns
     * the variable listener to the context, otherwise it raises a runtime error
     *
     * //@param context information about the current state of the app received from MainActivity
     */
    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmationDialog.OnFragmentInteractionListener) {
            listener = (ConfirmationDialog.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            listener = (DeleteMealPlanDialog.OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    /**
     * This method creates the add/edit ingredient fragment if the user input is valid
     * and sets errors if the input is invalid
     *
     * @param savedInstanceState an object of type {@link Bundle} that stores an
     *                           {@link IngredientInStorage} object
     * @return builder a {@link Dialog} object to build the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.delete_meal_plan, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(
                R.layout.add_ingredient_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading4);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Confirmation");
        return builder
                .setView(view)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNoPressed();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesPressed();
                    }
                }).create();


    }

}