package org.ulukay.game.battle.entity;

import org.ulukay.game.entity.armor.Armor;
import org.ulukay.game.entity.unit.UnitParams;
import org.ulukay.game.entity.weapon.Weapon;
import org.ulukay.game.graphic.adapter.IBattleUnitGraphicAdapter;
import org.ulukay.game.graphic.adapter.OnAnimationFinishedListener;

public class BattleUnit{

	private int position;
	private final int armyPos;
	
	private Cursor cursor = Cursor.NONE;
	private Weapon battleWeapon;
	private Armor battleArmor;
	private final UnitParams unitParams;
	
	private final IBattleUnitGraphicAdapter graphicAdapter;

	private final UnitOnAnimationFinished innerOnAnimationFinished = new UnitOnAnimationFinished();
	private OnAnimationFinishedListener onAnimationFinished;

	public BattleUnit(final UnitParams unitParams, final int position, final int armyPos, final IBattleUnitGraphicAdapter graphicAdapter) {
		this.unitParams = unitParams;
		this.graphicAdapter = graphicAdapter;
		this.position = position;
		this.armyPos = armyPos;
		if (unitParams.getWeapon() == null) {
			battleWeapon = unitParams.getDefaultWeapon();
		}else{
			battleWeapon = unitParams.getWeapon();
		}
		if (unitParams.getArmor() == null) {
			battleArmor = unitParams.getDefaultArmor();
		}else{
			battleArmor = unitParams.getArmor();
		}
	}


	// Graphic adapters methods

	public void recivingHit(int damange, final Weapon enemyWeapon) {
		damange = battleArmor.recivingHit(damange, enemyWeapon);
		if (damange > 0) {
			unitParams.setHp(unitParams.getHp() - damange);
			if (unitParams.getHp() <= 0) {
				unitParams.setHp(0);
				graphicAdapter.playDie(this, enemyWeapon, onAnimationFinished);
			} else {
				graphicAdapter.playHit(this, enemyWeapon, innerOnAnimationFinished);
			}
			reDrawHealt();
		} else {
			graphicAdapter.playArmorSave(this, enemyWeapon, innerOnAnimationFinished);
		}
	}

	public void recivingMissHit() {
		graphicAdapter.playMiss(this, innerOnAnimationFinished);
	}

	public void shoot() {
		graphicAdapter.playShoot(this, innerOnAnimationFinished);
	}

	public void playStay() {
		graphicAdapter.playStay(this, null);
	}

	public void setCursor(final Cursor cursor) {
		this.cursor = cursor;
		graphicAdapter.applyCursor(cursor);
	}
	
	public void reDrawHealt(){
		graphicAdapter.reDrawHealth(unitParams.getHp(), unitParams.getMaxHp());
	}

	// getters and setters

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public int getArmyPos() {
		return armyPos;
	}

	public Cursor getCursor() {
		return cursor;
	}

	
	public Weapon getWeapon() {
		return battleWeapon;
	}
	
	public Armor getArmor() {
		return battleArmor;
	}
	
	public UnitParams getUnitParams() {
		return unitParams;
	}


	public IBattleUnitGraphicAdapter getGraphicAdapter() {
		return graphicAdapter;
	}

	public void setOnAnimationFinished(final OnAnimationFinishedListener onAnimationFinished) {
		this.onAnimationFinished = onAnimationFinished;
		innerOnAnimationFinished.setAnimationFinishedListener(onAnimationFinished);

	}

	private final class UnitOnAnimationFinished implements OnAnimationFinishedListener {

		private OnAnimationFinishedListener animationFinishedListener;

		public void setAnimationFinishedListener(final OnAnimationFinishedListener animationFinishedListener) {
			this.animationFinishedListener = animationFinishedListener;
		}

		@Override
		public void animationFinished() {
			if (animationFinishedListener != null) {
				animationFinishedListener.animationFinished();
			}
			playStay();
		}

	}
	
	// delegate methods
	public int getValue() {
		return unitParams.getValue();
	}
	public int getHp() {
		return unitParams.getHp();
	}
	public int getInitiative() {
		return unitParams.getInitiative();
	}
	public int getMaxHp() {
		return unitParams.getMaxHp();
	}
}