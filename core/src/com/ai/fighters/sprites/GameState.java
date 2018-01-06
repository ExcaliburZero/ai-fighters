package com.ai.fighters.sprites;

/**
 * Created by chris on 1/5/18.
 */
public class GameState {
    public final float playerX;
    public final float playerY;
    public final float playerRotation;

    public final float enemyX;
    public final float enemyY;
    public final float enemyRotation;

    public final float playerBulletX;
    public final float playerBulletY;
    public final float playerBulletVelX;
    public final float playerBulletVelY;

    public final float enemyBulletX;
    public final float enemyBulletY;
    public final float enemyBulletVelX;
    public final float enemyBulletVelY;

    public final float playerReload;
    public final float enemyReload;

    public GameState(float playerX, float playerY, float playerRotation, float enemyX, float enemyY,
                     float enemyRotation, float playerBulletX, float playerBulletY, float playerBulletVelX,
                     float playerBulletVelY, float enemyBulletX, float enemyBulletY, float enemyBulletVelX,
                     float enemyBulletVelY, float playerReload, float enemyReload) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerRotation = (float) (playerRotation % (2 * Math.PI));
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.enemyRotation = enemyRotation;
        this.playerBulletX = playerBulletX;
        this.playerBulletY = playerBulletY;
        this.playerBulletVelX = playerBulletVelX;
        this.playerBulletVelY = playerBulletVelY;
        this.enemyBulletX = enemyBulletX;
        this.enemyBulletY = enemyBulletY;
        this.enemyBulletVelX = enemyBulletVelX;
        this.enemyBulletVelY = enemyBulletVelY;
        this.playerReload = playerReload;
        this.enemyReload = enemyReload;
    }
}
