package edu.fatec.managedbean;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fatec.entidades.Eleitor;
import edu.fatec.rmi.Election;

@ManagedBean
@SessionScoped
public class EleitorMB implements Serializable {

	private static final long serialVersionUID = -5354470351043126974L;
	private Eleitor eleitor;
	private List eleitores;
	private FacesContext mensagem;
	private int autenticado;

	public EleitorMB() {
		eleitor = new Eleitor();
		eleitores=new ArrayList<Eleitor>();
		this.getMensagem();
	}

	public String acessarMenu() {

		return "./titulo.xhtml?faces-redirect=true";
	}

	public String acessarUrna() {

		return "./urna.xhtml?faces-redirect=true";
	}

	public String verificar() {
		try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;

			eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
			eleitor = eleicao.verificar(eleitor.getTitulo());
			if (eleitor != null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						"Título aceito com sucesso", ""));
				this.setMensagem(context);
				this.setAutenticado(1);
				return "/menu.xhtml";

			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Título não cadastrado!",
						"Consulte sua zona eleitoral"));
				this.setMensagem(context);
				return "/titulo.xhtml";
			}
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Título não cadastrado!", "Consulte sua zona eleitoral"));
		this.setMensagem(context);
		return "./titulo.xhtml";
	}

	public String cadastrarEleitor() {
		Eleitor e = new Eleitor();
		try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;
			eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");

			e = eleicao.verificar(eleitor.getTitulo());
			if (e != null) {

				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Título já cadastrado !",
						""));
				this.setMensagem(context);

			} 
			else {
				boolean atualizado = eleicao.cadastrarEleitor(eleitor);
				if (atualizado == true) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_INFO,
							"Eleitor cadastrado com Sucesso!", ""));
					this.setMensagem(context);
				} else {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Erro ao cadastrar eleitor!", ""));
					this.setMensagem(context);
				}

			}

		} catch (NullPointerException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}

		return "cadastroEleitor.xhtml";

	}

	public String cancelar() {

		return "./principal.xhtml?faces-redirect=true";
	}

	public String sair() {
		this.setAutenticado(0);
		return "./titulo.xhtml?faces-redirect=true";
	}
	public String voltarMenu(){
		
		return("./menuFunc.xhtml?faces-redirect=true");
	}

	public String pesquisar(){
		Eleitor e;
		try {
		LocateRegistry.getRegistry("localhost");

		Election eleicao = null;
	    eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
	   
	    e=eleicao.verificar(eleitor.getTitulo());
	    if(e!=null){
	    	eleitores= new ArrayList<Eleitor>();
	    	eleitores.add(e);
	    } else{

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Eleitor não encontrado !",
					""));
			this.setMensagem(context);
	    	return("./consultarEleitor.xhtml");
	    }
	    
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
		} catch (RemoteException e1) {
			
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			
			e1.printStackTrace();
		}
		
		
		return("./consultarEleitor.xhtml?faces-redirect=true");
	}
	
	
	public String pesquisarTodos(){
		List<Eleitor> e;
		try {
		LocateRegistry.getRegistry("localhost");

		Election eleicao = null;
	    eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
	   
	    e=eleicao.trazerEleitores();
	    if(e!=null){
	    	eleitores= new ArrayList<Eleitor>();
	    	eleitores=e;
	    } else{

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Eleitor não encontrado !",
					""));
			this.setMensagem(context);
	    	return("./consultarEleitor.xhtml");
	    }
	    
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
		} catch (RemoteException e1) {
			
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			
			e1.printStackTrace();
		}
		
		
		return("./consultarEleitor.xhtml?faces-redirect=true");
	}
	public Eleitor getEleitor() {
		return eleitor;
	}

	public void setEleitor(Eleitor eleitor) {
		this.eleitor = eleitor;
	}

	public FacesContext getMensagem() {
		return mensagem;
	}

	public void setMensagem(FacesContext mensagem) {
		this.mensagem = mensagem;
	}

	public int getAutenticado() {
		return autenticado;
	}

	public void setAutenticado(int autenticado) {
		this.autenticado = autenticado;
	}

	public List getEleitores() {
		return eleitores;
	}

	public void setEleitores(List eleitores) {
		this.eleitores = eleitores;
	}

}
