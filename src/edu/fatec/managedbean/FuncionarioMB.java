package edu.fatec.managedbean;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import edu.fatec.entidades.Funcionario;
import edu.fatec.rmi.Election;

@ManagedBean
@SessionScoped
public class FuncionarioMB implements Serializable{

	private static final long serialVersionUID = 4663521641752224679L;
    private Funcionario funcionario;
    private int logado;
    private FacesContext mensagem;
	
    public FuncionarioMB(){
    	funcionario = new Funcionario();
   
    }
    
    
    public String logar(){
    	try {
			Funcionario func;
    		LocateRegistry.getRegistry("localhost");
		
    	    Election eleicao =null ;
			eleicao = (Election)Naming.lookup("//localhost:1990/eleicao");
			func=eleicao.autenticarFuncionario(funcionario.getUsuario());
			
			 FacesContext context = FacesContext.getCurrentInstance();
			if(func!=null && funcionario.getSenha().equals(func.getSenha())){
				 
		        	context.addMessage(null, new FacesMessage("Login efetuado com sucesso",""));
		        	this.setMensagem(context);
		        	this.setLogado(2);
		        	return "menuFunc.xhtml";
			} else{
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao efetuar login",""));    
				return"login.xhtml";
			}
			
    	}catch(NullPointerException ex){
    		ex.printStackTrace();
    	} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
    	
    	return"login.xhtml?faces-redirect=true";
    }
    
  
    public String cancela(){
    	return "principal.xhtml?faces-redirect=true";
    }
    
    public String acessarMenu(){
    	
    	return "login.xhtml?faces-redirect=true";
    }
    
    public String cadastroEleitor(){
    	
    	return ("cadastroEleitor.xhtml?faces-redirect=true");
    }
    
    public String cadastroCandidato(){
    	
    	return ("cadastroCandidato.xhtml?faces-redirect=true");
    }
    
    public String consultaEleitor(){
    	
    	return ("./consultarEleitor.xhtml?faces-redirect=true");
    }
    
    
    public String consultaCandidato(){
    	
    	return ("./consultarCandidato.xhtml?faces-redirect=true");
    }
    
    public String sair(){
		this.setLogado(0);
		return"./login.xhtml?faces-redirect=true";
	}
    
    public String cancelar(){
    	return"./menuFunc.xhtml?faces-redirect=true";
    }
    public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}


	public int getLogado() {
		return logado;
	}


	public void setLogado(int logado) {
		this.logado = logado;
	}


	public FacesContext getMensagem() {
		return mensagem;
	}


	public void setMensagem(FacesContext mensagem) {
		this.mensagem = mensagem;
	}
    
    
    
}
