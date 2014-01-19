package com.eg.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class CardBuilder implements Disposable{
	
	private Texture cardMap;
	private TextureRegion[] region;

	public CardBuilder(){
		cardMap = new Texture(Gdx.files.internal("cards3.png"));
		cardMap.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		region = new TextureRegion[20];
		for (int x=0; x<5; x++){
			for (int y=0; y<4; y++){
				region[x*4+y] = new TextureRegion(cardMap, x*768, y*1024, 768, 1024);
			}
		}
	}
	
	public Card createCard(byte id){
		if (id<0 || id>39) throw new IllegalArgumentException();
		return new Card(region[(id>19)? id-20 : id], id);
	}

	@Override
	public void dispose() {
		cardMap.dispose();		
	}

}
