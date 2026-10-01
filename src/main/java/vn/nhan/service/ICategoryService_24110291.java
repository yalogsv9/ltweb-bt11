package vn.nhan.service;

import java.util.List;
import vn.nhan.model.Category_24110291;

public interface ICategoryService_24110291 {
    List<Category_24110291> findAllWithVideoCount();
    Category_24110291 findById(int id);
}
