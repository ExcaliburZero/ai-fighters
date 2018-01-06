package com.ai.fighters.sprites;

import com.ai.fighters.AIFighters;
import com.ai.fighters.screens.PlayScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chris on 1/5/18.
 */
public class NNPlayer extends Player {
    private static final int NUM_OUTPUTS = 4;
    private static final float THRESHOLD = 0.5f;

    private float[][] weights;

    public NNPlayer(final PlayScreen play, final ArrayList<Bullet> bullets, final int number, final World world, final Vector2 position,
                    final float[][] weights) {
        super(play, bullets, number, world, position);

        this.weights = weights;
    }

    @Override
    public Set<Command> onStep(GameState gs) {
        final Set<Command> commands = new HashSet<Command>();

        final float[] inputs = stateToInputs(gs);
        final boolean[] nnOutputs = runNN(inputs);
        if (nnOutputs[0]) {
            commands.add(Command.LEFT);
        }
        if (nnOutputs[1]) {
            commands.add(Command.RIGHT);
        }
        if (nnOutputs[2]) {
            commands.add(Command.SHOOT);
        }
        if (nnOutputs[3]) {
            commands.add(Command.FOREWARD);
        }

        return commands;
    }

    private float[] stateToInputs(final GameState state) {
        final float[] inputs = new float[]{
                normalize(state.playerX, AIFighters.WIDTH),
                normalize(state.playerY, AIFighters.HEIGHT),
                normalize(state.playerRotation),
                normalize(state.enemyX, AIFighters.WIDTH),
                normalize(state.enemyY, AIFighters.HEIGHT),
                normalize(state.enemyRotation),
                normalize(state.enemyBulletX, AIFighters.WIDTH),
                normalize(state.enemyBulletY, AIFighters.HEIGHT),
                normalize(state.playerReload, Player.SHOOT_INTERVAL),
                normalize(state.enemyReload, Player.SHOOT_INTERVAL)
        };

        return inputs;
    }

    @Override
    public String toString() {
        return "NN Player " + number;
    }

    private boolean[] runNN(final float[] inputs) {
        //System.out.print("weights: ");
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                //System.out.print(weights[i][j] + " ");
            }
        }
        //System.out.println("");

        //System.out.print("inputs: ");
        for (int i = 0; i < inputs.length; i++) {
            //System.out.print(inputs[i] + " ");
        }
        //System.out.println("");

        //System.out.print("hidden: ");

        final float[] hiddenValues = new float[weights[0].length];
        for (int i = 0; i < weights[0].length; i++) {
            float sum = 0;
            for (int j = 0; j < inputs.length; j++) {
                sum += weights[0][i] * inputs[j];
            }
            hiddenValues[i] = normalize(sum / inputs.length);
            //System.out.print(hiddenValues[i] + " ");
        }

        //System.out.println("");

        final float[] outputValues = new float[NUM_OUTPUTS];
        for (int i = 0; i < outputValues.length; i++) {
            float sum = 0;
            for (int j = 0; j < hiddenValues.length; j++) {
                sum += weights[1][i] * hiddenValues[j];
            }
            outputValues[i] = normalize(sum / hiddenValues.length);
        }

        //System.out.print("Final: ");
        final boolean[] outputs = new boolean[outputValues.length];
        for (int i = 0; i < outputs.length; i++) {
            //System.out.print(outputValues[i] + "\t");
            if (outputValues[i] > THRESHOLD) {
                outputs[i] = true;
            }
        }
        //System.out.println("");

        return outputs;
    }

    private float normalize(final float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    private float normalize(final float x, final float range) {
        return (float) (1 / (1 + Math.exp(-x / range)));
    }

    public float[][] getWeights() {
        return weights;
    }
}
