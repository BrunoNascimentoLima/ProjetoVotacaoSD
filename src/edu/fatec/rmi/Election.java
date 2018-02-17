package edu.fatec.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.fatec.entidades.Candidato;
import edu.fatec.entidades.Eleitor;
import edu.fatec.entidades.Funcionario;

public interface Election extends Remote{
	
   public boolean vote(String nomeCandidato, String titulo, long numeroVotos)throws RemoteException;
   public Candidato result(String nomeCandidato)throws RemoteException;
   public Eleitor verificar(String titulo)throws RemoteException;
   public Candidato trazerCandidato(int numeroCandidato)throws RemoteException;
   public Candidato trazerCandidatoPorNome(String nomeCandidato)throws RemoteException;
   public List  trazerCandidatos()throws RemoteException;   
   public List trazerEleitores()throws RemoteException; 
   public Funcionario autenticarFuncionario(String usuario)throws RemoteException;  
   public boolean cadastrarEleitor(Eleitor eleitor)throws RemoteException;
   public boolean cadastrarCandidato(Candidato candidato)throws RemoteException;
} 
