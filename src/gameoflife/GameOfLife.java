
package gameoflife;

public class GameOfLife {

    private static final int DIM_VENTANA=700;
    private static final int DIM_TOOLS=200;
    
    public static void main(String[] args) {
        Ventana win = new Ventana(DIM_VENTANA,DIM_TOOLS);
        win.iniciaVentanaComponentes();
        win.setVisible(true);
        win.creaMundo();
        win.comienzaSimulacion();
    }   
}
