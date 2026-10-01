package vn.nhan.dao;

import java.util.List;
import vn.nhan.model.Video_24110291;

public interface IVideoDAO_24110291 {
    Video_24110291 findById(String videoId);
    List<Video_24110291> findByCategory(int categoryId, int page, int size);
    int countByCategory(int categoryId);
}
