package business.authenticationSessionBeans;

import models.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RegisterSessionBean implements RegisterSessionBeanLocal {

    @PersistenceContext(unitName = "WebShopJsfPU")
    private EntityManager entityManager;

    @Override
    public boolean register(String name, String surname, String username, String password) {
        try {
            Query query = entityManager.createNamedQuery("User.findByUsername");
            query.setParameter("username", username);
            List list = query.getResultList();

            if (list.size() > 0) {
                return false;
            }
            User user = new User();
            user.setName(name);
            user.setPassword(password);
            user.setSurname(surname);
            user.setUsername(username);
            entityManager.persist(user);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
