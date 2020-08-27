package business;

import business.model.User;
import business.model.UserProduct;
import java.util.List;
import javax.ejb.Local;

@Local
public interface LoginSessionBeanLocal {
    
    public User login(String username, String password);
    
    public User getNewInstanceOfUser(String username, String password);
    
    public List<UserProduct> getAllProductsForUser(User user);
    
    public void deleteProfile(String username, String password);
    
}
