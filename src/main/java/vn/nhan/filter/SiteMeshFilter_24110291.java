package vn.nhan.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class SiteMeshFilter_24110291 extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/admin/*", "/WEB-INF/decorators/admin.jsp")
               .addDecoratorPath("/*", "/WEB-INF/decorators/user.jsp");
    }
}
