package com.eg.cards.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Menu extends Table{
	private CardGame game;
	private GUI gui;
	private TabBar tabs;
	private Image nav;
	
	public static float moveDuration = 0.1f;
	private Actor previousKeyboardFocus, previousScrollFocus;
	private InputListener ignoreTouchDown = new InputListener() {
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			event.cancel();
			return false;
		}
	};
	
	public Menu(CardGame game, GUI gui, TabBar tabs){
		this.gui = gui;
		this.game = game;
		
		this.tabs = tabs;
		nav = tabs.getNavigationButton().getImage();
		
		if (CardGame.debug) debug();
		
		setTouchable(Touchable.enabled);
		
		AtlasRegion bg = game.getUIAtlas().findRegion("white");
		setBackground(new TextureRegionDrawable(bg));
		
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
		removeCaptureListener(ignoreTouchDown);

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
		if (moveDuration > 0){
			nav.addAction(Actions.moveBy(-nav.getWidth()/2, 0, moveDuration));
			addAction(Actions.moveBy(300, 0, moveDuration));
		}
		else setPosition(0, 0);
	}
	
	public void hide(){
		if (moveDuration > 0) {
			addCaptureListener(ignoreTouchDown);
			nav.addAction(Actions.moveBy(nav.getWidth()/2, 0, moveDuration));
			addAction(sequence(Actions.moveBy(-300, 0, moveDuration), Actions.removeListener(ignoreTouchDown, true),
				Actions.removeActor()));
		} else remove();
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
