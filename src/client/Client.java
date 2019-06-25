package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client
 * @author Francesco Raco
 *
 */
public class Client
{
	/**
	 * Client mark: every client error message should start with it (so that users can distinguish client and server messages)
	 */
	public static final String MARK = "clientMark";
	
	/**
	 * Server host name
	 */
	protected String serverHostName;
	
	/**
	 * TCP connection port
	 */
	protected int port;
	
	/**
	 * Write bus stops to output stream (equivalent to server input stream)
	 * and throw an appropriate exception if problems occurred during file reading
	 * @param out PrintWriter object created by socket output stream
	 * @param file Name of UTF-8 text file containing 1 bus stop per line (every line ends with '\n')
	 * @throws NotCompatibleFileException Not Compatible File Exception
	 */
	protected void writeBus(PrintWriter out, String file) throws NotCompatibleFileException
	{
		//Initialize buffered reader
		BufferedReader fileInput = null;
		
		//Try execute and handle eventual exceptions
		try
		{
			//Create a file InputStream object and throw an appropriate exception if it is null
			InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
			if (inputStream == null) throw new NotCompatibleFileException();
			
			//BufferedReader object created by the file inputStream and "UTF-8" as arguments
			fileInput = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		
			//Mark the present position in the stream
			fileInput.mark(1);
		
			//If first char is not 0xFEFF (UTF-8 Byte Order Mark), then reset the stream to the previous mark
			if (fileInput.read() != 0xFEFF) fileInput.reset();
		
			//Send every bus stop (without initial and ending white spaces) to the server
			String stop;
			while ((stop = fileInput.readLine()) != null) out.println(stop.trim());
		}
		catch(Exception e)
		{
			//Throw an appropriate exception if problems occurred during file reading
			throw new NotCompatibleFileException();
		}
		finally
		{
			//Close Buffered Reader resource
			try
			{
				fileInput.close();
			}
			catch (IOException e) {}
			catch(Exception e) {}
		}
	}
	
	/**
	 * Close all resources
	 * @param echoSocket Socket
	 * @param out PrintWriter instantiated by socket output stream (it writes data to server)
	 * @param in BufferedReader which reads data from input stream (equivalent to server output stream)
	 */
	protected void closeResources(Socket echoSocket, PrintWriter out, BufferedReader in)
	{
		try {
				if (out != null) out.close();
				if (in != null) in.close();
				if (echoSocket != null) echoSocket.close();
			}
			catch (IOException e) {}
	}
	
	/**
	 * Instantiate Client by server host name string and integer value of TCP
	 * connection port
	 * @param serverHostName Server host name
	 * @param port TCP connection port
	 */
	public Client(String serverHostName, int port)
	{
		//Assign server host name string and integer value of TCP connection port
		//to corresponding fields
		this.serverHostName = serverHostName;
		this.port = port;
	}

	/**
	 * Get server output by chosen output type which it has to return, string file name containing bus stops,
	 * start and end point related to single source best path.
	 * The last 3 arguments are processed if output type is appropriate.
	 * @param outputChoice Output type which server has to return
	 * @param file String file name containing bus stops
	 * @param startPoint Start point of single source best path
	 * @param endPoint end point of single source best path
	 * @return Server output
	 */
	public String getServerOutput(OutputChoice outputChoice, String file, String startPoint, String endPoint)
	{
		//Try to execute and handle eventual exceptions
		try
		{
			//Declare a socket and its input reader and output print writer
			Socket echoSocket = null;
			PrintWriter out = null;
			BufferedReader in = null;

			//Create socket by server host name and connection TCP port
			echoSocket = new Socket(serverHostName, port);
				
			//Create print writer and buffered reader by socket output and input stream
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			
			//Ask to server for a specific solution
			//(solution, test or single source best path)
			switch(outputChoice)
			{
				case SOLUTION: out.println("ShowSolution"); writeBus(out, file); break;
				case TEST: out.println("ShowTestSolution"); writeBus(out, file); break;
				case SINGLESOURCEBESTPATH: out.println("ShowSingleSourceBestPath " + startPoint + " " + endPoint); break;
			}
			
			//Tell the server that there are no other queries 
			out.println("END");
			
			//Create and initialize
			StringBuilder response = new StringBuilder();
			
			//While buffered reader reads a non null line and text received from the server is not "END"
			String responseLine;
			while ((responseLine = in.readLine()) != null && !responseLine.equals("END"))
			{
				//Append the line (without initial and ending white spaces) to the response string builder
				response.append(responseLine.trim()).append("\n");
			}
			
			//Get string object by String Builder response
			String responseString = response.toString();
			
			//If server answer is not empty
			if (!responseString.isEmpty())
			{
				//Thanks the server
				out.println("Grazie, ho ricevuto la tua risposta!");
				
				//Close all resources
				closeResources(echoSocket, out, in);
				
				//Return the server answer
				return responseString;
			}
			
			//Close all resources
			closeResources(echoSocket, out, in);
			
			//If server answer is empty or null,
			//explain the problem to server 
			out.println("Scusa, ma non ho ricevuto la tua risposta!");
		}
		catch (NotCompatibleFileException e)
		{
			return MARK + "Nessun file compatibile trovato!";
		}
		catch (UnknownHostException e)
		{
			return MARK + "Server non rilevato: " + serverHostName + '!';
		}
		catch (IOException e)
		{
			return MARK + "Impossibile ricevere risposta dal server!";
		}
		catch (Exception e)
		{
			return MARK + "Impossibile stabilire una connessione!";
		}
				
		//Server has not answered
		return MARK + "Server inaffidabile: non ha risposto!";
	}
	
	
	/**
	 * Get server output for TSP bus solution
	 * (with the possibility of asking the server to calculate testing operations)
	 * @param oc Output type which server has to return (Only solution or solution + testing operations; single source best path is not allowed here)
	 * @param file String file name containing bus stops
	 * @return Server output for TSP bus solution (with the possibility of asking the server to calculate testing operations)
	 */
	public String getServerOutput(OutputChoice oc, String file)
	{
		//Single source best path is not allowed here as output type which server has to return
		if (oc.equals(OutputChoice.SINGLESOURCEBESTPATH)) return "Tipologia di risposta non ammessa!";
		
		//Return server output string
		return getServerOutput(oc, file, null, null);
	}
	
	/**
	 * Get server output for default TSP bus solution (without testing operations)
	 * @param file String file name containing bus stops
	 * @return Server output for default TSP bus solution (without testing operations)
	 */
	public String getServerOutput(String file)
	{
		return getServerOutput(OutputChoice.SOLUTION, file, null, null);
	}
	
	/**
	 * Get server single source best path output by start and end point
	 * Every white space is replaced by a '-' char, so that server will be able to split the addresses,
	 * using the white space as delimiter
	 * @param startPoint Single source best path start point
	 * @param endPoint Single source best path end point
	 * @return Server single source best path output by start and end point
	 */
	public String getServerOutput(String startPoint, String endPoint)
	{
		return getServerOutput(OutputChoice.SINGLESOURCEBESTPATH, null, startPoint.replaceAll(" ", "-"), endPoint.replaceAll(" ", "-"));
	}
}