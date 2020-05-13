import javax.swing.*;
import javax.xml.soap.SOAPException;
import java.io.IOException;

public class Application_D {
    public static void main(String[] args) throws IOException, SOAPException {
        JFrame frame = new JFrame("D");
        Window window = new Window(5003,5000,"127.0.0.1");
        window.title = "D";
        frame.setContentPane(window.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
