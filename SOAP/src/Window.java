import javax.swing.*;
import javax.xml.soap.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class Window {
    public JPanel panel1;
    public JTextArea textArea1;
    public String title;
    private JList list1;
    private JList list2;
    private ButtonGroup GroupAdressed = new ButtonGroup();
    private JRadioButton bRadioButton;
    private JRadioButton aRadioButton;
    private JRadioButton cRadioButton;
    private JRadioButton dRadioButton;
    private JRadioButton unicastRadioButton;
    private JRadioButton broadCastRadioButton;
    private JButton sendButton;
    private JButton refrasheButton;
    private JButton startServerButton;
    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel listModel2 = new DefaultListModel();

    public ServerSocket serverSocket;


    public Window(int serverPort, int clientPort, String address) {
        sendButton.addActionListener(e -> {
            if (unicastRadioButton.isSelected()) {
                String chosen = getSelectedButtonText(GroupAdressed);
                try {
                    SOAPMessage message = createSOAPMessage(textArea1.getText(), chosen, title);
                    send(address, clientPort, message);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            } else {
                String box = "ABCD";
                for (int i = 0; i < box.length(); i++) {
                    if (title.charAt(0) != box.charAt(i)) {
                        try {
                            SOAPMessage message = createSOAPMessage(
                                    textArea1.getText(), String.valueOf(box.charAt(i)), title);
                            send(address, clientPort, message);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }

            }

        });

        refrasheButton.addActionListener(e -> {
            String line = null;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));
                line = in.readLine();
                if (line != null) {
                    ArrayList<String> data = convertMessage(line);
                    if (title.equals(data.get(0)))
                        listModel.addElement(data.get(2));
                    else {
                        SOAPMessage message = createSOAPMessage(data.get(2), data.get(0), data.get(1));
                        send(address, clientPort, message);
                        listModel2.addElement("message from " + data.get(1) + " to " + data.get(0));
                    }
                }
            } catch (IOException | SOAPException ioException) {
                ioException.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            System.out.println(line);
        });

        list2.setModel(listModel);
        list1.setModel(listModel2);
        GroupAdressed.add(aRadioButton);
        GroupAdressed.add(bRadioButton);
        GroupAdressed.add(cRadioButton);
        GroupAdressed.add(dRadioButton);
        ButtonGroup groupConf = new ButtonGroup();
        groupConf.add(unicastRadioButton);
        groupConf.add(broadCastRadioButton);


        startServerButton.addActionListener(e -> {
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private static SOAPMessage createSOAPMessage(String text, String to, String from) throws Exception {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

        SOAPBody soapBody = soapEnvelope.getBody();
        Name bodyName = SOAPFactory.newInstance().createName("text");
        SOAPBodyElement soapBodyElement = soapBody.addBodyElement(bodyName);
        soapBodyElement.addTextNode(text);

        SOAPHeader header = soapEnvelope.getHeader();

        Name headerName = SOAPFactory.newInstance().createName("send", "prefix", "uri");
        SOAPElement soapHeaderElement = header.addChildElement(headerName);
        soapHeaderElement.addTextNode(to);

        Name headerName2 = SOAPFactory.newInstance().createName("from", "prefix", "uri");
        SOAPElement soapHeaderElement2 = header.addChildElement(headerName2);
        soapHeaderElement2.addTextNode(from);

        soapMessage.saveChanges();

        return soapMessage;
    }

    private ArrayList<String> convertMessage(String message) throws IOException, SOAPException {
        InputStream is = new ByteArrayInputStream(message.getBytes());
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);

        SOAPHeader header = soapMessage.getSOAPPart().getEnvelope().getHeader();
        SOAPBody body = soapMessage.getSOAPPart().getEnvelope().getBody();

        Node node = (Node) header.getElementsByTagNameNS("uri", "send").item(0);
        String to = node.getFirstChild().getTextContent();

        node = (Node) header.getElementsByTagNameNS("uri", "from").item(0);
        String from = node.getFirstChild().getTextContent();

        node = (Node) body.getElementsByTagName("text").item(0);
        String text = node.getFirstChild().getTextContent();
        ArrayList<String> data = new ArrayList<>();
        data.add(to);
        data.add(from);
        data.add(text);
        return data;
    }

    private void send(String address, int clientPort, SOAPMessage message) throws IOException, SOAPException {
        Socket socket = new Socket(address, clientPort);
        PrintStream outLine = new PrintStream(socket.getOutputStream(), true);
        message.writeTo(outLine);
        outLine.close();
    }
}
