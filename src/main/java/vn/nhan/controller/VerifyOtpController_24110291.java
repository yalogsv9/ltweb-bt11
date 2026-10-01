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

@WebServlet("/verify-otp")
public class VerifyOtpController_24110291 extends HttpServlet {
    private final IUserService_24110291 userService = new UserServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("pendingUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("pendingUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        String otp = (String) session.getAttribute("otp");
        long expiry = (Long) session.getAttribute("otpExpiry");
        String input = req.getParameter("otp");

        String error = null;
        if (System.currentTimeMillis() > expiry) {
            error = "Ma OTP da het han, vui long dang ky lai.";
        } else if (input == null || !otp.equals(input.trim())) {
            error = "Ma OTP khong dung.";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
            return;
        }
        User_24110291 u = (User_24110291) session.getAttribute("pendingUser");
        u.setActive(true);
        userService.insert(u);
        session.removeAttribute("pendingUser");
        session.removeAttribute("otp");
        session.removeAttribute("otpExpiry");
        resp.sendRedirect(req.getContextPath() + "/login?registered=1");
    }
}
