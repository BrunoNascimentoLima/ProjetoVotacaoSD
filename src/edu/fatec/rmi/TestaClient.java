package edu.fatec.rmi;



import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import edu.fatec.entidades.Candidato;

public class TestaClient {

	
	public static void main(String[] args) throws  NotBoundException, MalformedURLException,ClassNotFoundException, RemoteException {
		

		try {
			LocateRegistry.getRegistry("localhost");
		  
			
    	Election eleicao =null ;
	
		eleicao = (Election)Naming.lookup("//localhost:1990/eleicao");
	
	     Candidato c = eleicao.result("");
		System.out.println(c.getId());
		System.out.println(c.getNome());
		System.out.println(c.getNumero());
		
		
		} catch (RemoteException  e2) {
	    	e2.printStackTrace();
		}catch(Exception e1){
			
			e1.printStackTrace();
		}
	} 
}