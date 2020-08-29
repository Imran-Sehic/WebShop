package managedBeans;

import business.authenticationSessionBeans.LoginSessionBeanLocal;
import business.productSessionBeans.ProductSessionBeanLocal;
import business.userSessionBeans.UserSessionBeanLocal;
import models.Product;
import models.User;
import models.UserProduct;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "productManagedBean")
@SessionScoped
public class ProductManagedBean implements Serializable {

    //local variables
    private String name;
    private Double price;
    private String emptyMessage;
    private boolean isTableEmpty;
    private String emptyBasketMessage;
    private boolean isBasketEmpty;
    private String emptyUsersProductMessage;
    private boolean isUsersProductEmpty;

    private String insertProductMessage;
    
    private Product product;

    //session beans injection
    @EJB
    private ProductSessionBeanLocal productSessionBean;
    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private UserSessionBeanLocal userSessionBean;

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    //constructor
    public ProductManagedBean() {
    }

    //get and set methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public boolean getIsTableEmpty() {
        return isTableEmpty;
    }

    public void setIsTableEmpty(boolean isTableEmpty) {
        this.isTableEmpty = isTableEmpty;
    }

    public String getEmptyBasketMessage() {
        return emptyBasketMessage;
    }

    public void setEmptyBasketMessage(String emptyBasketMessage) {
        this.emptyBasketMessage = emptyBasketMessage;
    }

    public boolean getIsBasketEmpty() {
        return isBasketEmpty;
    }

    public void setIsBasketEmpty(boolean isBasketEmpty) {
        this.isBasketEmpty = isBasketEmpty;
    }

    public String getEmptyUsersProductMessage() {
        return emptyUsersProductMessage;
    }

    public void setEmptyUsersProductMessage(String emptyUsersProductMessage) {
        this.emptyUsersProductMessage = emptyUsersProductMessage;
    }

    public boolean isIsUsersProductEmpty() {
        return isUsersProductEmpty;
    }

    public void setIsUsersProductEmpty(boolean isUsersProductEmpty) {
        this.isUsersProductEmpty = isUsersProductEmpty;
    }

    public String getInsertProductMessage() {
        return insertProductMessage;
    }

    public void setInsertProductMessage(String insertProductMessage) {
        this.insertProductMessage = insertProductMessage;
    }

    public Product getProduct() {
        return product;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }
    
    //managed bean methods
    public User refreshUserInformation(){
        User user = userSessionBean.getNewInstanceOfUser(userManagedBean.getUser().getUsername(), userManagedBean.getUser().getPassword());
        List<UserProduct> userProduct = productSessionBean.getAllProductsForUser(userManagedBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }
        return user;
    }

    public List<Product> getAllProductsToBuy() {
        if (productSessionBean.getAllProductsToBuy(userManagedBean.getUser()).isEmpty()) {
            return null;
        } else {
            return productSessionBean.getAllProductsToBuy(userManagedBean.getUser());
        }
    }

    public List<Product> getAllUsersProducts() {
        if (productSessionBean.getAllUsersProduct(userManagedBean.getUser()).isEmpty()) {
            return null;
        } else {
            return productSessionBean.getAllUsersProduct(userManagedBean.getUser());
        }
    }

    public String isTableEmpty() {
        if (productSessionBean.getAllProductsToBuy(userManagedBean.getUser()).isEmpty()) {
            setIsTableEmpty(true);
            return "No products available!";
        } else {
            setIsTableEmpty(false);
            return "Below are the products!";
        }
    }

    public String isUsersProductEmpty() {
        if (productSessionBean.getAllUsersProduct(userManagedBean.getUser()).isEmpty()) {
            setIsUsersProductEmpty(true);
            return "You have no products published!";
        } else {
            setIsUsersProductEmpty(false);
            return "Below are your products!";
        }
    }

    public String isBasketEmpty() {
        User user = refreshUserInformation();

        if (user.getUserProductList().isEmpty()) {
            setIsBasketEmpty(true);
            return "There are no products in your basket!";
        } else {
            setIsBasketEmpty(false);
            return "Below are the products you bought!";
        }
    }

    public void buyProduct(int productId) {
        User user = refreshUserInformation();
        
        for (UserProduct purchase : user.getUserProductList()) {
            int quantity = purchase.getQuantity();
            if (productId == purchase.getProductId().getId()) {
                quantity++;
                productSessionBean.updateBuy(quantity, purchase.getId());
                return;
            }
        }
        productSessionBean.buyProduct(user, productId);
    }

    public List<UserProduct> getProductsFromBasket() {
        User user = refreshUserInformation();
        return user.getUserProductList();
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

    public String insertProduct() {
        System.out.println(price);
        if (name.equals("") || name == null) {
            setInsertProductMessage("No field should be empty!");
            clearFields();
            return "insertProduct?faces-redirect=true";
        }

        if (productSessionBean.insertProduct(name, price, userManagedBean.getUser())) {
            setInsertProductMessage("");
            clearFields();
            return "products?faces-redirect=true";
        } else {
            setInsertProductMessage("The product with name already exists!");
            clearFields();
            return "insertProduct?faces-redirect=true";
        }
    }

    public String deleteProduct(int productId) {
        productSessionBean.deleteProduct(productId);
        return "deleteUpdateProduct?faces-redirect=true";
    }

    public String goToUpdateProduct(String name, Double price, int productId) {
        product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setId(productId);
        return "updateProduct?faces-redirect=true";
    }
    
    public String updateProduct(){
        productSessionBean.updateProduct(product.getName(), product.getPrice(), product.getId());
        return "deleteUpdateProduct?faces-redirect=true";
    }

    public String deleteFromBasket(int purchaseId) {
        productSessionBean.deleteFromBasket(purchaseId);
        return "basket?faces-redirect=true";
    }

    public void clearFields() {
        setName("");
        setPrice(null);
    }

}
