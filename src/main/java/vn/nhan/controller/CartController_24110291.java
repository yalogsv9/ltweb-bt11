package vn.nhan.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.nhan.model.Cart_24110291;
import vn.nhan.model.Video_24110291;
import vn.nhan.service.IVideoService_24110291;
import vn.nhan.service.VideoServiceImpl_24110291;

@WebServlet("/cart")
public class CartController_24110291 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IVideoService_24110291 videoService = new VideoServiceImpl_24110291();

    private Cart_24110291 getCart(HttpSession session) {
        Cart_24110291 cart = (Cart_24110291) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart_24110291();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("cart", getCart(session));

        // Lay flash message tu session neu co
        String flashMsg = (String) session.getAttribute("flashMsg");
        if (flashMsg != null) {
            req.setAttribute("flashMsg", flashMsg);
            session.removeAttribute("flashMsg");
        }
        String flashErr = (String) session.getAttribute("flashErr");
        if (flashErr != null) {
            req.setAttribute("flashErr", flashErr);
            session.removeAttribute("flashErr");
        }

        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart_24110291 cart = getCart(session);
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "add": {
                String videoId = req.getParameter("videoId");
                int qty = 1;
                try {
                    String qtyParam = req.getParameter("quantity");
                    if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                        qty = Integer.parseInt(qtyParam.trim());
                    }
                } catch (Exception e) {
                    qty = 1;
                }
                if (qty <= 0) {
                    qty = 1;
                }

                Video_24110291 video = videoService.findById(videoId);
                if (video != null) {
                    cart.add(video, qty);
                    session.setAttribute("flashMsg", "Đã thêm '" + video.getTitle() + "' vào giỏ hàng thành công!");
                } else {
                    session.setAttribute("flashErr", "Không tìm thấy sản phẩm!");
                }

                String redirect = req.getParameter("redirect");
                if ("cart".equals(redirect)) {
                    resp.sendRedirect(req.getContextPath() + "/cart");
                } else {
                    String referer = req.getHeader("referer");
                    if (referer != null && !referer.trim().isEmpty()) {
                        resp.sendRedirect(referer);
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/cart");
                    }
                }
                return;
            }

            case "update": {
                String videoId = req.getParameter("videoId");
                int qty = 1;
                try {
                    qty = Integer.parseInt(req.getParameter("quantity"));
                } catch (Exception e) {
                    qty = 1;
                }

                if (qty > Cart_24110291.MAX_QUANTITY) {
                    qty = Cart_24110291.MAX_QUANTITY;
                    session.setAttribute("flashErr", "Số lượng tối đa cho mỗi sản phẩm là " + Cart_24110291.MAX_QUANTITY + "!");
                } else if (qty <= 0) {
                    cart.remove(videoId);
                    session.setAttribute("flashMsg", "Đã xóa sản phẩm khỏi giỏ hàng!");
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    return;
                }

                cart.update(videoId, qty);
                session.setAttribute("flashMsg", "Đã cập nhật số lượng thành công!");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            case "remove": {
                String videoId = req.getParameter("videoId");
                cart.remove(videoId);
                session.setAttribute("flashMsg", "Đã xóa sản phẩm khỏi giỏ hàng!");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            case "clear": {
                cart.clear();
                session.setAttribute("flashMsg", "Đã xóa toàn bộ giỏ hàng!");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/cart");
                break;
        }
    }
}
