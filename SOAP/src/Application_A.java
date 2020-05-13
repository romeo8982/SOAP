import javax.swing.*;
import javax.xml.soap.SOAPException;
import java.io.IOException;

public class Application_A {
    public static void main(String[] args) throws IOException, SOAPException {
        JFrame frame = new JFrame("A");
        Window window = new Window(5000,5001,"127.0.0.1");
        window.title = "A";
        frame.setContentPane(window.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
