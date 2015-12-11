/**
 * Created by Jamie on 06/11/2015.
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JPanel;

import org.opencv.core.Mat;

public class CustomPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage IMAGE;

    public CustomPanel(){
        super();
    }

    public void display(Mat frame){
        convertToBufferedImage(frame);
        this.repaint();
    }

    public void convertToBufferedImage(Mat inputFrame){
        int width = inputFrame.width();
        int height = inputFrame.height();
        int channels = inputFrame.channels();

        //debug
        //System.out.println(width + " " + height + " " + channels);

        byte[] sourcePixels = new byte[width * height * channels];
        inputFrame.get(0, 0, sourcePixels);

        //test for channels
        switch (channels){
            case 1:
                this.IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                break;
            case 3:
                this.IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                break;
        }



        final byte[] targetPixels = ((DataBufferByte) this.IMAGE.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (this.IMAGE == null){
            return;
        }
        g.drawImage(this.IMAGE,10,10,this.IMAGE.getWidth(),this.IMAGE.getHeight(), null);
    }
}
