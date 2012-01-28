package org.ulukay.game.graphic.adapter.andengine;

import java.util.HashMap;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.ulukay.game.entity.weapon.Pistol;
import org.ulukay.game.entity.weapon.Weapon;

public class SoldierBattleUnitAndEngineAdapter extends BattleUnitAndEngineAdapter {

	protected static final HashMap<Class<? extends Weapon>, Pair> TILED_TEXTURE_COLS_ROWS = new HashMap<Class<? extends Weapon>, Pair>();

	private static final HashMap<Class<? extends Weapon>, Pair[]> ANIMATIONS = new HashMap<Class<? extends Weapon>, Pair[]>();
	private static final HashMap<Class<? extends Weapon>, HashMap<Class<? extends Weapon>, Pair[]>> DEPENDET_ANIMATIONS = new HashMap<Class<? extends Weapon>, HashMap<Class<? extends Weapon>, Pair[]>>();

	private Class<? extends Weapon> defaultWeaponClass = Pistol.class;

	static {
		// init parent map
		TILED_TEXTURE_COLS_ROWS.put(Pistol.class, new Pair(4, 2));

		// init own maps
		Pair[] pairs = new Pair[3];
		pairs[INDEPENDET_ANIMATION_STAY] = new Pair(0, 1);
		pairs[INDEPENDET_ANIMATION_SHOOT] = new Pair(1, 2);
		pairs[INDEPENDET_ANIMATION_MISS] = new Pair(2, 3);
		ANIMATIONS.put(Pistol.class, pairs);

		HashMap<Class<? extends Weapon>, Pair[]> enemyWeapons = new HashMap<Class<? extends Weapon>, Pair[]>();
		pairs = new Pair[3];
		pairs[DEPENDET_ANIMATION_GET_HIT] = new Pair(3, 4);
		pairs[DEPENDET_ANIMATION_ARMOR_SAVE] = new Pair(4, 5);
		pairs[DEPENDET_ANIMATION_DIE] = new Pair(5, 6);
		enemyWeapons.put(Pistol.class, pairs);
		DEPENDET_ANIMATIONS.put(Pistol.class, enemyWeapons);
	}

	public SoldierBattleUnitAndEngineAdapter(final int position, final float pX, final float pY,
			final TiledTextureRegion unitRegion, final TiledTextureRegion cursorRegion) {
		super(position, pX, pY, unitRegion, cursorRegion);
	}

	@Override
	protected Pair getAnimationIndex(final int aniationType, final Class<? extends Weapon> weaponClass) {
		return ANIMATIONS.get(weaponClass)[aniationType];
	}

	@Override
	protected Pair getAnimationIndex(final int aniationType, final Class<? extends Weapon> weaponClass,
			final Class<? extends Weapon> enemyWeaponClass) {
		Pair[] pairs = DEPENDET_ANIMATIONS.get(weaponClass).get(enemyWeaponClass);
		if (pairs == null) {
			pairs = DEPENDET_ANIMATIONS.get(weaponClass).get(defaultWeaponClass);
		}
		return pairs[aniationType];
	}
}
