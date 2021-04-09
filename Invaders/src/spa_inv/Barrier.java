package spa_inv;

public class Barrier extends Sprite {
	
	private int dmg = 0;
	
	public Barrier(int x, int y) {
		super(x, y);
		initBarrier();
	}
	
	private void initBarrier() {
		loadImg("src/resources/Barrier.png");
		getSprDimensions();
	}
	
	public int getDmg() {
		return dmg;
	}
	
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

}
