
package gameoflife;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DiagramasCiclos extends JPanel{
    
    private static final int PIXELES = 15;
    
    private JScrollPane diagrama_scroll;
    private JLabel diagrama_panel;
    private ImageIcon diagrama_img;
    private JButton simulacion;
    private JSpinner mundoX,mundoY,config;
    private SpinnerNumberModel sm1,sm2,sm3;
    private MundoPanel mu1,mu2;
    
    
    DiagramasCiclos(){
        super();
        this.setSize(1150,700);
        //this.setLocationRelativeTo(null);
        //this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //this.setTitle("Game of life graphics");
        //this.setResizable(false);
        this.setLayout(null);
        init_components();
    }
    private void init_components(){
        
        diagrama_img = new ImageIcon("out3x3.png");
        diagrama_panel=new JLabel(diagrama_img);
        
        diagrama_scroll = new JScrollPane(diagrama_panel);
        diagrama_scroll.setViewportView(diagrama_panel);
        diagrama_scroll.setBounds(0,0,650,650);
        diagrama_scroll.setBackground(Color.GRAY);
        this.add(diagrama_scroll);
        
        simulacion = new JButton("Regresa simulación");
        simulacion.setBounds(695,5, 160, 30);
        simulacion.setEnabled(true);
        this.add(simulacion);
        
        sm1 = new SpinnerNumberModel(2, 2, 5, 1);
        sm2 = new SpinnerNumberModel(2, 2, 5, 1);
        mundoX = new JSpinner(sm1);
        mundoY = new JSpinner(sm2);
        mundoX.setBounds(675, 50, 30, 30);
        mundoY.setBounds(715, 50, 30, 30);
        this.add(mundoX);
        this.add(mundoY);
        
        sm3 = new SpinnerNumberModel(0, 0, (int)Math.pow(2,(int)mundoX.getValue()*(int)mundoY.getValue())-1, 1);
        config = new JSpinner(sm3);
        config.setBounds(755, 50, 110, 30);
        this.add(config);
        
        mu1 = new MundoPanel(PIXELES,(int)mundoX.getValue(),(int)mundoY.getValue());
        mu1.setBounds(675,100,5*PIXELES,5*PIXELES);
        this.add(mu1);
        mu2 = new MundoPanel(PIXELES,(int)mundoX.getValue(),(int)mundoY.getValue());
        mu2.setBounds(675,200,5*PIXELES,5*PIXELES);
        this.add(mu2);
        
        add_ActionComponents();
    }
    public void addAction_Simul(ActionListener l){
        simulacion.addActionListener(l);
    }
    private void add_ActionComponents(){
        mundoX.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaDimensionesMundoPrueba();
                }
            });
        mundoY.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaDimensionesMundoPrueba();
                }
            });
        config.addChangeListener(
                new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaMundoMostrado();
                }
            });
    }
    private void cambiaDimensionesMundoPrueba(){
        mu1.setVisible(false);mu2.setVisible(false);
        mu1.cambiaDimensiones((int)mundoX.getValue(), (int)mundoY.getValue());
        mu2.cambiaDimensiones((int)mundoX.getValue(), (int)mundoY.getValue());
        mu1.setVisible(true);mu2.setVisible(true);
        sm3.setValue((int)0);
        sm3.setMaximum((int)Math.pow(2,(int)mundoX.getValue()*(int)mundoY.getValue())-1);
    }
    private void cambiaMundoMostrado(){
        mu1.setVisible(false);mu2.setVisible(false);
        mu1.intToMundoPanel((int)config.getValue());

        mu2.intToMundoPanel((int)config.getValue());
        mu2.sigIteracionTodasSecuencial();

        mu1.setVisible(true);mu2.setVisible(true);
    }
}
