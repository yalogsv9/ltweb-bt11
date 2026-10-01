package vn.nhan.service;

import java.util.List;
import vn.nhan.dao.IVideoDAO_24110291;
import vn.nhan.dao.VideoDAOImpl_24110291;
import vn.nhan.model.Video_24110291;

public class VideoServiceImpl_24110291 implements IVideoService_24110291 {
    private final IVideoDAO_24110291 dao = new VideoDAOImpl_24110291();

    @Override public Video_24110291 findById(String videoId) { return dao.findById(videoId); }

    @Override
    public List<Video_24110291> findByCategory(int categoryId, int page, int size) {
        return dao.findByCategory(categoryId, page, size);
    }

    @Override
    public int totalPagesByCategory(int categoryId, int size) {
        return (int) Math.ceil(dao.countByCategory(categoryId) / (double) size);
    }
}
