package gameoflife;

public class HiloContador extends Thread{
    private static final byte MUERTA=0;
    private static final byte VIVA=1;
    //S:supervivencia,N:Nacimiento
    private static final byte Smin=0;
    private static final byte Smax=1;
    private static final byte Nmin=2;
    private static final byte Nmax=3;
    
    private final byte[][] m,maux;
    private int inicioX,finX,inicioY,finY;
    private volatile int vivas,muertas;
    private final int[] regla;

    public HiloContador(byte[][] m, byte[][] maux, int inicioX, int finX, int inicioY, int finY,int[] regla) {
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
    public void sigEstadoHash(int x,int y){
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
        }else if(m[y][x]==VIVA){
            maux[y][x]=MUERTA;
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
    @Override
    public void run(){
        for(int x=inicioX;x<finX;x++){
            for(int y=inicioY;y<finY;y++){
                sigEstadoHash(x,y);
            }
        }
        muertas -= vivas;
    }
}
