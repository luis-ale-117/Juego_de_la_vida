package gameoflife;

public class HiloContador extends Thread{
    private static final byte MUERTA=0;
    private static final byte VIVA=1;
    //S:supervivencia,N:Nacimiento
    private static final byte Smin=0;
    private static final byte Smax=1;
    private static final byte Nmin=2;
    private static final byte Nmax=3;
    
    private byte[][] m,maux;
    private final int inicioX,finX,inicioY,finY;
    private volatile int vivas,muertas;
    private final int[] regla;

    /*PARA CONTROLAR EL HILO*/
    private volatile boolean trabaja;
    private volatile boolean destruido;
    
    public HiloContador(byte[][] m, byte[][] maux, int inicioX, int finX, int inicioY, int finY,int[] regla) {
        super();
        this.destruido = false;
        this.trabaja = false;
        
        this.m = m;
        this.maux = maux;
        this.inicioX = inicioX;
        this.finX = finX;
        this.inicioY = inicioY;
        this.finY = finY;
        this.vivas = 0;
        this.muertas = (finX-inicioX)*(finY-inicioY);
        this.regla=regla;
    }
    private void sigEstado(int x,int y){
        int vecinas_vivas = m[y-1][x-1] +
                    m[y-1][x] +
                    m[y-1][x+1] +
                    m[y][x-1] +
                    m[y][x+1] +
                    m[y+1][x-1] +
                    m[y+1][x] +
                    m[y+1][x+1];
        if(m[y][x]==MUERTA && vecinas_vivas>=regla[Nmin] && vecinas_vivas<=regla[Nmax]){
            maux[y][x]=VIVA;
            vivas++;
        }else if(m[y][x]==VIVA && vecinas_vivas>=regla[Smin] && vecinas_vivas<=regla[Smax]){
            maux[y][x]=VIVA;
            vivas++;
        }else{
            maux[y][x]=MUERTA;
        }
    }

    public int getVivas() {
        return vivas;
    }
    public int getMuertas() {
        return muertas;
    }
    public void intercambiaMundos(){
        byte[][] mun;
        mun = m;
        m = maux;
        maux = mun;
    }
    public void destruyeHilo(){
        this.destruido=true;
    }
    public void hazIteracion(){
        this.trabaja=true;
    }
    public boolean enActividad(){
        return trabaja;
    }
    @Override
    public void run(){
        while(!destruido){
            while(!trabaja && !destruido){
                continue;
            }
            if(trabaja && !destruido){
                vivas = 0;
                muertas = (finX-inicioX)*(finY-inicioY);
                for(int x=inicioX;x<finX;x++){
                    for(int y=inicioY;y<finY;y++){
                        sigEstado(x,y);
                    }
                }
                muertas -= vivas;
            }
            trabaja=false;
        }
    }
}
