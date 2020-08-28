package business.productSessionBeans;

import models.Product;
import models.User;
import models.UserProduct;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ProductSessionBean implements ProductSessionBeanLocal {

    @PersistenceContext(unitName = "WebShopJsfPU")
    private EntityManager entityManager;

    @Override
    public List<Product> getAllProductsToBuy(User user) {
        Query query = entityManager.createNamedQuery("Product.findAll");
        List<Product> list = query.getResultList();
        return list.stream().filter(p -> p.getUserId().getId() != user.getId()).collect(Collectors.toList());
    }

    @Override
    public void updateBuy(int quantity, int purchaseId) {
        Query query = entityManager.createNamedQuery("UserProduct.updateByPurchaseId");
        query.setParameter("quantity", quantity);
        query.setParameter("id", purchaseId);
        query.executeUpdate();
    }

    @Override
    public void buyProduct(User user, int productId) {
        Query query = entityManager.createNamedQuery("Product.findById");
        query.setParameter("id", productId);
        Product product = (Product) query.getResultList().get(0);

        UserProduct userProduct = new UserProduct();
        userProduct.setUserId(user);
        userProduct.setProductId(product);
        userProduct.setQuantity(1);
        entityManager.persist(userProduct);
    }

    @Override
    public boolean insertProduct(String name, double price, User user) {
        Query query = entityManager.createNamedQuery("Product.findByName");
        query.setParameter("name", name);
        List list = query.getResultList();

        if (list.size() > 0) {
            return false;
        }

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setUserId(user);
        entityManager.persist(product);

        return true;
    }

    @Override
    public List<Product> getAllUsersProduct(User user) {
        Query query = entityManager.createNamedQuery("Product.findAll");
        List<Product> list = query.getResultList();
        return list.stream().filter(p -> p.getUserId().getId() == user.getId()).collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(int productId) {
        Query query = entityManager.createNamedQuery("Product.findById");
        query.setParameter("id", productId);

        Product product = (Product) query.getSingleResult();

        entityManager.remove(product);
    }

    @Override
    public void updateProduct(String name, double price, int productId) {
        Query query = entityManager.createNamedQuery("Product.updateById");
        query.setParameter("name", name);
        query.setParameter("price", price);
        query.setParameter("id", productId);
        query.executeUpdate();
    }

    @Override
    public void deleteFromBasket(int purchaseId) {
        Query query = entityManager.createNamedQuery("UserProduct.findById");
        query.setParameter("id", purchaseId);

        UserProduct userProduct = (UserProduct) query.getSingleResult();
        entityManager.remove(userProduct);
    }

    @Override
    public List<UserProduct> getAllProductsForUser(User user) {
        Query query = entityManager.createNamedQuery("UserProduct.findByUserId");
        query.setParameter("userId", user);
        List<UserProduct> list = query.getResultList();
        if (!list.isEmpty()) {
            return list;
        } else {
            return null;
        }
    }

}
