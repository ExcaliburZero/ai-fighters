package com.ai.fighters;

import com.ai.fighters.screens.PlayScreen;
import com.ai.fighters.sprites.HumanPlayer;
import com.ai.fighters.sprites.NNPlayer;
import com.ai.fighters.sprites.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class AIFighters extends Game {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

	private static final int P1_X = 50;
	private static final int P1_Y = 50;

	private static final int P2_X = 300;
	private static final int P2_Y = 300;

	private SpriteBatch batch;
	private World world;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), false);

		final Player p1 = new HumanPlayer(1, world, new Vector2(P1_X, P1_Y));
		final Player p2 = new NNPlayer(2, world, new Vector2(P2_X, P2_Y));

		final PlayScreen playScreen = new PlayScreen(this, world, batch, p1, p2);
		this.setScreen(playScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
