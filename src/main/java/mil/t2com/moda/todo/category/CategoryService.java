package mil.t2com.moda.todo.category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category createCategoryIfNotExists(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException("Label cannot be null or empty");
        }

        String findTrimmedLabel = label.trim();
        String formattedLabel = Character.toUpperCase(findTrimmedLabel.charAt(0)) + findTrimmedLabel.substring(1);

        return categoryRepository.findByLabel(formattedLabel).orElseGet(() -> categoryRepository.save(new Category(formattedLabel)));

        // Refactored below to make the above cleaner, left it here so you can see the changes:
//        if(queryCategory.isEmpty()) {
//            String trimmedLabel = label.trim();
//            return categoryRepository.save(new Category(Character.toUpperCase(trimmedLabel.charAt(0)) + trimmedLabel.substring(1)));
//        }
//
//        return queryCategory.get();
    }

    public Optional<Category> findCategoryByLabel(String label) { return categoryRepository.findByLabel(label); }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long id) { return categoryRepository.findById(id); }

    // ADD with Tests for: Put, Delete

}