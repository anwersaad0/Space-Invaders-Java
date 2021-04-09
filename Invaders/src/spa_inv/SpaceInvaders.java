package spa_inv;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {

	public SpaceInvaders() {
		initWindow();
	}
	
	private void initWindow() {
		add(new Board());
		
		setResizable(false);
		pack();
		
		setTitle("Space Invaders");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			SpaceInvaders app = new SpaceInvaders();
			app.setVisible(true);
		});
	}
}
