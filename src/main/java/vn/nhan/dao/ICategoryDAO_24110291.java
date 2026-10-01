package vn.nhan.dao;

import java.util.List;
import vn.nhan.model.Category_24110291;

public interface ICategoryDAO_24110291 {
    /** Tat ca category kem so luong video (cau 6). */
    List<Category_24110291> findAllWithVideoCount();
    Category_24110291 findById(int id);
}
