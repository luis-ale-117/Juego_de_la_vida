
package gameoflife;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Atractores {
    private final Mundo munH1,munH2,munH3,munH4,munH5,munH6,munH7,munH8;
    private final int[] transiciones_t0,transiciones_t1,sin_simetria_t0,sin_simetria_t1;
    private int num_simet;
    private final int permutaciones,permutaciones_hilo;
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
        
        transiciones_t0=new int[permutaciones];
        transiciones_t1=new int[permutaciones];
        
        sin_simetria_t0=new int[permutaciones];
        sin_simetria_t1=new int[permutaciones];
        num_simet=0;
    }
    public void calculaTransiciones(){
        int x = munH1.getDimensionX();
        int y = munH1.getDimensionY();
        if(x>5 || y>5){
            return;
        }
        //System.out.println(permutaciones);
        //System.out.println(permutaciones_hilo);
        int perm_hilo = permutaciones_hilo;
        h1 = new HiloTransicion(munH1,0,perm_hilo,transiciones_t0,transiciones_t1);
        h2 = new HiloTransicion(munH2,perm_hilo,perm_hilo*2,transiciones_t0,transiciones_t1);
        h3 = new HiloTransicion(munH3,perm_hilo*2,perm_hilo*3,transiciones_t0,transiciones_t1);
        h4 = new HiloTransicion(munH4,perm_hilo*3,perm_hilo*4,transiciones_t0,transiciones_t1);
        
        h5 = new HiloTransicion(munH5,perm_hilo*4,perm_hilo*5,transiciones_t0,transiciones_t1);
        h6 = new HiloTransicion(munH6,perm_hilo*5,perm_hilo*6,transiciones_t0,transiciones_t1);
        h7 = new HiloTransicion(munH7,perm_hilo*6,perm_hilo*7,transiciones_t0,transiciones_t1);
        h8 = new HiloTransicion(munH8,perm_hilo*7,permutaciones,transiciones_t0,transiciones_t1);
        
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
    public void setReglaAtractores(int smin,int smax,int nmin,int nmax){
        munH1.setRegla(smin, smax, nmin, nmax);
        munH2.setRegla(smin, smax, nmin, nmax);
        munH3.setRegla(smin, smax, nmin, nmax);
        munH4.setRegla(smin, smax, nmin, nmax);
        munH5.setRegla(smin, smax, nmin, nmax);
        munH6.setRegla(smin, smax, nmin, nmax);
        munH7.setRegla(smin, smax, nmin, nmax);
        munH8.setRegla(smin, smax, nmin, nmax);
    }
    private boolean comparaEstados(byte[][] m1,byte[][] m2,int dx,int dy){
        boolean iguales = true;
        for (int y=0;y<dy;y++){
            for (int x=0;x<dx;x++){
                iguales = iguales && (m1[y][x] == m2[y][x]);
            }
        }
        return iguales;
    }
    private byte[][] trasladarEstados(byte[][] m1,boolean dir_horizontal,int dx,int dy){
        int col=dx;
        int fila=dy;
        byte[][] tras = new byte[fila][col];
        for (int y=0;y<fila;y++){
            for (int x=0;x<col;x++){
                if (dir_horizontal){
                    tras[y][(x+1)%col] = m1[y][x];
                }else{//Vertical
                    tras[(y+1)%fila][x] = m1[y][x];
                }
            }
        }
        return tras;
    }
    private byte[][] rotarEstados(byte[][] m1,int dx,int dy){
        int col=dx;
        int fila=dy;
        if (col!=fila){
            return m1;
        }
        byte[][] rotada = new byte[fila][col];
        for (int y=0;y<fila;y++){
            for (int x=0;x<col;x++){
                rotada[x][fila-1-y] = m1[y][x];
            }
        }
        return rotada;
    }
    private boolean comparaSimetria(byte[][] m1,byte[][] m_2,int dx,int dy){
        int trasV=0;
        int trasH=0;
        int rot = 0;
        byte[][] m2 = m_2;
        int x=dx;
        int y=dy;
        
        boolean simetria = false;
        while(!simetria && rot<4){
            while(!simetria && trasV<y){
                while(!simetria && trasH<x){
                    simetria = comparaEstados(m1,m2,dx,dy);
                    if (!simetria){
                        m2 = trasladarEstados(m2,true,dx,dy);
                    }
                    trasH++;
                }
                if (!simetria){
                    m2 = trasladarEstados(m2,false,dx,dy);
                }
                trasH=0;
                trasV++;
            }
            if (!simetria){
                m2 = rotarEstados(m2,dx,dy);
            }
            trasV=0;
            rot++;
        }
        return simetria;
    }
    public void eliminaSimetrias(){
        int x = munH1.getDimensionX();
        int y = munH1.getDimensionY();

        byte[][] m1;
        byte[][] m2;
        
        sin_simetria_t0[0] = transiciones_t0[0];
        sin_simetria_t1[0] = transiciones_t1[0];
        num_simet++;
        
        boolean simetria;
        /*Elimina las simetrias de origen*/
        for(int i=1;i<permutaciones;i++){
            simetria = false;
            munH1.intToMundo(transiciones_t0[i]);
            m1=munH1.getMundo();
            for(int j=0;j<num_simet;j++){
                munH2.intToMundo(sin_simetria_t0[j]);
                m2 = munH2.getMundo();
                //System.out.println(i+" "+j+"->"+simetria);
                simetria = simetria || comparaSimetria(m1,m2,x,y);
                if(simetria){ break; }
            }
            if (!simetria){
                sin_simetria_t0[num_simet] = transiciones_t0[i];
                sin_simetria_t1[num_simet] = transiciones_t1[i];
                num_simet++;
            }
        }
        /*Elimina las simetrias de destino*/
        
        
        
        for(int i=1;i<num_simet;i++){
            simetria = false;
            munH1.intToMundo(sin_simetria_t1[i]);
            m1=munH1.getMundo();
            for(int j=0;j<num_simet;j++){
                munH2.intToMundo(sin_simetria_t0[j]);
                m2 = munH2.getMundo();
                simetria = comparaSimetria(m1,m2,x,y);
                if(simetria){
                    sin_simetria_t1[i] = sin_simetria_t0[j];
                    break;
                }
            }
        }
    }
    public int getNumSimetrias(){
        return num_simet;
    }
    public String toStringInicio(){
        return "strict digraph G{\noverlap = true\nmaxiter = 500\n";
    }
    public String toStringIndex(int i){
        return transiciones_t0[i]+"->"+transiciones_t1[i]+"\n";
    }
    public String toStringIndexSimetrias(int i){
        return sin_simetria_t0[i]+"->"+sin_simetria_t1[i]+"\n";
    }
    public String toStringFin(){
        return "}";
    }
    @Override
    public String toString(){
        return "";
    }
}