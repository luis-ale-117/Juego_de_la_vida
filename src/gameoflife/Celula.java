package gameoflife;

public class Celula{
    private byte estado;
    private static final byte MUERTA=0;
    private static final byte VIVA=1;
    Celula(){
        estado = MUERTA;
    }
    public void setViva(){
        estado = VIVA;
    }
    public void setMuerta(){
        estado = MUERTA;
    }
    public int getEstado(){
        return (int)estado;
    }
    public boolean isViva(){
        return estado==VIVA;
    }
    public boolean isMuerta(){
        return estado==MUERTA;
    }
    public void switchEstado(){
        switch (estado){
            case MUERTA -> estado = VIVA;
            case VIVA -> estado = MUERTA;
        }
    }
}
