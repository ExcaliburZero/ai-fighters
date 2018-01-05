package com.ai.fighters.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by chris on 1/5/18.
 */
public abstract class Player extends Sprite {
    public void turnLeft() {
        // TODO(chris): Implement
    }

    public void turnRight() {
        // TODO(chris): Implement
    }

    public void moveForward() {
        // TODO(chris): Implement
    }

    public void shoot() {
        // TODO(chris): Implement
    }

    public abstract void onStep(final GameState gs);
}
