/**
* @author Alhadj adam Mamadou && Cherif Sy
* Programme pour établir une connection client/serveur, ce code gère le code client.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.OutputStream;
import java.io.*;
import java.nio.ByteBuffer;

public class Serveur {
	private ServerSocket serveur = null;
	private Socket client = null;
	private BufferedReader in;
	private PrintWriter out;
	private OutputStream oos;
	private DataOutputStream dout;
	/** Contructeur qui specifie le port écoute, qui doit être au dessus de 1024
	  * @param port le numero du port
	  */
	public Serveur(int port) {
		if (port < 1023) {// ports reserves
			System.err.println("");
			port =5555;
		}
		try{
			// on crée un serveur sur le port specifié
			serveur = new ServerSocket(port);
		}
		catch(IOException e){
			System.err.println("Impossible d'écouter le port "+port);
            System.exit(1);
        }
		System.out.println("Serveur demarré sur le Port: "+port+"...");
		try{
			serveur.setSoTimeout(30000); // attendre au max 10s
			// s'il y a une requête sur le port
			// on crée un Socket pour communiquer avec le client
			// On attend jusqu'a ce qu'il y ait une requête
			client = serveur.accept(); //"client" est le Socket
		}
		catch (SocketTimeoutException e){
			System.err.println("On quitte : TimeOut declenché");
			System.exit(1);
		}
		catch (IOException e) {
				System.err.println("Client refusé !.");
				System.exit(1);
        }
		System.out.println("Message du Client!");
		try {
			// on recupère les canaux de communication avec des filtres de lecture ecriture de données
			in = new BufferedReader(new InputStreamReader(client.getInputStream() ) );
			oos =  client.getOutputStream();
			dout = new DataOutputStream(client.getOutputStream());
			
		}
		catch (IOException e) {
            	System.err.println("Erreur lors de création des streams");
           		System.exit(1);
        }
		if (in == null)
			System.out.println("pas d'entrée !!!");
		if (out == null)
			System.out.println("pas de sortie !!!");
	}

	/** lit les caractères envoyés par le client.
		* @return un objet String qui contient l'ensemble des caractères lus
		*/
	public int[] lireClient(){
		String ligne=null;
		String Val1;
		String Val2;
		int N=0;
		int T=0;
	 
		try{
			ligne=in.readLine();
			Pattern p = Pattern.compile("(.*?)(\\d+)(.*?)(\\d+)");
			Matcher m = p.matcher(ligne);
			while(m.find()){
				Val1 = m.group(2);
				Val2 = m.group(4);
				 N = Integer.parseInt(Val1);
				 T = Integer.parseInt(Val2); 
			}
	 	}
		catch (IOException e) {
            System.err.println("rien a lire");
		}
		  return new int[] {N, T};
	}
	/** Envoie des données au client.
		* @param ligne les caractères à envoyer
		*/
	public void ecrireClient(int ligne){
		if (dout == null)
			System.out.println("pas de sortie !!! : ecrire ?");
		else{
			try {
					dout.writeInt(ligne);		
					dout.flush();
			} catch (Exception e) {
			}
	
		}
	}
	/** teste la connexion.
		*@return un booléen notifiant l'état de la connexion
		*/
	public boolean clientOK(){
		return client.isConnected();
	}
	/** Fermeture du socket.
		*/
	public void fermer(){
	// il faut fermer "proprement" les streams avant les Sockets
		try{
			in.close();
			dout.close();
			if (client != null)
				client.close();
			if (serveur != null)
				serveur.close();
			System.out.println ("Au revoir tout est Fermé!");
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
		// initialisation du serveur
			Serveur  srv;
			if (args.length >0)
				srv = new Serveur(Integer.parseInt(args[0]));
			else
				srv =new Serveur(0);
		//System.out.println("Client connecté!");
		// Envoie un message d'accueil
		// srv.ecrireClient("Bienvenue sur le Serveur - Pour quitter : taper \"fin\" ");
		// Ecoute du client
			boolean continuer =true;
			int []ligne;
			int reponse=0;
			while(continuer && srv.clientOK()) {
				ligne = srv.lireClient();
				if (ligne[0] == 0) {//peu importe la casse
					try{
						Thread.sleep(10000);
						continuer = false;
						// srv.ecrireClient(0);
					} catch (Exception e) {
						continuer = true;
					}
				}
				try {
				System.out.println("Le Client demande "+ligne[0] + " paquets "+ "et son buffer est "+ligne[1]);
				// Compare la taille de paquet et envoie en une ou plusieurs fois
				//si le paquet est inferieur à la taille de buffer il envoie en un seul coup
				if((ligne[0]*64)<ligne[1]){
					reponse =ligne[0]*64;
					srv.ecrireClient(reponse);
				}
				// si la taille de buffer est inferieur à la taille des paquets, envoie differé
				else{
					int n = 0;  // vraible qui recupere la n fois du paquet
					int diff=0;	// variable que recupere les restes du modulo
					n = ((ligne[0]*64)/ligne[1]);
					diff = ((ligne[0]*64) % ligne[1]);
						for(int i=1; i<=n; i++){
							// l += (ligne[1]);
							srv.ecrireClient(ligne[1]);
							//System.out.println(ligne[1]);
							}
							srv.ecrireClient(diff);
				}
				}catch(Exception e){
					//System.out.println("erreur lors de l'envoie!!!");
				}	
			}
			System.out.println("On termine");
			srv.fermer();
	}

}
 










