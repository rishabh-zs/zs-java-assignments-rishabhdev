package com.zs.assignment11.Controller;

import com.zs.assignment11.controller.ProductController;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {
    private MockMvc mockMvc;
    private ProductService productService;
    
    @BeforeEach
    public void setup(){
        productService=mock(ProductService.class);
        ProductController productController=new ProductController(productService);
        mockMvc=MockMvcBuilders.standaloneSetup(productController).build();
    }
    
    @Test
    public void stubApiGetAllProductsValidEndPoint() throws Exception {
        mockMvc.perform(get("/products/stubApi/GetallProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("laptop"))
                .andExpect(jsonPath("$[1].name").value("tv"))
                .andExpect(jsonPath("$[2].name").value("iPhone"))
                .andExpect(jsonPath("$[3].name").value("FaceCream"))
                .andExpect(jsonPath("$[4].name").value("faceGel"));

        verifyNoInteractions(productService);
    }
    
    @Test
    public void stubApiGetAllProductsInvalidEndPoint() throws Exception {
        mockMvc.perform(get("/products/stubApi/InvalidEndpoint")).andExpect(status().isNotFound());
        
        verifyNoInteractions(productService);
    }
    
    @Test
    public void getAllProductsFromService() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(
                new Product(1,"laptop",1000.0,1),
                new Product(2,"tv",2000.0,1),
                new Product(3,"iPhone",200.0,1),
                new Product(4,"FaceCream",4.50,2),
                new Product(5,"faceGel",10.0,2)));
        
        mockMvc.perform(get("/products/GetallProducts")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("laptop"))
                .andExpect(jsonPath("$[0].price").value(1000.0))
                .andExpect(jsonPath("$[1].name").value("tv"))
                .andExpect(jsonPath("$[1].price").value(2000.0))
                .andExpect(jsonPath("$[2].name").value("iPhone"))
                .andExpect(jsonPath("$[2].price").value(200.0))
                .andExpect(jsonPath("$[3].name").value("FaceCream"))
                .andExpect(jsonPath("$[3].price").value(4.50))
                .andExpect(jsonPath("$[4].price").value(10.0))
                .andExpect(jsonPath("$[4].name").value("faceGel"));

        verify(productService).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void getAllProductsFromServiceEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/products/GetallProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productService).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void getAllProductsFromServiceInvalidEndPoint() throws Exception {
        mockMvc.perform(get("/products/InvalidEndpoint"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(productService);
    }
    
    
    
}
