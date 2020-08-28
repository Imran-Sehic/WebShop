package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "user_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserProduct.findAll", query = "SELECT u FROM UserProduct u")
    , @NamedQuery(name = "UserProduct.findById", query = "SELECT u FROM UserProduct u WHERE u.id = :id")
    , @NamedQuery(name = "UserProduct.findByQuantity", query = "SELECT u FROM UserProduct u WHERE u.quantity = :quantity")
    , @NamedQuery(name = "UserProduct.findByUserId", query = "SELECT u FROM UserProduct u WHERE u.userId = :userId")
    , @NamedQuery(name = "UserProduct.updateByPurchaseId", query = "UPDATE UserProduct u SET u.quantity = :quantity WHERE u.id = :id")})
public class UserProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public UserProduct() {
    }

    public UserProduct(Integer id) {
        this.id = id;
    }

    public UserProduct(Integer id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public UserProduct(int id, User user, Product product, int quantity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProduct)) {
            return false;
        }
        UserProduct other = (UserProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "business.model.UserProduct[ id=" + id + " ]";
    }
    
}
