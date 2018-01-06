package com.ai.fighters;

import com.ai.fighters.sprites.NNPlayer;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * Created by chris on 1/5/18.
 */
public class GeneticAlgorithm {
    private static final int NUM_WEIGHTS = 10;
    private static final int NUM_LAYERS = 2;

    private final int numChromosomes;
    private final float[][][] chromosomes;
    private final boolean[] chromosomesSelected;
    private final int[] scores;
    private int numSelected;
    private int numRecieved;

    private final Random rand;

    public GeneticAlgorithm(final int numChromosomes, final long seed) {
        rand = new Random(seed);

        this.numChromosomes = numChromosomes;
        chromosomes = new float[numChromosomes][NUM_LAYERS][NUM_WEIGHTS];
        chromosomesSelected = new boolean[numChromosomes];
        scores = new int[numChromosomes];

        numSelected = 0;
        numRecieved = 0;

        createInitialChromosomes();
    }

    private void createInitialChromosomes() {
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = 0; j < chromosomes[0].length; j++) {
                for (int k = 0; k < chromosomes[0][0].length; k++) {
                    chromosomes[i][j][k] = rand.nextFloat() * 2 - 1;
                }
            }
        }
    }

    public float[][] getRandomPlayer() {
        if (numSelected == numChromosomes) {
            return null;
        }

        numSelected++;

        for (;;) {
            final int index = rand.nextInt(numChromosomes);
            if (!chromosomesSelected[index]) {
                return chromosomes[index];
            }
        }
    }

    private void resetSelected() {
        numSelected = 0;
        numRecieved = 0;
        for (int i = 0; i < chromosomesSelected.length; i++) {
            chromosomesSelected[i] = false;
        }
    }

    public void recieveScore(final NNPlayer chromosome, final int score) {
        numRecieved++;

        final int index = Arrays.asList(chromosomes).indexOf(chromosome.getWeights());
        scores[index] = score;

        if (numRecieved == numChromosomes) {
            newGeneration();
        }
    }

    private void newGeneration() {
        resetSelected();

        final int n = numChromosomes / 2;
        final int[] bestNIndicies = getBestNIndicies(n);

        final float[][][] chromosomesTemp = new float[n][NUM_LAYERS][NUM_WEIGHTS];
        for (int i = 0; i < n; i++) {
            chromosomesTemp[i] = chromosomes[bestNIndicies[i]];
        }

        for (int i = 0; i < n; i++) {
            chromosomes[i] = chromosomesTemp[i];
        }

        for (int i = n; i < chromosomes.length; i++) {
            chromosomes[i] = newChromosome(n);
        }
    }

    private int[] getBestNIndicies(final int n) {
        final int[] bestIndices = new int[n];
        final int[] bestScores = new int[n];

        // Fill the best scores with very negative scores
        for (int i = 0; i < n; i++) {
            bestScores[i] = -9999999;
        }

        for (int i = 0; i < chromosomes.length; i++) {
            final int score = scores[i];

            for (int j = 0; j < bestIndices.length; j++) {
                if (bestScores[j] < score) {
                    // Shift all the lower scored indices 1 place down
                    for (int k = j; k < bestScores.length; k++) {
                        bestScores[k] = bestScores[j];
                        bestIndices[k] = bestIndices[j];
                    }
                    bestScores[j] = score;
                    bestIndices[j] = i;

                    break;
                }
            }
        }

        System.out.println("Best score: " + bestScores[0]);

        return bestIndices;
    }

    private float[][] newChromosome(final int n) {
        final float[][] a = chromosomes[rand.nextInt(n)];
        final float[][] b = chromosomes[rand.nextInt(n)];

        return breed(a, b);
    }

    private float[][] breed(final float[][] a, final float[][] b) {
        final float[][] newWeights = new float[2][NUM_WEIGHTS];
        for (int l = 0; l < NUM_LAYERS; l++) {
            for (int i = 0; i < a.length; i++) {
                final int r = rand.nextInt(3);
                if (r == 0) {
                    newWeights[l][i] = a[l][i];
                } else if (r == 1) {
                    newWeights[l][i] = b[l][i];
                } else {
                    newWeights[l][i] = rand.nextFloat() * 2 - 1;
                }
            }
        }

        return newWeights;
    }
}
