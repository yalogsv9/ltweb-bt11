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

@WebServlet("/admin/users")
public class AdminUserController_24110291 extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final IUserService_24110291 userService = new UserServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            req.setAttribute("mode", "add");
            req.setAttribute("form", new User_24110291());
            req.getRequestDispatcher("/WEB-INF/views/admin/user-form.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            User_24110291 u = userService.findByUsername(req.getParameter("username"));
            if (u == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }
            req.setAttribute("mode", "edit");
            req.setAttribute("form", u);
            req.getRequestDispatcher("/WEB-INF/views/admin/user-form.jsp").forward(req, resp);
        } else {
            list(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalPages = Math.max(1, userService.totalPages(PAGE_SIZE));
        int page = parsePage(req.getParameter("page"), totalPages);
        req.setAttribute("users", userService.findPage(page, PAGE_SIZE));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            String username = req.getParameter("username");
            User_24110291 me = (User_24110291) req.getSession().getAttribute("user");
            if (me != null && me.getUsername().equals(username)) {
                req.getSession().setAttribute("flash", "Khong the xoa chinh tai khoan dang dang nhap.");
            } else {
                userService.delete(username);
                req.getSession().setAttribute("flash", "Da xoa user " + username + ".");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/users?page=" + nz(req.getParameter("page"), "1"));
            return;
        }

        // save (add / edit)
        boolean add = "add".equals(req.getParameter("mode"));
        User_24110291 u = new User_24110291();
        u.setUsername(nz(req.getParameter("username"), "").trim());
        u.setPassword(req.getParameter("password"));
        u.setPhone(req.getParameter("phone"));
        u.setFullname(req.getParameter("fullname"));
        u.setEmail(req.getParameter("email"));
        u.setAdmin(req.getParameter("admin") != null);
        u.setActive(req.getParameter("active") != null);
        u.setImages(req.getParameter("images"));

        if (u.getUsername().isEmpty()) {
            req.setAttribute("error", "Username khong duoc de trong.");
        } else if (add && userService.existsUsername(u.getUsername())) {
            req.setAttribute("error", "Username da ton tai.");
        }
        if (req.getAttribute("error") != null) {
            req.setAttribute("mode", add ? "add" : "edit");
            req.setAttribute("form", u);
            req.getRequestDispatcher("/WEB-INF/views/admin/user-form.jsp").forward(req, resp);
            return;
        }
        if (add) userService.insert(u); else userService.update(u);
        req.getSession().setAttribute("flash", add ? "Da them user " + u.getUsername() + "." : "Da cap nhat user " + u.getUsername() + ".");
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private static int parsePage(String s, int max) {
        try {
            return Math.min(Math.max(1, Integer.parseInt(s)), max);
        } catch (Exception e) {
            return 1;
        }
    }

    private static String nz(String s, String def) {
        return s == null ? def : s;
    }
}
