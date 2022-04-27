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


/**
 * Trida pro vytvoreni a zakladni nastaveni okna.
 */
public class WindowInitializer {
	
	private WindowInitializer() {};
	
	public static void init(Space space) {
		JFrame okno = new JFrame();
		okno.setTitle("Sarka Dvorakova, A21B0116P");
		
		DrawingPanel panel = new DrawingPanel(space);	// predani instance vesmiru do vykreslovaciho panelu
		panel.setBackground(Color.BLACK);
		
		okno.add(panel);
		okno.pack(); 
		okno.setSize(new Dimension(800, 600));	// zakladni rozmery 800 x 600 px
		
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setLocationRelativeTo(null);
		okno.setVisible(true);		
		
		Timer paintTimer = new Timer();	
		paintTimer.schedule(new TimerTask() {
			public void run() {
				panel.repaint();	// panel se prekresli kazdych 17 ms
			}
		}, 0, 17);
		
		Timer chartTimer = new Timer();	
		chartTimer.schedule(new TimerTask() {
			public void run() {
				panel.collectData();
				panel.updateChart();
			}
		}, 0, 100);
		
		panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			/**
			 * Metoda zjisti, zda uzivatel kliknul na nejaky objekt a pokud ano, zobrazi info o objektu.
			 * Pokud uzivatel kliknul mimo, info je smazano.
			 * @param e		udalost
			 */
			public void mousePressed(MouseEvent e) {
				if(panel.isObjectClicked(e.getX(), e.getY())) {	
					panel.showInfo();
					panel.showChart();
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
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            /**
             * Pokud uzivatel zmacne mezernik, simulace se pozastavi / znovu rozbehne.
             * @param e		udalost
             * @return		true pokud uzivatel zmacknul mezernik / jinak false
             */
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == ' ') {
                    panel.changeSimulationStatus();
                }
                
                return false;
            }
        });
	}

}
