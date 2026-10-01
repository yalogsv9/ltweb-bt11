package vn.nhan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vn.nhan.model.Video_24110291;
import vn.nhan.service.IVideoService_24110291;
import vn.nhan.service.VideoServiceImpl_24110291;

/** Cau 4: trang chi tiet 1 video. */
@WebServlet("/video")
public class VideoDetailController_24110291 extends HttpServlet {
    private final IVideoService_24110291 videoService = new VideoServiceImpl_24110291();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Video_24110291 v = id == null ? null : videoService.findById(id);
        if (v == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Khong tim thay video");
            return;
        }
        req.setAttribute("video", v);
        req.getRequestDispatcher("/WEB-INF/views/video-detail.jsp").forward(req, resp);
    }
}
