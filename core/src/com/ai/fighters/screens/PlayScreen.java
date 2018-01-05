package com.ai.fighters.screens;

import com.ai.fighters.AIFighters;
import com.ai.fighters.sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by chris on 1/5/18.
 */
public class PlayScreen implements Screen {
    private final AIFighters game;
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final SpriteBatch sb;
    private final OrthographicCamera camera;

    private final Player p1;
    private final Player p2;

    public PlayScreen(final AIFighters game, final World world, final SpriteBatch sb, final Player p1, final Player p2) {
        this.game = game;
        this.world = world;
        this.sb = sb;
        this.p1 = p1;
        this.p2 = p2;

        camera = new OrthographicCamera(AIFighters.WIDTH, AIFighters.HEIGHT);
        camera.position.set(AIFighters.WIDTH / 2, AIFighters.HEIGHT / 2, 0);
        camera.update();

        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    private void update(final float dt) {
        world.step(1 / 60f, 6, 2);

        p1.update(dt);
        p2.update(dt);
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
