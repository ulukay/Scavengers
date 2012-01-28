package org.ulukay.game.entity.weapon;

import java.util.Random;

import org.ulukay.game.battle.ShootState;
import org.ulukay.game.entity.InventoryObject;
import org.ulukay.game.entity.unit.UnitParams;


public abstract class Weapon extends InventoryObject {

	public static final int MISS_MARKER = Integer.MIN_VALUE;

	protected int accuracy = 0;
	protected int damangeMax = 0;
	protected int damangeMin = 0;
	protected int coeff = 0;

	/** see {@link ShootState*/
	private int animationType;
	
	public final String weaponName;
	protected final int weaponIndex;
	protected final Random random;

	protected int[][] CURSOR_MASK;
	protected int[][] DAMANGE_MASK;

	public Weapon(int weaponIndex, Random random) {
		super();
		this.weaponIndex = weaponIndex;
		this.random = random;
		this.weaponName = this.getClass().getSimpleName().toLowerCase() + "|" + weaponIndex;
		initialize();
	}



	public void doDamange(final UnitParams shooter, final int posShooter, final UnitParams[] invicts,
			final int posInvict, final int[] hits) {

		int damange = doInvictDamange();
		if (match(shooter, invicts[posInvict])) {
			hits[posInvict] = damange;
		} else {
			hits[posInvict] = MISS_MARKER;
		}

		UnitParams invict;
		for (int i = 0; i < hits.length; i++) {
			if (i == posInvict) {
				continue;
			}
			if (invicts.length > i && ((invict = invicts[i]) != null) && testDamange(posShooter, posInvict, i)) {
				if (match(shooter, invict)) {
					hits[i] = damange;
				} else {
					hits[i] = MISS_MARKER;
				}
			} else {
				hits[i] = 0;
			}
		}
	}

	public int doInvictDamange() {
		int damange = damangeMin + random.nextInt(damangeMax - damangeMin + 1);
		return damange;
	}

	public int doCoInvictDamange(final int invictDamange) {
		return invictDamange / 2;
	}

	public boolean testDamange(final int posShooter, final int posInvict, final int testPos) {
		return ((DAMANGE_MASK[posShooter][posInvict] & (1 << (testPos))) != 0);
	}

	public boolean testCursor(final int posShooter, final int armyShooter, final int posInvict, final int armyTestPos,
			final int testPos) {
		if (armyShooter == armyTestPos && (CURSOR_MASK[posShooter][posInvict] & (1 << (testPos / 2))) != 0) {
			return true;
		} else if (armyShooter != armyTestPos && (CURSOR_MASK[posShooter][posInvict] & (1 << (testPos / 2 + 3))) != 0) {
			return true;
		}
		return false;
	}

	public boolean match(final UnitParams shooter, final UnitParams invict) {
		return random.nextInt(101) <= getMatchPosibility(shooter, invict);
	}

	public int getMatchPosibility(final UnitParams shooter, final UnitParams invict) {
		int posibility = (int) ((accuracy / 50.0) * shooter.getAccuracy());
		return posibility;
	}

	public int getAnimationType() {
		return animationType;
	}

}
