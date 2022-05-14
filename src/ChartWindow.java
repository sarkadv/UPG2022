import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartWindow extends JFrame {
	
	/** uzivatelem zvoleny objekt */
	SpaceObject clickedObject;
	
	/** kolekce rychlosti objektu */
	List<Double> speedData;
	
	/** graf samotny */
	JFreeChart chart;
	
	/** panel pro graf */
	ChartPanel chartPanel;
	
	/** kolikrat za sekundu se uklada rychlost objektu */
	final double DATA_COLLECTED_PER_S;
	
	/**
	 * Konstruktor pro tridu ChartWindow.
	 * @param clickedObject			uzivatelem zvoleny objekt
	 * @param DATA_COLLECTED_PER_S	kolikrat za sekundu se uklada rychlost objektu
	 */
	public ChartWindow(SpaceObject clickedObject, double DATA_COLLECTED_PER_S) {
		this.clickedObject = clickedObject;
		this.speedData = clickedObject.getSpeedData();
		this.DATA_COLLECTED_PER_S = DATA_COLLECTED_PER_S;
		
		initWindow();
	}
	
	/**
	 * Metoda inicializuje nove okno pro graf - vytvori graf,
	 * prida ho do panelu pro graf a panel prida do okna.
	 */
	private void initWindow() {
		this.chart = createChart();
		this.chartPanel = new ChartPanel(chart);
		this.add(chartPanel);
		this.pack();
		this.setSize(new Dimension(500, 400));	// zakladni rozmery 800 x 600 px
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);	
	}
	
	/**
	 * Metoda vytvori samotny graf - zavola metodu pro ziskani datasetu,
	 * ze ktereho ziska XYLine (spojnicovy) graf.
	 * Pote je graf upraven po graficke strance.
	 * @return		vytvoreny graf
	 */
	private JFreeChart createChart() {
		XYDataset dataset = createDataset();
		
	    JFreeChart chart = ChartFactory.createXYLineChart(
	            this.clickedObject.name + "'s speed during the last 30 seconds",
	            "Time [s]",
	            "Speed [km/h]",
	            dataset,
	            PlotOrientation.VERTICAL,
	            true, true, false);
	    
	    XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.DARK_GRAY);
		plot.setDomainGridlinePaint(Color.DARK_GRAY);
		plot.getRenderer().setSeriesPaint(0, this.clickedObject.color);
		plot.getRenderer().setSeriesVisibleInLegend(0, false);

		return chart;

	}
	
	/**
	 * Metoda vytvori dataset pro spojnicovy graf.
	 * Data jsou ziskana z kolekce rychlosti zvoleneho objektu.
	 * Pokud je simulace spustena dele nez 30 sekund, do datasetu jsou vlozena
	 * pouze data za poslednich 30 sekund.
	 * @return		vznikly dataset
	 */
	private XYDataset createDataset() {
	    XYSeriesCollection dataset = new XYSeriesCollection();
	    XYSeries series = new XYSeries("speed");
	    
	    int startingPoint = 0;
	    
	    if(this.speedData.size() > 30*this.DATA_COLLECTED_PER_S) {
	    	startingPoint = (int) (this.speedData.size() - 30*this.DATA_COLLECTED_PER_S);
	    }
	    
		for (int i = startingPoint; i < this.speedData.size(); i++) {
			series.add(i / this.DATA_COLLECTED_PER_S, this.speedData.get(i)/3.6);	// prevod m/s na km/h
		}

	    dataset.addSeries(series);
	    
	    return dataset;
	}
	
	/**
	 * Metoda zavola metodu pro znovuvytvoreni grafu a graf prida do panelu.
	 */
	public void updateChart() {
		this.chart = createChart();
		chartPanel.setChart(chart);
		
	}
}
