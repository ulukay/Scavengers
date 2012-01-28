package org.ulukay.game.graphic.adapter.andengine;

import java.util.Arrays;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.battle.entity.Cursor;
import org.ulukay.game.entity.weapon.Weapon;
import org.ulukay.game.graphic.adapter.IBattleUnitGraphicAdapter;
import org.ulukay.game.graphic.adapter.OnAnimationFinishedListener;

public abstract class BattleUnitAndEngineAdapter implements IBattleUnitGraphicAdapter {

	protected static final int INDEPENDET_ANIMATION_STAY = 0;
	protected static final int INDEPENDET_ANIMATION_SHOOT = 1;
	protected static final int INDEPENDET_ANIMATION_MISS = 2;

	protected static final int DEPENDET_ANIMATION_DIE = 0;
	protected static final int DEPENDET_ANIMATION_ARMOR_SAVE = 1;
	protected static final int DEPENDET_ANIMATION_GET_HIT = 2;

	protected static final int FRAME_DURATION = 100;

	private final static int HEALTH_LINE_Y_OFFSET = -1;
	private final static int HEALTH_LINE_WIDTH = 4;

	private final static int Z_INDEX_HEALTH = 10;
	private final static int Z_INDEX_FULL_HEALTH = 11;
	private final static int Z_INDEX_CURSOR = 8;
	private final static int Z_INDEX_SPRITE = 9;

	private final static long CURSOR_ANIMATION_SPEED[] = { 100, 100, 100 };
	private final static int ANIMATION_COUNT = 3;

	public final static int CURSOR_TILES_X = 3;
	public final static int CURSOR_TILES_Y = 6;

	protected AnimatedSprite unitSprite;
	protected AnimatedSprite cursorSprite;
	private Line healthLine;
	private Line fullHealthLine;

	protected float pX;
	protected float pY;

	protected int width;
	protected int height;

	protected final TiledTextureRegion unitTexture;
	protected final TiledTextureRegion cursorTexture;

	protected int position;
	protected Cursor cursor;

	protected UnitAnimationListener animationListener = new UnitAnimationListener();

	protected static class UnitAnimationListener implements IAnimationListener {

		private OnAnimationFinishedListener animationFinishedListener;

		public void setAnimationFinishedListener(final OnAnimationFinishedListener animationFinishedListener) {
			this.animationFinishedListener = animationFinishedListener;
		}

		@Override
		public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
			if (animationFinishedListener != null) {
				animationFinishedListener.animationFinished();
			}
		}
	};

	private static int getCenterOffset(final int parentWidth, final int childWidth) {
		return (parentWidth - childWidth) / 2;
	}

	public BattleUnitAndEngineAdapter(final int position, final float pX, final float pY,
			final TiledTextureRegion unitRegion, final TiledTextureRegion cursorRegion) {
		this.pX = pX;
		this.pY = pY;

		this.unitTexture = unitRegion;
		this.cursorTexture = cursorRegion;

		this.position = position;

		width = unitRegion.getTileWidth();
		height = unitRegion.getTileHeight();
	}

	protected abstract Pair getAnimationIndex(final int aniationType, final Class<? extends Weapon> weaponClass);

	protected abstract Pair getAnimationIndex(final int aniationType, final Class<? extends Weapon> weaponClass,
			final Class<? extends Weapon> enemyWeaponClass);

	public void onLoadScene(final Scene scene) {
		fullHealthLine = new Line(pX, pY + HEALTH_LINE_Y_OFFSET, pX + width, pY + HEALTH_LINE_Y_OFFSET,
				HEALTH_LINE_WIDTH);
		fullHealthLine.setColor(1, 0, 0);
		fullHealthLine.setZIndex(position + Z_INDEX_HEALTH);

		healthLine = new Line(pX, HEALTH_LINE_Y_OFFSET + pY, pX + width, HEALTH_LINE_Y_OFFSET + pY, HEALTH_LINE_WIDTH);
		healthLine.setColor(0, 1, 0);
		healthLine.setZIndex(position + Z_INDEX_FULL_HEALTH);

		cursorSprite = new AnimatedSprite(pX + getCenterOffset(width, (int) cursorTexture.getTileWidth()),
				((pY + height) - (cursorTexture.getTileHeight() / 2)), cursorTexture);
		cursorSprite.setZIndex(position + Z_INDEX_CURSOR);

		unitSprite = new AnimatedSprite(pX, pY, unitTexture);
		unitSprite.setZIndex(position + Z_INDEX_SPRITE);

		scene.attachChild(cursorSprite);
		scene.attachChild(unitSprite);
		scene.attachChild(fullHealthLine);
		scene.attachChild(healthLine);
	}

	@Override
	public void applyCursor(final Cursor cursor) {
		if (this.cursor == cursor)
			return;
		this.cursor = cursor;
		if (Cursor.NONE.equals(cursor)) {
			cursorSprite.stopAnimation();
			cursorSprite.setVisible(false);
		} else {
			final int startIndex = ANIMATION_COUNT * (cursor.ordinal() - 1);
			final int endIndex = startIndex + ANIMATION_COUNT - 1;
			cursorSprite.animate(CURSOR_ANIMATION_SPEED, startIndex, endIndex, true);
			cursorSprite.setVisible(true);
		}
	}

	@Override
	public void reDrawHealth(final int currentHealth, final int maxHealth) {
		healthLine.setPosition(pX, pY + HEALTH_LINE_Y_OFFSET, pX + width * currentHealth / maxHealth, pY
				+ HEALTH_LINE_Y_OFFSET);
	}

	@Override
	public void playMiss(final BattleUnit unit, final OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(INDEPENDET_ANIMATION_MISS, 0, unit.getWeapon().getClass(), onAnimationFinished);
	}
	@Override
	public void playShoot(final BattleUnit unit, final OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(INDEPENDET_ANIMATION_SHOOT, 0, unit.getWeapon().getClass(), onAnimationFinished);
	}
	@Override
	public void playStay(final BattleUnit unit, final OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(INDEPENDET_ANIMATION_MISS, -1, unit.getWeapon().getClass(), onAnimationFinished);
	}
	@Override
	public void playArmorSave(BattleUnit unit, Weapon enemyWeapon, OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(DEPENDET_ANIMATION_ARMOR_SAVE, 0, unit.getWeapon().getClass(), enemyWeapon.getClass(),onAnimationFinished);
	}
	@Override
	public void playDie(BattleUnit unit, Weapon enemyWeapon, OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(DEPENDET_ANIMATION_DIE, 0, unit.getWeapon().getClass(), enemyWeapon.getClass(),onAnimationFinished);
	}
	@Override
	public void playHit(final BattleUnit unit, final Weapon enemyWeapon,
			final OnAnimationFinishedListener onAnimationFinished) {
		playAnimation(DEPENDET_ANIMATION_GET_HIT, 0, unit.getWeapon().getClass(), enemyWeapon.getClass(),onAnimationFinished);
	}

	private void playAnimation(final int aniationType, final int loop, final Class<? extends Weapon> weaponClass,
			final OnAnimationFinishedListener onAnimationFinished) {
		Pair pair = getAnimationIndex(aniationType, weaponClass);
		playAnimation(loop, pair, onAnimationFinished);
	}

	private void playAnimation(final int aniationType, final int loop, final Class<? extends Weapon> weaponClass,
			final Class<? extends Weapon> enemyWeaponClass, final OnAnimationFinishedListener onAnimationFinished) {
		Pair pair = getAnimationIndex(aniationType, weaponClass, enemyWeaponClass);
		playAnimation(loop, pair, onAnimationFinished);
	}

	private void playAnimation(final int loop, final Pair pair, final OnAnimationFinishedListener onAnimationFinished) {
		final long[] frameDurations = new long[pair.p2 - pair.p1+1];
		Arrays.fill(frameDurations, BattleUnitAndEngineAdapter.FRAME_DURATION);
		if (onAnimationFinished == null) {
			unitSprite.animate(frameDurations, pair.p1, pair.p2, loop);
		} else {
			animationListener.setAnimationFinishedListener(onAnimationFinished);
			unitSprite.animate(frameDurations, pair.p1, pair.p2, loop, animationListener);
		}
	}

	public static final class Pair {
		public final int p1;
		public final int p2;

		public Pair(int p1, int p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
	}

	public static final class TextureInfo {
		public final String textureName;
		public final Pair pair;

		public TextureInfo(String textureName, Pair pair) {
			super();
			this.textureName = textureName;
			this.pair = pair;
		}
	}
}