package com.ai.fighters.sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chris on 1/5/18.
 */
public class NNPlayer extends Player {
    public NNPlayer(final int number, final World world, final Vector2 position) {
        super(number, world, position);
    }

    @Override
    public Set<Command> onStep(GameState gs) {
        return new HashSet<Command>();
    }
}
