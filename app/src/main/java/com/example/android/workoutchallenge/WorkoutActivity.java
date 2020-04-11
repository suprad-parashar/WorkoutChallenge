package com.example.android.workoutchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.android.workoutchallenge.helper.Workout;
import com.example.android.workoutchallenge.ui.workout.FinishFragment;
import com.example.android.workoutchallenge.ui.workout.StartFragment;

import java.util.Locale;

/**
 * The Workout Activity which consists of three Fragments - Start, Workout and Finish.
 */
public class WorkoutActivity extends AppCompatActivity {

    //Initialise the Variables.
    static int[] indices;
    static int numberOfSets;
    static int workoutNumber;
    public static CountDownTimer timer = null;
    public static MediaPlayer mediaPlayer = null;
    private final static int[] music = {
            R.raw.a1,
            R.raw.a2,
            R.raw.a3,
            R.raw.a4,
            R.raw.a5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);

        //Initialise Variables.
        indices = getIntent().getIntArrayExtra("workouts");
        numberOfSets = getIntent().getIntExtra("sets", 1);
        workoutNumber = 0;

        //Display the Start Fragment.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance(false))
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed() {
        if (isLastWorkout()) {
            startActivity(new Intent(WorkoutActivity.this, HomeActivity.class));
            finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkoutActivity.this);
            dialog.setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to quit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (WorkoutActivity.timer != null)
                                WorkoutActivity.timer.cancel();
                            WorkoutActivity.timer = null;
                            if (WorkoutActivity.mediaPlayer != null)
                                mediaPlayer.stop();
                            mediaPlayer = null;
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, FinishFragment.newInstance()).commitNow();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.create().show();
        }
    }

    /**
     * End the workout session.
     */
    public static void endWorkout() {
        numberOfSets = 0;
    }

    /**
     * Get music for workout.
     *
     * @return Resource ID of the Audio File.
     */
    public static int getMusic() {
        return music[workoutNumber];
    }

    /**
     * Get the total workout time in form of X Mins and Y secs.
     *
     * @return Time String.
     */
    public static String getTotalWorkoutString() {
        int total = 0;
        for (int i = 0; i < 5; i++)
            total += Workout.workouts.get(indices[i]).getDurationInSeconds();
        return total / 60 + " Mins and " + String.format(Locale.getDefault(), "%02d", total % 60) + " Secs";
    }

    /**
     * Obtain the workout to be done next.
     *
     * @return The Workout object.
     */
    public static Workout getWorkout() {
        return Workout.workouts.get(indices[workoutNumber]);
    }

    /**
     * Check if it is the last workout of the session.
     *
     * @return True if last else False.
     */
    public static boolean isLastWorkout() {
        return numberOfSets == 0;
    }

    /**
     * Finish a workout.
     */
    public static void finishWorkout() {
        workoutNumber += 1;
        if (workoutNumber == 5) {
            workoutNumber = 0;
            numberOfSets -= 1;
        }
    }
}
