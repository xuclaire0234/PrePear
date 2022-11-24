/**
 * Class Name: ConfirmationDialog
 * Version Information: Version 1.0
 * Date: Nov 19th, 2022
 * Author: Marafi Mergani
 * Copyright Notice:
 */

package com.example.prepear;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import javax.security.auth.callback.Callback;

/**
 * This class creates a confirmation dialog to confirm if the user want to add a recipe or
 * ingredient as a meal plan
 */
public class ConfirmationDialog extends DialogFragment {
    private OnFragmentInteractionListener listener;

    /**
     * This method defines an interface of methods that the IngredientFragment needs to implement
     * in order to respond to the user clicking Confirm, and Cancel buttons.
     *
     * @see LoginActivity
     */
    public interface OnFragmentInteractionListener {
        /* list of methods implemented in IngredientFragment class
         */
        void onConfirmPressed();

        void onCancelPressed();
    }

    /**
     * This method is called when the fragment is created, to check if the parent fragment implements
     * the fragment interface OnFragmentInteractionListener
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    /**
     * This method creates the confirmation fragment when the user clicks on an ingredient or a recipe
     * @param savedInstanceState a {@link Bundle} a Bundle that provides a previous state for the fragment
     *                                            to re-construct it
     * @return builder a {@link Dialog} object to build the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.confirmation_dialog, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(
                R.layout.add_ingredient_custom_title, null);
        TextView title = titleView.findViewById(R.id.exemptionSubHeading4);

        // create a dialog builder and set the title
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setCustomTitle(titleView);
        title.setText("Confirmation");
        return builder
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelPressed(); // call interface method
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirmPressed(); // call interface method
                    }
                }).create();


    }
}