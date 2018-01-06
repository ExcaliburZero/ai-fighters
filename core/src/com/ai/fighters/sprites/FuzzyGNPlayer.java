package com.ai.fighters.sprites;

import com.ai.fighters.screens.PlayScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by chris on 1/6/18.
 */
public class FuzzyGNPlayer extends Player {
    private static final float AIMING_ANGLE_EPSILON = 0.03f;
    private static final float DODGE_THRESHOLD = 150f;
    private static final double DODGE_ANGLE_THRESHOLD = Math.PI / 5;
    private static final double DODGE_HIT_RANGE = 32f;
    private static final double HIT_EST_DETAIL = 0.1f;
    private static final int HIT_EST_LENGTH = 10;
    private static final float DESIRED_DISTANCE = 100f;
    private static final float DESIRED_DISTANCE_RANGE = 50f;
    private static final float RESPOITION_ANGLE_EPSILON = 0.1f;

    public FuzzyGNPlayer(final PlayScreen play, final ArrayList<Bullet> bullets, final int number, final World world,
                         final Vector2 position) {
        super(play, bullets, number, world, position);
    }

    @Override
    public Set<Command> onStep(final GameState gs) {
        final Set<Command> commands = new HashSet<>();

        final double enemyBulletDistance = getDistanceToEnemyBullet(gs);

        System.out.println("Bullet: " + enemyBulletDistance);
        System.out.println("WillCollide: " + willCollide(gs));
        if (willCollide(gs) && enemyBulletDistance < DODGE_THRESHOLD) {
            dodge(gs, commands);
        } else {
            if (Math.abs(getDistanceToEnemy(gs) - DESIRED_DISTANCE) > DESIRED_DISTANCE_RANGE) {
                reposition(gs, commands);
            } else {
                aimAndShoot(gs, commands);
            }
        }

        System.out.println(commands);

        return commands;
    }

    private void dodge(final GameState gs, final Set<Command> commands) {
        final double angleToEnemyBullet = getAngleToEnemyBullet(gs);
        final double da = Math.abs(angleToEnemyBullet - gs.playerRotation);
        final double daSign = angleToEnemyBullet - gs.playerRotation;

        if (da < DODGE_ANGLE_THRESHOLD || da > (Math.PI) - DODGE_ANGLE_THRESHOLD) {
            if (daSign < 0) {
                commands.add(Command.LEFT);
            } else {
                commands.add(Command.RIGHT);
            }
        } else {
            commands.add(Command.FOREWARD);
        }
    }

    private void reposition(final GameState gs, final Set<Command> commands) {
        final double angleToEnemy = getAngleToEnemy(gs);

        System.out.println("PlayerX: " + gs.playerX + "\tPlayerY: " + gs.playerY);
        System.out.println("EnemyX: " + gs.enemyX + "\tEnemyY: " + gs.enemyY);
        System.out.println("Rotation: " + gs.playerRotation + "\tDesired: " + angleToEnemy);

        if (Math.abs(angleToEnemy - gs.playerRotation) < RESPOITION_ANGLE_EPSILON) {
            commands.add(Command.SHOOT);
        } else {
            final double da = angleToEnemy - gs.playerRotation;
            if (da > 0) {
                commands.add(Command.LEFT);
            } else {
                commands.add(Command.RIGHT);
            }
        }
    }

    private void aimAndShoot(final GameState gs, final Set<Command> commands) {
        final double angleToEnemy = getAngleToEnemy(gs);

        System.out.println("PlayerX: " + gs.playerX + "\tPlayerY: " + gs.playerY);
        System.out.println("EnemyX: " + gs.enemyX + "\tEnemyY: " + gs.enemyY);
        System.out.println("Rotation: " + gs.playerRotation + "\tDesired: " + angleToEnemy);

        if (Math.abs(angleToEnemy - gs.playerRotation) < AIMING_ANGLE_EPSILON) {
            commands.add(Command.SHOOT);
        } else {
            final double da = angleToEnemy - gs.playerRotation;
            if (da > 0) {
                commands.add(Command.LEFT);
            } else {
                commands.add(Command.RIGHT);
            }
        }
    }

    private boolean willCollide(final GameState gs) {
        for (int i = 0; i < HIT_EST_LENGTH; i++) {
            final double t = HIT_EST_DETAIL * i;
            final float estX = (float) (gs.enemyBulletX + t * gs.enemyBulletVelX);
            final float estY = (float) (gs.enemyBulletY + t * gs.enemyBulletVelY);

            if (getDistanceTo(gs.playerX, gs.playerY, estX, estY) < DODGE_HIT_RANGE) {
                return true;
            }
        }
        return false;
    }

    private double getDistanceToEnemy(final GameState gs) {
        return Math.sqrt(Math.pow((gs.playerX - gs.enemyX), 2) + Math.pow((gs.playerY - gs.enemyY), 2));
    }

    private double getAngleToEnemy(final GameState gs) {
        return getAngleTo(gs.playerX, gs.playerY, gs.enemyX, gs.enemyY);
    }

    private double getDistanceToEnemyBullet(final GameState gs) {
        return Math.sqrt(Math.pow((gs.playerX - gs.enemyBulletX), 2) + Math.pow((gs.playerY - gs.enemyBulletY), 2));
    }

    private double getAngleToEnemyBullet(final GameState gs) {
        return getAngleTo(gs.playerX, gs.playerY, gs.enemyBulletX, gs.enemyBulletY);
    }

    private double getDistanceTo(final float x1, final float y1, final float x2, final float y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private double getAngleTo(final float x1, final float y1, final float x2, final float y2) {
        final double dx = x2 - x1;
        final double dy = y2 - y1;
        final double angle = Math.atan2(dy, dx) % (2 * Math.PI);

        return (angle > 0) ? angle : angle + 2 * Math.PI;
    }
}
