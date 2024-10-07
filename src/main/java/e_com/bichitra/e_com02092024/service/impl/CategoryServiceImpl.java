package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.exception.APIException;
import e_com.bichitra.e_com02092024.exception.BadRequestFoundException;
import e_com.bichitra.e_com02092024.exception.CategoryNotFoundException;
import e_com.bichitra.e_com02092024.model.Category;
import e_com.bichitra.e_com02092024.payload.CategoryDto;
import e_com.bichitra.e_com02092024.apiResponse.CategoryResponse;
import e_com.bichitra.e_com02092024.repository.CategoryRepository;
import e_com.bichitra.e_com02092024.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getAllCategory
            (Integer pageNumber,Integer pageSize,
             String sortByField,String sortingOrder
            ) {
        Sort sort=sortingOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortByField).ascending()
                :Sort.by(sortByField).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        final Page<Category> categoryPage = repository.findAll(pageable);//Taking data from database
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No categories found");
        }
        final List<CategoryDto> categoryDto = categories.stream().map(category -> modelMapper
                                    .map(category, CategoryDto.class))
                                    .collect(Collectors.toList());
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setCategories(categoryDto);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        return categoryResponse;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        final Category category = modelMapper.map(categoryDto, Category.class);
        final Category categoryFromDb = repository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb!=null){
           throw new APIException(categoryFromDb.getCategoryName()+"  name category already Exist with this");
        }
        final Category save = repository.save(category);
        return modelMapper.map(save, CategoryDto.class);

    }

    @Override
    public CategoryDto deleteCategory(Long id) {
        final Optional<Category> optionalCategory = repository.findById(id);
        if(optionalCategory.isPresent()){
            repository.deleteById(id);
        }else {
            throw new APIException(id.toString());
        }
        final Category category = optionalCategory.get();
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        return categoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        final Category categoryIdFromDB = repository.findById(id)
                .orElseThrow(() -> new BadRequestFoundException("Id not found"));
        final Category category = modelMapper.map(categoryDto, Category.class);
        final Category categoryFromDb = repository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb!=null){
            throw new APIException(categoryFromDb.getCategoryName()+"  name category already Exist with this");
        }
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryId(categoryDto.getCategoryId());
        Category categorySave = repository.save(category);
        return modelMapper.map(categorySave, CategoryDto.class);
    }
}
