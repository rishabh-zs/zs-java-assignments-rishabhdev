package com.zs.assignment11;

import com.zs.assignment11.service.CategoryService;
import com.zs.assignment11.service.ProductService;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Initialize.
 */
@Configuration
public class Initialize {
    private static final Logger log = LoggerUtil.getLogger(Initialize.class);

    /**
     * Initialize tables command line runner.
     *
     * @param categoryService the category service
     * @param productService  the product service
     * @return the command line runner
     */
    @Bean
    CommandLineRunner initializeTables(CategoryService categoryService, ProductService productService) {
        return args -> {
            try{
                log.info("Initializing database tables");
                categoryService.CreateCategoryTable();
                productService.CreateProductTable();
                log.info("Database tables initialized successfully");
            }catch(Exception e){
                e.printStackTrace();
                log.error("Database tables initialization failed");
            }
        };
    }
}
