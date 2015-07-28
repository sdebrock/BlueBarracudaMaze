package maze;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Timer timer;
	//private LocalTime sTime, eTime;
	private long sTime, eTime;
	private Map m;
	private Player p;
	private Fog f;
	private int stepCount;
	
	public Board() {
		stepCount = 0;
		m = new Map();
		p = new Player();
		f = new Fog();
		p.setPlayerStart(m.getStartX(), m.getStartY());
		f.createFog(p.getTileX(), p.getTileY());
		addKeyListener(new Al());
		setFocusable(true);
		
		//sTime = LocalTime.now();
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		for(int y = 0; y < 14; y++) {
			for(int x = 0; x < 14; x++) {
				if(m.getMap(x, y).equals("g")){
					g.drawImage(m.getGround(), x * 32, y *32, null);
				}
				if(m.getMap(x, y).equals("w")){
					g.drawImage(m.getWall(), x * 32, y *32, null);
				}
				if(m.getMap(x, y).equals("f")){
					g.drawImage(m.getFinish(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y).equals("s")){
					g.drawImage(m.getStart(), x * 32, y * 32, null);
				}
			}
		}
		g.drawImage(p.getPlayer(), p.getX(), p.getY(), null);
		int[][] fog = f.getFogMap();
		for(int i = 0; i < 14; i++){
			for(int j = 0; j < 14; j++){
				if(fog[i][j] == 1){
					g.drawImage(f.getFog(),i * 32, j * 32, null);
				}
				else if(fog[i][j] == 2){
					g.drawImage(f.getFogOpaque(), i * 32, j * 32, null);
				}
			}
		}
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_UP){
				if(!m.getMap(p.getTileX(), p.getTileY() - 1).equals("w")) {
					p.move(0, -32, 0, -1);
					stepCount++;
				}
			}
			if(keycode == KeyEvent.VK_S || keycode == KeyEvent.VK_DOWN){
				if(!m.getMap(p.getTileX(), p.getTileY() + 1).equals("w")) {
					p.move(0, 32, 0, 1);
					stepCount++;
				}
			}
			if(keycode == KeyEvent.VK_A || keycode == KeyEvent.VK_LEFT){
				if(!m.getMap(p.getTileX() - 1, p.getTileY()).equals("w")) {
					p.move(-32, 0, -1, 0);
					stepCount++;
				}
			}
			if(keycode == KeyEvent.VK_D || keycode == KeyEvent.VK_RIGHT){
				if(!m.getMap(p.getTileX() + 1, p.getTileY()).equals("w")) {
					p.move(32, 0, 1, 0);
					stepCount++;
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			if(m.getMap(p.getTileX(), p.getTileY()).equals("f")) {
				timer.stop();
				//eTime = LocalTime.now();
				eTime = System.currentTimeMillis();
				long seconds = (eTime - sTime) / 1000;
 		        //long seconds = ChronoUnit.SECONDS.between(sTime, eTime);
 		        long minutes = seconds / 60;
			    long secondsRemaining = seconds % 60;
			    String time = minutes + "m : " + secondsRemaining + "s";
				JOptionPane.showMessageDialog(new JFrame(), "You have won! \n Your Time: " + time + " \nSteps Taken: " + stepCount);
				System.exit(0);
			}
			
			f.reFog(p.getTileX(), p.getTileY());
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
