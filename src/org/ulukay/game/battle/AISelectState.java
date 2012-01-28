package org.ulukay.game.battle;

import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.config.Config;
import org.ulukay.game.entity.weapon.Weapon;

import android.os.Handler;

public class AISelectState extends AbstractState {

	private static final int DIE_MULTIPLY_COEFF = 2;
	private Handler handler = new Handler();
	private final Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			findOptimalTarget();
		}
	};

	public AISelectState(final BattleController battleController) {
		super(battleController);
	}

	@Override
	public void onKeyDown(int pKeyCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetState() {
		handler.postDelayed(runnable, Config.AI_TURN_DELAY);
	}

	@Override
	public void onDispatchState() {
		// TODO Auto-generated method stub

	}

	private void findOptimalTarget() {
		int maxProfit = Integer.MIN_VALUE;
		for (BattleUnit unit : battleController.armies[battleController.armyToSelect]) {
			if (unit!=null && testPossibility(unit.getPosition())) {
				int profit = calculateProfit(battleController.currentUnit, unit.getPosition());
				if (profit > maxProfit) {
					maxProfit = profit;
					battleController.selectedUnit = unit;
				}
			}
		}
		if (maxProfit == Integer.MIN_VALUE) {
			battleController.changeState(BattleController.STATE_SELECT);
		} else {
			battleController.changeState(BattleController.STATE_SHOOT);
		}
	}

	private int calculateProfitConcreteUnit(int damange, final Weapon enemyWeapon, final BattleUnit unit) {
		damange = unit.getArmor().recivingHit(damange, enemyWeapon);
		int profit = unit.getValue() + unit.getWeapon().getValue() / 5 + unit.getArmor().getValue() / 10;
		if (unit.getHp() > damange) {
			profit = profit / (unit.getHp() - damange) + damange + (unit.getMaxHp() - (unit.getHp() - damange));
		} else {
			profit = (profit + unit.getHp()) * DIE_MULTIPLY_COEFF;
		}
		return profit;
	}

	private int calculateProfit(final BattleUnit shooter, final int posInvict) {
		BattleUnit unit = battleController.armies[battleController.armyToSelect][posInvict];
		Weapon weapon = shooter.getWeapon();
		if (unit == null)
			return -1;

		int profit = 0;
		int damange = shooter.getWeapon().doInvictDamange();

		profit = calculateProfitConcreteUnit(damange, weapon, shooter);

		for (int i = 0; i < battleController.armies[battleController.armyToSelect].length; i++) {
			if (unit.getPosition() == i || battleController.armies[battleController.armyToSelect][i] == null
					|| battleController.armies[battleController.armyToSelect][i].getHp() == 0) {
				continue;
			}
			if (weapon.testDamange(shooter.getPosition(), posInvict,
					battleController.armies[battleController.armyToSelect][i].getPosition())) {
				profit = calculateProfitConcreteUnit(weapon.doCoInvictDamange(damange), weapon,
						battleController.armies[battleController.armyToSelect][i]);
			}
		}
		return profit;
	}

	private boolean testPossibility(final int posInvict) {
		for (int ac = 0; ac < 2; ac++) {
			for (int i = 0; battleController.armies[ac].length > i; i++) {
				if ((battleController.armies[ac][i] != null && i % 2 != 0)
						&& battleController.currentUnit.getWeapon().testCursor(
								battleController.currentUnit.getPosition(), battleController.currentUnit.getArmyPos(),
								posInvict, battleController.armies[ac][i].getArmyPos(), i)) {
					return false;
				}
			}
		}
		return true;
	}

}
