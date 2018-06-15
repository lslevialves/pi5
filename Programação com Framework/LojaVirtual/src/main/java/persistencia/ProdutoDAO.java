package persistencia;

import beans.Produto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public class ProdutoDAO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public void inserir (Produto produto) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction t = sessao.beginTransaction();
		sessao.merge(produto);
		t.commit();
		sessao.close();
	}

	public List<Produto> listar(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		List<Produto> produtos = session.createQuery("select p from Produto p").list();
		session.close();
		return produtos;
	}

	public boolean excluir(Produto produto) {
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.delete(produto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage()) ;
		}
		return false;
	}
}
