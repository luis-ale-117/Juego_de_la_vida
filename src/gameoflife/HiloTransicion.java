
package gameoflife;

public class HiloTransicion extends Thread{
    public Mundo m;
    public int index_inicio,index_fin;
    public int[] transiciones_t0,transiciones_t1;

    public HiloTransicion(Mundo m, int index_inicio, int index_fin,int[] transiciones_t0,int[] transiciones_t1) {
        super();
        this.m = m;
        this.index_inicio = index_inicio;
        this.index_fin = index_fin;
        this.transiciones_t0 = transiciones_t0;
        this.transiciones_t1 = transiciones_t1;
    }
    @Override
    public void run(){
        int valmundo;
        for(int i=index_inicio;i<index_fin;i++){
            m.intToMundo(i);
            m.sigIteracionHilos();
            valmundo = m.mundoToInt();
            transiciones_t0[i]= i;
            transiciones_t1[i]= valmundo;
        }
    }
    
}
