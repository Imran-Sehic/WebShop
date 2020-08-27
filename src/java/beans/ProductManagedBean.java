package beans;

import business.LoginSessionBeanLocal;
import business.ProductSessionBeanLocal;
import business.model.Product;
import business.model.User;
import business.model.UserProduct;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "productManagedBean")
@SessionScoped
public class ProductManagedBean implements Serializable {

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

    @EJB
    private ProductSessionBeanLocal productBean;
    @EJB
    private LoginSessionBeanLocal loginBean;

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userBean;

    public ProductManagedBean() {
    }

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

    public UserManagedBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserManagedBean userBean) {
        this.userBean = userBean;
    }

    public List<Product> getAllProductsToBuy() {
        if (productBean.getAllProductsToBuy(userBean.getUser()).isEmpty()) {
            return null;
        } else {
            return productBean.getAllProductsToBuy(userBean.getUser());
        }
    }

    public List<Product> getAllUsersProducts() {
        if (productBean.getAllUsersProduct(userBean.getUser()).isEmpty()) {
            return null;
        } else {
            return productBean.getAllUsersProduct(userBean.getUser());
        }
    }

    public String isTableEmpty() {
        if (productBean.getAllProductsToBuy(userBean.getUser()).isEmpty()) {
            setIsTableEmpty(true);
            return "No products available!";
        } else {
            setIsTableEmpty(false);
            return "Below are the products!";
        }
    }

    public String isUsersProductEmpty() {
        if (productBean.getAllUsersProduct(userBean.getUser()).isEmpty()) {
            setIsUsersProductEmpty(true);
            return "You have no products published!";
        } else {
            setIsUsersProductEmpty(false);
            return "Below are your products!";
        }
    }

    public String isBasketEmpty() {
        User user = loginBean.getNewInstanceOfUser(userBean.getUser().getUsername(), userBean.getUser().getPassword());
        List<UserProduct> userProduct = loginBean.getAllProductsForUser(userBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }

        if (user.getUserProductList().isEmpty()) {
            setIsBasketEmpty(true);
            return "There are no products in your basket!";
        } else {
            setIsBasketEmpty(false);
            return "Below are the products you bought!";
        }
    }

    public void buyProduct(int productId) {
        User user = loginBean.getNewInstanceOfUser(userBean.getUser().getUsername(), userBean.getUser().getPassword());
        List<UserProduct> userProduct = loginBean.getAllProductsForUser(userBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }
        for (UserProduct purchase : user.getUserProductList()) {
            int quantity = purchase.getQuantity();
            if (productId == purchase.getProductId().getId()) {
                quantity++;
                productBean.updateBuy(quantity, purchase.getId());
                return;
            }
        }
        productBean.buyProduct(user, productId);
    }

    public List<UserProduct> getProductsFromBasket() {
        User user = loginBean.getNewInstanceOfUser(userBean.getUser().getUsername(), userBean.getUser().getPassword());
        List<UserProduct> userProduct = loginBean.getAllProductsForUser(userBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }
        return user.getUserProductList();
    }

    public String increaseBuy(int id, int quantity) {
        User user = loginBean.getNewInstanceOfUser(userBean.getUser().getUsername(), userBean.getUser().getPassword());
        List<UserProduct> userProduct = loginBean.getAllProductsForUser(userBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }
        productBean.updateBuy(++quantity, id);
        return "basket?faces-redirect=true";
    }

    public String decreaseBuy(int id, int quantity) {
        User user = loginBean.getNewInstanceOfUser(userBean.getUser().getUsername(), userBean.getUser().getPassword());
        List<UserProduct> userProduct = loginBean.getAllProductsForUser(userBean.getUser());
        if (userProduct != null) {
            user.setUserProductList(userProduct);
        }

        int quantityDecreasedByOne = --quantity;

        if (quantityDecreasedByOne == 0) {
            productBean.deleteFromBasket(id);
            return "basket?faces-redirect=true";
        } else {
            productBean.updateBuy(quantityDecreasedByOne, id);
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

        if (productBean.insertProduct(name, price, userBean.getUser())) {
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
        productBean.deleteProduct(productId);
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
        productBean.updateProduct(product.getName(), product.getPrice(), product.getId());
        return "deleteUpdateProduct?faces-redirect=true";
    }

    public String deleteFromBasket(int purchaseId) {
        productBean.deleteFromBasket(purchaseId);
        return "basket?faces-redirect=true";
    }

    public void clearFields() {
        setName("");
        setPrice(0.0);
    }

}
