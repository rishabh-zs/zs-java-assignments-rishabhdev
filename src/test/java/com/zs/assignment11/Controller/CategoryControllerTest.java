package com.zs.assignment11.Controller;

import com.zs.assignment11.controller.CategoryController;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

	private MockMvc mockMvc;
	private CategoryService categoryService;
	private CategoryController categoryController;

	@BeforeEach
	void setUp() {
		categoryService = mock(CategoryService.class);
		categoryController = new CategoryController(categoryService);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	void stubsGetAllCategoriesValidEndPoint() throws Exception {
		mockMvc.perform(get("/categories/stubApi/GetAllCategories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].name").value("electronics"))
				.andExpect(jsonPath("$[1].name").value("Fashion"))
				.andExpect(jsonPath("$[2].name").value("mobile"));

		verifyNoInteractions(categoryService);
	}

	@Test
	void stubsGetAllCategoriesInvalidEndPoint() throws Exception {
		mockMvc.perform(get("/categoies/stubApi/InvalidApi"))
				.andExpect(status().isNotFound());

		verifyNoInteractions(categoryService);
	}

	@Test
	void handleGetAllCategoriesReturnCategoryFromService() throws Exception {
		when(categoryService.getAllCategories()).thenReturn(List.of(
				new Category(1, "electronics"),
				new Category(2, "fashion")
		));

		mockMvc.perform(get("/categories/GetallCategories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].name").value("electronics"))
				.andExpect(jsonPath("$[1].name").value("fashion"));

		verify(categoryService).getAllCategories();
	}

	@Test
	void stubsGetProductsByCategoryId() throws Exception {
		mockMvc.perform(get("/categories/stubApi/1/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))

				.andExpect(jsonPath("$[0].name").value("laptop"))
				.andExpect(jsonPath("$[0].price").value(1000.0))
				.andExpect(jsonPath("$[0].categoryId").value(1))

				.andExpect(jsonPath("$[1].name").value("tv"))
				.andExpect(jsonPath("$[1].price").value(2000.0))
				.andExpect(jsonPath("$[1].categoryId").value(1))

				.andExpect(jsonPath("$[2].name").value("iPhone"))
				.andExpect(jsonPath("$[2].price").value(200.0))
				.andExpect(jsonPath("$[2].categoryId").value(1));


		mockMvc.perform(get("/categories/stubApi/2/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))

				.andExpect(jsonPath("$[0].name").value("FaceCream"))
				.andExpect(jsonPath("$[0].price").value(4.50))
				.andExpect(jsonPath("$[0].categoryId").value(2))

				.andExpect(jsonPath("$[1].name").value("faceGel"))
				.andExpect(jsonPath("$[1].price").value(10.0))
				.andExpect(jsonPath("$[1].categoryId").value(2));

		verifyNoInteractions(categoryService);
	}

	@Test
	void handleGetAllProductByCategoryIdReturnsProductsFromService() throws Exception {
		when(categoryService.getProductsByCategoryId(1L)).thenReturn(List.of(
				new Product(1, "laptop", 4.50, 1),
				new Product(2, "iPhone", 10.0, 1),
				new Product(3, "tv", 10.0, 1)
		));

		when(categoryService.getProductsByCategoryId(2L)).thenReturn(List.of(
				new Product(4, "FaceCream", 4.50, 2),
				new Product(5, "faceGel", 10.0, 2)
		));

		mockMvc.perform(get("/categories/1/products")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))

				.andExpect(jsonPath("$[0].name").value("laptop"))
				.andExpect(jsonPath("$[0].price").value(4.50))
				.andExpect(jsonPath("$[0].categoryId").value(1))

				.andExpect(jsonPath("$[1].name").value("iPhone"))
				.andExpect(jsonPath("$[1].price").value(10.0))
				.andExpect(jsonPath("$[1].categoryId").value(1));


		mockMvc.perform(get("/categories/2/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))

				.andExpect(jsonPath("$[0].name").value("FaceCream"))
				.andExpect(jsonPath("$[0].price").value(4.5))
				.andExpect(jsonPath("$[0].categoryId").value(2))

				.andExpect(jsonPath("$[1].name").value("faceGel"))
				.andExpect(jsonPath("$[1].price").value(10.0))
				.andExpect(jsonPath("$[1].categoryId").value(2));

		verify(categoryService).getProductsByCategoryId(1L);
		verify(categoryService).getProductsByCategoryId(2L);
	}

	@Test
	void handleGetAllProductByCategoryIdReturnsServerErrorWhenServiceThrowsException() {
		when(categoryService.getProductsByCategoryId(-1L))
				.thenThrow(new IllegalArgumentException("Category id must be a positive number."));

		ServletException exception = assertThrows(ServletException.class,
				() -> mockMvc.perform(get("/categories/-1/products")));

		IllegalArgumentException cause = assertInstanceOf(IllegalArgumentException.class, exception.getCause());
		assertEquals("Category id must be a positive number.", cause.getMessage());

		verify(categoryService).getProductsByCategoryId(-1L);
	}
}
