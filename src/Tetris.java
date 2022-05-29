import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {

	private static final long serialVersionUID = 1L;



	private static Random random = new Random();



	private final static Color[] tetrominoColors = {Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red};

	private static boolean isEnd = false;
	private static Point location;
	private static int tetromino;
	private static int position;
	private final static int length = 12;
	private final static int height = 19;
	private final static int size_box = 20;
	private static long score;
	private static Color[][] field;

	private final static Point[][][] Tetrominos = {
			// I
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},

			// O
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},

			// T
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},

			// J
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},

			// L
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},

			// S
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},

			// Z
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};

	private boolean intersect(int x, int y, int position) {
		for (Point p : Tetrominos[tetromino][position]) {
			if (field[p.x + x][p.y + y] != Color.WHITE) {
				return true;
			}
		}
		return false;
	}

	public void nextStep() {
		if (!intersect(location.x, location.y + 1, position)) {
			location.y += 1;
		} else if(location.y == 0){
			isEnd = true;
		}
		else{	
			fix();
		}	
		repaint();
	}

	public void fix() {
		for (Point p : Tetrominos[tetromino][position]) {
			field[location.x + p.x][location.y + p.y] = tetrominoColors[tetromino];
		}
		drop();
		location = new Point(5, 0);
		position = 0;
		tetromino = random.nextInt(7);
	}

	public void move(int i) {
		if (!intersect(location.x + i, location.y, position)) {
			location.x += i;	
		}
		repaint();
	}

	public void rotate(int i) {
		int newPosition = (position + i) % 4;
		if (!intersect(location.x, location.y, newPosition)) {
			position = newPosition;
		}
		repaint();
	}
	
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				field[i][j+1] = field[i][j];
			}
		}
	}

	public void drop() {
		boolean all;
		int k = 0;

		for (int j = height - 2; j > 0; j--) {
			all = true;
			for (int i = 1; i < (length -1); i++) {
				if (field[i][j] == Color.WHITE) {
					all = false;
					break;
				}
			}
			if (all) {
				deleteRow(j);
				j += 1;
				k += 1;
			}
		}

		if(k > 0) {score += k*100*((int) Math.pow(2, k)) - 100;}
	}

	@Override 
	public void paintComponent(Graphics g)
	{
		g.fillRect(0, 0, size_box*length, size_box*height);
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < height; j++) {
				g.setColor(field[i][j]);
				g.fillRect(size_box*i, size_box*j, (size_box - 1), (size_box - 1));
			}
		}

		g.setColor(Color.BLACK);
		Font font = new Font("score", Font.PLAIN, 17);
		g.setFont(font);
		g.drawString("" + score, 15*length, (size_box - 1));

		if(isEnd) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, size_box*length, size_box*height);
			g.setColor(Color.RED);
			Font end_font = new Font("end_font", Font.PLAIN, 30);
			g.setFont(end_font);
			g.drawString("YOU LOSE", 10, (height / 2) * size_box);
			return;
		}

		g.setColor(tetrominoColors[tetromino]);
		for (Point p : Tetrominos[tetromino][position]) {
			g.fillRect((p.x + location.x) * size_box, 
					(p.y + location.y) * size_box, 
					(size_box - 1), (size_box - 1));
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(length*size_box+10, size_box*height+(size_box - 1));
		f.setVisible(true);
		final Tetris game = new Tetris();
		field = new Color[length][(height + 1)];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == (length -1) || j == (height -1)) {
					field[i][j] = Color.BLACK;
				} else {
					field[i][j] = Color.WHITE;
				}
			}
		}
		
		location = new Point(4, 0);
		position = 0;
		tetromino = random.nextInt(7);
		f.add(game);
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					System.exit(0);
				case KeyEvent.VK_UP:
					game.rotate(+3);
					break;
				case KeyEvent.VK_DOWN:
					game.rotate(+1);
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.nextStep();
					break;
				} 
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		new Thread() {
			@Override public void run() {
				while (true) {
					try {
						if((score / 10) > 900) {Thread.sleep(100);}{
							Thread.sleep(1000 - (score / 10));
						}
						game.nextStep();
						if(isEnd) {
							break;
						}
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}