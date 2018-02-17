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
import javax.jws.WebService;

import edu.fatec.entidades.Candidato;
import edu.fatec.entidades.Eleitor;
import edu.fatec.rmi.Election;

@ManagedBean
@SessionScoped
public class CandidatoMB implements Serializable {

	private static final long serialVersionUID = -4113296294772689033L;
	private Candidato candidato;
	private FacesContext mensagem;
	private List<Candidato> candidatos;

	
	public CandidatoMB() {
		candidato = new Candidato();
		candidatos = new ArrayList<Candidato>();
	}
	
	
	
	public String buscarCandidato() {
		try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;

			eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
			candidato = eleicao.trazerCandidato(candidato.getNumero());
			FacesContext ctx = FacesContext.getCurrentInstance();
			if(candidato!=null){
			System.out.println(candidato.getNome());
			return "/urna.xhtml?faces-redirect=true";
			}
			else{
				ctx.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,	"Candidato não encontrado","consulte o número de seu candidato"));
				this.setMensagem(ctx);
				candidato= new Candidato();
			}
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		return "/urna.xhtml";
	}

	public String confirmar() {
	
		try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;

			eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
			FacesContext ctx = FacesContext.getCurrentInstance();
			EleitorMB el = ctx.getApplication().evaluateExpressionGet(ctx,
					"#{eleitorMB}", EleitorMB.class);
            Candidato c; 
			c=eleicao.trazerCandidatoPorNome(candidato.getNome());
			if(c!=null){
			  if (el.getEleitor().getConfirmacao() == 0) {
				boolean confirma = eleicao.vote(candidato.getNome(), el
						.getEleitor().getTitulo(), el.getEleitor()
						.getConfirmacao());
				System.out.println(candidato.getNome());
				if (confirma == true){
					el.getEleitor().setConfirmacao(1);
					ctx.addMessage(null, new FacesMessage(
							"Voto registrado com sucesso", ""));
					this.setMensagem(ctx);
					return "/urna.xhtml";
				} else {
					ctx.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR,	"Erro ao registrar voto", ""));
					this.setMensagem(ctx);
					return "/urna.xhtml";
				}
			  }else{
				 ctx.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,"Seu voto já foi registrado", ""));
			    	this.setMensagem(ctx);
			    	return "/urna.xhtml";
			    }
			} else{
				ctx.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN,"É preciso BUSCAR UM CANDIDATO para voto", ""));
				this.setMensagem(ctx);
				return "/urna.xhtml";
			}
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		return "";
	}

	public String corrigir() {
		Candidato c = new Candidato();
		candidato = c;

		return ("");

	}

	 public String voltar(){
		  return"./menu.xhtml?faces-redirect=true";
	  }
	 
	 public String voltarMenu(){
		  return"./menu.xhtml?faces-redirect=true";
	  }
	 
	 public String trazerCandidato(){
		 try {
			LocateRegistry.getRegistry("localhost");
     		Election eleicao = null;
	    	eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
			candidatos=(List<Candidato>)eleicao.trazerCandidatos();
			for(Candidato c : candidatos){
				System.out.println(c.getNome());
				System.out.println(c.getNumero());
				System.out.println(c.getPartido());
				System.out.println(c.getVoto());
				
			}
			candidato=eleicao.result("");
			System.out.println("Vendedor:"+candidato.getNome());
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO,"Resultados parciais até o momento", ""));
			this.setMensagem(ctx);
		  return"/resultadoParcial.xhtml?faces-redirect=true";
		 } catch (MalformedURLException e){
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		     catch (RemoteException e) {
				e.printStackTrace();
			} 
		 catch (NullPointerException e) {
				e.printStackTrace();
			} 
		 return "";
	 }
	 
	 public String acessarResultado(){
			candidato= new Candidato();
			candidatos= new ArrayList<Candidato>();
			return"./resultadoParcial.xhtml?faces-redirect=true";
		}
	 
	 public String cadastrarCandidato(){
		 Candidato c = new Candidato();
			try {
				LocateRegistry.getRegistry("localhost");

				Election eleicao = null;
				eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");

				c = eleicao.trazerCandidato(candidato.getNumero());
				if (c != null) {

					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Número já cadastrado !",
							""));
					this.setMensagem(context);

				} 
				else {
					boolean atualizado = eleicao.cadastrarCandidato(candidato);
					if (atualizado == true) {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"Candidato cadastrado com Sucesso!", ""));
						this.setMensagem(context);
					} else {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Erro ao cadastrar Candidato!", ""));
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
			

		 return("cadastroCandidato.xhtml");
	 }
	 
	 public String pesquisar(){
			Candidato c;
			try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;
		    eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
		   
		    c=eleicao.trazerCandidato(candidato.getNumero());
		    if(c!=null){
		    	candidatos= new ArrayList<Candidato>();
		    	candidatos.add(c);
		    } else{
                 
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Candidato não encontrado !",
						""));
				this.setMensagem(context);
		    	return("./consultarCandidato.xhtml");
		    }
		    
			} catch (MalformedURLException e1) {
				
				e1.printStackTrace();
			} catch (RemoteException e1) {
				
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				
				e1.printStackTrace();
			}
			
			
			return("./consultarCandidato.xhtml?faces-redirect=true");
		}
	 
	 public String pesquisarTodos(){
			List<Candidato> c;
			try {
			LocateRegistry.getRegistry("localhost");

			Election eleicao = null;
		    eleicao = (Election) Naming.lookup("//localhost:1990/eleicao");
		   
		    c=eleicao.trazerCandidatos();
		    if(c!=null){
		    	candidatos= new ArrayList<Candidato>();
		    	candidatos=c;
		    } else{

				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Candidato não encontrado !",
						""));
				this.setMensagem(context);
		    	return("./consultarCandidato.xhtml");
		    }
		    
			} catch (MalformedURLException e1) {
				
				e1.printStackTrace();
			} catch (RemoteException e1) {
				
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				
				e1.printStackTrace();
			}
			
			
			return("./consultarCandidato.xhtml?faces-redirect=true");
		}
	 
	 
       public String cancelar(){
	        
    	   return("./menuFunc.xhtml?faces-redirect=true");
      }
       
       public String voltarAoMenu(){
    	   return("./menuFunc.xhtml?faces-redirect=true");
       }
	
	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato = candidato;
	}

	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}

	public FacesContext getMensagem() {
		return mensagem;
	}

	public void setMensagem(FacesContext mensagem) {
		this.mensagem = mensagem;
	}

}
