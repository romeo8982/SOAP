import javax.swing.*;
import javax.xml.soap.SOAPException;
import java.io.IOException;

public class Application_B {
    public static void main(String[] args) throws IOException, SOAPException {
        JFrame frame = new JFrame("B");
        Window window = new Window(5001,5002,"127.0.0.1");
        window.title = "B";
        frame.setContentPane(window.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
