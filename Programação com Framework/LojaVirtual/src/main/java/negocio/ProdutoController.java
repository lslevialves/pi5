package negocio;

import beans.Produto;
import persistencia.ProdutoDAO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
public class ProdutoController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Produto produto;
	private ProdutoDAO produtoDAO;
	private List<Produto> produtos;

	public void salvar(){
		try {
			getProdutoDAO().inserir(this.produto);
			setProduto(new Produto());
			pesquisarProdutos();
			FacesContext.
					getCurrentInstance().
					addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Produto salvo com sucesso", "Produto salvo com sucesso"));
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage("Falha ao salvar produto. " +
					"Verifique os dados e tente novamente", new FacesMessage());
		}
	}

	private void pesquisarProdutos() {
		List<Produto> produtos = getProdutoDAO().listar();
		setProdutos(produtos);
	}

	public void editar(Produto produto){
		this.produto = produto;
	}

	public void excluir(Produto produto){
		boolean result = getProdutoDAO().excluir(produto);
		pesquisarProdutos();
		if ( result ) {
			FacesContext.
					getCurrentInstance().
					addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Produto removido Com sucesso", "Produto removido Com sucesso"));
			return;
		}

		FacesContext.
				getCurrentInstance().
				addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Erro ao excluir produto", "Erro ao excluir produto"));
	}


	public ProdutoDAO getProdutoDAO() {
		if (this.produtoDAO == null) {
			produtoDAO = new ProdutoDAO();
		}
		return produtoDAO;
	}

	public void setProdutoDAO(ProdutoDAO produtoDAO) {
		this.produtoDAO = produtoDAO;
	}

	public List<Produto> getProdutos() {
		if (this.produtos == null){
			pesquisarProdutos();
		}
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto getProduto() {
		if (this.produto == null) {
			setProduto(new Produto());
		}
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}

