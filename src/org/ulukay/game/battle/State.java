package org.ulukay.game.battle;

public interface State {

	void onKeyDown(final int pKeyCode); 
	
	void onSetState ();
	
	void onDispatchState();
	
}
