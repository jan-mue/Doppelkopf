package com.eg.cards.ui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FontParameter> {
	public FreeTypeFontLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	FreeTypeFontParameter param;
	FreeTypeFontGenerator generator;
	
	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, FontParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, FontParameter parameter) {
		generator = new FreeTypeFontGenerator(file);
		param = new FreeTypeFontParameter();
		if (parameter != null){
			param.size = parameter.size;
			param.characters = parameter.characters;
			param.packer = parameter.packer;
			param.flip = parameter.flip;
			param.genMipMaps = parameter.genMipMaps;
			param.minFilter = parameter.minFilter;
			param.magFilter = parameter.magFilter;
		}
	}

	@Override
	public BitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, FontParameter parameter) {
		BitmapFont font = generator.generateFont(param);
		generator.dispose();
		return font;
	}
	
	static public class FontParameter extends AssetLoaderParameters<BitmapFont> {
		/** The size in pixels */
		public int size;
		/** The characters the font should contain */
		public String characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		/** The optional PixmapPacker to use */
		public PixmapPacker packer = null;
		/**Whether to flip the font horizontally */
		public boolean flip = false;
		/** Whether or not to generate mip maps for the resulting texture */
		public boolean genMipMaps = false;
		/** Minification filter */
		public TextureFilter minFilter = TextureFilter.Nearest;
		/** Magnification filter */
		public TextureFilter magFilter = TextureFilter.Nearest;
		
		public FontParameter(){
			this(16);
		}
		
		public FontParameter(int size){
			this.size = Math.abs(size);
		}
	}
}
