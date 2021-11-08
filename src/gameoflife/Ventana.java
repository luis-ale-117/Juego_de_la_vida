package gameoflife;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Ventana extends JFrame{
    private static final int ESPACIO_BARRA_MENU=13;
    private static final int ESPACIO_SCROLLBAR=50;
    
    private static final int DIM_MUNDO_SIMUL=700;
    private static final int DIM_TOOLS=250;
    
    private static final int NUM_CELULAS=1000;
    private static final int CELULA_PIXELES = 5;
    
    /*CONSTANTES PARA EL ZOOM*/
    private static final int DEFAULT_DIM_ZOOM=CELULA_PIXELES*NUM_CELULAS;
    private static final int DIM_ZOOM_1N=DEFAULT_DIM_ZOOM/2;
    private static final int DIM_ZOOM_2N=DEFAULT_DIM_ZOOM/4;
    private static final int DIM_ZOOM_3N=645;
    private static final int DIM_ZOOM_1P=DEFAULT_DIM_ZOOM*3;
    private static final int DIM_ZOOM_2P=DEFAULT_DIM_ZOOM*5;
    //private static final int DIM_ZOOM_3P=DEFAULT_DIM_ZOOM*5;
    
    /*CONSTANTES PARA LA VELOCIDAD*/
    private static final int DEFAULT_SPEED=100;
    private static final int SPEED_1N=500;
    private static final int SPEED_2N=2000;
    //private static final int SPEED_3N=2000;
    private static final int SPEED_1P=25;
    private static final int SPEED_2P=1;
    private static final int SPEED_3P=0;
    private static int SPEED=DEFAULT_SPEED;
    
    /*PARA DEFINIR LA FORMA DEL MUNDO*/
    private static final String[] WORLD_TYPES={"Toroidal","Finito"};
    
    private JScrollPane scrollpanel;
    public MundoPanel mp;
    public ToolsPanel tool;
    
    public JMenuBar barra_menu;
    public JMenu archivo;
    public JMenuItem abrir_arch,save_arch;
    
    /*BANDERAS*/
    public boolean running,switch_cell,graphs_updating;
    
    private int generation,gen_calculos;
    
    public Ventana(int simulacion,int herramientas){
        this.setSize(simulacion+herramientas,simulacion+ESPACIO_BARRA_MENU);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Conway's Life");
        this.setResizable(false);
        this.setLayout(null);
    }
    
    public void iniciaVentanaComponentes(){
        mp = new MundoPanel(CELULA_PIXELES,NUM_CELULAS);
        mp.addMouse(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                //placeAnt(e);
                //changeCell(e);
            }
        });
        scrollpanel = new JScrollPane(mp);
        scrollpanel.setViewportView(mp);
        scrollpanel.setBounds(0,0,DIM_MUNDO_SIMUL-ESPACIO_SCROLLBAR,DIM_MUNDO_SIMUL-ESPACIO_SCROLLBAR);
        this.add(scrollpanel);
        tool = new ToolsPanel(DIM_MUNDO_SIMUL-ESPACIO_SCROLLBAR, DIM_TOOLS, DIM_MUNDO_SIMUL);
        this.add(tool);
        abrir_arch = new JMenuItem();
        save_arch = new JMenuItem();
        archivo =  new JMenu();
        barra_menu = new JMenuBar();
        
        archivo.setText("Archivo");
        abrir_arch.setText("Abrir archivo");
        save_arch.setText("Guardar");
        archivo.add(abrir_arch);
        archivo.add(save_arch);
        barra_menu.add(archivo);
        this.setJMenuBar(barra_menu);
        
        //gr =  new GraphicsWindow();
        
        setButtonsActions();
        
        /*INICIA LAS BANDERAS*/
        running = false;
        switch_cell = false;
        graphs_updating = false;
        
        generation = 0;
        gen_calculos=0;
    }
    public void creaMundo(){
        mp.inicializaMundo();
        mp.muestraMundo();
    }
    public void comienzaSimulacion(){
        chooseKindOfWorld();
        while(true){
            //gr.updateGraphs(gen_calculos,mp.getNumVivasSimul(),graphs_updating);
            /*EN PAUSA SI LO ESTA*/
            loopSiPausado();
            mp.muestraMundo();
            mp.sigIteracionSimul();
            mp.muestraMundo();
            tool.actualizaDatos(generation,mp.getNumMuertasSimul(),mp.getNumVivasSimul());
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*PINTA LAS CELDAS DONDE ESTUVIERON LAS HORMIGAS*/
            //paintCellsAndLines();
            //ifSimEnded();
            generation++;
            if(graphs_updating){
                gen_calculos++;
            }
        }
    }
    private void loopSiPausado(){
        while(!running){
            mp.muestraMundo();
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void chooseKindOfWorld(){
        String worldKind = (String)JOptionPane.showInputDialog(this, "Escoge un tipo de mundo para la simulación",
                "Mundo", JOptionPane.QUESTION_MESSAGE, null, WORLD_TYPES, WORLD_TYPES[0]);
        if(worldKind != null){
            if(worldKind=="Toroidal")
                mp.setToroidalSimul(true);
            else
                mp.setToroidalSimul(false);
        }
    }
    private void setButtonsActions(){
        tool.addAction_StartSim(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start_button();
            }
        });
        tool.addAction_Graph(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //showGraphicsWin();
            }
        });
        tool.addAction_Reset(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSim();
            }
        });
        tool.addAction_Random(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                randomSim();
            }
        });
        tool.addAction_EditCell(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                editCellValue();
            }
        });
        mp.addMouse(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                switchCelula(e);
            }
        });
        tool.addChange_Zoom(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                zoomSimulation();
            }
        });
        tool.addChange_Vel(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                speedSimulation();
            }
        });
        tool.addChange_Regla(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                aplicaRegla();
            }
        });
        save_arch.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveFile();
            }
        });
        abrir_arch.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openFile();
            }
        });
    }
    
    private void start_button(){
        running = !running;
        if(running){
            tool.startSimText("Pausa");
            tool.editEnable(false);
            tool.reglasEnable(false);
            tool.randomEnable(false);
            tool.resetEnable(false);
        }
        else{
            tool.startSimText("Sigue");
            tool.editEnable(true);
            tool.reglasEnable(true);
            tool.randomEnable(true);
            tool.resetEnable(true);
        }
    }
    private void zoomSimulation(){
        switch (tool.getZoomValue()){
            case 0->{
                mp.cambiaTamano(DEFAULT_DIM_ZOOM,DEFAULT_DIM_ZOOM);
                scrollpanel.setViewportView(mp);
            }
            case -1->{
                mp.cambiaTamano(DIM_ZOOM_1N,DIM_ZOOM_1N);
                scrollpanel.setViewportView(mp);
            }
            case -2->{
                mp.cambiaTamano(DIM_ZOOM_2N,DIM_ZOOM_2N);
                scrollpanel.setViewportView(mp);
            }
            case -3->{
                mp.cambiaTamano(DIM_ZOOM_3N,DIM_ZOOM_3N);
                scrollpanel.setViewportView(mp);
            }
            case 1->{
                mp.cambiaTamano(DIM_ZOOM_1P,DIM_ZOOM_1P);
                scrollpanel.setViewportView(mp);
            }
            case 2->{
                mp.cambiaTamano(DIM_ZOOM_2P,DIM_ZOOM_2P);
                scrollpanel.setViewportView(mp);
            }
            default->{
                mp.cambiaTamano(DEFAULT_DIM_ZOOM,DEFAULT_DIM_ZOOM);
                scrollpanel.setViewportView(mp);
            }
        }
    }
    private void speedSimulation(){
        boolean aux=running;
        switch (tool.getVelocValue()){
            case 0->{
                running = false;
                SPEED=DEFAULT_SPEED;
                running=aux;
            }
            case -1->{
                running = false;
                SPEED=SPEED_1N;
                running=aux;
            }
            case -2->{
                running = false;
                SPEED=SPEED_2N;
                running=aux;
            }
            case 1->{
                running = false;
                SPEED=SPEED_1P;
                running=aux;
            }
            case 2->{
                running = false;
                SPEED=SPEED_2P;
                running=aux;
            }
            case 3->{
                running = false;
                SPEED=SPEED_3P;
                running=aux;
            }
            default->{
                running = false;
                SPEED=DEFAULT_SPEED;
                running=aux;
            }
        }
    }
    private void switchEstadoCelula(){    
        switch_cell = true;
        running = false;
        tool.startSimText("Sigue");
        tool.startSimEnable(false);
        tool.setZoomDefault();
    }
    private void resetSim(){
        running = false;
        mp.resetMundoSimul();
        generation = 0;
        tool.actualizaDatos(0,mp.getNumMuertasSimul(),mp.getNumVivasSimul());
        //gr.clearGraphics();
        tool.startSimEnable(true);
        //chooseKindOfWorld();
//        if(tool.zoom_sld.getValue()<0){
//            tool.zoom_sld.setValue(0);
//        }
    }
    private void randomSim(){
        SpinnerNumberModel sModel2 = new SpinnerNumberModel(0, 0, 100, 0.1);
        JSpinner spinner2 = new JSpinner(sModel2);
        int option2 = JOptionPane.showOptionDialog(this, spinner2, "Porcentaje casillas vivas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option2 == JOptionPane.CANCEL_OPTION){
            return;
        }

        running = false;
        mp.randomMundoSimul((double)spinner2.getValue());
        generation = 0;
        tool.actualizaDatos(generation,mp.getNumMuertasSimul(),mp.getNumVivasSimul());
        tool.startSimEnable(true);
    }
    private void showGraphicsWin(){
        //gr.setVisible(!gr.isVisible());
        //graphs_updating = gr.isVisible();
    }
    private void editCellValue(){
        if(tool.editSelected()){
//            JOptionPane.showMessageDialog(this,
//                "De clic izquierdo para alternar el estado de la celula",
//                "Configuración de la celula",
//                JOptionPane.WARNING_MESSAGE);
            running = false;
            tool.startSimText("Sigue");
            tool.startSimEnable(false);
            tool.randomEnable(false);
            tool.resetEnable(false);
        }else{
            tool.startSimEnable(true);
            tool.randomEnable(true);
            tool.resetEnable(true);
        }
        
    }
    private void switchCelula(MouseEvent e){
        if(tool.editSelected()){
            int pos_x,pos_y;
            pos_x = ((int)e.getX())/(CELULA_PIXELES);
            pos_y = ((int)e.getY())/(CELULA_PIXELES);
            if(e.getButton()==MouseEvent.BUTTON1){//Izquierdo
                mp.switchCelEstadoSimul(pos_x, pos_y);
            }
        }
    }
    private void aplicaRegla(){
        mp.setReglaSimul(tool.getSmin(),tool.getSmax(),tool.getNmin(),tool.getNmax());
    }
    private void saveFile(){
    }
    private void openFile(){
    }
}
