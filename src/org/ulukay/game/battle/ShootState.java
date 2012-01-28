package org.ulukay.game.battle;

import java.util.concurrent.atomic.AtomicInteger;

import org.ulukay.game.battle.entity.BattleUnit;
import org.ulukay.game.config.Config;
import org.ulukay.game.entity.unit.UnitParams;
import org.ulukay.game.entity.weapon.Weapon;
import org.ulukay.game.graphic.adapter.OnAnimationFinishedListener;

import android.os.Handler;

public class ShootState extends AbstractState {

	public static final byte ANIMATION_PLAY_TYPE_ORDERRED = 0;
	public static final byte ANIMATION_PLAY_TYPE_DELAYED = 1;
	public static final byte ANIMATION_PLAY_TYPE_SIMPLE = 3;

	private int delay;
	private int[] hits = new int[Config.ARMY_MAX_SIZE];
	private UnitParams[] unitParams = new UnitParams[Config.ARMY_MAX_SIZE];
	private AtomicInteger activeAnimtions = new AtomicInteger();
	private Handler handler = new Handler();
	private boolean delayAccomplish;

	public ShootState(BattleController battleController) {
		super(battleController);
	}

	@Override
	public void onKeyDown(int pKeyCode) {

	}

	@Override
	public void onSetState() {
		for (int i = 0; i < battleController.armies[battleController.armyToSelect].length; i++) {
			BattleUnit battleUnit = battleController.armies[battleController.armyToSelect][i];
			if (battleUnit == null) {
				unitParams[i] = null;
			} else {
				unitParams[i] = battleUnit.getUnitParams();
			}
		}

		battleController.currentUnit.getWeapon().doDamange(battleController.currentUnit.getUnitParams(),
				battleController.currentUnit.getPosition(), unitParams, battleController.selectedUnit.getArmyPos(),
				hits);

		switch (battleController.currentUnit.getWeapon().getAnimationType()) {
		case ANIMATION_PLAY_TYPE_ORDERRED:
			battleController.currentUnit.setOnAnimationFinished(orderAnimationFinishedListener);
			battleController.currentUnit.shoot();
			break;
		case ANIMATION_PLAY_TYPE_DELAYED:
			delayAccomplish = false;
			activeAnimtions.incrementAndGet();
			battleController.currentUnit.setOnAnimationFinished(delayAnimationFinishedListener);
			battleController.currentUnit.shoot();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					synchronized (ShootState.this) {
						processHits();
						delayAccomplish = true;
					}
				}

			}, delay);
			break;
		case ANIMATION_PLAY_TYPE_SIMPLE:
			activeAnimtions.incrementAndGet();
			battleController.currentUnit.setOnAnimationFinished(simpleOnAnimationFinishedListener);
			battleController.currentUnit.shoot();
			processHits();
		}

	}

	@Override
	public void onDispatchState() {
		// TODO Auto-generated method stub

	}

	private void processHits() {
		for (BattleUnit unit : battleController.armies[battleController.armyToSelect]) {
			if (unit == null)
				continue;
			int hit = hits[unit.getPosition()];
			if (hit == Weapon.MISS_MARKER) {
				activeAnimtions.incrementAndGet();
				unit.setOnAnimationFinished(simpleOnAnimationFinishedListener);
				unit.recivingMissHit();
			} else if (hit > 0) {
				activeAnimtions.incrementAndGet();
				unit.setOnAnimationFinished(simpleOnAnimationFinishedListener);
				unit.recivingHit(hit, battleController.currentUnit.getWeapon());
			}
		}

		if (activeAnimtions.get() == 0) {
			finishState();
		}
	}

	private void finishState() {
		boolean hasAlive = false;
		for (BattleUnit unit : battleController.armies[battleController.armyToSelect]) {
			if (unit != null) {
				if (unit.getHp() > 0){
					hasAlive = true;
				}else{
					battleController.order.remove(unit);
				}
			}
		}
		if (hasAlive) {
			battleController.changeState(BattleController.STATE_SELECT);
		} else {
			// TODO:create win/lose dialog;
		}
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(final int delay) {
		this.delay = delay;
	}

	private OnAnimationFinishedListener simpleOnAnimationFinishedListener = new SimpleOnAnimationFinishedListener();
	private OnAnimationFinishedListener orderAnimationFinishedListener = new OrderedOnAnimationFinishedListener();
	private OnAnimationFinishedListener delayAnimationFinishedListener = new DelayOnAnimationFinishedListener();

	class SimpleOnAnimationFinishedListener implements OnAnimationFinishedListener {

		@Override
		public void animationFinished() {
			if (activeAnimtions.decrementAndGet() == 0 && battleController.getCurrentState() == ShootState.this) {
				finishState();
			}
		}

	}

	class OrderedOnAnimationFinishedListener implements OnAnimationFinishedListener {

		@Override
		public void animationFinished() {
			processHits();
		}

	}

	class DelayOnAnimationFinishedListener implements OnAnimationFinishedListener {

		@Override
		public void animationFinished() {
			synchronized (ShootState.this) {
				if (activeAnimtions.decrementAndGet() == 0 && delayAccomplish
						&& battleController.getCurrentState() == ShootState.this) {
					finishState();
				}
			}
		}

	}

}
