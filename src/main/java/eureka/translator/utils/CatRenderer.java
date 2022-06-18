package eureka.translator.utils;

import eureka.translator.gui.Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CatRenderer extends JPanel implements ActionListener {
    public CatRenderer() {
        this.setBackground(new Color(130, 160, 227));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            if (Gui.cat == null) Gui.cat = ImageIO.read(ClassLoader.getSystemResource("assets/normalCat.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graphics2D g2D = (Graphics2D) g;

        int width = (int) (getWidth() / 3.5) - 60;
        int height = (int) (getHeight() - 70 * 4.3);
        int scale = height / 5;
        g2D.drawImage(resize(Gui.cat, (70 * 3) + scale, (100 * 3) + scale), width, height - scale, this);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();

        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
