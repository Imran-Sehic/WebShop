package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserManagedBean;

public class LoginFilter implements Filter {

    public static final String LOGIN_PAGE = "/faces/login.xhtml";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // managed bean name is exactly the session attribute name
        UserManagedBean userManager = (UserManagedBean) httpServletRequest.getSession().getAttribute("userManagedBean");

        if (userManager != null) {
            if (userManager.isLoggedIn()) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else{
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE);
            }
        } else {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE);
        }
        
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        
    }

    @Override
    public void destroy() {
        // close resources
    }
}
