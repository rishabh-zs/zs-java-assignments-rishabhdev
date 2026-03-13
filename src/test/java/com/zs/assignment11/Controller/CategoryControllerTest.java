package com.zs.assignment11.Controller;

import com.zs.assignment11.controller.CategoryController;
import com.zs.assignment11.exception.GlobalExceptionHandler;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void stubsGetAllCategoriesValidEndPoint() throws Exception {
		mockMvc.perform(get("/categories/stubApi/GetAllCategories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].name").value("electronics"))
				.andExpect(jsonPath("$[1].name").value("Fashion"))
				.andExpect(jsonPath("$[2].name").value("Sports"));

		verifyNoInteractions(categoryService);
	}

	@Test
	void stubsGetAllCategoriesInvalidEndPoint() throws Exception {
		mockMvc.perform(get("/categoies/stubApi/InvalidApi"))
				.andExpect(status().isNotFound());

		verifyNoInteractions(categoryService);
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
	void handleGetAllCategoriesReturnCategoryFromService() throws Exception {
		when(categoryService.getAllCategories()).thenReturn(List.of(
				new Category(1, "electronics"),
				new Category(2, "fashion")
		));

		mockMvc.perform(get("/categories/GetallCategories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
				.andExpect(jsonPath("$.message").value("all category fetched successfully"))
				.andExpect(jsonPath("$.categories.length()").value(2))
				.andExpect(jsonPath("$.categories[0].name").value("electronics"))
				.andExpect(jsonPath("$.categories[1].name").value("fashion"))
				.andExpect(jsonPath("$.totalCategory").value(2))
				.andExpect(jsonPath("$.totalCategoryCount").value(2));

		verify(categoryService).getAllCategories();
	}

	@Test
	void handleAddCategoryDelegatesToService() throws Exception {
		when(categoryService.addCategory(org.mockito.ArgumentMatchers.any(Category.class)))
				.thenReturn(new Category(10, "electronics"));

		mockMvc.perform(post("/categories/addCategory")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "electronics"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
				.andExpect(jsonPath("$.message").value("category added successfully with id: 10"))
				.andExpect(jsonPath("$.addedCategory.id").value(10))
				.andExpect(jsonPath("$.addedCategory.name").value("electronics"));

		verify(categoryService).addCategory(org.mockito.ArgumentMatchers.any(Category.class));
		verifyNoMoreInteractions(categoryService);
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
				.andExpect(jsonPath("$.status").value("success"))
				.andExpect(jsonPath("$.products.length()").value(3))

				.andExpect(jsonPath("$.products[0].name").value("laptop"))
				.andExpect(jsonPath("$.products[0].price").value(4.50))
				.andExpect(jsonPath("$.products[0].categoryId").value(1))

				.andExpect(jsonPath("$.products[1].name").value("iPhone"))
				.andExpect(jsonPath("$.products[1].price").value(10.0))
				.andExpect(jsonPath("$.products[1].categoryId").value(1))
				.andExpect(jsonPath("$.totalProductCountInCategory").value(3));


		mockMvc.perform(get("/categories/2/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
				.andExpect(jsonPath("$.products.length()").value(2))

				.andExpect(jsonPath("$.products[0].name").value("FaceCream"))
				.andExpect(jsonPath("$.products[0].price").value(4.5))
				.andExpect(jsonPath("$.products[0].categoryId").value(2))

				.andExpect(jsonPath("$.products[1].name").value("faceGel"))
				.andExpect(jsonPath("$.products[1].price").value(10.0))
				.andExpect(jsonPath("$.products[1].categoryId").value(2))
				.andExpect(jsonPath("$.totalProductCountInCategory").value(2));

		verify(categoryService).getProductsByCategoryId(1L);
		verify(categoryService).getProductsByCategoryId(2L);
	}

	@Test
	void handleDeleteCategoryDelegatesToService() throws Exception {
		when(categoryService.deleteCategory(1L)).thenReturn(new Category(1, "electronics"));

		mockMvc.perform(delete("/categories/deleteCategory/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("success"))
				.andExpect(jsonPath("$.message").value("category deleted with id :1"))
				.andExpect(jsonPath("$.deletedCategory.id").value(1))
				.andExpect(jsonPath("$.deletedCategory.name").value("electronics"));

		verify(categoryService).deleteCategory(1L);
		verifyNoMoreInteractions(categoryService);
	}

	@Test
	void handleGetAllProductByCategoryIdReturnsNotFoundWhenCategoryIdDoesNotExist() throws Exception {
		when(categoryService.getProductsByCategoryId(-1L))
				.thenThrow(new IllegalArgumentException("Category id must be a positive number."));

		mockMvc.perform(get("/categories/-1/products"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value("error"))
				.andExpect(jsonPath("$.message").value("category id does not exists"));

		verify(categoryService).getProductsByCategoryId(-1L);
	}
}
