package vn.nhan.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.nhan.model.User_24110291;
import vn.nhan.service.IUserService_24110291;
import vn.nhan.service.UserServiceImpl_24110291;

@WebServlet("/login")
public class LoginController_24110291 extends HttpServlet {
    private final IUserService_24110291 userService = new UserServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User_24110291 u = userService.login(username, password);
        if (u != null && u.isAdmin()) {
            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/admin/home");
            return;
        }
        req.setAttribute("username", username);
        req.setAttribute("error", u == null
                ? "Sai ten dang nhap, mat khau hoac tai khoan chua kich hoat."
                : "Tai khoan khong co quyen quan tri (admin).");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
