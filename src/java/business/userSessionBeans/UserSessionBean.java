package business.userSessionBeans;

import models.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserSessionBean implements UserSessionBeanLocal {

    @PersistenceContext(unitName = "WebShopJsfPU")
    private EntityManager entityManager;

    @Override
    public void updateProfile(User user) {
        Query query = entityManager.createNamedQuery("User.updateById");
        query.setParameter("name", user.getName());
        query.setParameter("surname", user.getSurname());
        query.setParameter("username", user.getUsername());
        query.setParameter("password", user.getPassword());
        query.setParameter("id", user.getId());
        query.executeUpdate();
    }

    @Override
    public User getNewInstanceOfUser(String username, String password) {
        Query query = entityManager.createNamedQuery("User.findByUsernameAndPassword");
        query.setParameter("username", username);
        query.setParameter("password", password);

        User user = (User) query.getResultList().get(0);
        return user;
    }

    @Override
    public void deleteProfile(String username, String password) {
        Query query = entityManager.createNamedQuery("User.findByUsernameAndPassword");
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        User user = (User) query.getSingleResult();
        
        entityManager.remove(user);
    }

}
