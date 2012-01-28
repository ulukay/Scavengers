package org.ulukay.game.entity.unit;

import java.util.Random;

import org.ulukay.game.entity.armor.Armor;
import org.ulukay.game.entity.armor.KevlarArmor;
import org.ulukay.game.entity.weapon.Pistol;
import org.ulukay.game.entity.weapon.Weapon;

public class Soldier extends UnitParams {

	private static final Weapon defaultWeapon = new Pistol(0, new Random());
	private static final Armor defaultArmor = new KevlarArmor(0, new Random());

	@Override
	public Weapon getDefaultWeapon() {
		return defaultWeapon;
	}

	@Override
	public Armor getDefaultArmor() {
		return defaultArmor;
	}

	@Override
	protected void initialize() {
		maxHp = 10;
		accuracy = 70;
		initiative = (new Random()).nextInt(200);
	}

}
