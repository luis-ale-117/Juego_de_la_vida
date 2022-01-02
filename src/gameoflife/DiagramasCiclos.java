
package gameoflife;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DiagramasCiclos extends JPanel{
    
    private static final int PIXELES = 15;
    
    private JScrollPane diagrama_scroll;
    private JLabel diagrama_panel,mundo_actual,mundo_siguiente,rule_label;
    private ImageIcon diagrama_img;
    private JButton simulacion,calcula_atractores;
    private JSpinner mundoX,mundoY,config,smin,smax,nmin,nmax;
    private SpinnerNumberModel sm1,sm2,sm3;
    private MundoPanel mu1,mu2;
    
    
    DiagramasCiclos(){
        super();
        this.setSize(1150,700);
        this.setLayout(null);
        this.setBackground(Color.GRAY);
        init_components();
    }
    private void init_components(){
        
        diagrama_img = new ImageIcon("out.png");
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
        
        mundo_actual = new JLabel("Mundo en t");
        mundo_actual.setBounds(675,100, 160, 20);
        mundo_actual.setEnabled(true);
        this.add(mundo_actual);
        
        mundo_actual = new JLabel("Mundo en t+1");
        mundo_actual.setBounds(675,200, 160, 20);
        mundo_actual.setEnabled(true);
        this.add(mundo_actual);
        
        mu1 = new MundoPanel(PIXELES,(int)mundoX.getValue(),(int)mundoY.getValue());
        mu1.setBounds(675,120,5*PIXELES,5*PIXELES);
        this.add(mu1);
        mu2 = new MundoPanel(PIXELES,(int)mundoX.getValue(),(int)mundoY.getValue());
        mu2.setBounds(675,220,5*PIXELES,5*PIXELES);
        this.add(mu2);
        
        
        rule_label = new JLabel("Regla: Smin   Smax   Nmin   Nmax");
        rule_label.setBounds(675, 290, 200, 25);
        SpinnerNumberModel sm1 = new SpinnerNumberModel(2, 0, 8, 1);
        SpinnerNumberModel sm2 = new SpinnerNumberModel(3, 0, 8, 1);
        SpinnerNumberModel sm3 = new SpinnerNumberModel(3, 0, 8, 1);
        SpinnerNumberModel sm4 = new SpinnerNumberModel(3, 0, 8, 1);
        smin = new JSpinner(sm1);
        smax = new JSpinner(sm2);
        nmin = new JSpinner(sm3);
        nmax = new JSpinner(sm4);
        smin.setBounds(714, 315, 33, 25);//
        smax.setBounds(753, 315, 33, 25);//+39
        nmin.setBounds(793,315, 33, 25);//+40
        nmax.setBounds(832, 315,33, 25);//+38
        
        this.add(rule_label);
        this.add(smin);
        this.add(smax);
        this.add(nmin);
        this.add(nmax);
        
        calcula_atractores = new JButton("Calcula atractores");
        calcula_atractores.setBounds(675,350, 140, 30);
        calcula_atractores.setEnabled(true);
        this.add(calcula_atractores);
        
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
        
        smin.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaRegla();
                }
        });
        smax.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaRegla();
                }
        });
        nmin.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaRegla();
                }
        });
        nmax.addChangeListener(
            new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e){
                    cambiaRegla();
                }
        });
        calcula_atractores.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculaAtractoresDot();
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
        mu1.setReglaSimul((int)smin.getValue(), (int)smax.getValue(), (int)nmin.getValue(), (int)nmax.getValue());
        mu2.setReglaSimul((int)smin.getValue(), (int)smax.getValue(), (int)nmin.getValue(), (int)nmax.getValue());
        
        mu1.setVisible(false);mu2.setVisible(false);
        mu1.intToMundoPanel((int)config.getValue());

        mu2.intToMundoPanel((int)config.getValue());
        mu2.sigIteracionTodasSecuencial();

        mu1.setVisible(true);mu2.setVisible(true);
    }
    private void cambiaRegla(){
        mu1.setReglaSimul((int)smin.getValue(), (int)smax.getValue(), (int)nmin.getValue(), (int)nmax.getValue());
        mu2.setReglaSimul((int)smin.getValue(), (int)smax.getValue(), (int)nmin.getValue(), (int)nmax.getValue());
        cambiaMundoMostrado();
    }
    private void calculaAtractoresDot(){
        int n=(int)mundoX.getValue(),m=(int)mundoY.getValue(),res;
        Atractores at= new Atractores(n,m);
        int[] regint = mu1.getReglaSimul();
        String regla = "S"+regint[0]+""+regint[1]+"N"+regint[2]+""+regint[3];
        at.setReglaAtractores((int)smin.getValue(), (int)smax.getValue(),(int)nmin.getValue(),(int)nmax.getValue());
        String layout;
        if(n<4 && m<4){ layout = "-Kneato";} else{layout = "-Ksfdp";}
        
    /* ***********************************************
     * ************ARCHIVOS .DOT DEL DIAGRAMA*********
     * ***********************************************/
        File f = new File(n+"x"+m+"_"+regla+".dot");
        res=JOptionPane.YES_OPTION;
        if(f.exists()){
            res = JOptionPane.showConfirmDialog(this,
                "Los atractores " +n+"x"+m+" ya han sido calculados.\n¿Desea recalcularlos?",
                "Atractores ya calculados",
                JOptionPane.YES_NO_OPTION);
        }else{
            res = JOptionPane.showConfirmDialog(this,
                "¿Desea calcular los atractores para " +n+"x"+m+"?",
                "Calcular atractores",
                JOptionPane.YES_NO_OPTION);
        }
        if (res==JOptionPane.YES_OPTION){
            at.calculaTransiciones();////////////////////////////
            FileWriter f_w = null;
            PrintWriter f_pr = null;
            try {
                f_w = new FileWriter(f);
                f_pr = new PrintWriter(f_w);
                f_pr.write(at.toStringInicio());
                int pot = (int) Math.pow(2, n*m);
                for(int i=0;i<pot;i++){
                    f_pr.write(at.toStringIndex(i));
                }
                f_pr.write(at.toStringFin());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al intentar abrir el archvio\nError: "+ex,
                    "Error en Archivo",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    f_pr.close();
                    f_w.close();
                    JOptionPane.showMessageDialog(this,
                    "Calculo de atractores exitoso para un mundo "+n+"x"+m
                            +". Guardado en:\n"+f.getAbsolutePath(),
                    "Calculo completado",
                    JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error al intentar cerrar el archvio\nError: "+ex,
                        "Error en Archivo",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        File f2 = new File(n+"x"+m+"_"+regla+"_SIM"+".dot");
        res=JOptionPane.YES_OPTION;
        if(f2.exists()){
            res = JOptionPane.showConfirmDialog(this,
                "Los atractores sin simetrías" +n+"x"+m+" ya han sido calculados.\n¿Desea recalcularlos?",
                "Atractores sin simetrías ya calculados",
                JOptionPane.YES_NO_OPTION);
        }else{
            res = JOptionPane.showConfirmDialog(this,
                "¿Desea calcular los atractores sin simetrías para " +n+"x"+m+"?",
                "Calcular atractores sin simetrías",
                JOptionPane.YES_NO_OPTION);
        }
        if (res==JOptionPane.YES_OPTION){
            at.calculaTransiciones();////////////////////////////
            at.eliminaSimetrias();///////////////////////////////
            FileWriter f_w = null;
            PrintWriter f_pr = null;
            try {
                f_w = new FileWriter(f2);
                f_pr = new PrintWriter(f_w);
                f_pr.write(at.toStringInicio());
                int pot = at.getNumSimetrias();
                for(int i=0;i<pot;i++){
                    f_pr.write(at.toStringIndexSimetrias(i));
                }
                f_pr.write(at.toStringFin());
                //f_pr.write(at4x4.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al intentar abrir el archvio\nError: "+ex,
                    "Error en Archivo",
                    JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    f_pr.close();
                    f_w.close();
                    JOptionPane.showMessageDialog(this,
                    "Calculo de atractores sin simetrías exitoso para un mundo "+n+"x"+m
                            +". Guardado en:\n"+f2.getAbsolutePath(),
                    "Calculo completado sin simetrías",
                    JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error al intentar cerrar el archvio\nError: "+ex,
                        "Error en Archivo",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    /* ***********************************************
     * *****************IMAGENES DEL DIAGRAMA*********
     * ***********************************************/
        File f3 = new File("Diag"+n+"x"+m+"_"+regla+".png");
        if(f3.exists()){
            res = JOptionPane.showConfirmDialog(this,
                "El diagrama " +n+"x"+m+" ya existe.\n¿Desea recrearlo?",
                "Diagrama ya creado",
                JOptionPane.YES_NO_OPTION);
        }else{
            res = JOptionPane.showConfirmDialog(this,
                "¿Desea generar el diagrama para " +n+"x"+m+"?",
                "Generar diagrama",
                JOptionPane.YES_NO_OPTION);
        }
        if(f.exists() && f.length()>1000000 && res==JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(this,
                    "Error al intentar generar el diagrama, pues es demasiado grande.\n"
                            + "Se recomienda reducir su tamaño eliminando simetrías",
                    "Error al generar diagrama",
                    JOptionPane.ERROR_MESSAGE);
        }else if(f.exists() && res==JOptionPane.YES_OPTION){
            ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "dot "+layout+" -Tpng "+f.getPath()+" > "+f3.getPath());
            builder.redirectErrorStream(true);
            Process p;
            try {
                p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }
                JOptionPane.showMessageDialog(this,
                    "Diagrama generado exitosamente para un mundo "+n+"x"+m
                            +". Guardado en:\n"+f3.getAbsolutePath(),
                    "Diagrama generado",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(DiagramasCiclos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (res==JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(this,
                    "Error al intentar generar el diagrama,"+f.getPath()+" No existe.\n"
                            + "Se recomienda calcular los atractores primero",
                    "Error al generar diagrama",
                    JOptionPane.ERROR_MESSAGE);
        }
        File f4 = new File("Diag"+n+"x"+m+"_"+regla+"_SIM"+".png");
        if(f4.exists()){
            res = JOptionPane.showConfirmDialog(this,
                "El diagrama " +n+"x"+m+" ya existe sin simetrías.\n¿Desea recrearlo?",
                "Diagrama ya creado sin simetrías",
                JOptionPane.YES_NO_OPTION);
        }else{
            res = JOptionPane.showConfirmDialog(this,
                "¿Desea generar el diagrama para " +n+"x"+m+" sin simetrías?",
                "Generar diagrama sin simetrías",
                JOptionPane.YES_NO_OPTION);
        }
        if(f2.exists() && res==JOptionPane.YES_OPTION){
            ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "dot "+layout+" -Tpng "+f2.getPath()+" > "+f4.getPath());
            builder.redirectErrorStream(true);
            Process p;
            try {
                p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }
                JOptionPane.showMessageDialog(this,
                    "Diagrama generado exitosamente para un mundo "+n+"x"+m
                            +". Guardado en:\n"+f4.getAbsolutePath(),
                    "Diagrama generado",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(DiagramasCiclos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (res==JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(this,
                    "Error al intentar generar el diagrama,"+f2.getPath()+" No existe.\n"
                            + "Se recomienda calcular los atractores primero",
                    "Error al generar diagrama",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}