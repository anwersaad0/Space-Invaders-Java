package spa_inv;

import java.io.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private Timer timer;
	private Tank player;
	private List<Enemy> enemies;
	private List<Barrier> barriers;
	private int dir = 1;
	private int started = 0;
	private int spacing = 0;
	private int level = 1;
	private int score = 0;
	private int penalty = 0;
	private int lives = 3;
	private String highscore = "";
	private boolean gameplay;
	private boolean hardMode;
	
	private final int X_START = 145;
	private final int Y_START = 250;
	private final int E_START_Y = 85;
	private final int E_START_X = 100;
	private final int B_START_X = 35;
	private final int B_START_Y = 226;
	private final int W_BOARD = 300;
	private final int H_BOARD = 300;
	private final int DELAY = 20;
	private final int RIGHT_BORDER = 10;
	private final int VERT_DIST = 15;
	private final int GROUND_LEVEL = 260;
	private final int MAX_LEVELS = 3;
	private final int DURABILITY = 20;
	
	public Board() {
		initBoard();
	}
	
	private void initBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(W_BOARD, H_BOARD));
	}
	
	public void beginGame() {
		player = new Tank(X_START, Y_START);
		initEnemies();
		if (!hardMode) {
			initBarriers();
		}
		
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void initEnemies() {
		enemies = new ArrayList<>();
		
		for(int i = 0; i < 6 + level; i++) {
			enemies.add(new Enemy(E_START_X + (15 * i) - (5 * spacing) , E_START_Y - (15 * spacing)));
			for (int j = 0; j < 2 + level - 1; j++) {
				enemies.add(new FrontEnemy(E_START_X + (15 * i) - (5 * spacing) , E_START_Y + 15 + (15 * j) - (15 * spacing)));
			}
		}
	}
	
	public void initBarriers() {
		barriers = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			barriers.add(new Barrier(B_START_X + (100 * i), B_START_Y));
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		if(gameplay) {
			drawAssets(g);
		} else if (!gameplay && lives == 0) {
			timer.stop();
			gameOver(g2d);
			return;
		} else {
			if (started < 1) {
				startScreen(g2d);
			} else if (started >= 1 && level < MAX_LEVELS) {
				nextLevel(g2d);
			} else {
				victoryScreen(g2d);
			}
		}
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void restartLevel() {
		player.setX(X_START);
		initEnemies();
		if (!hardMode) {
			initBarriers();
		}
	}
	
	private void drawAssets(Graphics g) {
		
		if (player.isSeen()) {
			g.drawImage(player.getImg(), player.getX(), player.getY(), this);
		}
		
		List<Bullet> ammo = player.getAmmo();
		for (Bullet b : ammo) {
			if (b.isSeen()) {
				g.drawImage(b.getImg(), b.getX(), b.getY(), this);
			}
		}
		
		for (Enemy e : enemies) {
			if(e.isSeen()) {
				g.drawImage(e.getImg(), e.getX(), e.getY(), this);
			}
			List<AlienShot> eAmmo = e.getShots();
			for (AlienShot a: eAmmo) {
				if (a.isSeen()) {
					g.drawImage(a.getImg(), a.getX(), a.getY(), this);
				}
			}
		}
		
		for (Enemy e : enemies) {
			if (e.isSeen()) {
				g.drawImage(e.getImg(), e.getX(), e.getY(), this);
			}
		}
		
		if (!hardMode) {
			for (Barrier b: barriers) {
				if (b.isSeen()) {
					g.drawImage(b.getImg(), b.getX(), b.getY(), this);
				}
			}
		}
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, W_BOARD, 20);
		
		g.setColor(Color.WHITE);
		g.drawString("Score: " + score, 5, 15);
		g.drawString("Level: " + level, 130, 15);
		g.drawString("Lives: " + lives, 255, 15);
		
		g.setColor(new Color(150, 80, 20));
		g.fillRect(0, GROUND_LEVEL, W_BOARD, 40);
	}
	
	public void startScreen(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(50, H_BOARD / 2 - 45, W_BOARD - 100, 80);
		g.setColor(Color.GREEN);
		g.drawRect(50, H_BOARD / 2 - 45, W_BOARD - 100, 80);
		
		String intro = "Space Invaders";
		String prompt = "Press 'Z' to start on Normal";
		String prompt2 = "Press 'X' to start on Hard";
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics fontM = this.getFontMetrics(f);
		
		g.setColor(Color.WHITE);
		g.setFont(f);
		g.drawString(intro, (W_BOARD - fontM.stringWidth(intro)) / 2, (H_BOARD / 2) - 24);
		g.drawString(prompt, (W_BOARD - fontM.stringWidth(prompt)) / 2, (H_BOARD / 2) + 6);
		g.setColor(new Color(200, 120, 120));
		g.drawString(prompt2, (W_BOARD - fontM.stringWidth(prompt2)) / 2, (H_BOARD / 2) + 24);
	}
	
	public void nextLevel(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 50);
		g.setColor(Color.GREEN);
		g.drawRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 50);
		
		String complete = "Level Complete!";
		String prompt = "Press 'Z' to continue";
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics fontM = this.getFontMetrics(f);
		
		g.setColor(Color.WHITE);
		g.setFont(f);
		g.drawString(complete, (W_BOARD - fontM.stringWidth(complete)) / 2, (H_BOARD / 2) - 12);
		g.drawString(prompt, (W_BOARD - fontM.stringWidth(prompt)) / 2, (H_BOARD / 2) + 6);
	}
	
	public void gameOver(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 60);
		g.setColor(Color.RED);
		g.drawRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 60);
		
		String gOver = "GAME OVER";
		String oof = "The Aliens have invaded!";
		String result = "Your Score: " + score;
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics fontM = this.getFontMetrics(f);
		
		g.setColor(Color.RED);
		g.setFont(f);
		g.drawString(gOver, (W_BOARD - fontM.stringWidth(gOver)) / 2, (H_BOARD / 2) - 12);
		g.setColor(Color.WHITE);
		g.drawString(oof, (W_BOARD - fontM.stringWidth(oof)) / 2, (H_BOARD / 2) + 5);
		g.drawString(result, (W_BOARD - fontM.stringWidth(result)) / 2, (H_BOARD / 2) + 22);
	}
	
	public void victoryScreen(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 60);
		g.setColor(Color.CYAN);
		g.drawRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 60);
		
		String win = "CONGRATULATIONS";
		String win2 = "The Aliens are defeated!";
		String result = "Your Score: " + score;
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics fontM = this.getFontMetrics(f);
		
		g.setColor(Color.WHITE);
		g.setFont(f);
		g.drawString(win, (W_BOARD - fontM.stringWidth(win)) / 2, (H_BOARD / 2) - 12);
		g.drawString(win2, (W_BOARD - fontM.stringWidth(win2)) / 2, (H_BOARD / 2) + 5);
		g.drawString(result, (W_BOARD - fontM.stringWidth(result)) / 2, (H_BOARD / 2) + 22);
	}
	
	public void deathScreen(Graphics2D g) { //IMPLEMENT LATER
		g.setColor(new Color(50, 50, 50));
		g.fillRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 50);
		g.setColor(new Color(60, 10, 10));
		g.drawRect(50, H_BOARD / 2 - 30, W_BOARD - 100, 50);
		
		String died = "You died!";
		String prompt = "Press 'Z' to try again";
		Font f = new Font("Arial", Font.BOLD, 12);
		FontMetrics fontM = this.getFontMetrics(f);
		
		g.setColor(Color.WHITE);
		g.setFont(f);
		g.drawString(died, (W_BOARD - fontM.stringWidth(died)) / 2, (H_BOARD / 2) - 12);
		g.drawString(prompt, (W_BOARD - fontM.stringWidth(prompt)) / 2, (H_BOARD / 2) + 6);
	}
	
	public void actionPerformed(ActionEvent a) {
		updateTank();
		updateBullets();
		updateEnemies();
		if (!hardMode) {
			updateBarriers();
		}
		updateEnemyShots();
		
		collision();
		
		repaint();
	}
	
	private void updateTank() {
		if (player.isSeen()) {
			player.move();
		}
	}
	
	private void updateBullets() {
		List<Bullet> ammo = player.getAmmo();
		for(int i = 0; i < ammo.size(); i++) {
			Bullet b = ammo.get(i);
			if (b.isSeen()) {
				b.move();
			} else {
				ammo.remove(i);
			}
		}
	}
	
	private void updateBarriers() {
		for (Barrier b: barriers) {
			if (!b.isSeen()) {
				barriers.remove(b);
			}
			if (b.getDmg() == DURABILITY) {
				b.setSeen(false);
			}
		}
	}
	
	private void updateEnemyShots() {
		for (Enemy e: enemies) {
			List <AlienShot> eAmmo = e.getShots();
			for (int i = 0; i < eAmmo.size(); i++) {
				AlienShot a = eAmmo.get(i);
				if (a.isSeen()) {
					a.move();
				} else {
					eAmmo.remove(i);
				}
			}
		}
	}
	
	private void updateEnemies() {
		if (enemies.isEmpty()) {
			penalty = 0;
			gameplay = false;
			if (level >= MAX_LEVELS) {
				return;
			}
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (!e.isSeen()) {
				enemies.remove(i);
			}
		}
		
		for (Enemy e: enemies) {
			int x = e.getX();
			if (x >= (W_BOARD - RIGHT_BORDER) && dir != -1) {
				dir = -1;
				Iterator<Enemy> it1 = enemies.iterator();
				while (it1.hasNext()) {
					Enemy e2 = it1.next();
					e2.setY(e2.getY() + VERT_DIST);
				}
			}
			
			if (x <= 0 && dir != 1) {
				dir = 1;
				Iterator<Enemy> it2 = enemies.iterator();
				while (it2.hasNext()) {
					Enemy e3 = it2.next();
					e3.setY(e3.getY() + VERT_DIST);
				}
			}
			
			if (e.getY() >= GROUND_LEVEL - 10) {
				restartLevel();
				lives--;
				score -= penalty;
				penalty = 0;
				if (lives == 0) {
					gameplay = false;
				}
			}
		}
		
		Iterator<Enemy> e1It = enemies.iterator();
		while(e1It.hasNext()) {
			Enemy e = e1It.next();
			if (e.isSeen()) {
				e.strafe(dir);
			}
		}
	}
	
	public void collision() {
		Rectangle r3 = player.getHitBox();
		for (Enemy e : enemies) {
			List<AlienShot> as = e.getShots();
			Rectangle r2 = e.getHitBox();
			if (r3.intersects(r2)) {
				restartLevel();
				lives--;
				score -= penalty;
				penalty = 0;
				if (lives == 0) {
					gameplay = false;
				}
			}
			for (AlienShot a: as) {
				Rectangle r4 = a.getHitBox();
				if (r3.intersects(r4)) {
					restartLevel();
					lives--;
					score -= penalty;
					penalty = 0;
					if (lives == 0) {
						gameplay = false;
					}
				}
				if (!hardMode) {
					for (Barrier br: barriers) {
						Rectangle r5 = br.getHitBox();
						if (r4.intersects(r5)) {
							a.setSeen(false);
							br.setDmg(br.getDmg() + 1);
						}
					}
				}	
			}
		}
		
		List<Bullet> bs = player.getAmmo();
		for (Bullet b : bs) {
			Rectangle r1 = b.getHitBox();
			for (Enemy e: enemies) {
				Rectangle r2 = e.getHitBox();
				if (r1.intersects(r2)) {
					b.setSeen(false);
					e.setSeen(false);
					score += (10 * lives);
					penalty += (10 * lives);
					if (hardMode) {
						score += 10;
						penalty += 10;
					}
				}
			}
			if (!hardMode) {
				for (Barrier br: barriers) {
					Rectangle r5 = br.getHitBox();
					if (r1.intersects(r5)) {
						b.setSeen(false);
						br.setDmg(br.getDmg() + 1);
					}
				}
			}
		}
	}
	
	public String getHighScores() { //IMPLEMENT LATER
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader("Scores.txt");
			br = new BufferedReader(fr);
			return br.readLine();
		} catch (Exception e) {
			return "0";
		} finally {
			//br.close();
		}
	}
	
	private class TAdapter extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent k) {
				player.keyRelease(k);
		}
		
		@Override
		public void keyPressed(KeyEvent k) {
			int key = k.getKeyCode();
			if (gameplay) {
				player.keyPress(k);
			} else {
				if (key == 'z' || key == 'Z') {
					if (started == 1) {
						List<Bullet> ammo = player.getAmmo();
						for (Bullet b : ammo) {
							ammo.remove(b);
						}
						
						level++;
						spacing++;
						gameplay = true;
						
						player.setX(X_START);
						initEnemies();
						if (!hardMode) {
							initBarriers();
						}
					} else {
						started++;
						gameplay = true;
						hardMode = false;
						beginGame();
					}
				}
				
				if (key == 'x' || key == 'X') {
					started++;
					gameplay = true;
					hardMode = true;
					beginGame();
				}
			}
		}
	}
}
