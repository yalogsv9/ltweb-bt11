package vn.nhan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.nhan.model.Category_24110291;
import vn.nhan.model.Video_24110291;
import vn.nhan.service.CategoryServiceImpl_24110291;
import vn.nhan.service.ICategoryService_24110291;
import vn.nhan.service.IVideoService_24110291;
import vn.nhan.service.VideoServiceImpl_24110291;

@WebServlet("/videos")
public class VideoByCategoryController_24110291 extends HttpServlet {
    private static final int PAGE_SIZE = 3;
    private final ICategoryService_24110291 categoryService = new CategoryServiceImpl_24110291();
    private final IVideoService_24110291 videoService = new VideoServiceImpl_24110291();

    public static class Block_24110291 {
        private final Category_24110291 category;
        private final List<Video_24110291> videos;
        private final int page;
        private final int totalPages;
        private final String baseQuery; // cac tham so trang cua category khac
        private final int categoryId;

        Block_24110291(Category_24110291 category, List<Video_24110291> videos, int page, int totalPages,
                       String baseQuery) {
            this.category = category;
            this.videos = videos;
            this.page = page;
            this.totalPages = totalPages;
            this.baseQuery = baseQuery;
            this.categoryId = category.getCategoryId();
        }

        public Category_24110291 getCategory() { return category; }
        public List<Video_24110291> getVideos() { return videos; }
        public int getPage() { return page; }
        public int getTotalPages() { return totalPages; }

        public String link(int p) {
            return "?" + baseQuery + "p" + categoryId + "=" + p;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category_24110291> categories = categoryService.findAllWithVideoCount(); // co videoCount (cau 6)
        List<Block_24110291> blocks = new ArrayList<>();
        for (Category_24110291 c : categories) {
            int totalPages = videoService.totalPagesByCategory(c.getCategoryId(), PAGE_SIZE);
            int page = parse(req.getParameter("p" + c.getCategoryId()), Math.max(1, totalPages));

            StringBuilder base = new StringBuilder();
            for (Category_24110291 o : categories) {
                if (o.getCategoryId() == c.getCategoryId()) continue;
                String v = req.getParameter("p" + o.getCategoryId());
                if (v != null && v.matches("\\d+")) base.append('p').append(o.getCategoryId()).append('=').append(v).append('&');
            }
            blocks.add(new Block_24110291(c, videoService.findByCategory(c.getCategoryId(), page, PAGE_SIZE),
                    page, totalPages, base.toString()));
        }
        req.setAttribute("blocks", blocks);
        req.getRequestDispatcher("/WEB-INF/views/videos.jsp").forward(req, resp);
    }

    private static int parse(String s, int max) {
        try {
            return Math.min(Math.max(1, Integer.parseInt(s)), max);
        } catch (Exception e) {
            return 1;
        }
    }
}
