package gameoflife;

import java.awt.Color;
import java.awt.Graphics;

public class HiloPintador extends Thread{
    private int inicioX,finalX,inicioY,finalY,celPixeles;
    private Mundo m;
    private Graphics mdraw;

    public HiloPintador(int inicioX, int finalX, int incioY, int finalY,int celPixeles, Mundo m, Graphics mdraw) {
        this.inicioX = inicioX;
        this.finalX = finalX;
        this.inicioY = incioY;
        this.finalY = finalY;
        this.celPixeles=celPixeles;
        this.m = m;
        this.mdraw = mdraw;
    }
    @Override
    public void run(){
        for(int x=inicioX;x<finalX;x++){
            for(int y=inicioY;y<finalY;y++){
                if(m.cambioCelula(x, y)){
                    if(m.isCelViva(x, y)){
                        mdraw.setColor(Color.WHITE);
                    }else{
                        mdraw.setColor(Color.BLACK);
                    }
                    mdraw.fillRect(x*celPixeles,y*celPixeles, celPixeles,celPixeles);
                }
            }
        }
    }    
}
