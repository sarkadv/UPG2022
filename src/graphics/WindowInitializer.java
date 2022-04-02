package graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import main.Space;

public class WindowInitializer {
	
	private WindowInitializer() {};
	
	public static void init(Space space) {
		JFrame okno = new JFrame();
		okno.setTitle("Sarka Dvorakova, A21B0116P");
		DrawingPanel panel = new DrawingPanel(space);
		panel.setBackground(Color.BLACK);
		okno.add(panel);
		
		okno.pack(); 
		okno.setSize(new Dimension(800, 600));
		
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setLocationRelativeTo(null);
		okno.setVisible(true);		
		
		Timer tm = new Timer();	
		tm.schedule(new TimerTask() {
			public void run() {
				panel.repaint();
			}
		}, 0, 20);
		
		panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(panel.isObjectClicked(e.getX(), e.getY())) {
					panel.showInfo();
				}
				else {
					panel.stopShowingInfo();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED 
                        && e.getKeyChar() == ' ') {
                    panel.changeSimulationStatus();
                }
                
                return false;
            }
        });
	}

}
