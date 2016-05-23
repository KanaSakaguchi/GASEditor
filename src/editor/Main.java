package editor;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

class Main extends JFrame {
    private Main() {
        setSize(400, 300);
        add(new GASEditor());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}