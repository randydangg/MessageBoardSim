
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

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
	JTextArea textArea = new JTextArea(50, 30);
	JScrollPane scrollPane = new JScrollPane(textArea);
	private JButton cnnButton;

	/* Socket API */
	private Socket socket = null;
	private DataInputStream in = null; // in.readUTF
	private DataOutputStream out = null; // out.writeUTF
	// private BufferedReader in;
	// private PrintWriter out;

	/* Variables needed on first connection */
	private String[] colors = { "GREEN", "YELLOW", "BLUE" }; // CHANGE TO NULL
	private int[] boardSize = { 120, 120 };

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
		cnnButton = new JButton("CONNECT");
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
		String errorText = ""; // error text that may show up. if errortext
								// exists, no action will be executed
		String actionCommand = e.getActionCommand();
		String commandString = ""; // string to be sent to the server
		String postColor = "";
		boolean hasColor = false;

		if (actionCommand == "CONNECT") {
			if (ipInput.getText().equals("") || portInput.getText().equals("")) {
				errorText = "Please enter port number and ip address";
			} else {
				String ipAddress = ipInput.getText();
				try {
					int port = Integer.parseInt(portInput.getText());
				} catch (NumberFormatException nfe) {
					errorText = "Please enter an integer for Port Number";
				}
				test();
				// try {
				// connect(ipAddress, port);
				// } catch (SocketException e1) {
				// // TODO Auto-generated catch block
				// // WORK IN PROGRESS
				// errorString = "Bad Connection. Please try another port or IP
				// _ address";
				// }
			}
		}

		if (actionCommand == "DISCONNECT") {
			try {
				disconnect();
			} catch (Exception excep) {
				// TODO Auto-generated catch block
				excep.printStackTrace();
			}
		}

		if (actionCommand == "POST") {
			if (postInput.getText().equals("")) {
				errorText = "Please enter post command: <coordinates <width> <height> <color> <message>";
				// if user's input color not in the list, then assume that it
				// will be the message
			} else {
				String[] postArr = postInput.getText().split(" ");

				try {
					commandString = "POST";
					int boardWidth = Integer.parseInt(postArr[0]);
					int boardHeight = Integer.parseInt(postArr[1]);
					commandString += " " + boardWidth + " " + boardHeight;
					if (!(Arrays.asList(this.colors).contains(postArr[2]))) {
						postColor = this.colors[0];
					} else {
						postColor = postArr[2];
						// Arrays.asList(this.colors).contains(
					}
					commandString += " " + postColor;
					// copy rest of message into the command string
					String[] message = Arrays.copyOfRange(postArr, 3, postArr.length);
					for (String m : message) {
						commandString += " " + m;
					}

					// send command string to server

				} catch (NumberFormatException nfe) {
					errorText = "The first two numbers must be integers";
				}
			}
		}

		if (actionCommand == "GET") {
			// If all empty
			if (colorInput.getText().equals("") && substrInput.getText().equals("") & xInput.getText().equals("")
					&& yInput.getText().equals("")) {
				errorText = "Please fill in at least one of the requirements. ";

			} else if ((xInput.getText().equals("") && !yInput.getText().equals(""))
					|| !xInput.getText().equals("") && yInput.getText().equals("")) {
				// if x is empty but y is not empty OR if x is not empty but y
				// is empty
				errorText += "Please include both coordinates";
			} else { // at least one is not empty
				commandString += "GET";
				// if has color or has refersTo and there have been no errors
				// from above, then it is a good input
				if ((!colorInput.getText().equals("")) && errorText.equals("")) {

					hasColor = Arrays.asList(this.colors).contains(colorInput.getText());
					// only need to see if color is valid because
					if (hasColor) {
						// create a string to send to server
						System.out.println("color is valid");
						commandString += " color=" + colorInput.getText();
					}
				}
				if (!xInput.getText().equals("") && !yInput.getText().equals("")) {
					commandString += " contains= " + xInput.getText() + " " + yInput.getText();
				}
				if ((!substrInput.getText().equals("")) && errorText.equals("")) {
					commandString += " refersTo=" + substrInput.getText();
				}

			}

			// IF BOTH SUBSTRING AND COLOR INPUT IS EMPTY BUT HAS BOTH
			// COORDINATES, SEND COORDINATES

		}

		if (actionCommand == "GET PINS") {
			// grab pins from server
			commandString = "GET PINS";

		}
		if (actionCommand == "PIN/UNPIN") {
			if (pinxInput.getText().equals("") || pinyInput.getText().equals("")) {
				errorText = "Please enter both coordinates";

			} else {
				try {
					commandString = "PIN/UNPIN";
					int x = Integer.parseInt(pinxInput.getText());
					int y = Integer.parseInt(pinyInput.getText());

					commandString += " " + x + " " + y;
				} catch (NumberFormatException nfe) {
					errorText = "Please enter integer values";
				}
			}
		}

		if (actionCommand == "CLEAR") {
			// message Nothing to Clear
			// message Done!
			commandString = "CLEAR";

		}

		// over here, have a final IF statement: if error string is empty, send
		// command
		this.textArea.setText(errorText + "\nCommand: " + commandString);

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
		this.cnnButton.setText("DISCONNECT");

	}

	public void connect(String address, int port) throws Exception {
		// new socket to connect to server
		try {
			this.socket = new Socket(address, port);
			// setting up readers and writers
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			// first line from server should be a string specifying board
			// dimensions
			// and colors of note
			String response = in.readUTF();
			String[] responseArr = response.split(" "); // split into array
														// string

			// declare array for boardSize {width, height}
			this.boardSize = new int[2];
			boardSize[0] = Integer.parseInt(responseArr[0]);
			boardSize[1] = Integer.parseInt(responseArr[1]);
			// declare array of colors
			this.colors = new String[responseArr.length - 2];

			String displayText = "Board Width: " + responseArr[0] + "\nBoard Height: " + responseArr[1]
					+ "\nColors Available: ";

			for (int i = 2; i < responseArr.length; i++) {
				displayText += responseArr[i] + " ";
				this.colors[i - 2] = responseArr[i];
			}

			this.cnnButton.setText("DISCONNECT");
			this.textArea.setText(displayText);
		} catch (Exception e) {
			this.textArea.setText("Connection Error. Please see console.");
			e.printStackTrace();
		}

		// size of board
		// list of color

		// send a message to the server // out.println(message)
		// get response //String response = in.readLine();
	}

	public void disconnect() throws Exception {
		this.cnnButton.setText("CONNECT");
		// try {
		// this.socket.close();
		// } catch (IOException e) {
		// this.textArea.setText("Error. Please see console.");
		// e.printStackTrace();
		// }

	}

	public void request(String command) throws Exception {
		this.out.writeUTF(command);
		String[] com = command.split(" ");
		String req = com[0];
		String response = this.in.readUTF();
		String displayText;

		if (req.equals("GET")) {
			// this get request will print the details (color, coordinates, pin
			// status, message) of the notes as per request
		} else if (req.equals("POST")) {
			// post successful
		} else if (req.equals("PIN/UNPIN")) {

		} else if (req.equals("GET PINS")) {

		} else if (req.equals("CLEAR")) {
			if (response.equals("NO PINS TO CLEAR")) {
				displayText = "No notes to unpin to clear";
			}
		} else {

			textArea.setText("Unable to process request " + req);

		}

	}

	public static void main(String[] args) {
		ClientWindow window = new ClientWindow();
		window.setVisible(true);
	}

}
