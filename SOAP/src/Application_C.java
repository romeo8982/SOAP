import javax.swing.*;
import javax.xml.soap.SOAPException;
import java.io.IOException;

public class Application_C {
    public static void main(String[] args) throws IOException, SOAPException {
        JFrame frame = new JFrame("C");
        Window window = new Window(5002,5003,"127.0.0.1");
        window.title = "C";
        frame.setContentPane(window.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
