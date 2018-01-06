package com.ai.fighters.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by chris on 1/5/18.
 */
public class Bullet extends Sprite {
    private static final String BULLET_IMG = "bullet.png";

    private final Player shooter;
    private final Vector2 position;
    private final Vector2 velocity;

    public Bullet(final Player shooter, final Vector2 position, final Vector2 velocity) {
        super(new Texture(BULLET_IMG));

        position.add(+getWidth(), +getHeight());

        this.shooter = shooter;
        this.velocity = velocity;
        this.position = position;
    }

    public void update(final float dt) {
        velocity.scl(dt);
        position.add(velocity);
        velocity.scl(1 / dt);

        setPosition(getCenter().x, getCenter().y);
    }

    private Vector2 getCenter() {
        return new Vector2(position.x - getWidth() / 2, position.y - getHeight() / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, getWidth(), getHeight());
    }

    public Player getShooter() {
        return shooter;
    }
}
