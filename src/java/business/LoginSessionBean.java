package business;

import business.model.User;
import business.model.UserProduct;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LoginSessionBean implements LoginSessionBeanLocal {

    @PersistenceContext(unitName = "WebShopJsfPU")
    private EntityManager entityManager;
    
    @Override
    public User login(String username, String password) {
        Query query = entityManager.createNamedQuery("User.findByUsernameAndPassword");
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        User user = null;
        
        List list = query.getResultList();
        
        if(list == null){
            return user;
        }else if(!list.isEmpty()){
            user = (User) list.get(0);
            return user;
        }
        return user;
    }
    
    @Override
    public User getNewInstanceOfUser(String username, String password){
        Query query = entityManager.createNamedQuery("User.findByUsernameAndPassword");
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        User user = (User) query.getResultList().get(0);
        return user;
    }

    @Override
    public List<UserProduct> getAllProductsForUser(User user) {
        Query query = entityManager.createNamedQuery("UserProduct.findByUserId");
        query.setParameter("userId", user);
        List<UserProduct> list = query.getResultList();
        if(!list.isEmpty()){
            return list;
        }else{
            return null;
        }
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
