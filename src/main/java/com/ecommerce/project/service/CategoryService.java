package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryRequestDTO createCategory(CategoryRequestDTO category);

    CategoryRequestDTO deleteCategory(Long categoryId);

    CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId);
}
