import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.svg.SVGGraphics2D;


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
		
		okno.add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ColorPicker.colors[4]);
		okno.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnSVG = new JButton("Export to SVG");
		buttonPanel.add(btnSVG);
		btnSVG.setBackground(Color.BLACK);
		btnSVG.setFocusable(false);
		btnSVG.setFont(new Font("Monospaced", Font.PLAIN, 10));
		btnSVG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.exportSVG();
			}
		});
		
		JButton btnPNG = new JButton("Export to PNG");
		buttonPanel.add(btnPNG);
		btnPNG.setBackground(Color.BLACK);
		btnPNG.setFocusable(false);
		btnPNG.setFont(new Font("Monospaced", Font.PLAIN, 10));
		btnPNG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.exportPNG();
			}
		});
		
		JButton btnFaster = new JButton("2x Faster");
		buttonPanel.add(btnFaster);
		btnFaster.setBackground(Color.BLACK);
		btnFaster.setFocusable(false);
		btnFaster.setFont(new Font("Monospaced", Font.PLAIN, 10));
		btnFaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.faster();
			}
		});
		
		JButton btnSlower = new JButton("2x Slower");
		buttonPanel.add(btnSlower);
		btnSlower.setBackground(Color.BLACK);
		btnSlower.setFocusable(false);
		btnSlower.setFont(new Font("Monospaced", Font.PLAIN, 10));
		btnSlower.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.slower();
			}
		});
		
		JButton btnReset = new JButton("Reset Time Step");
		buttonPanel.add(btnReset);
		btnReset.setBackground(Color.BLACK);
		btnReset.setFocusable(false);
		btnReset.setFont(new Font("Monospaced", Font.PLAIN, 10));
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.resetTimeStep();
			}
		});
		
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
