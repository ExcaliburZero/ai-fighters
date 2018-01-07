package com.ai.fighters;

import com.ai.fighters.sprites.FuzzyGNPlayer;
import com.ai.fighters.sprites.NNPlayer;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by chris on 1/5/18.
 */
public class FuzzyGeneticAlgorithm {
    private final int numChromosomes;
    private final FuzzyGNChromosome[] chromosomes;
    private final boolean[] chromosomesSelected;
    private final int[] scores;
    private int numSelected;
    private int numRecieved;
    private int generation;

    private final Random rand;

    public FuzzyGeneticAlgorithm(final int numChromosomes, final long seed) {
        rand = new Random(seed);

        this.numChromosomes = numChromosomes;
        chromosomes = new FuzzyGNChromosome[numChromosomes];
        chromosomesSelected = new boolean[numChromosomes];
        scores = new int[numChromosomes];

        numSelected = 0;
        numRecieved = 0;
        generation = 0;

        createInitialChromosomes();
    }

    private void createInitialChromosomes() {
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = FuzzyGNChromosome.random(rand);
        }
    }

    public FuzzyGNChromosome getRandomPlayer() {
        if (numSelected == numChromosomes) {
            return null;
        }

        numSelected++;

        for (; ; ) {
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

    public void recieveScore(final FuzzyGNPlayer chromosome, final int score) {
        numRecieved++;

        final int index = Arrays.asList(chromosomes).indexOf(chromosome.chromosome);
        scores[index] = score;

        if (numRecieved == numChromosomes) {
            newGeneration();
        }
    }

    private void newGeneration() {
        resetSelected();

        boolean first = true;
        for (final int score : scores) {
            System.out.println(generation + "," + score);
        }

        final int n = numChromosomes / 2;
        final int[] bestNIndicies = getBestNIndicies(n);

        final FuzzyGNChromosome[] chromosomesTemp = new FuzzyGNChromosome[n];
        for (int i = 0; i < n; i++) {
            chromosomesTemp[i] = chromosomes[bestNIndicies[i]];
        }

        for (int i = 0; i < n; i++) {
            chromosomes[i] = chromosomesTemp[i];
        }

        for (int i = n; i < chromosomes.length; i++) {
            chromosomes[i] = newChromosome(n);
        }

        ++generation;
    }

    private int[] getBestNIndicies(final int n) {
        final int[] bestIndices = new int[n];
        final int[] bestScores = new int[n];

        // Fill the best scores with very negative scores
        for (int i = 0; i < n; i++) {
            bestScores[i] = Integer.MIN_VALUE;
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

        return bestIndices;
    }

    private FuzzyGNChromosome newChromosome(final int n) {
        final FuzzyGNChromosome a = chromosomes[rand.nextInt(n)];
        final FuzzyGNChromosome b = chromosomes[rand.nextInt(n)];

        return a.breed(b, rand);
    }
}
