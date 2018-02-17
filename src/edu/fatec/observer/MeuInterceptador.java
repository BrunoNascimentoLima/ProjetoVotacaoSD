package edu.fatec.observer;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.fatec.managedbean.EleitorMB;
import edu.fatec.managedbean.FuncionarioMB;

public class MeuInterceptador implements PhaseListener {

	private static final long serialVersionUID = -4040778940413271197L;

	@Override
	public void afterPhase(PhaseEvent e) {
		PhaseId fase = e.getPhaseId();

		FacesContext ctx = e.getFacesContext();
		String pagina = ctx.getViewRoot().getViewId();
	
		System.out.println(pagina);
		System.out.println(fase);
		if(pagina.equalsIgnoreCase("/menu.xhtml")
		   ||(pagina.equalsIgnoreCase("/urna.xhtml"))
		   ||(pagina.equalsIgnoreCase("/resultadoParcial.xhtml"))){
			EleitorMB el = ctx.getApplication().evaluateExpressionGet(ctx,
					"#{eleitorMB}", EleitorMB.class);
			System.out.println(el.getAutenticado());
			NavigationHandler nav = ctx.getApplication().getNavigationHandler();
			if (el.getAutenticado() != 1) {
				nav.handleNavigation(ctx, null,
						"/titulo.xhtml?faces-redirect=true");
				ctx.renderResponse();						 
			}
		}	else if(pagina.equalsIgnoreCase("/menuFunc.xhtml")
				||(pagina.equalsIgnoreCase("/cadastroCandidato.xhtml"))
				||(pagina.equalsIgnoreCase("/consultarEleitor.xhtml"))
				||(pagina.equalsIgnoreCase("/consultarCandidato.xhtml"))
				||(pagina.equalsIgnoreCase("/cadastroEleitor.xhtml"))){
			FuncionarioMB f =ctx.getApplication().evaluateExpressionGet(ctx,
					"#{funcionarioMB}", FuncionarioMB.class);
			System.out.println(f.getLogado());
			NavigationHandler nav = ctx.getApplication().getNavigationHandler();
			if (f.getLogado() != 2) {
				nav.handleNavigation(ctx, null,
						"/login.xhtml?faces-redirect=true");
				ctx.renderResponse();						 
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
