package negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import beans.Produto;
import persistencia.ProdutoDAO;

@ManagedBean
@SessionScoped
public class ProdutoCtrl implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Produto produto = new Produto();
	public String filtro = "";
	public 	List<Produto> lista = new ArrayList<>();

	public List<Produto> getLista() {
		return lista;
	}
	public void setLista(List<Produto> lista) {
		this.lista = lista;
	}
	public Produto getProduto() {
		if (produto == null) {
			produto = new Produto();
		}
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
		
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro (String filtro) {
		this.filtro = filtro;
	}

	public List<Produto> getListagem() {
		return ProdutoDAO.listagem(filtro);
	}
	
	
	public String actionGravar() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (produto.getId() == 0) {
			ProdutoDAO.inserir(produto);
			context.addMessage(null, new FacesMessage("Sucesso", "Inserido com sucesso"));
		} else {
			ProdutoDAO.alterar(produto);
			context.addMessage(null, new FacesMessage("Sucesso", "Alterado com Sucesso"));
		}
		return "/admin/lista_produto";

	}
		public String actionInserir() {
			produto = new Produto();
			return "lista_produto";
			
		}
		public String actionExcluir() {
			ProdutoDAO.excluir(produto);
			return "lista_produto";
		}
		public void onRowSelect(SelectEvent event) {
			FacesMessage msg = new FacesMessage("Produto Selecionado",
					String.valueOf(((Produto) event.getObject()).getId()));
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	
}

