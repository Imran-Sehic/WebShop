package business.userSessionBeans;

import models.User;
import javax.ejb.Local;

@Local
public interface UserSessionBeanLocal {
    
    public void updateProfile(User user);
    
    public User getNewInstanceOfUser(String username, String password);
    
    public void deleteProfile(String username, String password);
    
}
