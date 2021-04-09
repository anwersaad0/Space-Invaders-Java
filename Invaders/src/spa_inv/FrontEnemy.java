package spa_inv;

public class FrontEnemy extends Enemy {

	public FrontEnemy(int x, int y) {
		super(x, y);
		initEnemy1();
	}
	
	private void initEnemy1() {
		loadImg("src/resources/Alien.png");
		getSprDimensions();
	}
	
	public void strafe(int dir) {
		this.x += dir;
	}
}
