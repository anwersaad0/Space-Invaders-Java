package spa_inv;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Sprite {
	
	protected int w;
	protected int h;
	protected int x;
	protected int y;
	protected boolean seen;
	protected Image img;
	
	public Sprite (int x, int y) {
		this.x = x;
		this.y = y;
		seen = true;
	}
	
	protected void getSprDimensions() {
		w = img.getWidth(null);
		h = img.getHeight(null);
	}
	
	protected void loadImg(String fileName) {
		ImageIcon fileI = new ImageIcon(fileName);
		img = fileI.getImage();
	}
	
	public Image getImg() {
		return img;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isSeen() {
		return seen;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle(x, y, w, h);
	}
}
