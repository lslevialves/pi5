package persistencia;

import beans.Pedido;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PedidoDao {
    public void salva(Pedido pedido){
        Session sessao = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sessao.beginTransaction();
        sessao.merge(pedido);
        t.commit();
        sessao.close();
    }
}
