/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.input.GameInputs;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.gamestate.MyScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.model.GameWorld;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Polygon;
import java.util.ArrayList;

/**
 *
 * @author Dmitry
 */
public class GameScreen extends MyScreen {

    OrthographicCamera camera;
    Viewport viewport;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    GameWorld world;
    ArrayList<Vector2> potentialPolygon;
    InputMultiplexer im;
    // UI & Menu
    GameMenu menu;

    /**
     * Creates a UI object
     *
     * @param gameStateManager
     */
    public GameScreen(ScreenManager gameStateManager) {
        super(gameStateManager);
    }

    /**
     * Draws the game
     *
     * @param deltaTime
     */
    @Override
    public void render(float deltaTime) {
        // updates the camera to the world space
        camera.update();
        // loads the batch and sets it to follow the camera's projection matrix
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (int i = 0; i < potentialPolygon.size(); i++) {
            shapeRenderer.circle(potentialPolygon.get(i).x, potentialPolygon.get(i).y, 1);
            shapeRenderer.line(potentialPolygon.get(i), potentialPolygon.get(i + 1 == potentialPolygon.size() ? 0 : i + 1));
        }

        for (Polygon polygon : world.getPolygons()) {
            if (polygon.containsPoint(GameInputs.getMousePosition())) {
                System.out.println("Mouse is in a polygon");
            }
            Vector2[] shapeVertices = polygon.getVertices();
            for (int i = 0; i < shapeVertices.length; i++) {
                shapeRenderer.line(shapeVertices[i], shapeVertices[i + 1 == shapeVertices.length ? 0 : i + 1]);
            }
        }
        if (world.getPlayer() != null) {
            Player player = world.getPlayer();
            if (player.containsPoint(GameInputs.getMousePosition())) {
                System.out.println("Mouse is in the player");
            }
            Vector2[] playerVertices = player.getVertices();
            for (int i = 0; i < playerVertices.length; i++) {
                shapeRenderer.line(playerVertices[i], playerVertices[i + 1 == playerVertices.length ? 0 : i + 1]);
            }
        }
        shapeRenderer.end();

        //render the menu
//        menu.render(deltaTime);


////        shapeRenderer.setProjectionMatrix(camera.combined);
//        
//        shapeRenderer.begin(ShapeType.Line);
//        
//        ArrayList<Polygon> collidedPolygons = world.collidePolygons();
//        
//        for (Polygon polygon: world.getPolygons())
//        {
//            shapeRenderer.setColor(Color.WHITE);
//            if (collidedPolygons.contains(polygon))
//                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
//                {
//                    shapeRenderer.setColor(Color.YELLOW);
//                }
//                else
//                    shapeRenderer.setColor(Color.RED);
//            else
//                if (polygon == world.collidePoint(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())))
//                    shapeRenderer.setColor(Color.GREEN);
//            shapeVertices = new float[polygon.getVertices().length*2];
//            for (int i = 0; i < shapeVertices.length-1; i += 2)
//            {
//                shapeVertices[i] = polygon.getVertices()[i/2].x;
//                shapeVertices[i+1] = polygon.getVertices()[i/2].y;
//            }
//            shapeRenderer.polygon(shapeVertices);
//        }
//        
//        shapeRenderer.setColor(Color.WHITE);
//        for (int i = 0; i < world.potentialPolygon.size(); i ++)
//        {
//            shapeRenderer.point(world.potentialPolygon.get(i).x, world.potentialPolygon.get(i).y, 0);
//            shapeRenderer.line(world.potentialPolygon.get(i), world.potentialPolygon.get(i+1==world.potentialPolygon.size() ? 0: i+1));
//            
//        }
//        
//        shapeRenderer.end();
    }

    @Override
    public void init() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
        viewport.apply(true);

        shapeRenderer = new ShapeRenderer();
        world = new GameWorld();
        potentialPolygon = new ArrayList();
        //create menu
        menu = new GameMenu();
        menu.create();
        //initialize input multiplexer. ensures that menu is above gamescreen, so that menu gets priority
        im = new InputMultiplexer(menu.stage, MyGdxGame.gameInput);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void update(float deltaTime) {
        processInput();

        world.update(deltaTime);
    }

    @Override
    public void processInput() {
        if (GameInputs.isKeyDown(GameInputs.Keys.W)) {
            world.getPlayer().setVelocity(new Vector2(0, 20));
        }
        if (GameInputs.isKeyDown(GameInputs.Keys.A)) {
            world.getPlayer().setVelocity(new Vector2(-20, 0));
        }
        if (GameInputs.isKeyDown(GameInputs.Keys.S)) {
            world.getPlayer().setVelocity(new Vector2(0, -20));
        }
        if (GameInputs.isKeyDown(GameInputs.Keys.D)) {
            world.getPlayer().setVelocity(new Vector2(20, 0));
        }
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ESCAPE)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_MENU);
        }
        if (GameInputs.isKeyDown(GameInputs.Keys.TAB)) {
            menu.render(Gdx.graphics.getDeltaTime());
        }
        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.LEFT)) {
            Vector2 newPoint = new Vector2(Gdx.input.getX(), MyGdxGame.HEIGHT - Gdx.input.getY());
            if (potentialPolygon.size() > 2 && !(new Polygon(potentialPolygon.toArray(new Vector2[potentialPolygon.size()]), 0).containsPoint(newPoint))) {
                potentialPolygon.add(newPoint);
                world.setPotentialPolygon(potentialPolygon);

            } else if (potentialPolygon.size() <= 2) {
                potentialPolygon.add(newPoint);
                world.setPotentialPolygon(potentialPolygon);
            }
        }

        if (GameInputs.isMouseButtonJustPressed(GameInputs.MouseButtons.RIGHT)) {
            if (potentialPolygon.size() > 0) {
                potentialPolygon.remove(potentialPolygon.size() - 1);
                world.setPotentialPolygon(potentialPolygon);
            }
        }

        if (GameInputs.isKeyJustPressed(GameInputs.Keys.ENTER)) {
            if (potentialPolygon.size() > 2) {
                world.createPolygon(potentialPolygon);
                potentialPolygon.clear();
                System.out.println("Polygon made.");
            }
        }
        if (GameInputs.isKeyJustPressed(GameInputs.Keys.P)) {
            if (world.getPlayer() == null) {
                world.createPlayer(potentialPolygon);
                potentialPolygon.clear();
                System.out.println("Player made.");
            }
        }
    }

    @Override
    public void show() {
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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        // test
        viewport.apply(true);
    }
}
