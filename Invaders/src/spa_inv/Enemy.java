package spa_inv;

import java.util.*;

public class Enemy extends Sprite {
	
	private List<AlienShot> eAmmo;
	private long cooldown;
	
	private final int SHOT_CHANCE = 2;
	
	public Enemy(int x, int y) {
		super(x, y);
		initEnemy2();
	}
	
	private void initEnemy2() {
		eAmmo = new ArrayList<>();
		
		loadImg("src/resources/Alien2.png");
		getSprDimensions();
	}
	
	public List<AlienShot> getShots() {
		return eAmmo;
	}
	
	public void fire() {
		long current = System.currentTimeMillis();
		if(current - cooldown >= 2000) {
			eAmmo.add(new AlienShot(x + (w / 2) - 2, (y + h)));
			cooldown = System.currentTimeMillis();
		}
	}
	
	public void strafe(int dir) {
		this.x += dir;
		
		var shotGen = new Random();
		int fired = shotGen.nextInt(50);
		if (fired == SHOT_CHANCE) {
			fire();
		}
	}
	
}
