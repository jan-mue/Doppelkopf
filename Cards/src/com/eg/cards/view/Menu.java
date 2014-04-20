package com.eg.cards.view;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Menu extends Table{
	private final CardGame game;
	private final GUI gui;
	private final TabBar tabs;
	private final Image nav, cover;
	
	private ImageButton back, replay, settings, help;
	
	public static float duration = 0.1f;
	private Actor previousKeyboardFocus, previousScrollFocus;
	private InputListener ignoreTouchDown = new InputListener() {
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			event.cancel();
			return false;
		}
	};
	
	public Menu(final CardGame game, final GUI gui, TabBar tabs){
		this.gui = gui;
		this.game = game;
		
		this.tabs = tabs;
		nav = tabs.getNavigationButton().getImage();
		
		if (CardGame.DEBUG) debug();
		
		setTouchable(Touchable.enabled);
		center();
		
		TextureAtlas atlas = game.getAssets().get("ui.atlas", TextureAtlas.class);
		
		AtlasRegion white = atlas.findRegion("white");
		AtlasRegion backRegion = atlas.findRegion("menu");
		AtlasRegion replayRegion = atlas.findRegion("replay");
		AtlasRegion settingsRegion = atlas.findRegion("settings");
		AtlasRegion helpRegion = atlas.findRegion("help");
		
		TextureRegionDrawable bg = new TextureRegionDrawable(white);
		Drawable bgDown = new Skin().newDrawable(bg, GUI.LIGHT);
		
		setBackground(new TextureRegionDrawable(white));
		setSize(300, 1700);	
		
		ImageButtonStyle backStyle = new ImageButtonStyle();
		TextureRegionDrawable backIcon = new TextureRegionDrawable(backRegion);
		backStyle.up = bg;
		backStyle.down = bgDown;
		backStyle.imageUp = new TextureRegionDrawable(backIcon);
		
		ImageButtonStyle replayStyle = new ImageButtonStyle();
		TextureRegionDrawable replayIcon = new TextureRegionDrawable(replayRegion);
		replayStyle.up = bg;
		replayStyle.down = bgDown;
		replayStyle.imageUp = new TextureRegionDrawable(replayIcon);
		
		ImageButtonStyle settingsStyle = new ImageButtonStyle();
		TextureRegionDrawable settingsIcon = new TextureRegionDrawable(settingsRegion);
		settingsStyle.up = bg;
		settingsStyle.down = bgDown;
		settingsStyle.imageUp = new TextureRegionDrawable(settingsIcon);
		
		ImageButtonStyle helpStyle = new ImageButtonStyle();
		TextureRegionDrawable helpIcon = new TextureRegionDrawable(helpRegion);
		helpStyle.up = bg;
		helpStyle.down = bgDown;
		helpStyle.imageUp = new TextureRegionDrawable(helpIcon);
		
		back = new ImageButton(backStyle);
		back.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
        		game.toggleMainMenu();
        		gui.toggleMenu();
        	}
		});
		replay = new ImageButton(replayStyle);
		replay.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
        		gui.getLoop().start();
        		gui.toggleMenu();
        	}
		});
		settings = new ImageButton(settingsStyle);
		help = new ImageButton(helpStyle);
		
		add(back).size(300, 300);
		row();
		add(replay).size(300, 300);
		row();
		add(settings).size(300, 300);
		row();
		add(help).size(300, 300);
		
		cover = new Image(bg);
		cover.setColor(Color.BLACK);
		cover.setBounds(300, 0, 1080, 1700);
		cover.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
        		gui.toggleMenu();
        	}
		});
		
		addActor(cover);
		
		addListener(new FocusListener() {
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				if (!focused) focusChanged(event);
			}

			public void scrollFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				if (!focused) focusChanged(event);
			}

			private void focusChanged (FocusEvent event) {
				Stage stage = getStage();
				if (stage != null && stage.getRoot().getChildren().size > 0
					&& stage.getRoot().getChildren().peek() == Menu.this) { // Dialog is top most actor.
					Actor newFocusedActor = event.getRelatedActor();
					if (newFocusedActor != null && !newFocusedActor.isDescendantOf(Menu.this)) event.cancel();
				}
			}
		});
	}
	
	public void show(){
		clearActions();
		cover.clearActions();
		nav.clearActions();
		removeCaptureListener(ignoreTouchDown);
		nav.removeCaptureListener(ignoreTouchDown);

		previousKeyboardFocus = null;
		Actor actor = game.stage.getKeyboardFocus();
		if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

		previousScrollFocus = null;
		actor = game.stage.getScrollFocus();
		if (actor != null && !actor.isDescendantOf(this)) game.stage.setScrollFocus(previousScrollFocus);

		//pack();
		setPosition(-300, 0);
		gui.addActor(this);
		game.stage.setKeyboardFocus(this);
		game.stage.setScrollFocus(this);
		if (duration > 0){
			nav.addAction(Actions.moveBy(-nav.getWidth()/2, 0, duration));
			cover.getColor().a = 0;
			cover.addAction(Actions.alpha(0.6f, duration, Interpolation.fade));
			addAction(Actions.moveBy(300, 0, duration));
		}
		else setPosition(0, 0);
	}
	
	public void hide(){
		if (duration > 0 && gui.hasParent()) {
			addCaptureListener(ignoreTouchDown);
			nav.addCaptureListener(ignoreTouchDown);
			nav.addAction(sequence(Actions.moveBy(nav.getWidth()/2, 0, duration), Actions.removeListener(ignoreTouchDown, true)));
			cover.addAction(Actions.fadeOut(duration, Interpolation.fade));
			addAction(sequence(Actions.moveBy(-300, 0, duration), Actions.removeListener(ignoreTouchDown, true),
				Actions.removeActor()));
		} else{
			nav.setX(nav.getX()+nav.getWidth()/2);
			remove();
		}
	}
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		Actor hit = super.hit(x, y, touchable);
		if (tabs.hit(x+getX()-tabs.getX(), y+getY()-tabs.getY(), touchable) != null) return null;
		if (hit == null  && (!touchable || getTouchable() == Touchable.enabled)) return this;
		return hit;
	}
	
	@Override
	protected void setParent (Group parent) {
		super.setParent(parent);
		if (parent == null) {
			Stage stage = getStage();
			if (stage != null) {
				if (previousKeyboardFocus != null && previousKeyboardFocus.getStage() == null) previousKeyboardFocus = null;
				Actor actor = stage.getKeyboardFocus();
				if (actor == null || actor.isDescendantOf(this)) stage.setKeyboardFocus(previousKeyboardFocus);

				if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
				actor = stage.getScrollFocus();
				if (actor == null || actor.isDescendantOf(this)) stage.setScrollFocus(previousScrollFocus);
			}
		}
	}

}
