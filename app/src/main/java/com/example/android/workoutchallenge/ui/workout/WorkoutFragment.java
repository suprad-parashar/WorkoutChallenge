package com.example.android.workoutchallenge.ui.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.workoutchallenge.R;
import com.example.android.workoutchallenge.WorkoutActivity;
import com.example.android.workoutchallenge.helper.Workout;

import java.util.Locale;
import java.util.Objects;

public class WorkoutFragment extends Fragment {

    /**
     * Initialise Global Variables
     */
    private TextView workoutDuration;

    /**
     * Obtain a new Instance of the Fragment.
     *
     * @return A new Instance of the Fragment.
     */
    static WorkoutFragment newInstance() {
        return new WorkoutFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Hide the Toolbar.
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        //Initialise Variables.
        TextView workoutName = view.findViewById(R.id.workout_name_display);
        ImageView workoutImage = view.findViewById(R.id.workout_image_display);
        workoutDuration = view.findViewById(R.id.count_down_display);
        final Workout workout = WorkoutActivity.getWorkout();
        WorkoutActivity.mediaPlayer = MediaPlayer.create(getContext(), WorkoutActivity.getMusic());

        //Set up View.
        workoutName.setText(workout.getName());
        workoutImage.setImageResource(workout.getImage());
        WorkoutActivity.mediaPlayer.start();

        //Timer to break.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
        WorkoutActivity.timer = new CountDownTimer(workout.getDurationInSeconds() * 1000, 1000) {
            int time = workout.getDurationInSeconds();

            @Override
            public void onTick(long millisUntilFinished) {
                String timeRemaining = (time / 60) + ":" + String.format(Locale.getDefault(), "%02d", time % 60);
                workoutDuration.setText(timeRemaining);
                time--;
                if (!WorkoutActivity.mediaPlayer.isPlaying())
                    WorkoutActivity.mediaPlayer.start();
            }

            @Override
            public void onFinish() {
                WorkoutActivity.mediaPlayer.stop();
                WorkoutActivity.mediaPlayer = null;
                WorkoutActivity.finishWorkout();
                assert getFragmentManager() != null;
                if (WorkoutActivity.isLastWorkout())
                    getFragmentManager().beginTransaction().replace(R.id.container, FinishFragment.newInstance()).commitNow();
                else
                    getFragmentManager().beginTransaction().replace(R.id.container, StartFragment.newInstance(true)).commitNow();
            }
        };
        WorkoutActivity.timer.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.workout_fragment, container, false);
    }
}
