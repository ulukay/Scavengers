package org.ulukay.game;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.ulukay.game.battle.BattleController;
import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.config.Config;
import org.ulukay.game.entity.unit.Soldier;
import org.ulukay.game.entity.unit.UnitParams;
import org.ulukay.game.graphic.adapter.andengine.AdapterFactory;
import org.ulukay.game.graphic.adapter.andengine.BattleUnitAndEngineAdapter;
import org.ulukay.game.graphic.adapter.andengine.BattleUnitAndEngineAdapter.TextureInfo;

import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 400;
	private UnitParams[][] units;

	private BattleUnit[][] armies;

	private static final String FOLDER_UNITS = "units/";
	private static final String CURSOR_TEXTURE_PATH = "cursor.png";


	private static final int[][][] positions = {
			{ { 10, 10 }, { 40, 10 }, { 10, 40 }, { 40, 40 }, { 10, 70 }, { 40, 70 } },
			{ { 250, 10 }, { 220, 20 }, { 245, 70 }, { 215, 80 }, { 240, 120 }, { 210, 130 } } };

	private Camera mCamera;
	private BattleController battleController;

	
	@Override
	public Engine onLoadEngine() {
		stub();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(
				new EngineOptions(true, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), this.mCamera)
						.setNeedsSound(true));
	}

	@Override
	public void onLoadResources() {
		TextureManager textureManager = getEngine().getTextureManager();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(FOLDER_UNITS);
		armies = new BattleUnit[2][Config.ARMY_MAX_SIZE];
		for (int army = 0; army < units.length; army++) {
			for (int position = 0; position < units[army].length; position++) {
				final UnitParams unitParams = units[army][position];
				if (unitParams == null)
					continue;
				BitmapTextureAtlas pTexture = new BitmapTextureAtlas(Config.TEXTURE_MAX_WIDTH, Config.TEXTURE_MAX_HEIGHT, TextureOptions.BILINEAR);
				TextureInfo textureInfo = AdapterFactory.produceTextureInfo(unitParams);

				TiledTextureRegion unitTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pTexture, this,
						textureInfo.textureName, 0, 0, textureInfo.pair.p1, textureInfo.pair.p2);
				TiledTextureRegion cursorTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pTexture, this,
						CURSOR_TEXTURE_PATH, unitTiledTextureRegion.getWidth() + 1,
						unitTiledTextureRegion.getHeight() + 1, BattleUnitAndEngineAdapter.CURSOR_TILES_X,
						BattleUnitAndEngineAdapter.CURSOR_TILES_Y);

				BattleUnitAndEngineAdapter adapter = AdapterFactory.produce(unitParams, position,
						positions[army][position][0], positions[army][position][1], unitTiledTextureRegion,
						cursorTiledTextureRegion);

				textureManager.loadTexture(pTexture);
				armies[army][position] = new BattleUnit(unitParams, position, army, adapter);
			}
		}

	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene();
		//scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		for (int army = 0; army < armies.length; army++) {
			for (int position = 0; position < armies[army].length; position++) {
				final BattleUnit battleUnit = armies[army][position];
				if (battleUnit == null)
					continue;
				((BattleUnitAndEngineAdapter) battleUnit.getGraphicAdapter()).onLoadScene(scene);
			}
		}
		battleController = new BattleController(armies, true);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (battleController != null && event.getAction() == KeyEvent.ACTION_DOWN){
			battleController.onKeyDown(keyCode);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void stub() {
		units = new UnitParams[2][Config.ARMY_MAX_SIZE];
		units[BattleController.LEFT_ARMY][0] = new Soldier();
		units[BattleController.LEFT_ARMY][1] = new Soldier();
		units[BattleController.LEFT_ARMY][3] = new Soldier();
		units[BattleController.LEFT_ARMY][4] = new Soldier();
		units[BattleController.RIGHT_ARMY][0] = new Soldier();
		units[BattleController.RIGHT_ARMY][1] = new Soldier();
		units[BattleController.RIGHT_ARMY][2] = new Soldier();
		units[BattleController.RIGHT_ARMY][3] = new Soldier();
		units[BattleController.RIGHT_ARMY][4] = new Soldier();
	}
}