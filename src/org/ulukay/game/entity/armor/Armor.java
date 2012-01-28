package org.ulukay.game.entity.armor;

import java.util.Random;

import org.ulukay.game.entity.InventoryObject;
import org.ulukay.game.entity.weapon.Weapon;

public abstract class Armor extends InventoryObject {

	protected int armorHP;
	protected int opacity;
	protected int maxProtect;
	protected int minProtect;
	
	protected final Random random;
	
	protected final int armorIndex;
	
	Armor(final int armorIndex, final Random random){
		this.armorIndex = armorIndex;
		this.random = random;
		initialize();
	}
	
	public int recivingHit(int damange, final Weapon enemyWeapon){
		if (opacity >= random.nextInt(100)) {
			damange -= armorHP;
			int protect = minProtect + random.nextInt(maxProtect - minProtect + 1);
			damange *= protect / 100.0;
		}
		return damange;
	}
	
	public int getArmorHP() {
		return armorHP;
	}

	public int getOpacity() {
		return opacity;
	}

	public int getMaxProtect() {
		return maxProtect;
	}

	public int getMinProtect() {
		return minProtect;
	}	
}
