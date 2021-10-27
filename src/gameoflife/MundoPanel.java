
package gameoflife;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class MundoPanel extends JPanel{
    private int simulX,simulY;//Varia con el zoom
    private final int celPixeles,numCelX,numCelY;
    private final int imgX,imgY;//Tamaño original de la imagen
    private final BufferedImage mundoImg;
    private final Graphics mundoDraw;
    
    private final Mundo mundo;
    
    MundoPanel(int celPix,int mundoX){//Que sea cuadrado
        super();
        numCelX = mundoX;
        numCelY = mundoX;
        celPixeles = celPix;
        simulX = numCelX*celPixeles;
        simulY = numCelY*celPixeles;
        imgX = numCelX*celPixeles;
        imgY = numCelY*celPixeles;
        setPreferredSize(new Dimension(simulX,simulY));
        mundo = new Mundo(numCelX,numCelY);
        mundoImg = new BufferedImage(simulX,simulY,BufferedImage.TYPE_INT_RGB);
        mundoDraw = mundoImg.createGraphics();
    }
    MundoPanel(int celPix, int mundoX, int mundoY){//Puede ser rectangular
        super();
        numCelX = mundoX;
        numCelY = mundoY;
        celPixeles = celPix;
        simulX = numCelX*celPixeles;
        simulY = numCelY*celPixeles;
        imgX = numCelX*celPixeles;
        imgY = numCelY*celPixeles;
        setPreferredSize(new Dimension(simulX,simulY));
        mundo = new Mundo(numCelX,numCelY);
        mundoImg = new BufferedImage(simulX,simulY,BufferedImage.TYPE_INT_RGB);
        mundoDraw = mundoImg.createGraphics();
    }
    public void inicializaMundo(){
        mundoDraw.setColor(Color.BLACK);
        mundoDraw.fillRect(0, 0, imgX-1, imgY-1);
    }
    public void cambiaTamano(int x, int y){
        simulX = x;
        simulY = y;
        this.setPreferredSize(new Dimension(simulX,simulY));
    }
    public void resetMundoSimul(){
        mundoDraw.setColor(Color.BLACK);
        mundoDraw.fillRect(0, 0, imgX-1, imgY-1);
        mundo.resetMundo();
    }
    private void pintaCelulas(){
        for(int x=0;x<numCelX;x++){
            for(int y=0;y<numCelY;y++){
                if(mundo.cambioCelula(x, y)){
                    if(mundo.isCelViva(x, y)){
                        mundoDraw.setColor(Color.WHITE);
                    }else{
                        mundoDraw.setColor(Color.BLACK);
                    }
                    mundoDraw.fillRect(x*celPixeles,y*celPixeles, celPixeles,celPixeles);
                }
            }
        }
    }
    public void sigIteracionSimul(){
        mundo.sigIteracion();
        pintaCelulas();
    }
    public void randomMundoSimul(double porcen_vivas){
        mundo.inicioRandom(porcen_vivas);
        pintaCelulas();
    }
    public void switchCelEstadoSimul(int x, int y){
        mundo.switchCelEstado(x, y);
        if(mundo.isCelViva(x, y)){
            mundoDraw.setColor(Color.WHITE);
        }
        else{
            mundoDraw.setColor(Color.BLACK);
        }
        mundoDraw.fillRect(x*celPixeles,y*celPixeles, celPixeles,celPixeles);
    }
    public void muestraMundo(){
        repaint();
    }
    public void setToroidalSimul(boolean t){
        mundo.setToroidal(t);
    }
    public boolean isToroidalSimul(){
        return mundo.isToroidal();
    }
    public void setReglaSimul(int smin,int smax,int nmin,int nmax){
        mundo.setRegla(smin, smax, nmin, nmax);
    }
    public int[] getReglaSimul(){
        return mundo.getRegla();
    }
    public int getNumMuertasSimul(){
        return mundo.getNumMuertas();
    }
    public int getNumVivasSimul(){
        return mundo.getNumVivas();
    }
    @Override
    public void paint(Graphics g){
        g.drawImage(mundoImg, 0, 0,simulX,simulY,this);
        g.dispose();
    }
    public void addMouse(MouseAdapter m){
        addMouseListener(m);
    }
}
