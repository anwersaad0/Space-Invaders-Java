package spa_inv;

import java.awt.event.KeyEvent;
import java.util.*;

public class Tank extends Sprite {

	private int dx;
	private long cooldown;
	private List<Bullet> ammo;
	
	private final int B_WIDTH = 290;
	
	public Tank(int x, int y) {
		super(x, y);
		initTank();
	}
	
	public void initTank() {
		ammo = new ArrayList<>();
		loadImg("src/resources/Tank.png");
		getSprDimensions();
	}
	
	public List<Bullet> getAmmo() {
		return ammo;
	}
	
	public void move() {
		x += dx;
		
		if (x < 1) {
			x = 1;
		}
		
		if (x > B_WIDTH) {
			x = B_WIDTH;
		}
	}
	
	public void shoot() {
		long current = System.currentTimeMillis();
		if (current - cooldown >= 500) {
			ammo.add(new Bullet(x + (w / 2) - 2, (y - h) + 6));
			cooldown = System.currentTimeMillis();
		}
	}
	
	public void keyPress(KeyEvent k) {
		int key = k.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT) {
			dx = -4;
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			dx = 4;
		}
	}
	
	public void keyRelease(KeyEvent k) {
		int key = k.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
		
		if (key == KeyEvent.VK_SPACE) {
			shoot();
		}
	}
}
