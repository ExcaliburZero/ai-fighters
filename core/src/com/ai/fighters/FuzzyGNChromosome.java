package com.ai.fighters;

import java.util.Random;

/**
 * Created by chris on 1/6/18.
 */
public class FuzzyGNChromosome {
    public static final int NUM_VALUES = 9;

    private static final float AIMING_ANGLE_EPSILON_MAX = 0.1f;//(float) (2 * Math.PI);
    private static final float AIMING_ANGLE_EPSILON_MIN = 0.01f;//0;

    private static final float DODGE_THRESHOLD_MAX = 150f + 1f;//1000f;
    private static final float DODGE_THRESHOLD_MIN = 150f;//0f;

    private static final float DODGE_ANGLE_THRESHOLD_MAX = (float) (Math.PI / 5) + 0.001f;//(float) Math.PI;
    private static final float DODGE_ANGLE_THRESHOLD_MIN = (float) (Math.PI / 5);//0.01f;

    private static final float DODGE_HIT_RANGE_MAX = 32f + 1;//100f;
    private static final float DODGE_HIT_RANGE_MIN = 32f;//0f;

    private static final float HIT_EST_DETAIL_MAX = 1.0f;
    private static final float HIT_EST_DETAIL_MIN = 0.05f;

    private static final float HIT_EST_LENGTH_MAX = 100f;
    private static final float HIT_EST_LENGTH_MIN = 1f;

    private static final float DESIRED_DISTANCE_MAX = 400f;
    private static final float DESIRED_DISTANCE_MIN = 0f;

    private static final float DESIRED_DISTANCE_RANGE_MAX = 400f;
    private static final float DESIRED_DISTANCE_RANGE_MIN = 10f;

    private static final float REPOSITION_ANGLE_EPSILON_MAX = (float) (Math.PI / 4);
    private static final float REPOSITION_ANGLE_EPSILON_MIN = 0.05f;

    public final float AIMING_ANGLE_EPSILON;
    public final float DODGE_THRESHOLD;
    public final float DODGE_ANGLE_THRESHOLD;
    public final float DODGE_HIT_RANGE;
    public final float HIT_EST_DETAIL;
    public final float HIT_EST_LENGTH;
    public final float DESIRED_DISTANCE;
    public final float DESIRED_DISTANCE_RANGE;
    public final float REPOSITION_ANGLE_EPSILON;

    public final float[] values;

    public FuzzyGNChromosome(final float[] values) {
        if (values.length != NUM_VALUES) {
            throw new IllegalArgumentException("Values array did not have the correct number of values. Expected " +
                    NUM_VALUES + ", Actual " + values.length);
        }

        this.values = values;

        this.AIMING_ANGLE_EPSILON = values[0];
        this.DODGE_THRESHOLD = values[1];
        this.DODGE_ANGLE_THRESHOLD = values[2];
        this.DODGE_HIT_RANGE = values[3];
        this.HIT_EST_DETAIL = values[4];
        this.HIT_EST_LENGTH = values[5];
        this.DESIRED_DISTANCE = values[6];
        this.DESIRED_DISTANCE_RANGE = values[7];
        this.REPOSITION_ANGLE_EPSILON = values[8];
    }

    public static FuzzyGNChromosome random(final Random rand) {
        return new FuzzyGNChromosome(new float[]{
                randInRange(rand, AIMING_ANGLE_EPSILON_MAX, AIMING_ANGLE_EPSILON_MIN),
                randInRange(rand, DODGE_THRESHOLD_MAX, DODGE_THRESHOLD_MIN),
                randInRange(rand, DODGE_ANGLE_THRESHOLD_MAX, DODGE_ANGLE_THRESHOLD_MIN),
                randInRange(rand, DODGE_HIT_RANGE_MAX, DODGE_HIT_RANGE_MIN),
                randInRange(rand, HIT_EST_DETAIL_MAX, HIT_EST_DETAIL_MIN),
                randInRange(rand, HIT_EST_LENGTH_MAX, HIT_EST_LENGTH_MIN),
                randInRange(rand, DESIRED_DISTANCE_MAX, DESIRED_DISTANCE_MIN),
                randInRange(rand, DESIRED_DISTANCE_RANGE_MAX, DESIRED_DISTANCE_RANGE_MIN),
                randInRange(rand, REPOSITION_ANGLE_EPSILON_MAX, REPOSITION_ANGLE_EPSILON_MIN)
        });
    }

    private static float randInRange(final Random rand, final float max, final float min) {
        return rand.nextFloat() * (max - min) + min;
    }

    @Override
    public String toString() {
        return "AIMING_ANGLE_EPSILON: " + AIMING_ANGLE_EPSILON + "\n" +
                "DODGE_THRESHOLD: " + DODGE_THRESHOLD + "\n" +
                "DODGE_ANGLE_THRESHOLD: " + DODGE_ANGLE_THRESHOLD + "\n" +
                "DODGE_HIT_RANGE: " + DODGE_HIT_RANGE + "\n" +
                "HIT_EST_DETAIL: " + HIT_EST_DETAIL + "\n" +
                "HIT_EST_LENGTH: " + HIT_EST_LENGTH + "\n" +
                "DESIRED_DISTANCE: " + DESIRED_DISTANCE + "\n" +
                "DESIRED_DISTANCE_RANGE: " + DESIRED_DISTANCE_RANGE + "\n" +
                "REPOSITION_ANGLE_EPSILON: " + REPOSITION_ANGLE_EPSILON + "\n";
    }

    public FuzzyGNChromosome breed(final FuzzyGNChromosome other, final Random rand) {
        final float[] newValues = new float[FuzzyGNChromosome.NUM_VALUES];
        for (int i = 0; i < values.length; i++) {
            final int r = rand.nextInt(3);
            if (r == 0) {
                newValues[i] = values[i];
            } else if (r == 1) {
                newValues[i] = other.values[i];
            } else {
                newValues[i] = rand.nextFloat() * 2 - 1;
            }
        }

        return new FuzzyGNChromosome(newValues);
    }
}
