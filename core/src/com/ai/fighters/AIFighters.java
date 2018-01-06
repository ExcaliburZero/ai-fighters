package com.ai.fighters;

import com.ai.fighters.screens.PlayScreen;
import com.ai.fighters.sprites.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class AIFighters extends Game {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    public static final int P1_X = 150;
    public static final int P1_Y = 150;

    public static final int P2_X = 300;
    public static final int P2_Y = 300;

    public static final float TIME_LIMIT = 60f;

    private static final int NUM_CHROMOSOMES = 16;
    private static final long GA_SEED = 42;

    private SpriteBatch batch;
    private World world;

    @Override
    public void create() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), false);

        final ArrayList<Bullet> bullets = new ArrayList<Bullet>();

        final GeneticAlgorithm ga = new GeneticAlgorithm(NUM_CHROMOSOMES, GA_SEED);

        final PlayScreen play = new PlayScreen(this, TIME_LIMIT, bullets, world, batch, ga);

        //final Player p1 = new HumanPlayer(play, bullets, 1, world, new Vector2(P1_X, P1_Y));
        final Player p1 = new FuzzyGNPlayer(play, bullets, 1, world, new Vector2(P1_X, P1_Y));
        //final Player p1 = new NNPlayer(play, bullets, 2, world, new Vector2(P1_X, P1_Y), ga.getRandomPlayer());
        //final Player p2 = new NNPlayer(play, bullets, 2, world, new Vector2(P2_X, P2_Y), ga.getRandomPlayer());
        final Player p2 = new FuzzyGNPlayer(play, bullets, 2, world, new Vector2(P2_X, P2_Y));
        play.setPlayers(new Player[]{p1, p2});

        this.setScreen(play);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
