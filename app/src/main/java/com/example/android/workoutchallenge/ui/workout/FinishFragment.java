package com.example.android.workoutchallenge.ui.workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.workoutchallenge.HomeActivity;
import com.example.android.workoutchallenge.R;
import com.example.android.workoutchallenge.WorkoutActivity;

import java.util.Objects;

/**
 * Finish Screen of the Workout Activity.
 */
public class FinishFragment extends Fragment {

    /**
     * Obtain a new Instance of FinishFragment.
     *
     * @return A new Instance of FinishFragment.
     */
    public static FinishFragment newInstance() {
        return new FinishFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Create Variables to access Views.
        TextView durationView = view.findViewById(R.id.workout_duration_total);
        Button back = view.findViewById(R.id.dashboard);

        //Set message to the Duration View.
        String message;
        if (WorkoutActivity.isLastWorkout())
            message = "Congratulations! You worked out for " + WorkoutActivity.getTotalWorkoutString();
        else
            message = "You did not complete your workout!";
        durationView.setText(message);

        //End workout Session.
        WorkoutActivity.endWorkout();

        //Set an onclick Listener to the button which redirects to the Home Screen.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish, container, false);
    }
}