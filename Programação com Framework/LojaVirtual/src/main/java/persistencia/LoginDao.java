package persistencia;

import beans.Pessoa;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class LoginDao {

    public Pessoa logar(String login, String senha) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select p from Pessoa p where p.login = :login and p.senha = :senha");
        query.setString("login", login);
        query.setString("senha", senha);

        List<Pessoa> list = query.list();
        session.close();

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }
}
