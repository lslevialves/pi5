package persistencia;

import beans.Pessoa;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PessoaDAO {

	public static void main(String[] args){
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			List<Pessoa> pessoas = pessoaDAO.listar();
			System.out.println(pessoas.size());
			System.out.println("Sucesso");
		} catch (Exception e) {
			System.out.println();
		}
	}

	public void salvar(Pessoa pessoa) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction t = sessao.beginTransaction();
		sessao.merge(pessoa);
		t.commit();
		sessao.close();
	}

	public List<Pessoa> listar(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		List<Pessoa> pessoas = session.createQuery("select p from Pessoa p").list();
		session.close();
		return pessoas;
	}

	public boolean excluir(Pessoa pessoa) {
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.delete(pessoa);
			transaction.commit();
			return true;
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage()) ;
		}
		return false;
	}
}
