import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NotesClient {
	private static int PORT = 9890;	//appropriate port#? 
	
	public static void main(String[] args) throws Exception {
//      System.out.println("Enter the IP address of a machine running the capitalize server:");
      String serverAddress = "192.168.232.1";//new Scanner(System.in).nextLine();
      										//129.97.7.120 
      Socket socket = new Socket(serverAddress, PORT);
      
      //initialize server-client communication 
//      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      DataInputStream in = new DataInputStream(socket.getInputStream());
	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());

     //this will display the welcome message that was sent from the server
//     System.out.println(in.readLine());
	  System.out.println(in.readUTF());
	  Scanner scanner = new Scanner(System.in);
     while (true) {
    	 //whatever is in this loop was just a test to read commands 
    	 out.writeUTF("POST 2 3 10 20 white Meeting next friday fam");
    	 String input = in.readUTF();
//    	 System.out.println(input);
    	 if (input.equals("")) {
    		 break;
    	 }
    	 
    	 out.writeUTF("GET color=white refersTo=boy");
    	 String input2 = in.readUTF();
    	 System.out.println(input2);
    	 if (input2.equals("")) {
    		 break;
    	 }
    	 
     }
     
     socket.close();
	}
}
