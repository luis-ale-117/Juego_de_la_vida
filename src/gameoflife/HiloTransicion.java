
package gameoflife;

public class HiloTransicion extends Thread{
    public Mundo m;
    public int index_inicio,index_fin;
    public Transicion[] transiciones;

    public HiloTransicion(Mundo m, int index_inicio, int index_fin,Transicion[] transiciones) {
        super();
        this.m = m;
        this.index_inicio = index_inicio;
        this.index_fin = index_fin;
        this.transiciones = transiciones;
    }
    @Override
    public void run(){
        int valmundo;
        for(int i=index_inicio;i<index_fin;i++){
            m.intToMundo(i);
            m.sigIteracion();
            valmundo = m.mundoToInt();
            transiciones[i]= new Transicion(i,valmundo);
        }
    }
    
}
