package org.ulukay.game.entity.unit;

import org.ulukay.game.entity.armor.Armor;
import org.ulukay.game.entity.weapon.Weapon;

public abstract class UnitParams {

	protected int accuracy;
	protected int hp;
	protected int maxHp;
	protected int level;
	protected int expirience;
	protected int initiative;

	protected Armor armor;
	protected Weapon weapon;
	
	protected int value;

	public UnitParams(int hp, int level, int expirience, Armor armor, Weapon weapon) {
		super();
		this.hp = hp;
		this.level = level;
		this.expirience = expirience;
		this.armor = armor;
		this.weapon = weapon;
		initialize();
	}
	
	public UnitParams() {
		super();
		initialize();
		this.hp = maxHp;
		this.level = 1;
		this.expirience = 0;
		
	}
	
	public abstract Weapon getDefaultWeapon();
	public abstract Armor getDefaultArmor();
	protected abstract void initialize();

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(final int accuracy) {
		this.accuracy = accuracy;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(final int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(final int maxHp) {
		this.maxHp = maxHp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public int getExpirience() {
		return expirience;
	}

	public void setExpirience(final int expirience) {
		this.expirience = expirience;
	}

	public int getInitiative() {
		return initiative;
	}

	public void setInitiative(final int initiative) {
		this.initiative = initiative;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor(final Armor armor) {
		this.armor = armor;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(final Weapon weapon) {
		this.weapon = weapon;
	}	
}
