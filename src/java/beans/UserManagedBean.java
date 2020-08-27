package beans;

import business.LoginSessionBeanLocal;
import business.RegisterSessionBeanLocal;
import business.model.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import business.UserSessionBeanLocal;

@ManagedBean(name = "userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String loginError;
    private String registerError;
    
    private User user;

    @EJB
    RegisterSessionBeanLocal registerBean;
    @EJB
    LoginSessionBeanLocal loginBean;
    @EJB
    UserSessionBeanLocal userBean;

    public UserManagedBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User getUser() {
        return user;
    }

    public String getLoginError() {
        return loginError;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    public String getRegisterError() {
        return registerError;
    }

    public void setRegisterError(String registerError) {
        this.registerError = registerError;
    }

    public String login() {
        user = loginBean.login(username, password);
        
        if (username == null && password == null) {
            return "login?faces-redirect=true";
        } else if (username.equals("") || password.equals("")) {
            setLoginError("No field should be blank!");
            clearFields();
            return "login?faces-redirect=true";
        }

        if (user != null) {
            setLoginError("");
            clearFields();
            return "products?faces-redirect=true";
        } else {
            setLoginError("Wrong username and/or password!");
            return "login?faces-redirect=true";
        }
    }
    
    public String logout(){
        user = null;
        clearFields();
        return "login?faces-redirect=true";
    }
    
    public boolean isLoggedIn(){
        return user != null;
    }

    public String register() {
        if (username.equals("") || password.equals("") || name.equals("") || surname.equals("")) {
            setRegisterError("No field should be blank!");
            return "register?faces-redirect=true";
        }

        if (password.length() < 8) {
            setRegisterError("Your password should be minimum 8 characters long!");
            clearFields();
            return "register?faces-redirect=true";
        }

        if (registerBean.register(name, surname, username, password)) {
            setRegisterError("");
            clearFields();
            return "login?faces-redirect=true";
        } else {
            setRegisterError("The username you entered is occupied!");
            clearFields();
            return "register?faces-redirect=true";
        }
    }

    public void clearFields() {
        setName("");
        setSurname("");
        setUsername("");
        setPassword("");
    }

    public String goToRegister() {
        setLoginError("");
        clearFields();
        return "register?faces-redirect=true";
    }

    public String goToLogin() {
        setRegisterError("");
        clearFields();
        return "login?faces-redirect=true";
    }
    
    public String deleteProfile(){
        loginBean.deleteProfile(user.getUsername(), user.getPassword());
        return logout();
    }
    
    public String updateProfile(){
        userBean.updateProfile(user);
        return "products?faces-redirect=true";
    }

}
