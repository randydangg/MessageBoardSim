import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NotesServer {
	private String colors[] = { "RED", "BLUE", "YELLOW" };

	public static void main(String[] args) throws Exception {
		System.out.println("Server is up and running.");
		// args[0] is port number, args[1] is width, args[2] is height, args[3]
		// and beyond are the note colors
		// with args [3] being the default color

		// Map<String, String> dictionary = new HashMap<String, String>();
		// //used to store client details
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(9898); // port number

		try {
			while (true) {
				Client client = new Client(listener.accept(), clientNumber++);
			}
		} finally {
			listener.close();
		}

	}
}

class Client extends Thread {
	Socket socket;
	int clientNumber;
	String command; // this will be client command
	String splitCommand[];
	BufferedReader input;
	PrintWriter output;

	public Client(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;

		System.out.println("Attempting to make connection with client# " + clientNumber);

	}

	@Override
	public void run() {
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("Connection with client# " + clientNumber + " is successful, welcome!");

			while (true) {
				command = input.readLine();
				splitCommand = command.split(" "); // split the command into
													// tokens
				if (command.startsWith("POST")) { // if client wants to POST
					int x_pos = Integer.parseInt(splitCommand[1]);
					int y_pos = Integer.parseInt(splitCommand[2]);
					int width = Integer.parseInt(splitCommand[3]);
					int height = Integer.parseInt(splitCommand[4]);
					String color = splitCommand[5]; // make an exception for
													// when color is invalid
					String message[] = {};
					int temp = 0;
					for (int i = 6; i < splitCommand.length; i++) { // store
																	// message
																	// in array
						message[temp] = splitCommand[i];
						temp++;
					}
				} else if (command.startsWith("GET")) { // if client wants to
														// GET

				} else if (command.startsWith("PIN")) { // if client wants to
														// PIN

				} else if (command.startsWith("UNPIN")) { // if client wants to
															// UNPIN

				} else if (command.startsWith("CLEAR")) { // if client wants to
															// CLEAR

				} else if (command.startsWith("DISCONNECT")) { // if client
																// wants to
																// DISCONNECT

				}
			}
		} catch (IOException e) {
			System.out.println("connection with client# " + clientNumber + " has failed");
		}
	}
}
