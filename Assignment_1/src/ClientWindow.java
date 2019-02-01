
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientWindow extends JFrame implements ActionListener {
	public static final int WIDTH = 600;
	public static final int HEIGHT = 800;

	/* Text fields */
	private JTextField ipInput = new JTextField(20);
	private JTextField portInput = new JTextField(5);
	private JTextField colorInput = new JTextField(10);
	private JTextField xInput = new JTextField(3);
	private JTextField yInput = new JTextField(3);
	private JTextField pinxInput = new JTextField(3);
	private JTextField pinyInput = new JTextField(3);
	private JTextField substrInput = new JTextField(15);
	private JTextField postInput = new JTextField(20);
	JTextArea textArea = new JTextArea(5, 20);
	JScrollPane scrollPane = new JScrollPane(textArea);

	/* Socket API */
	private Socket socket = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private BufferedReader in;
	private PrintWriter out;

	/* Variables needed on first connection */
	private String[] colors = null;

	public ClientWindow() {
		setSize(WIDTH, HEIGHT);
		setTitle("Client Request");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		JPanel mainPanel = new JPanel();
		JPanel cnnPanel = new JPanel();
		JPanel getPanel = new JPanel();
		JPanel postPanel = new JPanel();
		JPanel pinPanel = new JPanel();
		JPanel getPinsPanel = new JPanel();
		JPanel resultsPanel = new JPanel();
		JPanel clearPanel = new JPanel();

		mainPanel.setLayout(new GridLayout(15, 1));
		// postPanel.setLayout(new BorderLayout());
		/* Labels */
		JLabel ipLabel = new JLabel("IP Address");
		JLabel portLabel = new JLabel("Port Number");
		JLabel postLabel = new JLabel("Post Command");
		// change to disconnect if connected
		JLabel getColorLabel = new JLabel("Color");
		JLabel getxLabel = new JLabel("X");
		JLabel getyLabel = new JLabel("Y");
		JLabel xLabel = new JLabel("X");
		JLabel yLabel = new JLabel("Y");
		JLabel getSubstrLabel = new JLabel("RefersTo");
		JLabel pinLabel = new JLabel("PIN/UNPIN Coordinates");

		/* Buttons */
		JButton cnnButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		JButton getPinsButton = new JButton("GET PINS"); // button
		JButton pinButton = new JButton("PIN/UNPIN");
		JButton postButton = new JButton("POST");
		JButton clearButton = new JButton("CLEAR");

		cnnButton.addActionListener(this);
		getButton.addActionListener(this);
		getPinsButton.addActionListener(this);
		pinButton.addActionListener(this);
		postButton.addActionListener(this);
		clearButton.addActionListener(this);

		/* Result box */

		this.textArea.setEditable(false);

		cnnPanel.add(ipLabel);
		cnnPanel.add(ipInput);
		cnnPanel.add(portLabel);
		cnnPanel.add(portInput);
		cnnPanel.add(cnnButton);

		postPanel.add(postLabel);
		postPanel.add(postInput);
		postPanel.add(postButton);

		getPanel.add(getColorLabel);
		getPanel.add(colorInput);
		getPanel.add(getxLabel);
		getPanel.add(xInput);
		getPanel.add(getyLabel);
		getPanel.add(yInput);
		getPanel.add(getSubstrLabel);
		getPanel.add(substrInput);
		getPanel.add(getButton);

		// pinPanel
		pinPanel.add(xLabel);
		pinPanel.add(pinxInput);
		pinPanel.add(yLabel);
		pinPanel.add(pinyInput);
		pinPanel.add(pinButton);

		getPinsPanel.add(getPinsButton);

		clearPanel.add(clearButton);

		resultsPanel.add(textArea);

		add(mainPanel);
		mainPanel.add(cnnPanel);
		mainPanel.add(postPanel);
		mainPanel.add(getPanel);
		mainPanel.add(pinPanel);
		mainPanel.add(getPinsPanel);
		mainPanel.add(clearPanel);
		mainPanel.add(resultsPanel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String errorText = "";
		String actionCommand = e.getActionCommand();
		String commandString = "";

		if (actionCommand == "CONNECT") {
			if (ipInput.getText().equals("") || portInput.getText().equals("")) {
				errorText = "Please enter port number and ip address";
			} else {
				String ipAddress = ipInput.getText();
				int port = Integer.parseInt(portInput.getText());
				test();
				// try {
				// connect(ipAddress, port);
				// } catch (Exception e1) {
				// // TODO Auto-generated catch block
				// // WORK IN PROGRESS
				// e1.printStackTrace();
				// }
			}
		}

		if (actionCommand == "POST") {
			if (postInput.getText().equals("")) {
				errorText = "Please enter post command: <width> <height> <color> <message>";
				// if user's input color not in the list, then assume that it
				// will be the message
			} else {
				String[] postArr = postInput.getText().split(" ");
				if (postArr.length < 4) {
					if (postArr[0].matches("[-+]?\\d*\\.?\\d+")) { // if it is a
																	// number

					}
				}
			}
		}

		if (actionCommand == "GET") {
			// add if statement to flag error if only one coordinate is filled
			// out
			if (colorInput.getText().equals("") && substrInput.getText().equals("") && xInput.getText().equals("")
					&& yInput.getText().equals("")) {
				errorText = "Please fill in at least one of the requirements";

			}
			commandString = colorInput.getText() + xInput.getText() + yInput.getText() + substrInput.getText();
		}

		if (actionCommand == "GET PINS")

		{
			// grab pins from server
		}
		if (actionCommand == "PIN/UNPIN") {
			if (ipInput.getText().equals("") || portInput.getText().equals("")) {
				errorText = "Please enter port number and ip address";
				System.out.println(e.toString());
			}
		}

		if (actionCommand == "CLEAR") {
			// message Nothing to Clear
			// message Done!
		}

		// this.textArea.setText(errorText + "\nCommand: " + commandString);

	}

	public void test() {
		String response = "800 600 red blue yellow green";

		String displayText;
		String[] responseArr = response.split(" ");
		this.colors = new String[responseArr.length - 2];

		displayText = "Board Width: " + responseArr[0] + "\nBoard Height: " + responseArr[1] + "\nColors Available: ";

		for (int i = 2; i < responseArr.length; i++) {
			displayText += responseArr[i] + " ";
			colors[i - 2] = responseArr[i];
		}

		this.textArea.setText(displayText);

	}

	public void connect(String address, int port) throws Exception {
		// this.socket = new Socket(address, port);
		// this.in = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		// this.out = new PrintWriter(socket.getOutputStream(), true);
		// String response = in.readLine();

		String response = "800 600 red blue yellow green";

		String displayText;
		String[] responseArr = response.split(" ");
		this.colors = new String[responseArr.length - 2];

		displayText = "Board Width: " + responseArr[0] + "\nBoard Height: " + responseArr[1] + "\nColors Available: ";

		for (int i = 2; i < responseArr.length; i++) {
			displayText += responseArr[i] + " ";
			colors[i - 2] = responseArr[i];
		}

		this.textArea.setText(displayText);

		// size of board
		// list of color

		// send a message to the server // out.println(message)
		// get response //String response = in.readLine();
	}

	public static void main(String[] args) {
		ClientWindow window = new ClientWindow();
		window.setVisible(true);
	}

}
