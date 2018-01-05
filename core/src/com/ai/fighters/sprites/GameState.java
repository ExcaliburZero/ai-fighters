package com.ai.fighters.sprites;

/**
 * Created by chris on 1/5/18.
 */
public class GameState {
    public final int playerX;
    public final int playerY;
    public final float playerRotation;

    public final int enemyX;
    public final int enemyY;
    public final float enemyRotation;

    public final int playerBulletX;
    public final int playerBulletY;

    public final int enemyBulletX;
    public final int enemyBulletY;

    public final int playerReload;
    public final int enemyReload;

    public GameState(int playerX, int playerY, float playerRotation, int enemyX, int enemyY, float enemyRotation, int playerBulletX, int playerBulletY, int enemyBulletX, int enemyBulletY, int playerReload, int enemyReload) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerRotation = playerRotation;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.enemyRotation = enemyRotation;
        this.playerBulletX = playerBulletX;
        this.playerBulletY = playerBulletY;
        this.enemyBulletX = enemyBulletX;
        this.enemyBulletY = enemyBulletY;
        this.playerReload = playerReload;
        this.enemyReload = enemyReload;
    }
}
