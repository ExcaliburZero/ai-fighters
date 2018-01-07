package com.ai.fighters.screens;

import com.ai.fighters.AIFighters;
import com.ai.fighters.FuzzyGeneticAlgorithm;
import com.ai.fighters.sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by chris on 1/5/18.
 */
public class PlayScreen implements Screen {
    private static final int INITIAL_SCORE = 0;
    private static final int HIT_SCORE_INC = 10;
    private static final int SHOOT_SCORE_DEC = -1;
    private static final int HIT_SCORE_DEC = -HIT_SCORE_INC + SHOOT_SCORE_DEC;

    private static final float PLAYER_DIST_MIN = 30f;
    private static final int COLLIDE_SCORE_DEC = -50;

    private static final int SCORE_1_X = 10;
    private static final int SCORE_1_Y = 30;

    private static final int SCORE_2_X = 100;
    private static final int SCORE_2_Y = 30;

    private final AIFighters game;
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final SpriteBatch sb;
    private final FuzzyGeneticAlgorithm ga;
    private final OrthographicCamera camera;
    private float timeLeft;

    private final ArrayList<Bullet> bullets;
    private Player p1;
    private Player p2;

    private final HashMap<Player,Integer> scores;
    private final BitmapFont font;

    public PlayScreen(final AIFighters game, final float timeLimit, final ArrayList<Bullet> bullets, final World world,
                      final SpriteBatch sb, final FuzzyGeneticAlgorithm ga) {
        this.game = game;
        this.timeLeft = timeLimit;
        this.bullets = bullets;
        this.world = world;
        this.sb = sb;
        this.ga = ga;

        camera = new OrthographicCamera(AIFighters.WIDTH, AIFighters.HEIGHT);
        camera.position.set(AIFighters.WIDTH / 2, AIFighters.HEIGHT / 2, 0);
        camera.update();

        b2dr = new Box2DDebugRenderer();

        scores = new HashMap<Player, Integer>();
        font = new BitmapFont();

        defineWalls(world);
    }

    private void defineWalls(final World world) {
        for (int i = 0; i < 2; i++) {       // Wall (0) or floor (1)
            for (int j = 0; j < 2; j++) {   // Top/Left (0) or Bottom/Right (1)
                final BodyDef bdef = new BodyDef();
                //bdef.position.set(position);
                bdef.type = BodyDef.BodyType.StaticBody;

                final Body body = world.createBody(bdef);

                final FixtureDef fdef = new FixtureDef();
                final PolygonShape shape = new PolygonShape();

                if (i == 0) {
                    Vector2[] vertexArray = new Vector2[4];
                    vertexArray[0] = new Vector2((j == 0) ? -1f : AIFighters.WIDTH - 1, 0f);
                    vertexArray[1] = new Vector2((j == 0) ? 1f : AIFighters.WIDTH + 1, 0f);
                    vertexArray[2] = new Vector2((j == 0) ? -1f : AIFighters.WIDTH - 1, AIFighters.HEIGHT);
                    vertexArray[3] = new Vector2((j == 0) ? 1f : AIFighters.WIDTH + 1, AIFighters.HEIGHT);

                    shape.set(vertexArray);
                } else {
                    Vector2[] vertexArray = new Vector2[4];
                    vertexArray[0] = new Vector2(0f, (j == 0) ? -1f : AIFighters.HEIGHT - 1);
                    vertexArray[1] = new Vector2(0f, (j == 0) ? 1f : AIFighters.HEIGHT + 1);
                    vertexArray[2] = new Vector2(AIFighters.WIDTH, (j == 0) ? -1f : AIFighters.HEIGHT - 1);
                    vertexArray[3] = new Vector2(AIFighters.WIDTH, (j == 0) ? 1f : AIFighters.HEIGHT + 1);

                    shape.set(vertexArray);
                }

                fdef.shape = shape;
                body.createFixture(fdef);
            }
        }
    }

    public void setPlayers(final Player[] players) {
        p1 = players[0];
        p2 = players[1];

        for (final Player p : players) {
            scores.put(p, INITIAL_SCORE);
        }
    }

    public void shoot(final Player p) {
        scores.put(p, scores.get(p) + SHOOT_SCORE_DEC);
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

                    scores.put(p, scores.get(p) + HIT_SCORE_DEC);
                    scores.put(b.getShooter(), scores.get(b.getShooter()) + HIT_SCORE_INC);
                    //System.out.println("Hit!");

                    break;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    private void update(final float dt) {
        timeLeft -= dt;
        //System.out.println(timeLeft);
        if (timeLeft <= 0) {
            for (final Player p : scores.keySet()) {
                //System.out.println(p + ":\t" + scores.get(p));
            }
            endGame();
        }

        world.step(1 / 60f, 6, 2);

        p1.update(dt);
        p2.update(dt);
        updateBullets(dt);

        if (getPlayersDistance() < PLAYER_DIST_MIN) {
            p1.body.setTransform(AIFighters.P1_X, AIFighters.P1_Y, 0);
            p2.body.setTransform(AIFighters.P2_X, AIFighters.P2_Y, 0);

            scores.put(p1, scores.get(p1) + COLLIDE_SCORE_DEC);
            scores.put(p2, scores.get(p2) + COLLIDE_SCORE_DEC);
        }
    }

    public GameState getState(final Player p) {
        final Player e = (p == p1) ? p2 : p1;

        final Optional<Bullet> pBullet = bullets.stream().filter(b -> b.getShooter() == p).reduce((first, second) -> second);
        final Optional<Bullet> eBullet = bullets.stream().filter(b -> b.getShooter() == e).reduce((first, second) -> second);

        return new GameState(p.getX(), p.getY(), p.getBody().getAngle(),
                e.getX(), e.getY(), e.getBody().getAngle(),
                pBullet.map(b -> b.getX()).orElse(-1.0f),
                pBullet.map(b -> b.getY()).orElse(-1.0f),
                pBullet.map(b -> b.getVelocity().x).orElse(0.0f),
                pBullet.map(b -> b.getVelocity().y).orElse(0.0f),
                eBullet.map(b -> b.getX()).orElse(-1.0f),
                eBullet.map(b -> b.getY()).orElse(-1.0f),
                eBullet.map(b -> b.getVelocity().x).orElse(0.0f),
                eBullet.map(b -> b.getVelocity().y).orElse(0.0f),
                p.getReloadTime(),
                e.getReloadTime());
    }

    private void endGame() {
        ga.recieveScore((FuzzyGNPlayer) p1, scores.get(p1));
        ga.recieveScore((FuzzyGNPlayer) p2, scores.get(p2));

        final ArrayList<Bullet> bullets = new ArrayList<>();

        final World world = new World(new Vector2(0, 0), false);

        final PlayScreen play = new PlayScreen(game, AIFighters.TIME_LIMIT, bullets, world, sb, ga);

        final Player p1 = new FuzzyGNPlayer(play, bullets, 1, world, new Vector2(AIFighters.P1_X, AIFighters.P1_Y), ga.getRandomPlayer());
        final Player p2 = new FuzzyGNPlayer(play, bullets, 2, world, new Vector2(AIFighters.P2_X, AIFighters.P2_Y), ga.getRandomPlayer());

        play.setPlayers(new Player[]{p1, p2});
        game.setScreen(play);
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

        font.setColor(Color.BLACK);
        font.draw(sb, scores.get(p1).toString(), SCORE_1_X, SCORE_1_Y);
        font.draw(sb, scores.get(p2).toString(), SCORE_2_X, SCORE_2_Y);
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

    private double getPlayersDistance() {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
