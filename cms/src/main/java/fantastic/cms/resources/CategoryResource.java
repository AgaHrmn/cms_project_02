package fantastic.cms.resources;

import java.util.Arrays;
import java.util.List;

import fantastic.cms.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import fantastic.cms.models.Category;
import fantastic.cms.requests.CategoryRequest;

@RestController
@RequestMapping("/api/category")
@Tag(name = "category", description = "Category API")
public class CategoryResource {

    private final CategoryService categoryService;
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Find", description = "Find the Category by ID")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "200", description = "Category found")
    public ResponseEntity<Category> findOne(@PathVariable("id") String id) {
        return ResponseEntity.ok(new Category());
    }

    @GetMapping
    @Operation(summary = "List", description = "List all categories")
    @ApiResponse(responseCode = "404", description = "Categories not found")
    @ApiResponse(responseCode = "200", description = "Category found")
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(Arrays.asList(new Category(), new Category()));
    }

    @PostMapping
    @Operation(summary = "Create", description = "Create a new category")
    @ApiResponse(responseCode = "400", description = "invalid request")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    public ResponseEntity<Category> newCategory(@RequestBody CategoryRequest category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return new ResponseEntity<>(this.categoryService.create(category, currentPrincipalName), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete", description = "Delete category")
    @ApiResponse(responseCode = "404", description = "Categories not found")
    @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable("id") String id) {

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update", description = "Update category")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") String id, CategoryRequest category) {
        return new ResponseEntity<>(new Category(), HttpStatus.OK);
    }
}
