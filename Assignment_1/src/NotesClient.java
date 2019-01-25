import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NotesClient {
	private static int PORT = 9898; // Port number will be entered by client

	public static void main(String[] args) throws Exception {
		// System.out.println("Enter the IP address of a machine running the
		// capitalize server:");
		String serverAddress = "192.168.232.1";// new
												// Scanner(System.in).nextLine();
												// 129.97.7.120
		Socket socket = new Socket(serverAddress, PORT);

		// initialize server-client communication
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		// this will display the welcome message that was sent from the server
		System.out.println(in.readLine());

		// my idea; create an initial GUI asking for IP and Port to form a
		// connection, and then a separate GUI
		// for user inputs. That way, if the port# and IP addresses don't match,
		// connection will fail
	}
}
