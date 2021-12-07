
package gameoflife;

public class Transicion {
    //public static int n=2;
    private final int edo_actual;
    private final int edo_sig;
    Transicion(){
        edo_actual = 0;
        edo_sig = 0;
    }
    Transicion(int actual, int sig){
        edo_actual = actual;
        edo_sig = sig;
    }
    /*
    public int getEdo_actual() {
        return edo_actual;
    }
    public void setEdo_actual(int edo_actual) {
        this.edo_actual = edo_actual;
    }
    public int getEdo_sig() {
        return edo_sig;
    }
    public void setEdo_sig(int edo_sig) {
        this.edo_sig = edo_sig;
    }
    
    public static int getN() {
        return n;
    }
    public static void setN(int n) {
        Transicion.n = n;
    }*/
    @Override
    public String toString(){
        return edo_actual+" -> "+edo_sig+"\n";
    }
}
