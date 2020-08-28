package business.authenticationSessionBeans;

import models.User;
import javax.ejb.Local;

@Local
public interface LoginSessionBeanLocal {
    
    public User login(String username, String password);
    
}
