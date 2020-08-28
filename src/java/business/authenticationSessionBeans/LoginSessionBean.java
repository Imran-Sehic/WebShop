package business.authenticationSessionBeans;

import models.User;
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
    
}
