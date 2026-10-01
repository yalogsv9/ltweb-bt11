package vn.nhan.service;

import java.util.List;
import vn.nhan.dao.CategoryDAOImpl_24110291;
import vn.nhan.dao.ICategoryDAO_24110291;
import vn.nhan.model.Category_24110291;

public class CategoryServiceImpl_24110291 implements ICategoryService_24110291 {
    private final ICategoryDAO_24110291 dao = new CategoryDAOImpl_24110291();

    @Override public List<Category_24110291> findAllWithVideoCount() { return dao.findAllWithVideoCount(); }
    @Override public Category_24110291 findById(int id) { return dao.findById(id); }
}
