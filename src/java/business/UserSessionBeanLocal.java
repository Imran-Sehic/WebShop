package business;

import business.model.User;
import javax.ejb.Local;

@Local
public interface UserSessionBeanLocal {
    
    public void updateProfile(User user);
    
}
