package eureka.translator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gui extends JFrame {
    public static JTextArea input = new JTextArea("Input");
    public static JTextArea output = new JTextArea("Output");
    public static CatRenderer catRenderer = new CatRenderer();

    public static BufferedImage cat;
    public static BufferedImage logo;

    public static JMenuBar menu = new JMenuBar();
    public static JMenu language = new JMenu("Language");
    public static JRadioButtonMenuItem english, polish, spanish, ukrainian, russian;

    public static boolean pop = false;

    public Gui(int width, int height) throws IOException {
        this.setTitle("Translator");
        this.setBounds(600, 300, width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(130, 160, 227));

        logo = ImageIO.read(ClassLoader.getSystemResource("assets/logo.png"));

        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (pop) {
                    try {
                        Gui.cat = null;
                        Gui.cat = ImageIO.read(ClassLoader.getSystemResource("assets/popCat.png"));
                        pop = false;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                repaint();
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    Translator translator = new Translator();
                    translator.init();

                    try {
                        output.setText(translator.translateText(input.getText(), "auto", getLanguage()));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!pop) {
                    try {
                        Gui.cat = null;
                        Gui.cat = ImageIO.read(ClassLoader.getSystemResource("assets/normalCat.png"));
                        pop = true;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                repaint();
            }
        });

        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setBackground(new Color(125, 150, 215));
        input.setForeground(new Color(36, 61, 111));
        input.setFont(new Font("Bryndan Write", Font.PLAIN, 28));
        input.setBorder(new LineBorder(Color.BLACK.brighter()));

        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setBackground(new Color(125, 150, 215));
        output.setForeground(new Color(36, 61, 111));
        output.setFont(new Font("Bryndan Write", Font.PLAIN, 28));

        addItems();
        menu.add(language);

        this.setLayout(new GridLayout());
        this.add(catRenderer);
        this.add(input);
        this.add(output);
        this.setJMenuBar(menu);
        this.setIconImage(logo);
        this.setVisible(true);
    }

    private void addItems() {
        Translator translator = new Translator();
        ButtonGroup group = new ButtonGroup();

        english = new JRadioButtonMenuItem(translator.language.get(0));
        group.add(english);
        language.add(english);
        polish = new JRadioButtonMenuItem(translator.language.get(1));
        group.add(polish);
        language.add(polish);
        spanish = new JRadioButtonMenuItem(translator.language.get(2));
        group.add(spanish);
        language.add(spanish);
        ukrainian = new JRadioButtonMenuItem(translator.language.get(3));
        group.add(ukrainian);
        language.add(ukrainian);
        russian = new JRadioButtonMenuItem(translator.language.get(4));
        group.add(russian);
        language.add(russian);

        english.setSelected(true);
    }

    private String getLanguage() {
        if (english.isSelected()) return "en";
        if (polish.isSelected()) return "pl";
        if (spanish.isSelected()) return "es";
        if (ukrainian.isSelected()) return "uk";
        if (russian.isSelected()) return "ru";

        return "null";
    }
}
