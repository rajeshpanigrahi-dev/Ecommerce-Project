package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize) {

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        List<Category> getCategory = categoryRepository.findAll();
        if(getCategory.isEmpty()){
            throw new APIException("No categories created till now");
        }
        List<CategoryRequestDTO> categoryDTOs = getCategory.stream()
                .map(category -> modelMapper.map(category,CategoryRequestDTO.class)).toList();

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryDTOs);
        return categoryResponseDTO;
    }

    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = modelMapper.map(categoryRequestDTO,Category.class);
        Category findCategory = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if(findCategory!=null){
            throw new APIException("Category with the name "+ categoryRequestDTO.getCategoryName()+" already exists");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryRequestDTO.class);

    }

    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
            categoryRepository.delete(category);
            return modelMapper.map(category,CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Category category = modelMapper.map(categoryRequestDTO,Category.class);
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryRequestDTO.class);
    }
}
