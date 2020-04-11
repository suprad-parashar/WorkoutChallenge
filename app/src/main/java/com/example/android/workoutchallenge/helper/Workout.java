package com.example.android.workoutchallenge.helper;

import java.util.ArrayList;

/**
 * The workout class contains the Name, Image and the duration of the workout.
 */
public class Workout {

    /** Class Members. */
    private String name;
    private int image;
    private int durationInSeconds;

    /**
     * Inserts workout to the List of Workouts.
     * @param name Name of the workout.
     * @param image Resource ID of the Image of the workout.
     * @param durationInSeconds Duration of the workout in seconds.
     */
    public static void addWorkout(String name, int image, int durationInSeconds) {
        //Create Workout.
        Workout workout = new Workout();
        //Set Data
        workout.setName(name);
        workout.setImage(image);
        workout.setDurationInSeconds(durationInSeconds);
        //Add Workout to List.
        workouts.add(workout);
    }

    /** All the workouts added in a Static ArrayList for easy access. */
    public static ArrayList<Workout> workouts = new ArrayList<>();

    /**
     * Get Image.
     * @return Resource ID of the Image of the workout.
     */
    public int getImage() {
        return image;
    }

    /**
     * Set Image
     * @param image Image of the workout.
     */
    private void setImage(int image) {
        this.image = image;
    }

    /**
     * Get Duration of Workout in Seconds
     * @return Number of seconds of Workout.
     */
    public int getDurationInSeconds() {
        return durationInSeconds;
    }
    /**
     * Set Duration of Workout in Seconds
     * @param durationInSeconds Number of seconds of Workout.
     */
    private void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    /**
     * Get Name of Workout.
     * @return Name of Workout
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workout.
     * @param name Name of the workout.
     */
    public void setName(String name) {
        this.name = name;
    }
}
