package org.ulukay.game.graphic.adapter.andengine;

import java.util.HashMap;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.ulukay.game.entity.unit.Soldier;
import org.ulukay.game.entity.unit.UnitParams;
import org.ulukay.game.entity.weapon.Weapon;
import org.ulukay.game.graphic.adapter.andengine.BattleUnitAndEngineAdapter.Pair;
import org.ulukay.game.graphic.adapter.andengine.BattleUnitAndEngineAdapter.TextureInfo;

public final class AdapterFactory {

	private final static String EXTENSION = ".png";

	public static BattleUnitAndEngineAdapter produce(final UnitParams params, final int position, final float pX,
			final float pY, final TiledTextureRegion unitRegion, final TiledTextureRegion cursorRegion) {
		if (Soldier.class.equals(params.getClass())) {
			return new SoldierBattleUnitAndEngineAdapter(position, pX, pY, unitRegion, cursorRegion);
		}
		throw new IllegalArgumentException();
	}

	public static TextureInfo produceTextureInfo(final UnitParams params) {
		if (Soldier.class.equals(params.getClass())) {
			return getTextureInfo(params, SoldierBattleUnitAndEngineAdapter.TILED_TEXTURE_COLS_ROWS);
		}

		throw new IllegalArgumentException();
	}

	private static TextureInfo getTextureInfo(final UnitParams unitParams, HashMap<Class<? extends Weapon>, Pair> map) {
		Weapon weapon = unitParams.getWeapon() == null ? unitParams.getDefaultWeapon() : unitParams.getWeapon();

		Pair pair = map.get(weapon.getClass());
		TextureInfo textureInfo = new TextureInfo(unitParams.getClass().getSimpleName().toLowerCase() + "/"
				+ weapon.getClass().getSimpleName().toLowerCase() + EXTENSION, pair);
		return textureInfo;
	}
}
