package business;

import javax.ejb.Local;

@Local
public interface RegisterSessionBeanLocal {
    
    public boolean register(String name, String surname, String username, String password);
    
}
