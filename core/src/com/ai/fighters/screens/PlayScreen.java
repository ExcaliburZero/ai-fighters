package com.ai.fighters.screens;

import com.ai.fighters.AIFighters;
import com.ai.fighters.sprites.Bullet;
import com.ai.fighters.sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chris on 1/5/18.
 */
public class PlayScreen implements Screen {
    private static final int INITIAL_SCORE = 0;
    private static final int HIT_SCORE_INC = 2;

    private final AIFighters game;
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final SpriteBatch sb;
    private final OrthographicCamera camera;
    private float timeLeft;

    private final ArrayList<Bullet> bullets;
    private final Player p1;
    private final Player p2;

    private final HashMap<Player,Integer> scores;

    public PlayScreen(final AIFighters game, final float timeLimit, final ArrayList<Bullet> bullets, final World world,
                      final SpriteBatch sb, final Player p1, final Player p2) {
        this.game = game;
        this.timeLeft = timeLimit;
        this.bullets = bullets;
        this.world = world;
        this.sb = sb;
        this.p1 = p1;
        this.p2 = p2;

        camera = new OrthographicCamera(AIFighters.WIDTH, AIFighters.HEIGHT);
        camera.position.set(AIFighters.WIDTH / 2, AIFighters.HEIGHT / 2, 0);
        camera.update();

        b2dr = new Box2DDebugRenderer();

        scores = new HashMap<Player, Integer>();

        final Player[] players = {p1, p2};
        for (final Player p : players) {
            scores.put(p, INITIAL_SCORE);
        }
    }

    @Override
    public void show() {

    }

    private void updateBullets(final float dt) {
        final ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for (final Bullet b : bullets) {
            b.update(dt);

            final Player[] players = {p1, p2};
            for (final Player p : players) {
                if (b.getShooter() != p && b.getBounds().overlaps(p.getBounds())) {
                    bulletsToRemove.add(b);

                    scores.put(p, scores.get(p) + HIT_SCORE_INC);
                    System.out.println("Hit!");

                    break;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    private void update(final float dt) {
        timeLeft -= dt;
        System.out.println(timeLeft);
        if (timeLeft <= 0) {
            for (final Player p : scores.keySet()) {
                System.out.println(p + ":\t" + scores.get(p));
            }
            System.exit(0);
        }

        world.step(1 / 60f, 6, 2);

        p1.update(dt);
        p2.update(dt);
        updateBullets(dt);
    }

    @Override
    public void render(final float dt) {
        update(dt);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        p1.draw(sb);
        p2.draw(sb);

        for (final Bullet b : bullets) {
            b.draw(sb);
        }
        sb.end();

        b2dr.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
