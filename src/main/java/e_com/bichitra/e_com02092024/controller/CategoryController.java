package e_com.bichitra.e_com02092024.controller;

import e_com.bichitra.e_com02092024.payload.CategoryDto;
import e_com.bichitra.e_com02092024.apiResponse.CategoryResponse;
import e_com.bichitra.e_com02092024.service.impl.CategoryServiceImpl;
import e_com.bichitra.e_com02092024.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class CategoryController {

    @Autowired
    public CategoryServiceImpl categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories
            (
                 @RequestParam(name = "pageNumber",defaultValue = "0") Integer pageNumber,
                 @RequestParam(name = "pageSize",defaultValue = "15") Integer pageSize,
                 @RequestParam(name = "sortCategoryByField",defaultValue = AppConstants.SORT_CATEGORY_NAME) String sortCategoryByField,
                 @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder
            ){
        return new ResponseEntity<>(categoryService.
                getAllCategory(pageNumber,pageSize,sortCategoryByField,sortOrder), HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto>  addCategory(@Valid @RequestBody CategoryDto categoryDto){
        final CategoryDto categoryDto1 = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1,HttpStatus.CREATED);

    }

    @PutMapping("/public/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable Long id){
        final CategoryDto newCategoryDto = categoryService.updateCategory(categoryDto, id);
        return new ResponseEntity<>(newCategoryDto,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id){
        final CategoryDto categoryDto = categoryService.deleteCategory(id);
        return new ResponseEntity<>(categoryDto,HttpStatus.ACCEPTED);
    }
}
