package org.ulukay.game.graphic.adapter;

import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.battle.entity.Cursor;
import org.ulukay.game.entity.weapon.Weapon;

public interface IBattleUnitGraphicAdapter {
	
	void playMiss(BattleUnit unit, OnAnimationFinishedListener onAnimationFinished);
	
	void playHit(BattleUnit unit, Weapon enemyWeapon, OnAnimationFinishedListener onAnimationFinished);

	void playShoot(BattleUnit unit, OnAnimationFinishedListener onAnimationFinished);
	
	void playStay(BattleUnit unit, OnAnimationFinishedListener onAnimationFinished);
	
	void playArmorSave(BattleUnit unit, Weapon enemyWeapon, OnAnimationFinishedListener onAnimationFinished);
	
	void playDie(BattleUnit unit, Weapon enemyWeapon, OnAnimationFinishedListener onAnimationFinished);
	
	void applyCursor(Cursor cursor);
	
	void reDrawHealth(int currentHealth, int maxHealth);
}
