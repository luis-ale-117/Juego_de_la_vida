
package gameoflife;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Atractores {
    private final Mundo munH1,munH2,munH3,munH4,munH5,munH6,munH7,munH8;
    private final int[] transiciones_t0_1,transiciones_t1_1;
    private final int[] transiciones_t0_2,transiciones_t1_2;
    private final int[] transiciones_t0_3,transiciones_t1_3;
    private final int[] transiciones_t0_4,transiciones_t1_4;
    private final int[] transiciones_t0_5,transiciones_t1_5;
    private final int[] transiciones_t0_6,transiciones_t1_6;
    private final int[] transiciones_t0_7,transiciones_t1_7;
    private final int[] transiciones_t0_8,transiciones_t1_8;
    private int permutaciones,permutaciones_hilo;
    private HiloTransicion h1,h2,h3,h4,h5,h6,h7,h8;
    
    Atractores(int tamx, int tamy){
        munH1 = new Mundo(tamx,tamy);
        munH2 = new Mundo(tamx,tamy);
        munH3 = new Mundo(tamx,tamy);
        munH4 = new Mundo(tamx,tamy);
        munH5 = new Mundo(tamx,tamy);
        munH6 = new Mundo(tamx,tamy);
        munH7 = new Mundo(tamx,tamy);
        munH8 = new Mundo(tamx,tamy);
        
        permutaciones = (int)Math.pow(2, tamx*tamy);
        permutaciones_hilo = permutaciones/8;
        
        int num_trans = permutaciones;
        int tam_arr_trans = permutaciones_hilo;
        transiciones_t0_1=new int[tam_arr_trans]; transiciones_t1_1=new int[tam_arr_trans];
        transiciones_t0_2=new int[tam_arr_trans]; transiciones_t1_2=new int[tam_arr_trans];
        transiciones_t0_3=new int[tam_arr_trans]; transiciones_t1_3=new int[tam_arr_trans];
        transiciones_t0_4=new int[tam_arr_trans]; transiciones_t1_4=new int[tam_arr_trans];
        transiciones_t0_5=new int[tam_arr_trans]; transiciones_t1_5=new int[tam_arr_trans];
        transiciones_t0_6=new int[tam_arr_trans]; transiciones_t1_6=new int[tam_arr_trans];
        transiciones_t0_7=new int[tam_arr_trans]; transiciones_t1_7=new int[tam_arr_trans];
        transiciones_t0_8=new int[tam_arr_trans]; transiciones_t1_8=new int[tam_arr_trans];
    }
    public void calculaTransiciones(){
        int x = munH1.getDimensionX();
        int y = munH1.getDimensionY();
        if(x>5 || y>5){
            return;
        }
        int perm_hilo = permutaciones_hilo;
        h1 = new HiloTransicion(munH1,0,perm_hilo,transiciones_t0_1,transiciones_t1_1);
        h2 = new HiloTransicion(munH2,perm_hilo,perm_hilo*2,transiciones_t0_2,transiciones_t1_2);
        h3 = new HiloTransicion(munH3,perm_hilo*2,perm_hilo*3,transiciones_t0_3,transiciones_t1_3);
        h4 = new HiloTransicion(munH4,perm_hilo*3,perm_hilo*4,transiciones_t0_4,transiciones_t1_4);
        
        h5 = new HiloTransicion(munH5,perm_hilo*4,perm_hilo*5,transiciones_t0_5,transiciones_t1_5);
        h6 = new HiloTransicion(munH6,perm_hilo*5,perm_hilo*6,transiciones_t0_6,transiciones_t1_6);
        h7 = new HiloTransicion(munH7,perm_hilo*6,perm_hilo*7,transiciones_t0_7,transiciones_t1_7);
        h8 = new HiloTransicion(munH8,perm_hilo*7,permutaciones,transiciones_t0_8,transiciones_t1_8);
        
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        
        h5.start();
        h6.start();
        h7.start();
        h8.start();
        try {
            h1.join();
            h2.join();
            h3.join();
            h4.join();
            
            h5.join();
            h6.join();
            h7.join();
            h8.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MundoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String toStringInicio(){
        return "strict digraph G{\noverlap = true\nmaxiter = 500\n";
    }
    public String toStringIndex(int i){
        switch(i/permutaciones_hilo){
            case(0)->{
                return transiciones_t0_1 + "->" + transiciones_t1_1;
            }
            case(1)->{
                return transiciones_t0_2 + "->" + transiciones_t1_2;
            }
            case(2)->{
                return transiciones_t0_3 + "->" + transiciones_t1_3;
            }
            case(3)->{
                return transiciones_t0_4 + "->" + transiciones_t1_4;
            }
            case(4)->{
                return transiciones_t0_5 + "->" + transiciones_t1_5;
            }
            case(5)->{
                return transiciones_t0_6 + "->" + transiciones_t1_6;
            }
            case(6)->{
                return transiciones_t0_7 + "->" + transiciones_t1_7;
            }
            case(7)->{
                return transiciones_t0_8 + "->" + transiciones_t1_8;
            }
            default->{
                return "";
            }
        }
    }
    public String toStringFin(){
        return "}";
    }
    @Override
    public String toString(){
        return "";
    }
}
