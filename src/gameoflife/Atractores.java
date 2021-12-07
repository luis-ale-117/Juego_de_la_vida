
package gameoflife;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Atractores {
    private final Mundo munH1,munH2,munH3,munH4,munH5,munH6,munH7,munH8;
    private final Transicion[] transiciones;
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
        
        transiciones = new Transicion[(int)Math.pow(2, tamx*tamy)];
    }
    public void calculaTransiciones(){
        int x = munH1.getDimensionX();
        int y = munH1.getDimensionY();
        if(x>5 || y>5){
            return;
        }
        int permutaciones = (int)Math.pow(2, x*y);
        h1 = new HiloTransicion(munH1,0,permutaciones/8,transiciones);
        h2 = new HiloTransicion(munH2,permutaciones/8,permutaciones/4,transiciones);
        h3 = new HiloTransicion(munH3,permutaciones/4,3*(permutaciones/8),transiciones);
        h4 = new HiloTransicion(munH4,3*(permutaciones/8),permutaciones/2,transiciones);
        
        h5 = new HiloTransicion(munH5,permutaciones/2,5*(permutaciones/8),transiciones);
        h6 = new HiloTransicion(munH6,5*(permutaciones/8),3*(permutaciones/4),transiciones);
        h7 = new HiloTransicion(munH7,3*(permutaciones/4),7*(permutaciones/8),transiciones);
        h8 = new HiloTransicion(munH8,7*(permutaciones/8),permutaciones,transiciones);
        
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
        /*int valmundo;
        for(int i=0;i<permutaciones;i++){
            mun.intToMundo(i);
            mun.sigIteracion();
            valmundo = mun.mundoToInt();
            transiciones[i]= new Transicion(i,valmundo);
        }*/
    }
    public String toStringInicio(){
        return "strict digraph G{\noverlap = true\nmaxiter = 500\n";
    }
    public String toStringIndex(int i){
        return transiciones[i].toString();
    }
    public String toStringFin(){
        return "}";
    }
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(10000);
        str.append("strict digraph G{\n");
        str.append("overlap = true\nmaxiter = 500\n");
        for(Transicion t: transiciones){
            str.append(t.toString());
        }
        str.append("}");
        return str.toString();
    }
}
