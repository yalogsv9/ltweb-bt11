package vn.nhan.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.nhan.model.User_24110291;
import vn.nhan.service.IUserService_24110291;
import vn.nhan.service.UserServiceImpl_24110291;
import vn.nhan.util.MailUtil_24110291;
import vn.nhan.util.OtpUtil_24110291;

@WebServlet("/register")
public class RegisterController_24110291 extends HttpServlet {
    static final long OTP_TTL_MS = 5 * 60 * 1000L;
    private final IUserService_24110291 userService = new UserServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24110291 u = new User_24110291();
        u.setUsername(trim(req.getParameter("username")));
        u.setPassword(req.getParameter("password"));
        u.setFullname(trim(req.getParameter("fullname")));
        u.setEmail(trim(req.getParameter("email")));
        u.setPhone(trim(req.getParameter("phone")));
        u.setAdmin(false);
        u.setActive(true);
        req.setAttribute("form", u);

        String error = null;
        if (u.getUsername().isEmpty() || u.getPassword() == null || u.getPassword().isEmpty()
                || u.getEmail().isEmpty()) {
            error = "Vui long nhap ten dang nhap, mat khau va email.";
        } else if (userService.existsUsername(u.getUsername())) {
            error = "Ten dang nhap da ton tai.";
        } else if (userService.existsEmail(u.getEmail())) {
            error = "Email da duoc su dung.";
        }
        if (error == null) {
            String otp = OtpUtil_24110291.generate();
            try {
                MailUtil_24110291.sendOtp(u.getEmail(), otp);
            } catch (Exception e) {
                error = "Khong gui duoc mail OTP: " + e.getMessage();
            }
            if (error == null) {
                HttpSession session = req.getSession();
                session.setAttribute("pendingUser", u);
                session.setAttribute("otp", otp);
                session.setAttribute("otpExpiry", System.currentTimeMillis() + OTP_TTL_MS);
                resp.sendRedirect(req.getContextPath() + "/verify-otp");
                return;
            }
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
