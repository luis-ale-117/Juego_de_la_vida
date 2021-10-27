package gameoflife;

import java.awt.Color;
import java.awt.Graphics;

public class HiloPintador extends Thread{
    private int inicioX,finalX,inicioY,finalY,celPixeles,offsetX,offsetY;
    private Mundo m;
    private Graphics mdraw;

    public HiloPintador(int inicioX, int finalX, int incioY, int finalY,int celPixeles, Mundo m, Graphics mdraw,int offsetX,int offsetY) {
        this.inicioX = inicioX;
        this.finalX = finalX;
        this.inicioY = incioY;
        this.finalY = finalY;
        this.celPixeles=celPixeles;
        this.m = m;
        this.mdraw = mdraw;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
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
                    mdraw.fillRect((x-offsetX)*celPixeles,(y-offsetY)*celPixeles, celPixeles,celPixeles);
                }
            }
        }
    }    
}
