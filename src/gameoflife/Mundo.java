
package gameoflife;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Mundo {
    private static final byte MUERTA=0;
    private static final byte VIVA=1;
    //S:supervivencia,N:Nacimiento
    private static final byte Smin=0;
    private static final byte Smax=1;
    private static final byte Nmin=2;
    private static final byte Nmax=3;
    
    private int num_vivas,num_muertas;
    private boolean toroidal;
    private final int dimensionX,dimensionY;
    private final int[] regla = new int[4];//R(Smin,Smax,Nmin,Nmax)
    
    //Para los atractores
    private int estado_int=0,n=1;
    
    private byte[][] mundo,mundo_aux;
    
    private HiloContador hc1,hc2,hc3,hc4;
    
    Mundo(int x, int y){
        toroidal = true;
        mundo = new byte[y][x];
        mundo_aux = new byte[y][x];
        dimensionX = x;
        dimensionY = y;
        num_muertas = x*y;
        num_vivas=0;
        regla[Smin]=2; regla[Smax]=3;regla[Nmin]=3;regla[Nmax]=3;//Regla clasica 
        
        for(int xs=0;xs<dimensionX;xs++){
            for(int ys=0;ys<dimensionY;ys++){
                mundo[ys][xs] = MUERTA;
                mundo_aux[ys][xs] = MUERTA;
            }
        }
        /*hc1 = new HiloContador(mundo,mundo_aux, 1, dimensionX/2, 1, dimensionY/2 ,regla);
        hc2 = new HiloContador(mundo,mundo_aux, dimensionX/2, dimensionX-1, 1, dimensionY/2 ,regla);
        hc3 = new HiloContador(mundo,mundo_aux, 1, dimensionX/2, dimensionY/2, dimensionY-1 ,regla);
        hc4 = new HiloContador(mundo,mundo_aux, dimensionX/2, dimensionX-1, dimensionY/2, dimensionY-1 ,regla);
        */
        //Para las transiciones
        estado_int=0;n=1;
    }
    public void iniciaHilosContadores(){
        hc1.start();
        hc2.start();
        hc3.start();
        hc4.start();
    }
    public void destruyeHilosContadores(){
        hc1.destruyeHilo();
        hc2.destruyeHilo();
        hc3.destruyeHilo();
        hc4.destruyeHilo();
        try {
            hc1.join();
            hc2.join();
            hc3.join();
            hc4.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MundoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setToroidal(boolean tor){
        toroidal=tor;
    }
    public boolean isToroidal(){
        return toroidal;
    }
    public void setRegla(int smin,int smax,int nmin,int nmax){
        regla[Smin]=smin; regla[Smax]=smax;regla[Nmin]=nmin;regla[Nmax]=nmax;
    }
    public int[] getRegla(){
        int[] r = {regla[Smin],regla[Smax],regla[Nmin],regla[Nmax]};
        return r;
    }
    private void sigEstadoBordesToroidal(int x,int y){
        int vecinas_vivas=0;
        
        int ymenos1 = y-1<0?dimensionY-1:y-1;
        int ymas1 = y+1>=dimensionY?0:y+1;
        int xmenos1 = x-1<0?dimensionX-1:x-1;
        int xmas1 = x+1>=dimensionX?0:x+1;
        
        vecinas_vivas =  mundo[ymenos1][xmenos1] +
                    mundo[ymenos1][x] +
                    mundo[ymenos1][xmas1] +
                    mundo[y][xmenos1] +
                    mundo[y][xmas1] +
                    mundo[ymas1][xmenos1] +
                    mundo[ymas1][x] +
                    mundo[ymas1][xmas1];
        
        if(mundo[y][x]==MUERTA && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x] = VIVA;
        }else if(mundo[y][x]==VIVA && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x] = VIVA;
        }else{
            mundo_aux[y][x] = MUERTA;
        }
    }
    /*SE CONSIDERAN CELULAS MUERTAS FUERA DE LOS BORDES*/
    private void sigEstadoBordesFinito(int x,int y){
        int vecinas_vivas=0;
        
        vecinas_vivas =  y-1<0 || x-1<0 ? 0:mundo[y-1][x-1] +
                    y-1<0 ? 0:mundo[y-1][x] +
                    y-1<0 || x+1>=dimensionX ? 0:mundo[y-1][x+1] +
                    x-1<0 ? 0:mundo[y][x-1] +
                    x+1>=dimensionX ? 0:mundo[y][x+1] +
                    y+1>=dimensionY || x-1<0 ? 0:mundo[y+1][x-1] +
                    y+1>=dimensionY ? 0:mundo[y+1][x] +
                    y+1>=dimensionY || x+1>=dimensionX ? 0:mundo[y+1][x+1];
        
        if(mundo[y][x]==MUERTA && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x]=VIVA;
        }else if(mundo[y][x]==VIVA && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x]=VIVA;
        }else{
            mundo_aux[y][x]=MUERTA;
        }
    }
    public void sigIteracionHilos(){
        /*SIN CONTAR LOS BORDES*/
        hc1.hazIteracion();
        hc2.hazIteracion();
        hc3.hazIteracion();
        hc4.hazIteracion();
        
        while(hc1.enActividad() || hc2.enActividad() || hc3.enActividad() || hc4.enActividad()){
            continue;
        }
        hc1.intercambiaMundos();
        hc2.intercambiaMundos();
        hc3.intercambiaMundos();
        hc4.intercambiaMundos();
        
        num_vivas = hc1.getVivas()+hc2.getVivas()+hc3.getVivas()+hc4.getVivas();
        num_muertas = hc1.getMuertas()+hc2.getMuertas()+hc3.getMuertas()+hc4.getMuertas();
        /*PARA LOS BORDES*/
        int bordes_vivas=0,bordes_muertas = 2*dimensionX + 2*(dimensionY-2);
        if(isToroidal()){
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesToroidal(x,0);
                sigEstadoBordesToroidal(x,dimensionY-1);
                bordes_vivas += mundo_aux[0][x];
                bordes_vivas += mundo_aux[dimensionY-1][x];
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesToroidal(0,y);
                sigEstadoBordesToroidal(dimensionX-1,y);
                bordes_vivas += mundo_aux[y][0];
                bordes_vivas += mundo_aux[y][dimensionX-1];
            }
        }else{
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesFinito(x,0);
                sigEstadoBordesFinito(x,dimensionY-1);
                bordes_vivas += mundo_aux[0][x];
                bordes_vivas += mundo_aux[dimensionY-1][x];
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesFinito(0,y);
                sigEstadoBordesFinito(dimensionX-1,y);
                bordes_vivas += mundo_aux[y][0];
                bordes_vivas += mundo_aux[y][dimensionX-1];
            }
        }
        bordes_muertas -= bordes_vivas;
        num_vivas += bordes_vivas;
        num_muertas += bordes_muertas;
        byte[][] m = mundo;
        mundo = mundo_aux;
        mundo_aux = m;
    }
    private void sigEstadoSecuencial(int x,int y){
        int vecinas_vivas = mundo[y-1][x-1] +
                    mundo[y-1][x] +
                    mundo[y-1][x+1] +
                    mundo[y][x-1] +
                    mundo[y][x+1] +
                    mundo[y+1][x-1] +
                    mundo[y+1][x] +
                    mundo[y+1][x+1];
        if(mundo[y][x]==MUERTA && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x]=VIVA;
        }else if(mundo[y][x]==VIVA && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x]=VIVA;
        }else{
            mundo_aux[y][x]=MUERTA;
        }
    }
    public void sigIteracionSecuencial(){
        num_vivas = 0;
        num_muertas = (dimensionX)*(dimensionY);
        for(int x=1;x<dimensionX-1;x++){
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoSecuencial(x,y);
                num_vivas += mundo_aux[y][x];
            }
        }
        /*PARA LOS BORDES*/
        if(isToroidal()){
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesToroidal(x,0);
                sigEstadoBordesToroidal(x,dimensionY-1);
                num_vivas += mundo_aux[0][x];
                num_vivas += mundo_aux[dimensionY-1][x];
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesToroidal(0,y);
                sigEstadoBordesToroidal(dimensionX-1,y);
                num_vivas += mundo_aux[y][0];
                num_vivas += mundo_aux[y][dimensionX-1];
            }
        }else{
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesFinito(x,0);
                sigEstadoBordesFinito(x,dimensionY-1);
                num_vivas += mundo_aux[0][x];
                num_vivas += mundo_aux[dimensionY-1][x];
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesFinito(0,y);
                sigEstadoBordesFinito(dimensionX-1,y);
                num_vivas += mundo_aux[y][0];
                num_vivas += mundo_aux[y][dimensionX-1];
            }
        }
        num_muertas -= num_vivas;
        byte[][] m = mundo;
        mundo = mundo_aux;
        mundo_aux = m;
    }
    private void sigEstadoGeneralToroidalSecuencial(int x,int y){
        int ymenos1 = y-1<0?dimensionY-1:y-1;
        int ymas1 = y+1>=dimensionY?0:y+1;
        int xmenos1 = x-1<0?dimensionX-1:x-1;
        int xmas1 = x+1>=dimensionX?0:x+1;
        
        int vecinas_vivas = mundo[ymenos1][xmenos1] +
                    mundo[ymenos1][x] +
                    mundo[ymenos1][xmas1] +
                    mundo[y][xmenos1] +
                    mundo[y][xmas1] +
                    mundo[ymas1][xmenos1] +
                    mundo[ymas1][x] +
                    mundo[ymas1][xmas1];
        if(mundo[y][x]==MUERTA && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x]=VIVA;
        }else if(mundo[y][x]==VIVA && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x]=VIVA;
        }else{
            mundo_aux[y][x]=MUERTA;
        }
    }
    public void sigIteracionGeneralToroidalSecuencial(){
        num_vivas = 0;
        num_muertas = (dimensionX)*(dimensionY);
        for(int x=0;x<dimensionX;x++){
            for(int y=0;y<dimensionY;y++){
                sigEstadoGeneralToroidalSecuencial(x,y);
                num_vivas += mundo_aux[y][x];
            }
        }
        num_muertas -= num_vivas;
        
        byte[][] m = mundo;
        mundo = mundo_aux;
        mundo_aux = m;
    }
    public boolean isCelViva(int x, int y){
        return mundo[y][x]==VIVA;
    }
    public boolean cambioCelula(int x,int y){
        return mundo[y][x]==VIVA ^ mundo_aux[y][x]==VIVA;
    }
    /*POSIBLE BORRADO*/
    public void setCelEstado(int x, int y, int est){
        if(mundo[y][x]==VIVA && est==MUERTA){
            num_muertas++;
            num_vivas--;
            mundo[y][x]=MUERTA;
        }
        else if(mundo[y][x]==MUERTA && est==VIVA){
            num_muertas--;
            num_vivas++;
            mundo[y][x]=VIVA;
        }
    }
    public void switchCelEstado(int x, int y){
        mundo[y][x] = mundo[y][x]==VIVA?MUERTA:VIVA;
        if(mundo[y][x]==VIVA){
            num_vivas++;
            num_muertas--;
        }else{
            num_vivas--;
            num_muertas++;
        }
    }
    public int getNumVivas(){
        return num_vivas;
    }
    public int getNumMuertas(){
        return num_muertas;
    }
    public void resetMundo(){
        for(int x = 0;x<dimensionX;x++){
            for(int y = 0;y<dimensionY;y++){
                mundo[y][x]=MUERTA;
            }
        }
        num_vivas=0;
        num_muertas=dimensionX*dimensionY;
    }
    public void inicioRandom(double p_v){
        double porcen_vivas = p_v*0.01;
        num_vivas=0;
        num_muertas=dimensionX*dimensionY;
        
        for(int x = 0;x<dimensionX;x++){
            for(int y = 0;y<dimensionY;y++){
                if(Math.random()<=porcen_vivas){
                    mundo[y][x]=VIVA;
                    num_vivas++;
                }else{
                    mundo[y][x]=MUERTA;
                }
            }
        }
        
        num_muertas-=num_vivas;
    }
    public int mundoToInt(){
        /*if(dimensionX>5 || dimensionY>5){
            return -1;
        }*/

        estado_int=0;
        n = 1;
        for(int x = 0;x<dimensionX;x++){
            for(int y = 0;y<dimensionY;y++){
                if(mundo[y][x]==VIVA){
                    estado_int = estado_int | n;//OR
                }
                n = n<<1;
            }
        }
        return estado_int;
        
        /*0 3 6
         *1 4 7
         *2 5 8
        */
    }
    public void intToMundo(int estado){
        /*if(dimensionX>5 || dimensionY>5){
            return;
        }
        else if (estado > Math.pow(2,dimensionX*dimensionY)){
            return;
        }*/
        n = 1;
        estado_int = estado;
        for(int x = 0;x<dimensionX;x++){
            for(int y = 0;y<dimensionY;y++){
                if((estado_int & n)>0){
                    mundo[y][x]=VIVA;
                }
                else{
                    mundo[y][x]=MUERTA;
                }
                estado_int = estado_int>>1;
            }
        }
    }

    public byte[][] getMundo() {
        byte[][] m = new byte[dimensionY][dimensionX];
        for(int x = 0; x < dimensionX;x++){
            for(int y = 0; y < dimensionY;y++){
                m[y][x] = mundo[y][x];
            }
        }
        return m;
    }
    public int getDimensionX() {
        return dimensionX;
    }
    public int getDimensionY() {
        return dimensionY;
    }
}
