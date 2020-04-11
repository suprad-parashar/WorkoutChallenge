package com.example.android.workoutchallenge.ui.workout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.workoutchallenge.HomeActivity;
import com.example.android.workoutchallenge.R;
import com.example.android.workoutchallenge.WorkoutActivity;

import java.util.Objects;

public class StartFragment extends Fragment {

    /**
     * Class variables.
     */
    private static boolean isBreak;
    private int time;

    /**
     * Sets if it is a break and returns a new Instance of the Fragment.
     *
     * @param isBreakValue A boolean which tells if it is a break.
     * @return A new instance of the Fragment.
     */
    public static StartFragment newInstance(boolean isBreakValue) {
        isBreak = isBreakValue;
        return new StartFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Hide the Toolbar.
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        //Initialise Variables.
        Button cancel = view.findViewById(R.id.cancel_button);
        WorkoutActivity.mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);
        final TextView timer = view.findViewById(R.id.count_down);
        final TextView breakView = view.findViewById(R.id.break_text_view);

        //Set time and Change other texts based on if it is a break.
        if (!isBreak) {
            time = 3;
            breakView.setVisibility(View.GONE);
        } else {
            time = 40;
            breakView.setVisibility(View.VISIBLE);
            cancel.setText(getString(R.string.chicken_out));
        }
        timer.setText(String.valueOf(time));

        //Timer to next Workout.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
        WorkoutActivity.timer = new CountDownTimer(time * 1000, 1000) {
            int counter = time;

            @Override
            public void onTick(long millisUntilFinished) {
                //Play a beep tone.
                if (counter <= 3)
                    WorkoutActivity.mediaPlayer.start();
                timer.setText(String.valueOf(counter));
                counter--;
            }

            @Override
            public void onFinish() {
                //Go to the workout Fragment.
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, WorkoutFragment.newInstance())
                        .commitNow();
            }
        };
        WorkoutActivity.timer.start();

        //Set functionality to the cancel Button.
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBreak) {
                    //Create a dialog and ask if they want to exit.
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Confirm Exit")
                            .setMessage("Are you sure you want to quit?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WorkoutActivity.timer.cancel();
                                    WorkoutActivity.timer = null;
                                    WorkoutActivity.mediaPlayer.stop();
                                    WorkoutActivity.mediaPlayer = null;
                                    assert getFragmentManager() != null;
                                    getFragmentManager().beginTransaction().replace(R.id.container, FinishFragment.newInstance()).commitNow();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.create().show();
                } else {
                    WorkoutActivity.timer.cancel();
                    WorkoutActivity.timer = null;
                    WorkoutActivity.mediaPlayer.stop();
                    WorkoutActivity.mediaPlayer = null;
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }
}
