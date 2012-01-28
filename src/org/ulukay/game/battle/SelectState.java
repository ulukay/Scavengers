package org.ulukay.game.battle;

import org.ulukay.game.battle.entity.Cursor;

import android.view.KeyEvent;

public class SelectState extends AbstractState {

	public SelectState(BattleController battleController) {
		super(battleController);
	}

	private final static byte[] cursorInvert = { 1, 0, 3, 2, 5, 4 };
	private int selectedUnitPos;
	private boolean activeInput;

	@Override
	public void onKeyDown(final int pKeyCode) {
		if (!activeInput) {
			return;
		}
		switch (pKeyCode) {

		case KeyEvent.KEYCODE_DPAD_LEFT:
			selectedUnitPos--;
			if (selectedUnitPos < 0) {
				selectedUnitPos = 5;
			}
			keySelected(-1);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			selectedUnitPos++;
			if (selectedUnitPos > 5) {
				selectedUnitPos = 0;
			}
			keySelected(1);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			selectedUnitPos -= 2;
			if (selectedUnitPos < 0) {
				selectedUnitPos = 6 + selectedUnitPos;
			}
			keySelected((selectedUnitPos % 2 == 0) ? 1 : -1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			selectedUnitPos += 2;
			if (selectedUnitPos > 5) {
				selectedUnitPos = (selectedUnitPos - 6);
			}
			keySelected((selectedUnitPos % 2 == 0) ? 1 : -1);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			battleController.changeState(BattleController.STATE_SHOOT);
		}

	}

	private void keySelected(final int offset) {
		int realPos = selectedUnitPos;
		if (battleController.armyToSelect == 1) {
			realPos = cursorInvert[selectedUnitPos];
		}
		while (battleController.armies[battleController.armyToSelect][realPos] == null) {
			selectedUnitPos += offset;
			if (selectedUnitPos < 0)
				selectedUnitPos = 5;
			else if (selectedUnitPos > 5)
				selectedUnitPos = 0;
			if (battleController.armyToSelect == 1) {
				realPos = cursorInvert[selectedUnitPos];
			} else {
				realPos = selectedUnitPos;
			}
		}
		doSelect(realPos, battleController.armyToSelect);
	}

	@Override
	public void onSetState() {
		battleController.clearCursors();
		nextUnit();
		if (battleController.currentUnit.getArmyPos() == BattleController.RIGHT_ARMY && battleController.withAI) {
			battleController.changeState(BattleController.STATE_AI_TURN);
			return;
		}
		selectedUnitPos = 0;
		keySelected(1);
		activeInput = true;

	}

	@Override
	public void onDispatchState() {
		activeInput = false;
	}

	public void doSelect(final int posInvict, final int armyInvict) {
		battleController.clearCursors();
		battleController.currentUnit.setCursor(Cursor.CURRENT);
		if (!activeInput) {
			return;
		}
		Cursor cursor = Cursor.SELECTED;
		/*for (int ac = 0; ac < 2; ac++) {
			for (int i = 0; battleController.armies[ac].length > i; i++) {
				if ((battleController.armies[ac][i] != null && i % 2 != 0)
						&& battleController.currentUnit.getWeapon().testCursor(
								battleController.currentUnit.getPosition(), battleController.currentUnit.getArmyPos(),
								posInvict, battleController.armies[ac][i].getArmyPos(), i)) {
					battleController.armies[ac][i].setCursor(Cursor.BLOCKER);
					cursor = Cursor.BLOCKED;

				}
			}
		}*/

		if (cursor == Cursor.SELECTED) {
			battleController.selectedUnit = battleController.armies[armyInvict][posInvict];
			markCoDamangeUnits(posInvict, armyInvict);
		}
		battleController.armies[armyInvict][posInvict].setCursor(cursor);

	}

	private void markCoDamangeUnits(final int posInvict, final int armyInvict) {
		for (int i = 0; battleController.armies[armyInvict].length > i; i++) {
			if (posInvict == i)
				continue;
			if (battleController.armies[armyInvict][i] != null
					&& battleController.armies[armyInvict][i].getWeapon().testDamange(
							battleController.currentUnit.getArmyPos(), posInvict, i)) {
				battleController.armies[armyInvict][i].setCursor(Cursor.CO_DAMANGED);
			}
		}
	}

	private void nextUnit() {
		battleController.currentUnitIndex++;
		if (battleController.currentUnitIndex >= battleController.order.size()) {
			battleController.currentUnitIndex = 0;
		}
		battleController.currentUnit = battleController.order.get(battleController.currentUnitIndex);
		battleController.currentUnit.setCursor(Cursor.CURRENT);
		battleController.armyToSelect =(1-battleController.currentUnit.getArmyPos())%2;
	}
}
