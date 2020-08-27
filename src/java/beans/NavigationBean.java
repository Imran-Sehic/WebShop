package beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

@ManagedBean(name = "navigationBean")
@RequestScoped
public class NavigationBean {

    private String option;
    private List<SelectItem> options;

    public NavigationBean() {
        options = new ArrayList<>();

        SelectItemGroup productGroup = new SelectItemGroup("Products");
        productGroup.setSelectItems(new SelectItem[]{
            new SelectItem("products", "All Products"),
            new SelectItem("insertProduct", "Insert Product"),
            new SelectItem("deleteUpdateProduct", "Delete-Update Product"),
            new SelectItem("basket", "Basket")
        });
        options.add(productGroup);

        SelectItemGroup userGroup = new SelectItemGroup("User");
        userGroup.setSelectItems(new SelectItem[]{
            new SelectItem("updateProfile", "Update Profile"),
            new SelectItem("deleteProfile", "Delete Profile")
        });
        options.add(userGroup);
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public List<SelectItem> getOptions() {
        return options;
    }

    public void valueChanged() {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler handler = context.getApplication().getNavigationHandler();
        handler.handleNavigation(context, null, option + "?faces-redirect=true");
    }

}
