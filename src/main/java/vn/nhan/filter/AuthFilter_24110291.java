package vn.nhan.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.nhan.model.User_24110291;

/** Chi admin moi vao duoc /admin/*. */
@WebFilter("/admin/*")
public class AuthFilter_24110291 implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);
        User_24110291 u = session == null ? null : (User_24110291) session.getAttribute("user");
        if (u == null || !u.isAdmin()) {
            ((HttpServletResponse) resp).sendRedirect(request.getContextPath() + "/login");
            return;
        }
        chain.doFilter(req, resp);
    }
}
