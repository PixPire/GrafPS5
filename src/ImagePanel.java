import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private float scale;
    public float startingScale = 1.0f;
    public float scaleChangeTick = 0.2f;

    @Getter
    private BufferedImage image;


    public ImagePanel() {
        super();

        this.scale = startingScale;

        this.addMouseWheelListener(e -> {
            if(e.getWheelRotation()==1){
                if(scale>=0.3f)scale-=scaleChangeTick;
                repaint();
            }else if(e.getWheelRotation()==-1){
                scale+=scaleChangeTick;
                repaint();
            }
        });

    }

    public void setImage(BufferedImage image){
        this.image=image;
        this.setFocusable(true);
        this.requestFocusInWindow();
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = (int) (image.getWidth() * scale);
        int height = (int) (image.getHeight() * scale);
        g2d.drawImage(image, 0, 0, width, height, this);
    }

}
