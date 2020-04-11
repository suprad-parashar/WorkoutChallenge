package com.example.android.workoutchallenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.workoutchallenge.helper.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Main activity of the class.
 */
public class HomeActivity extends AppCompatActivity {

    //Define Workouts.
    final String[] workoutNames = {
            "Push Ups",
            "Abdominal Crunches",
            "Abs Workout",
            "Stretching",
            "Toe Touches",
            "Plank Row",
            "Dumbbell Curls",
            "Exercise Ball",
            "Yoga",
            "Seated Rows",
            "Inclined Barbell Rows",
            "Flat Barbell Rows",
            "Bench Press",
            "Decline Crunches"
    };
    final int[] workoutDurations = {
            180,
            120,
            120,
            240,
            240,
            180,
            180,
            180,
            300,
            90,
            90,
            90,
            90,
            120
    };
    final int[] workoutImages = {
            R.drawable.w1,
            R.drawable.w2,
            R.drawable.w3,
            R.drawable.w4,
            R.drawable.w5,
            R.drawable.w6,
            R.drawable.w7,
            R.drawable.w8,
            R.drawable.w9,
            R.drawable.w10,
            R.drawable.w11,
            R.drawable.w12,
            R.drawable.w13,
            R.drawable.w14
    };

    //Define Global Variables.
    final CardView[] cardViews = new CardView[5];
    final int[] cardResourceIds = {R.id.workout_1, R.id.workout_2, R.id.workout_3, R.id.workout_4, R.id.workout_5};
    final int[] workoutIndices = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //Initialise Variables.
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
        FloatingActionButton fab = findViewById(R.id.fab);

        //Add workout to the Workout list defined in Workout class.
        for (int i = 0; i < workoutNames.length; i++)
            Workout.addWorkout(workoutNames[i], workoutImages[i], workoutDurations[i]);

        //Initialise the different Workout Cards.
        for (int i = 0; i < 5; i++)
            cardViews[i] = findViewById(cardResourceIds[i]);

        //Define the workouts to the Cards.
        defineWorkouts();

        //Create a Swipe to Refresh.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                defineWorkouts();
                refreshLayout.setRefreshing(false);
            }
        });

        //Add functionality to the Floating Action Button by generating a dialog to ask the number of sets the user wants to do.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout layout = new RelativeLayout(HomeActivity.this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                RelativeLayout.LayoutParams setsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                setsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                final NumberPicker setsPicker = new NumberPicker(HomeActivity.this);
                setsPicker.setMaxValue(10);
                setsPicker.setMinValue(1);
                layout.setLayoutParams(params);
                layout.addView(setsPicker, setsParams);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Select Number of Sets");
                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HomeActivity.this, WorkoutActivity.class);
                        intent.putExtra("workouts", workoutIndices);
                        intent.putExtra("sets", setsPicker.getValue());
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * Define the workouts by choosing unique, Random workouts to the cards.
     */
    private void defineWorkouts() {
        ArrayList<Integer> done = new ArrayList<>();
        while (done.size() != 5) {
            int index = new Random().nextInt(workoutNames.length);
            if (!done.contains(index))
                done.add(index);
        }
        for (int i = 0; i < 5; i++) {
            TextView workoutName = cardViews[i].findViewById(R.id.workout_name);
            ImageView workoutImage = cardViews[i].findViewById(R.id.workout_image);
            TextView workoutDuration = cardViews[i].findViewById(R.id.workout_duration);
            int index = done.get(i);
            workoutIndices[i] = index;
            Workout workout = Workout.workouts.get(index);
            workoutName.setText(workout.getName());
            workoutImage.setImageResource(workout.getImage());
            String workoutDurationString = (workout.getDurationInSeconds() / 60) + " Min " + (workout.getDurationInSeconds() % 60) + " Sec";
            workoutDuration.setText(workoutDurationString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.creator_page) {
            startActivity(new Intent(HomeActivity.this, AboutDeveloper.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
