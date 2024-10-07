package e_com.bichitra.e_com02092024.service;

import e_com.bichitra.e_com02092024.payload.CategoryDto;
import e_com.bichitra.e_com02092024.apiResponse.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategory(Integer pageNumber,Integer pageSize,String sortBy,String sortingOrder);
    CategoryDto addCategory(CategoryDto categoryDto);


    CategoryDto deleteCategory(Long id);

    CategoryDto updateCategory(CategoryDto categoryDto, Long id);
}
