package org.ulukay.game.entity.armor;

import java.util.Random;

public class KevlarArmor extends Armor {

	public KevlarArmor(int armorIndex, Random random) {
		super(armorIndex, random);
	}

	@Override
	protected void initialize() {
		armorHP = 2;
		opacity = 60;
		maxProtect = 10;
		minProtect = 5;
	}
}
