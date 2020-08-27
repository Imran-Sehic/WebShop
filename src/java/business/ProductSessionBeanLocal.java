package business;

import business.model.Product;
import business.model.User;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ProductSessionBeanLocal {
    
    public List<Product> getAllProductsToBuy(User user);
    
    public List<Product> getAllUsersProduct(User user);
    
    public void updateBuy(int quantity, int purchaseId);
    
    public void buyProduct(User user, int productId);
    
    public boolean insertProduct(String name, double price, User user);
    
    public void deleteProduct(int productId);
    
    public void updateProduct(String name, double price, int productId);
    
    public void deleteFromBasket(int purchaseId);
    
}
