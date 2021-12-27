
package gameoflife;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class MundoPanel extends JPanel{
    private int simulX,simulY;//Varia con el zoom
    private int celPixeles,numCelX,numCelY;
    private int imgX,imgY;//Tamaño original de la imagen
    private BufferedImage mundoImg;
    private Graphics mundoDraw,md1,md2,md3,md4;
    
    private Mundo mundo;
    
    private HiloPintador hp1,hp2,hp3,hp4;
    
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
        /*Para los hilos*/
        /*md1 = mundoDraw.create(0, 0, imgX/2, imgY/2);
        md2 = mundoDraw.create(imgX/2, 0, imgX/2, imgY/2);
        md3 = mundoDraw.create(0, imgY/2, imgX/2, imgY/2);
        md4 = mundoDraw.create(imgX/2, imgY/2, imgX/2, imgY/2);*/
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
        /*Para los hilos*/
        md1 = mundoDraw.create(0, 0, imgX/2, imgY/2);
        md2 = mundoDraw.create(imgX/2, 0, imgX/2, imgY/2);
        md3 = mundoDraw.create(0, imgY/2, imgX/2, imgY/2);
        md4 = mundoDraw.create(imgX/2, imgY/2, imgX/2, imgY/2);
    }
    public void inicializaMundo(){
        //mundo.iniciaHilosContadores();
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
    private void pintaCelulasHilos(){
        hp1 = new HiloPintador(0,numCelX/2,0,numCelY/2,celPixeles,mundo,md1,0,0);
        hp2 = new HiloPintador(numCelX/2,numCelX,0,numCelY/2,celPixeles,mundo,md2,numCelX/2,0);
        hp3 = new HiloPintador(0,numCelX/2,numCelY/2,numCelY,celPixeles,mundo,md3,0,numCelY/2);
        hp4 = new HiloPintador(numCelX/2,numCelX,numCelY/2,numCelY,celPixeles,mundo,md4,numCelX/2,numCelY/2);

        hp1.start();
        hp2.start();
        hp3.start();
        hp4.start();
        try {
            hp1.join();
            hp2.join();
            hp3.join();
            hp4.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MundoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void pintaCelulasSecuencial(){
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
    public void sigIteracionSimulHilos(){
        mundo.sigIteracionHilos();
        pintaCelulasHilos();
    }
    public void sigIteracionSimulSecuencial(){
        mundo.sigIteracionSecuencial();
        pintaCelulasSecuencial();
    }
    public void randomMundoSimulHilos(double porcen_vivas){
        mundo.inicioRandom(porcen_vivas);
        pintaCelulasHilos();
    }
    public void randomMundoSimulSecuencial(double porcen_vivas){
        mundo.inicioRandom(porcen_vivas);
        pintaCelulasSecuencial();
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
    public void cambiaDimensiones(int x, int y){
        numCelX = x;
        numCelY = y;
        //int maxX=5,maxY=5;
        simulX = numCelX*celPixeles;
        simulY = numCelY*celPixeles;
        imgX = numCelX*celPixeles;
        imgY = numCelY*celPixeles;
        setPreferredSize(new Dimension(simulX,simulY));
        mundo = new Mundo(numCelX,numCelY);
        
        mundoImg = new BufferedImage(simulX,simulY,BufferedImage.TYPE_INT_RGB);
        mundoDraw = mundoImg.createGraphics();
        mundoDraw.setColor(Color.BLACK);
        mundoDraw.fillRect(0, 0, imgX-1, imgY-1);
        
        muestraMundo();
    }
    public int mundoToIntPanel(){
        return mundo.mundoToInt();
    }
    private void pintaTodasCelulasSecuencial(){
        for(int x=0;x<numCelX;x++){
            for(int y=0;y<numCelY;y++){
                if(mundo.isCelViva(x, y)){
                    mundoDraw.setColor(Color.WHITE);
                }else{
                    mundoDraw.setColor(Color.BLACK);
                }
                mundoDraw.fillRect(x*celPixeles,y*celPixeles, celPixeles,celPixeles);
            }
        }
    }
    public void sigIteracionTodasSecuencial(){
        mundo.sigIteracionGeneralToroidalSecuencial();
        pintaTodasCelulasSecuencial();
        muestraMundo();
    }
    public void intToMundoPanel(int n){
        mundo.intToMundo(n);
        pintaTodasCelulasSecuencial();
        muestraMundo();
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(mundoImg, 0, 0,simulX,simulY,this);        
        g.dispose();
    }
    public void addMouse(MouseAdapter m){
        addMouseListener(m);
    }
}
