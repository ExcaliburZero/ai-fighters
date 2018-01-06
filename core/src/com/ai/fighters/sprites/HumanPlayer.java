package com.ai.fighters.sprites;

import com.ai.fighters.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chris on 1/5/18.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(final PlayScreen play, final ArrayList<Bullet> bullets, final int number, final World world, final Vector2 position) {
        super(play, bullets, number, world, position);
    }

    @Override
    public Set<Command> onStep(GameState gs) {
        final Set<Command> commands = new HashSet<Command>();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            commands.add(Command.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            commands.add(Command.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            commands.add(Command.FOREWARD);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            commands.add(Command.SHOOT);
        }

        return commands;
    }

    @Override
    public String toString() {
        return "Human Player " + number;
    }
}
