package org.ulukay.game.entity;

public abstract class InventoryObject {
	
	protected int value;
	protected boolean canWear;
	
	protected abstract void initialize();
	
	public int getValue() {
		return value;
	}

	public boolean isCanWear() {
		return canWear;
	}
}
