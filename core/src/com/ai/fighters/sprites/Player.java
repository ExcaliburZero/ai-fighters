package com.ai.fighters.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by chris on 1/5/18.
 */
public abstract class Player extends Sprite {
    private static final int RADIUS = 5;

    private final int number;
    private final World world;
    public final Body body;

    public Player(final int number, final World world, final Vector2 position) {
        super(new Texture("player" + number + ".png"));

        this.number = number;
        this.world = world;
        body = defineBody(position);
    }

    private Body defineBody(final Vector2 position) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;

        final Body body = world.createBody(bdef);

        final FixtureDef fdef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        fdef.shape = shape;
        body.createFixture(fdef);

        return body;
    }

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

    public void update(final float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public abstract void onStep(final GameState gs);
}
