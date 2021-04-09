package spa_inv;

public class Bullet extends Sprite {
	
	private final int BULLET_SPEED = -6;
	
	public Bullet(int x, int y) {
		super(x, y);
		initBullet();
	}
	
	private void initBullet() {
		loadImg("src/resources/Bullet.png");
		getSprDimensions();
	}
	
	public void move() {
		y += BULLET_SPEED;
		
		if (y < 20) {
			seen = false;
		}
	}
	
}
