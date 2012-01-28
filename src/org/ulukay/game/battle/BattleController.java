package org.ulukay.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.battle.entity.Cursor;

public class BattleController {

	public static final int STATE_SELECT = 0;
	public static final int STATE_AI_TURN = 1;
	public static final int STATE_SHOOT = 2;

	public static final byte LEFT_ARMY = 0;
	public static final byte RIGHT_ARMY = 1;

	final List<BattleUnit> order = new ArrayList<BattleUnit>();

	private final HashMap<Integer, State> states = new HashMap<Integer, State>();

	final BattleUnit[][] armies;
	BattleUnit currentUnit;
	BattleUnit selectedUnit;
	private State currentState;

	int armyToSelect;
	int currentUnitIndex = -1;

	final boolean withAI;

	public BattleController(final BattleUnit[][] armies, final boolean withAI) {
		this.armies = armies;
		this.withAI = withAI;
		init();
	}

	public void init() {
		states.put(STATE_SELECT, new SelectState(this));
		states.put(STATE_AI_TURN, new AISelectState(this));
		states.put(STATE_SHOOT, new ShootState(this));
		createOrder();
		for (int army = 0; army < armies.length; army++) {
			for (int position = 0; position < armies[army].length; position++) {
				final BattleUnit battleUnit = armies[army][position];
				if (battleUnit == null)
					continue;
				battleUnit.playStay();
			}

		}
		changeState(STATE_SELECT);
	}

	private void createOrder() {
		for (int max, globalPos = 0, pos = 0, selectedAc = 0, lastMax = Integer.MAX_VALUE, lastPos = 0;;) {
			max = Integer.MIN_VALUE;
			for (byte ac = 0; ac < 2; ac++) {
				for (byte i = 0; i < armies[ac].length; i++) {
					if (armies[ac][i] != null) {
						if (armies[ac][i].getInitiative() > max
								&& (lastMax > armies[ac][i].getInitiative() || (lastMax == armies[ac][i]
										.getInitiative() && lastPos < ((ac == LEFT_ARMY) ? i : i
										+ armies[LEFT_ARMY].length)))) {
							max = armies[ac][i].getInitiative();
							pos = i;
							selectedAc = ac;
							globalPos = (ac == LEFT_ARMY) ? i : i + armies[LEFT_ARMY].length;
						}
					}
				}
			}

			if (lastPos == globalPos && lastPos != 0)
				break;
			order.add(armies[selectedAc][pos]);
			lastMax = max;
			lastPos = globalPos;
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public BattleUnit[][] getArmies() {
		return armies;
	}

	// package methods
	void clearCursors() {
		for (BattleUnit[] units : armies) {
			for (BattleUnit unit : units) {
				if (unit != null) {
					unit.setCursor(Cursor.NONE);
				}
			}
		}
	}

	// public methods
	public void onKeyDown(final int pKeyCode) {
		currentState.onKeyDown(pKeyCode);
	}

	public void changeState(final int stateIndex) {
		if (currentState != null) {
			currentState.onDispatchState();
		}
		currentState = states.get(stateIndex);
		currentState.onSetState();
	}

}
