/**
* @author Alhadj adam Mamadou && Cherif Sy
* Programme pour établir une connection client/serveur, ce code gère le code client.
*/
import java.io.*;
import java.net.*;
import java.net.SocketTimeoutException;
import java.nio.BufferOverflowException;
import java.nio.IntBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.ByteBuffer;


public class Client {
	private Socket client = null;
	private BufferedReader in;
	private PrintWriter out;
	private DataInputStream din;

	/** Le constructeur qui indique sur quel serveur se connecter et quel port solliciter
	  * @param hostname le nom du serveur
	  * @param port le numero de port
	  *	@throws IOException si erreur de connexion
	  */
	public Client(String hostName, int port) throws IOException {
		try {
		// Convertir la chaine de caracteres "hostName" en une adresse IP valide du serveur
			InetAddress adresseIP = InetAddress.getByName(hostName);
		//creer le Socket vers le serveur
			client = new Socket(adresseIP, port);
		//on fixe un timeOut
			client.setSoTimeout(1000);// 10s
		}
		catch(UnknownHostException e){
			System.err.println("Je ne connais pas le serveur: "+hostName);
			throw e;
		}
		/*catch(SocketException e){
			System.err.println("erreur de connexion ou timeout");
			throw 
		}*/
		catch (IOException e) {
			System.err.println("Probleme de connexion sur:"+hostName);
			throw e;
		}
		System.out.println("Connexion OK sur "+hostName);
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()) );
			out = new PrintWriter(client.getOutputStream() );
			//is = client.getInputStream();
			din = new DataInputStream(client.getInputStream());
		}
		catch (IOException e) {
            	System.err.println("Erreur lors de création des streams");
           		System.exit(1);
        	}
	}
	/** lit les caractères envoyés par le serveur.
		* @return 
		*/
	public int lireServeur(){
		int b=0;
		int t=100000;
		try {
			for(int i=0; i<=t; i++){
			b += din.readInt();
			//	System.out.println(b);
			}
		} catch (Exception e) {
		}
		return b;
	}

	/** Envoie des données au serveur.
		* @param ligne  les caractères à envoyer
		*/
	public void ecrireServeur(String ligne){
		out.println(ligne);
		out.flush();
	}
	
	/** teste la connexion.						
		*@return un booléen notifiant l'état de la connexion
		*/
	public boolean estConnect(){
		return client.isConnected();
	}
	/** Fermeture du socket.
		*/
	public void fermer(){
		try{
			in.close();
			out.close();
			if (client != null)
				client.close();
			System.out.println ("Fermeture ok");
		}
		catch(IOException e){
			System.err.println("Erreur à la fermeture des flux !");
		}
	}
	protected void finalize(){
		fermer();
	}
	public static void main(String[] args){
	// Pour tester un protocole simple de communication
	// Bien entendu il faut que serveur et client soient compatibles
	// initialisation du client
	// on peut passer server et port par la ligne de commande

		Client  cl=null; //initialisation de la variable locale
		try{
			if (args.length >1) 
				cl = new Client(args[0],Integer.parseInt(args[1]));
			else
				cl = new Client("localhost", 5555);
				System.out.println("mc = "+cl+ "client:"+cl.client);
		}
		catch(IOException e) {
			System.err.println("erreur de realisation du client"+cl);
		}
		if (cl != null) {
	// Que dit le serveur ?
			int ligne = cl.lireServeur();
			System.out.println("Message: vous etes bien connecté pour quitter taper FIN ou "+ligne+ " sinon dites par exemple: Je veux 10 paquets et mon Buffer est 256");
			System.out.println("");
			String line;
	//Requête au serveur a partir de la console
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			String in;								
			try{
				while ((in = console.readLine()) != null) {
					cl.ecrireServeur(in);
					ligne = cl.lireServeur();
					if(ligne==0){
						System.out.println("Fermeture de la connexion en cours!!!");
					}
					else{
						System.out.println("Serveur à envoyé: "+ligne + " octets");	// pour voir
					}
				
				}
				console.close();
			
			}
			catch(IOException e){
				System.err.println("Erreur de lecture console");
			}
			cl.fermer();
		}
		else
			System.err.println("Impossible de créer le client, mc == null !!");
	}//Fin du main
}


