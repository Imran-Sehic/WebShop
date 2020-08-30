package managedBeans;

import business.productSessionBeans.ProductSessionBeanLocal;
import business.userSessionBeans.UserSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import models.User;
import models.UserProduct;

@ManagedBean(name = "basketManagedBean")
@SessionScoped
public class BasketManagedBean {

    //local variables
    private boolean isBasketEmpty;

    //session beans injection
    @EJB
    private ProductSessionBeanLocal productSessionBean;
    @EJB
    private UserSessionBeanLocal userSessionBean;

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    //constructor
    public BasketManagedBean() {
    }

    //get and set methods
    public boolean isIsBasketEmpty() {
        return isBasketEmpty;
    }

    public void setIsBasketEmpty(boolean isBasketEmpty) {
        this.isBasketEmpty = isBasketEmpty;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    //managed bean methods
    public User refreshUserInformation() {
        User user = userSessionBean.getNewInstanceOfUser(userManagedBean.getUser().getUsername(), userManagedBean.getUser().getPassword());
        List<UserProduct> userProduct = productSessionBean.getAllProductsForUser(userManagedBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }
        return user;
    }

    public List<UserProduct> getProductsFromBasket() {
        User user = refreshUserInformation();
        return user.getUserProductList();
    }

    public String areThereProductsInTheBasket() {
        if (getProductsFromBasket().isEmpty()) {
            setIsBasketEmpty(true);
            return "There are no products in your basket!";
        } else {
            setIsBasketEmpty(false);
            return "Below are the products you bought!";
        }
    }

    public String increaseBuy(int id, int quantity) {
        productSessionBean.updateBuy(++quantity, id);
        return "basket?faces-redirect=true";
    }

    public String decreaseBuy(int id, int quantity) {
        int quantityDecreasedByOne = --quantity;

        if (quantityDecreasedByOne == 0) {
            productSessionBean.deleteFromBasket(id);
            return "basket?faces-redirect=true";
        } else {
            productSessionBean.updateBuy(quantityDecreasedByOne, id);
            return "basket?faces-redirect=true";
        }
    }

    public String deleteFromBasket(int purchaseId) {
        productSessionBean.deleteFromBasket(purchaseId);
        return "basket?faces-redirect=true";
    }

}
