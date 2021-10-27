package gameoflife;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;

public class ToolsPanel extends JPanel{
    
    private JLabel generacion,celdas_muertas,celdas_vivas,zo_label,vel_label;
    private JButton start_sim,graph,reset,random;
    private JToggleButton edit_cell;
    private JSlider zoom_sld,vel_sld;
    
    ToolsPanel(int inicio, int dimenX,int dimenY){
        super();
        this.setBounds(inicio,0, dimenX, dimenY);
        this.setBackground(Color.gray);
        this.setLayout(null);
        initComponents();
    }
    
    private void initComponents(){
        generacion = new JLabel("Generacion: ");
        generacion.setBounds(5, 0, 180, 30);
        this.add(generacion);
        
        celdas_muertas = new JLabel("Celdas muertas: ");
        celdas_muertas.setBounds(5, 31, 180, 30);
        this.add(celdas_muertas);
        
        celdas_vivas = new JLabel("Celas vivas: ");
        celdas_vivas.setBounds(5, 62, 120, 30);
        this.add(celdas_vivas);
        
        start_sim = new JButton("Inicia");
        start_sim.setBounds(5, 93, 80, 30);
        start_sim.setEnabled(true);
        this.add(start_sim);
        
        zoom_sld = new JSlider(-3,2,0);
        zoom_sld.setMajorTickSpacing(1);
        zoom_sld.setMinorTickSpacing(1);
        zoom_sld.setPaintTicks(true);
        zoom_sld.setPaintLabels(true);
        zoom_sld.setBounds(5, 124, 160, 50);
        zoom_sld.setBackground(Color.GRAY);
        this.add(zoom_sld);
        zo_label = new JLabel("ZOOM");
        zo_label.setBounds(168, 130, 70, 30);
        this.add(zo_label);
        
        vel_sld = new JSlider(-2,3,0);
        vel_sld.setMajorTickSpacing(1);
        vel_sld.setMinorTickSpacing(1);
        vel_sld.setPaintTicks(true);
        vel_sld.setPaintLabels(true);
        vel_sld.setBounds(5, 174, 160, 50);
        vel_sld.setBackground(Color.GRAY);
        this.add(vel_sld);
        vel_label = new JLabel("SPEED");
        vel_label.setBounds(168, 180, 70, 30);
        this.add(vel_label);

        graph = new JButton("Graficas");
        graph.setBounds(5, 230, 90, 30);
        this.add(graph);
        
        reset = new JButton("Reset");
        reset.setBounds(100, 230, 80, 30);
        this.add(reset);
        
        random = new JButton("Random");
        random.setBounds(5, 265, 90, 30);
        this.add(random);
        
        edit_cell = new JToggleButton("Edit");
        edit_cell.setBounds(5, 300, 90, 30);
        this.add(edit_cell);
    }
    public void startSimEnable(boolean b){
        start_sim.setEnabled(b);
    }
    public void startSimText(String t){
        start_sim.setText(t);
    }
    public void editEnable(boolean b){
        edit_cell.setEnabled(b);
    }
    public boolean editSelected(){
        return edit_cell.isSelected();
    }
    public void setZoomDefault(){
        zoom_sld.setValue(0);
    }
    public int getZoomValue(){
        return zoom_sld.getValue();
    }
    public int getVelocValue(){
        return vel_sld.getValue();
    }
    
    public void actualizaDatos(int gen,int cant_celdas_muertas, int cant_celdas_vivas){
        generacion.setText("Generacion: "+gen);
        celdas_muertas.setText("Celdas muertas: "+cant_celdas_muertas);
        celdas_vivas.setText("Celdas vivas: "+cant_celdas_vivas);
    }
    public void addAction_StartSim(ActionListener l){
        start_sim.addActionListener(l);
    }
    public void addAction_Graph(ActionListener l){
        graph.addActionListener(l);
    }
    public void addAction_Reset(ActionListener l){
        reset.addActionListener(l);
    }
    public void addAction_Random(ActionListener l){
        random.addActionListener(l);
    }
    public void addAction_EditCell(ActionListener l){
        edit_cell.addActionListener(l);
    }
    public void addChange_Zoom(ChangeListener l){
        zoom_sld.addChangeListener(l);
    }
    public void addChange_Vel(ChangeListener l){
        vel_sld.addChangeListener(l);
    }
}