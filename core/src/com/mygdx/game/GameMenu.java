/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.gamestate.MyScreen;
import com.mygdx.game.gamestate.ScreenManager;
import com.mygdx.game.input.GameInputs;

/**
 *
 * @author branc2347
 */
public class GameMenu extends MyScreen {

    Skin skin;
    Stage stage;
    GameScreen background;
    InputMultiplexer im;
    TextButton bResetPlayer, bResetShape;
    Slider gravitySliderY;

    public GameMenu(ScreenManager gameStateManager, GameScreen background) {
        super(gameStateManager);
        this.background = background;
    }

    @Override
    public void init() {
        stage = new Stage();

        //Input multiplexer, giving priority to stage over gameinput
        im = new InputMultiplexer(this.stage, MyGdxGame.gameInput);
        // set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(im);

        //initialize skin by imlpementing the json file that implements the atlas
        // the json file has the buttonstyle,etc already coded into it, only need to call the name to use it
        skin = new Skin(Gdx.files.internal("ui-data/uiskin.json"));

        gravitySliderY = new Slider(-100, 100, 10, true, skin);

        // Create a table1 that fills the screen, the buttons, etc go into this table1
        Table table1 = new Table();
        Table table2 = new Table();
        //table 1 LAYOUT
        table1.setFillParent(true);
        table1.align(Align.topLeft);
        stage.addActor(table1);

        //create the buttons... b for button:
        bResetPlayer = new TextButton("Reset Player", skin, "default");
        bResetShape = new TextButton("Reset Shapes", skin, "default");
        //add the buttons to the table1
        table1.debugAll();
        table1.add(bResetPlayer).pad(4, 4, 4, 4);
        table1.add(bResetShape).pad(4, 0, 4, 4);
        table1.add(gravitySliderY);

        //taken from the internet:
        //        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        //        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        //        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        //        // revert the checked state.
        addInputs();
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.getInputProcessor() != im) {
            Gdx.input.setInputProcessor(im);
        }
        processInput();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        background.render(deltaTime);
        stage.act(deltaTime);
        stage.draw();
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

    //to avoid the clutter of code
    public void addInputs() {
        bResetPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("test");
                //if the player exists, reset the location of the player
                if (background.world.getPlayer() != null) {
                    background.resetPlayer();
                    background.update(Gdx.graphics.getDeltaTime());
                }
            }
        });
        bResetShape.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //if the player exists, reset the location of the player
                if (background.world.getPlayer() != null) {
                    background.resetPlayer();
                }
            }
        });
        gravitySliderY.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                System.out.println(gravitySliderY.getVisualValue());
                background.world.setGravity(new Vector2(background.world.getGravity().x, gravitySliderY.getVisualValue()));
            }
        });
    }

    @Override
    public void processInput() {
        if (!GameInputs.isKeyDown(GameInputs.Keys.TAB)) {
            gameStateManager.setGameScreen(ScreenManager.GameScreens.MAIN_GAME);
        }
    }
}
