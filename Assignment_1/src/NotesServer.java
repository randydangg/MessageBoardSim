import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Vector;
=======
import java.util.HashMap;
import java.util.Map;
import java.util.*;

//V2
>>>>>>> a37dc529ad2ad3d68d16aa0a36fe93f494b04116

public class NotesServer {
	private static ArrayList<String> COLORS = new ArrayList<String>();
	private static String COLOR_LIST = "";
	// a 2d vector that will store client details
	private static Vector<Note> NOTES = new Vector<Note>();
	// another 2d vector that will store locations of pins
	private static Vector<Pin> PINS = new Vector<Pin>();
	private static int PORT_NUM;
	private static int BOARD_WIDTH;
	private static int BOARD_HEIGHT;

	public static void main(String[] args) throws Exception {
		System.out.println("Server is up and running.");

		// get data from terminal and store appropriately
		PORT_NUM = Integer.parseInt(args[0]);
		BOARD_WIDTH = Integer.parseInt(args[1]);
		BOARD_HEIGHT = Integer.parseInt(args[2]);
		for (int i = 3; i < args.length; i++) {
			COLORS.add(args[i]);
		}

		for (int j = 0; j < COLORS.size(); j++) {
			COLOR_LIST = COLOR_LIST + COLORS.get(j) + " ";
		}

		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(PORT_NUM); // port number
		try {
			while (true) {
				new Client(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Client extends Thread {
		Socket socket;
		int clientNumber;
		String command; // this will be client command
		String splitCommand[];
		// BufferedReader input;
		// PrintWriter output;
		// might have to use data stream classes to better handle the long
		// strings, only problem is that it keeps throwing an exception
		DataInputStream input;
		DataOutputStream output;

		public Client(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;

			System.out.println("Attempting to make connection with client# " + clientNumber);

		}

		@Override
		public void run() {
			try {
				// input = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));
				input = new DataInputStream(socket.getInputStream());
				// output = new PrintWriter(socket.getOutputStream(), true);
				output = new DataOutputStream(socket.getOutputStream());
				// output.println("Connection with client# " + clientNumber + "
				// is successful, welcome!");
				output.writeUTF(BOARD_WIDTH + " " + BOARD_HEIGHT + " " + COLOR_LIST + "\n");

				while (true) {
					// command = input.readLine();
					command = input.readUTF();
					splitCommand = command.split(" "); // split the command into
														// tokens
					System.out.println(command);
					if (command.startsWith("POST")) { // if client wants to POST
						int x_pos = Integer.parseInt(splitCommand[1]);
						int y_pos = Integer.parseInt(splitCommand[2]);
						int width = Integer.parseInt(splitCommand[3]);
						int height = Integer.parseInt(splitCommand[4]);
						String color = splitCommand[5]; // make an exception for
														// when color is invalid
														// (I lied, client side
														// handles that)
						String message = "";
						for (int i = 6; i < splitCommand.length; i++) { // store
																		// message
																		// as
																		// one
																		// string
							message = message + splitCommand[i] + " ";
						}
						// create a new note with data above and store in Vector
						Note note = new Note(x_pos, y_pos, width, height, color, message, 0);
						NOTES.add(note);
						// output.println("Note is successfully posted!");
						// output.writeUTF("Note is successfully posted!");
						// output.writeUTF(note.x_pos + " " + note.color + " " +
						// note.message);
					} else if (command.startsWith("GET")) { // if client wants
															// to GET
						String output_notes = ""; // this will be the string of
													// notes that is sent to the
													// client
						if (splitCommand[1].contains("PINS")) {
							// get coordinates of all pins and send to client
							String all_coordinates = "";
							for (int i = 0; i < PINS.size(); i++) {
								all_coordinates = all_coordinates + "(" + PINS.get(i).x + ", " + PINS.get(i).y + ") \n";
							}
							if (all_coordinates.equals("")) {
								// output.println("There are currently no PINs
								// on the board");
								output.writeUTF("There are currently no relevant PINs on the board");
							} else {
								// output.println(all_coordinates);
								output.writeUTF(all_coordinates);
							}
						} else {
							boolean getColor = false;
							String chosen_color = "";

							boolean getCoordinate = false;
							int chosen_x = 0;
							int chosen_y = 0;

							boolean getReference = false;
							String reference_str = "";

							for (int i = 1; i < splitCommand.length; i++) {
								if (splitCommand[i].contains("color")) {
									chosen_color = splitCommand[i].substring(splitCommand[i].indexOf("=") + 1);
									getColor = true;
								} else if (splitCommand[i].contains("contains")) {
									chosen_x = Integer.parseInt(splitCommand[i + 1]);
									chosen_y = Integer.parseInt(splitCommand[i + 2]);
									getCoordinate = true;
								} else if (splitCommand[i].contains("refersTo")) {
									// get the string after the "="
									reference_str = splitCommand[i].substring(splitCommand[i].indexOf("=") + 1);
									getReference = true;
								}
							}
							// now process the requests according to what the
							// client wants
							if (getColor == true && getCoordinate == true && getReference == true) {
								// requesting all three
								for (int a = 0; a < NOTES.size(); a++) {
									int low_x_bound = NOTES.elementAt(a).x_pos;
									int up_x_bound = NOTES.elementAt(a).x_pos + NOTES.elementAt(a).width;
									int low_y_bound = NOTES.elementAt(a).y_pos;
									int up_y_bound = NOTES.elementAt(a).y_pos + NOTES.elementAt(a).height;
									if (NOTES.elementAt(a).color.equals(chosen_color) && low_x_bound < chosen_x
											&& chosen_x < up_x_bound && low_y_bound < chosen_y && chosen_y < up_y_bound
											&& NOTES.elementAt(a).message.contains(reference_str)) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == true && getCoordinate == true && getReference == false) {
								// requesting color and coordinates
								for (int a = 0; a < NOTES.size(); a++) {
									int low_x_bound = NOTES.elementAt(a).x_pos;
									int up_x_bound = NOTES.elementAt(a).x_pos + NOTES.elementAt(a).width;
									int low_y_bound = NOTES.elementAt(a).y_pos;
									int up_y_bound = NOTES.elementAt(a).y_pos + NOTES.elementAt(a).height;
									if (NOTES.elementAt(a).color.equals(chosen_color) && low_x_bound < chosen_x
											&& chosen_x < up_x_bound && low_y_bound < chosen_y
											&& chosen_y < up_y_bound) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == true && getCoordinate == false && getReference == true) {
								// requesting color and reference
								// output.writeUTF("requesting color and
								// reference!");
								for (int a = 0; a < NOTES.size(); a++) {
									if (NOTES.elementAt(a).color.equals(chosen_color)
											&& NOTES.elementAt(a).message.contains(reference_str)) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == true && getCoordinate == false && getReference == false) {
								// requesting only color
								// output.writeUTF("requesting color");
								for (int a = 0; a < NOTES.size(); a++) {
									if (NOTES.elementAt(a).color.equals(chosen_color)) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == false && getCoordinate == true && getReference == true) {
								// requesting coordinates and reference
								for (int a = 0; a < NOTES.size(); a++) {
									int low_x_bound = NOTES.elementAt(a).x_pos;
									int up_x_bound = NOTES.elementAt(a).x_pos + NOTES.elementAt(a).width;
									int low_y_bound = NOTES.elementAt(a).y_pos;
									int up_y_bound = NOTES.elementAt(a).y_pos + NOTES.elementAt(a).height;
									if (low_x_bound < chosen_x && chosen_x < up_x_bound && low_y_bound < chosen_y
											&& chosen_y < up_y_bound
											&& NOTES.elementAt(a).message.contains(reference_str)) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == false && getCoordinate == true && getReference == false) {
								// requesting only coordinates
								for (int a = 0; a < NOTES.size(); a++) {
									int low_x_bound = NOTES.elementAt(a).x_pos;
									int up_x_bound = NOTES.elementAt(a).x_pos + NOTES.elementAt(a).width;
									int low_y_bound = NOTES.elementAt(a).y_pos;
									int up_y_bound = NOTES.elementAt(a).y_pos + NOTES.elementAt(a).height;
									if (low_x_bound < chosen_x && chosen_x < up_x_bound && low_y_bound < chosen_y
											&& chosen_y < up_y_bound) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							} else if (getColor == false && getCoordinate == false && getReference == true) {
								// requesting only reference
								for (int a = 0; a < NOTES.size(); a++) {
									if (NOTES.elementAt(a).message.contains(reference_str)) {
										// store the desired note as one big
										// string
										output_notes = output_notes + NOTES.elementAt(a).color + " Note at: ("
												+ NOTES.elementAt(a).x_pos + ", " + NOTES.elementAt(a).y_pos + ") "
												+ NOTES.elementAt(a).message + "\n";
									}
								}
							}
						}
						if (output_notes.equals("") && splitCommand[1] != "PINS") {
							// couldnt fulfill the request made
							// output.println("There are no notes satisfying
							// your request!");
							output.writeUTF("There are no notes satisfying your request!");
						} else {
							// output.println(output_notes);
							output.writeUTF(output_notes);
						}
					} else if (command.startsWith("PIN")) { // if client wants
															// to PIN
						int temp_x = Integer.parseInt(splitCommand[1].substring(splitCommand[1].indexOf(",") - 1));
						int temp_y = Integer.parseInt(splitCommand[1].substring(splitCommand[1].indexOf(",") + 1));
						boolean pinned = true;

						for (int i = 0; i < PINS.size(); i++) {
							if (PINS.elementAt(i).x == temp_x && PINS.elementAt(i).y == temp_y) {
								// if there is already a pin at the location
								// being requested, ignore the current pin
								pinned = false;
								break;
							}
						}

						if (pinned) {
							Pin pin = new Pin(temp_x, temp_y);
							PINS.add(pin);
							// output.println("Pin is successfully pinned!");
							output.writeUTF("Pin is successfully pinned!");
							for (int i = 0; i < NOTES.size(); i++) {
								// check if there are notes in the position of
								// the requested pin location
								int lower_x_bound = NOTES.elementAt(i).x_pos;
								int upper_x_bound = NOTES.elementAt(i).x_pos + NOTES.elementAt(i).width;
								int lower_y_bound = NOTES.elementAt(i).y_pos;
								int upper_y_bound = NOTES.elementAt(i).y_pos + NOTES.elementAt(i).height;

								if (lower_x_bound < temp_x && temp_x < upper_x_bound && lower_y_bound < temp_y
										&& temp_y < upper_y_bound) {
									// check if the current note is located in
									// the range of the pin location
									// if so, change the notes status to pinned
									NOTES.elementAt(i).status = 1;
								}
							}
						} else {
							output.writeUTF("A pin already exists at this location");
						}
					} else if (command.startsWith("UNPIN")) { // if client wants
																// to UNPIN
						int temp_x = Integer.parseInt(splitCommand[1].substring(splitCommand[1].indexOf(",") - 1));
						int temp_y = Integer.parseInt(splitCommand[1].substring(splitCommand[1].indexOf(",") + 1));
						boolean hasPin = false;

						for (int i = 0; i < PINS.size(); i++) {
							if (PINS.elementAt(i).x == temp_x && PINS.elementAt(i).y == temp_y) {
								// if there is already a pin at the location
								// being requested, remove it
								PINS.removeElementAt(i);
								break;
							}
						}

						for (int i = 0; i < NOTES.size(); i++) {
							// check if there are notes in the position of the
							// requested pin location
							int lower_x_bound = NOTES.elementAt(i).x_pos;
							int upper_x_bound = NOTES.elementAt(i).x_pos + NOTES.elementAt(i).width;
							int lower_y_bound = NOTES.elementAt(i).y_pos;
							int upper_y_bound = NOTES.elementAt(i).y_pos + NOTES.elementAt(i).height;

							if (lower_x_bound < temp_x && temp_x < upper_x_bound && lower_y_bound < temp_y
									&& temp_y < upper_y_bound) {
								// check if the current note is located in the
								// range of the pin location
								// if so, we have to check if the note has
								// another pin on it
								for (int j = 0; j < PINS.size(); j++) {
									if (lower_x_bound < PINS.elementAt(j).x && PINS.elementAt(j).x < upper_x_bound
											&& lower_y_bound < PINS.elementAt(j).y
											&& PINS.elementAt(j).y < upper_y_bound) {
										// there is another pin on the note
										hasPin = true;
										break;
									}
								}
							}
							if (hasPin == false) {
								NOTES.elementAt(i).status = 0; // the current
																// note has no
																// other pin on
																// it, so change
																// status to
																// unpinned
							} else if (hasPin == true) {
								hasPin = false; // if the current note has
												// another pin, re initialize
												// the hasPin variable
							}
						}
					} else if (command.startsWith("CLEAR")) { // if client wants
																// to CLEAR
						for (int i = 0; i < NOTES.size(); i++) {
							if (NOTES.elementAt(i).status == 0) {
								// any unpinned notes will be removed
								NOTES.removeElementAt(i);
							}
						}
						// output.println("All unpinned notes have been
						// cleared");
						output.writeUTF("All unpinned notes have been cleared");
					} else if (command.equals("DISCONNECT")) { // if client
																// wants to
																// DISCONNECT
						output.writeUTF("6");
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Error handling client# " + clientNumber + ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client# " + clientNumber + " closed");
			}
		}
	}
}

class Note {
	int x_pos;
	int y_pos;
	int width;
	int height;
	String color;
	String message;
	int status; // 0 = unpinned, 1 = pinned

	public Note(int x_pos, int y_pos, int width, int height, String color, String message, int status) {
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.width = width;
		this.height = height;
		this.color = color;
		this.message = message;
		this.status = status;
	}
}

class Pin {
	int x;
	int y;

	public Pin(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
