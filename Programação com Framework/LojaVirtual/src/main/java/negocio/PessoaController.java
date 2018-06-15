package negocio;

import beans.Pessoa;
import persistencia.PessoaDAO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class PessoaController implements Serializable {

	private static final long serialVersionUID = -6898332734920770502L;
	private Pessoa pessoa;
	private PessoaDAO pessoaDAO;
	private List<Pessoa> pessoas;

	public void inserir() {
		try {
			getPessoaDAO().salvar(pessoa);
			setPessoa(new Pessoa());
			pesquisarPessoas();
			FacesContext.
                    getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Pessoa salva com sucesso", "Pessoa salva Com sucesso"));
		} catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage("Falha ao salvar o produto. " +
                    "Verifique os dados e tente novamente", new FacesMessage());
		}
	}

	public void pesquisarPessoas(){
        this.pessoas = getPessoaDAO().listar();
        System.out.println(this.getPessoas().size());
    }

    public void editar(Pessoa pessoa){
	    setPessoa(pessoa);
    }

    public void excluir(Pessoa pessoa){
        boolean result = getPessoaDAO().excluir(pessoa);
        pesquisarPessoas();
        if ( result ) {
            FacesContext.
                    getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Pessoa removida com sucesso", "Pessoa removida com sucesso"));
            return;
        }

        FacesContext.
                getCurrentInstance().
                addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Erro ao excluir pessoa", "Erro ao excluir pessoa"));
    }

    public Pessoa getPessoa() {
	    if (this.pessoa == null) {
	        setPessoa(new Pessoa());
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaDAO getPessoaDAO() {
	    if (this.pessoaDAO == null) {
	        this.pessoaDAO = new PessoaDAO();
        }
        return pessoaDAO;
    }

    public void setPessoaDAO(PessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    public List<Pessoa> getPessoas() {
	    if (this.pessoas == null) {
	        pesquisarPessoas();
        }
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
}
