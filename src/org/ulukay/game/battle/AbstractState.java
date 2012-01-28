/**
 * 
 */
package org.ulukay.game.battle;

/**
 * @author Ulukay
 *
 */
public abstract class AbstractState implements State {

	protected final BattleController battleController;

	public AbstractState(final BattleController battleController) {
		super();
		this.battleController = battleController;
	}
}
