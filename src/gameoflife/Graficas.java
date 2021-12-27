
package gameoflife;

import javax.swing.JFrame;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficas extends JFrame{
    XYSeries data_black_cells,log_data_black_cells, prm_series,var_series;
    XYSeriesCollection black_cell_series,log_black_cell_series, prm_coll,var_coll;
    JFreeChart black_cells_graph,log_black_cells_graph, prom_graph, var_graph;
    ChartPanel bc_chart,log_bc_chart, prm_chart,var_chart;
    
    JLabel media_label,var_label;
    double media,var_std;
    
    Graficas(){
        super();
        this.setSize(1150,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Game of life graphics");
        this.setResizable(false);
        this.setLayout(null);
        init_components();
    }
    private void init_components(){
        
        data_black_cells = new XYSeries("Celulas vivas");
        black_cell_series = new XYSeriesCollection();
        black_cell_series.addSeries(data_black_cells);
        black_cells_graph = ChartFactory.createXYLineChart("Celulas vivas","Generacion","Celulas",black_cell_series,PlotOrientation.VERTICAL,true,false,false);
        bc_chart = new ChartPanel(black_cells_graph);
        bc_chart.setBounds(0, 0, 400, 300);
        add(bc_chart);
        
        log_data_black_cells = new XYSeries("Celulas vivas en log");
        log_black_cell_series = new XYSeriesCollection();
        log_black_cell_series.addSeries(log_data_black_cells);
        log_black_cells_graph = ChartFactory.createXYLineChart("Logaritmo celulas vivas","Generacion","Log celulas",log_black_cell_series,PlotOrientation.VERTICAL,true,false,false);
        log_bc_chart = new ChartPanel(log_black_cells_graph);
        log_bc_chart.setBounds(0, 305, 400, 300);
        add(log_bc_chart);
        
        prm_series = new XYSeries("Prom celulas vivas");
        prm_coll = new XYSeriesCollection();
        prm_coll.addSeries(prm_series);
        prom_graph = ChartFactory.createXYLineChart("Media","Generacion","Celulas",prm_coll,PlotOrientation.VERTICAL,true,false,false);
        prm_chart = new ChartPanel(prom_graph);
        prm_chart.setBounds(405, 0, 400, 300);
        add(prm_chart);
        
        var_series = new XYSeries("Vaianza celulas vivas");
        var_coll = new XYSeriesCollection();
        var_coll.addSeries(var_series);
        var_graph = ChartFactory.createXYLineChart("Varianza","Generacion","Celulas^2",var_coll,PlotOrientation.VERTICAL,true,false,false);
        var_chart = new ChartPanel(var_graph);
        var_chart.setBounds(405, 305, 400, 300);
        add(var_chart);
        
        media_label = new JLabel("Media: 0");
        media_label.setBounds(810, 0, 210, 30);
        add(media_label);
        
        var_label = new JLabel("Varianza: 0");
        var_label.setBounds(810, 40, 210, 30);
        add(var_label);
        
        media = 0;
        var_std=0;
    }
    public void updateGraphs(int generation, int num_black, boolean show){
        if (show==false) return;
        data_black_cells.add(generation, num_black);
        media = media + (num_black-media)*((double)1/(generation+1));
        prm_series.add(generation,media);
        varianza();
        var_series.add(generation,var_std);
        if(Math.log10(num_black)<0){
            log_data_black_cells.add(generation,0.1);
        }
        else{
            log_data_black_cells.add(generation,Math.log10(num_black));
        }
        var_label.setText("Varianza: "+var_std);
        media_label.setText("Media: "+media);
    }
    private double varianza(){
        XYDataItem aux;
        int n = data_black_cells.getItems().size();
        for(Object d: data_black_cells.getItems()){
            aux = (XYDataItem)d;
            var_std+= (aux.getYValue()-media)*(aux.getYValue()-media);
        }
        var_std=var_std/n;
        return var_std;
    }
    public void clearGraphics(){
        data_black_cells.clear();
        prm_series.clear();
        log_data_black_cells.clear();
        var_series.clear();
        media=0;
        var_std=0;
        var_label.setText("Varianza: 0");
        media_label.setText("Media: 0");
    }
}

