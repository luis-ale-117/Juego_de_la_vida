
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
    
    private Celula[][] mundo,mundo_aux;
    
    private HiloContador hc1,hc2,hc3,hc4;
    
    Mundo(int x, int y){
        toroidal = true;
        mundo = new Celula[y][x];
        mundo_aux = new Celula[y][x];
        dimensionX = x;
        dimensionY = y;
        num_muertas = x*y;
        num_vivas=0;
        regla[Smin]=2; regla[Smax]=3;regla[Nmin]=3;regla[Nmax]=3;//Regla clasica 
        
        for(int xs=0;xs<dimensionX;xs++){
            for(int ys=0;ys<dimensionY;ys++){
                mundo[ys][xs]=new Celula();
                mundo_aux[ys][xs]=new Celula();
            }
        }
        //Para las transiciones
        estado_int=0;n=1;
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
    private void sigEstado(int x,int y){
        int vecinas_vivas=0;
        for(int xaux=x-1 ;xaux<=x+1 ;xaux++){
            for(int yaux=y-1 ;yaux<=y+1 ;yaux++){
                if((yaux!=y || xaux!=x) && mundo[yaux][xaux].isViva()){
                    vecinas_vivas++;
                }
            }
        }
        if(mundo[y][x].isMuerta() && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x].setViva();
            num_vivas++;
            num_muertas--;
        }else if(mundo[y][x].isViva() && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x].setViva();
            
        }else if(mundo[y][x].isViva()){
            mundo_aux[y][x].setMuerta();
            num_vivas--;
            num_muertas++;
        }else{
            mundo_aux[y][x].setMuerta();
        }
    }
    private void sigEstadoBordesToroidal(int x,int y){
        int vecinas_vivas=0;
        int xs,ys;
        for(int xaux=x-1 ;xaux<=x+1 ;xaux++){
            for(int yaux=y-1 ;yaux<=y+1 ;yaux++){
                xs=xaux; ys=yaux;
                
                if(yaux<0){ ys=dimensionY-1;}
                else if(yaux>dimensionY-1){ys=0;}
                
                if(xaux<0){xs=dimensionX-1;}
                else if(xaux>dimensionX-1){xs=0;}
                
                if((ys!=y || xs!=x) && mundo[ys][xs].isViva()){
                    vecinas_vivas++;
                }
            }
        }
        if(mundo[y][x].isMuerta() && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x].setViva();
//            num_vivas++;
//            num_muertas--;
        }else if(mundo[y][x].isViva() && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x].setViva();
        }else if(mundo[y][x].isViva()){
            mundo_aux[y][x].setMuerta();
//            num_vivas--;
//            num_muertas++;
        }else{
            mundo_aux[y][x].setMuerta();
        }
    }
    /*SE CONSIDERAN CELULAS MUERTAS FUERA DE LOS BORDES*/
    private void sigEstadoBordesFinito(int x,int y){
        int vecinas_vivas=0;
        for(int xaux=x-1 ;xaux<=x+1 ;xaux++){
            for(int yaux=y-1 ;yaux<=y+1 ;yaux++){
                /*Si no es fuera de los bordes*/
                if(!(yaux<0 || yaux>dimensionY-1 || xaux<0 || xaux>dimensionX-1)){
                    if((yaux!=y || xaux!=x) && mundo[yaux][xaux].isViva()){
                        vecinas_vivas++;
                    }
                }
            }
        }
        if(mundo[y][x].isMuerta() && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x].setViva();
//            num_vivas++;
//            num_muertas--;
        }else if(mundo[y][x].isViva() && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x].setViva();
        }else if(mundo[y][x].isViva()){
            mundo_aux[y][x].setMuerta();
//            num_vivas--;
//            num_muertas++;
        }else{
            mundo_aux[y][x].setMuerta();
        }
    }
    public void sigIteracion(){
        /*SIN CONTAR LOS BORDES*/
        /*POSIBLE IMPLEMENTACION DE HILOS*/
//        for(int x=1;x<dimensionX-1;x++){
//            for(int y=1;y<dimensionY-1;y++){
//                sigEstadoHash(x,y);
//            }
//        }
        hc1 = new HiloContador(mundo,mundo_aux, 1, dimensionX/2, 1, dimensionY/2 ,regla);
        hc2 = new HiloContador(mundo,mundo_aux, dimensionX/2, dimensionX-1, 1, dimensionY/2 ,regla);
        hc3 = new HiloContador(mundo,mundo_aux, 1, dimensionX/2, dimensionY/2, dimensionY-1 ,regla);
        hc4 = new HiloContador(mundo,mundo_aux, dimensionX/2, dimensionX-1, dimensionY/2, dimensionY-1 ,regla);
        
        hc1.start();
        hc2.start();
        hc3.start();
        hc4.start();
        try {
            hc1.join();
            hc2.join();
            hc3.join();
            hc4.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MundoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*Corregir*/
        num_vivas = hc1.getVivas()+hc2.getVivas()+hc3.getVivas()+hc4.getVivas();
        num_muertas = hc1.getMuertas()+hc2.getMuertas()+hc3.getMuertas()+hc4.getMuertas();
        /*PARA LOS BORDES*/
        int bordes_vivas=0,bordes_muertas = 2*dimensionX + 2*(dimensionY-2);
        if(isToroidal()){
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesToroidal(x,0);
                sigEstadoBordesToroidal(x,dimensionY-1);
                bordes_vivas += mundo_aux[0][x].getEstado();
                bordes_vivas += mundo_aux[dimensionY-1][x].getEstado();
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesToroidal(0,y);
                sigEstadoBordesToroidal(dimensionX-1,y);
                bordes_vivas += mundo_aux[y][0].getEstado();
                bordes_vivas += mundo_aux[y][dimensionX-1].getEstado();
            }
        }else{
            for(int x=0;x<dimensionX;x++){
                sigEstadoBordesFinito(x,0);
                sigEstadoBordesFinito(x,dimensionY-1);
                bordes_vivas += mundo_aux[0][x].getEstado();
                bordes_vivas += mundo_aux[dimensionY-1][x].getEstado();
            }
            for(int y=1;y<dimensionY-1;y++){
                sigEstadoBordesFinito(0,y);
                sigEstadoBordesFinito(dimensionX-1,y);
                bordes_vivas += mundo_aux[y][0].getEstado();
                bordes_vivas += mundo_aux[y][dimensionX-1].getEstado();
            }
        }
        bordes_muertas -= bordes_vivas;
        num_vivas += bordes_vivas;
        num_muertas += bordes_muertas;
        Celula[][] m = mundo;
        mundo = mundo_aux;
        mundo_aux = m;
    }
    /*POSIBLE BORRADO*/
    public int getCelEstado(int x, int y){
        return mundo[y][x].getEstado();
    }
    public boolean isCelViva(int x, int y){
        return mundo[y][x].isViva();
    }
    public boolean isCelMuerta(int x, int y){
        return mundo[y][x].isMuerta();
    }
    private boolean isCelAnteriorViva(int x, int y){
        return mundo_aux[y][x].isViva();
    }
    private boolean isCelAnteriorMuerta(int x, int y){
        return mundo_aux[y][x].isMuerta();
    }
    public boolean cambioCelula(int x,int y){
        return isCelViva(x,y) ^ isCelAnteriorViva(x,y);
    }
    /*POSIBLE BORRADO*/
    public void setCelEstado(int x, int y, int est){
        if(mundo[y][x].isViva() && est==MUERTA){
            num_muertas++;
            num_vivas--;
            mundo[y][x].setMuerta();
        }
        else if(mundo[y][x].isMuerta() && est==VIVA){
            num_muertas--;
            num_vivas++;
            mundo[y][x].setViva();
        }
    }
    public void switchCelEstado(int x, int y){
        mundo[y][x].switchEstado();
        if(mundo[y][x].isViva()){
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
                mundo[y][x].setMuerta();
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
                    mundo[y][x].setViva();
                    num_vivas++;
                }else{
                    mundo[y][x].setMuerta();
                }
            }
        }
        
        num_muertas-=num_vivas;
    }
    private void sigEstadoHash(int x,int y){
        int vecinas_vivas = mundo[y-1][x-1].getEstado() +
                    mundo[y-1][x].getEstado() +
                    mundo[y-1][x+1].getEstado() +
                    mundo[y][x-1].getEstado() +
                    mundo[y][x+1].getEstado() +
                    mundo[y+1][x-1].getEstado() +
                    mundo[y+1][x].getEstado() +
                    mundo[y+1][x+1].getEstado();
        if(mundo[y][x].isMuerta() && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            mundo_aux[y][x].setViva();
            num_vivas++;
            num_muertas--;
        }else if(mundo[y][x].isViva() && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            mundo_aux[y][x].setViva();
            
        }else if(mundo[y][x].isViva()){
            mundo_aux[y][x].setMuerta();
            num_vivas--;
            num_muertas++;
        }else{
            mundo_aux[y][x].setMuerta();
        }
    }
    public int mundoToInt(){
        /*if(dimensionX>5 || dimensionY>5){
            return -1;
        }*/

        estado_int=0;
        n = 1;
        for(int x = 0;x<dimensionX;x++){
            for(int y = 0;y<dimensionY;y++){
                if(mundo[y][x].isViva()){
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
                    mundo[y][x].setViva();
                }
                else{
                    mundo[y][x].setMuerta();
                }
                estado_int = estado_int>>1;
            }
        }
    }
    public int getDimensionX() {
        return dimensionX;
    }
    public int getDimensionY() {
        return dimensionY;
    }
}
