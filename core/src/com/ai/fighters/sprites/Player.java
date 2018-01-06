package com.ai.fighters.sprites;

import com.ai.fighters.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by chris on 1/5/18.
 */
public abstract class Player extends Sprite {
    private static final float TURN_SPEED = 3.0f;
    private static final float MOVE_SPEED = 80.0f;
    private static final int RADIUS = 5;
    private static final float BULLET_SPEED = 80.0f;
    public static final float SHOOT_INTERVAL = 2.0f;

    public enum Command {LEFT, RIGHT, FOREWARD, SHOOT}

    protected final int number;
    private final World world;
    public final Body body;

    private float reloadTime;
    private final ArrayList<Bullet> bullets;
    private final PlayScreen play;

    public Player(final PlayScreen play, final ArrayList<Bullet> bullets, final int number, final World world, final Vector2 position) {
        super(new Texture("player" + number + ".png"));

        this.play = play;
        this.bullets = bullets;
        this.number = number;
        this.world = world;
        body = defineBody(position);
        reloadTime = 0.0f;
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

    private void turnLeft() {
        //System.out.println("Left");
        body.setAngularVelocity(TURN_SPEED);
    }

    private void turnRight() {
        //System.out.println("Right");
        body.setAngularVelocity(-TURN_SPEED);
    }

    private void moveForward() {
        final float forceX = (float) Math.cos(body.getAngle()) * MOVE_SPEED;
        final float forceY = (float) Math.sin(body.getAngle()) * MOVE_SPEED;

        //System.out.println("Forward");
        body.setLinearVelocity(forceX, forceY);
    }

    private void shoot() {
        // TODO(chris): Implement
        if (reloadTime <= 0.0f) {
            //System.out.println("Shoot!");

            final float velX = (float) Math.cos(body.getAngle()) * BULLET_SPEED;
            final float velY = (float) Math.sin(body.getAngle()) * BULLET_SPEED;

            final Vector2 velocity = new Vector2(velX, velY);

            bullets.add(new Bullet(this, getCenter(), velocity));

            reloadTime = SHOOT_INTERVAL;
        }
    }

    public void update(final float dt) {
        setPosition(getCenter().x, getCenter().y);

        // TODO(chris): Add the actual game state
        final GameState state = play.getState(this);

        final Set<Command> commands = onStep(state);
        for (final Command c : commands) {
            if (c == Command.LEFT) {
                turnLeft();
            } else if (c == Command.RIGHT) {
                turnRight();
            } else if (c == Command.FOREWARD) {
                moveForward();
            } else if (c == Command.SHOOT) {
                shoot();
            }
        }

        if (!(commands.contains(Command.LEFT) || commands.contains(Command.RIGHT))) {
            body.setAngularVelocity(0);
        }
        if (!commands.contains(Command.FOREWARD)) {
            body.setLinearVelocity(0, 0);
        }

        if (reloadTime > 0.0f) {
            reloadTime -= dt;
        }
    }

    private Vector2 getCenter() {
        return new Vector2(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(body.getPosition().x, body.getPosition().y, getWidth(), getHeight());
    }

    public abstract Set<Command> onStep(final GameState gs);

    public Body getBody() {
        return body;
    }

    public float getReloadTime() {
        return reloadTime;
    }
}
