package mil.t2com.moda.todo.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    Category delayed;
    Category completed;
    Category returnNotFound;

    List<Category> categories;

    // Start using when refactoring
    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        delayed = new Category("delayed");
        delayed.setId(1L);
        completed = new Category("completed");
        completed.setId(2L);

        categories = new ArrayList<>(List.of(delayed, completed));
    }

    @Test
    void shouldSaveNewCategory() {

        // Act
        when(categoryRepository.save(delayed)).thenReturn(delayed);
        Category result = categoryService.saveCategory(delayed);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLabel()).isEqualTo("delayed");
        // other entity properties can be validated here

        verify(categoryRepository, only()).save(delayed);
    }

    @Test
    void shouldFindTaskByLabel() {

        // Act
        when(categoryRepository.findByLabel(delayed.getLabel())).thenReturn(Optional.of(delayed));
        Optional<Category> result = categoryService.findCategoryByLabel(delayed.getLabel());

        // Assert
        assertThat(result.get().getLabel()).isEqualTo("delayed");
        // other entity properties can be validated here

        verify(categoryRepository, only()).findByLabel(delayed.getLabel());

    }

    @Test
    void shouldFindAllCategories() {
        // Act
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> actualRequest = categoryService.findAllCategories();
        // Assert
        verify(categoryRepository, times(1)).findAll();
        assertThat(actualRequest).isEqualTo(categories);
    }

    @Nested
    @DisplayName("Category Save If Not Exists")
    class ValidCurrencyTests {

        Category returnFound;
        Category labelCheck;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            // Arrange
            returnNotFound = new Category("Started");
            returnNotFound.setId(4L);

            returnFound = new Category("Postponed");
            returnFound.setId(5L);

            labelCheck = new Category("lowercase");
        }

        @Test
        void shouldCheckExistingCategoryAndSaveIfNotExists() {
            // Act
            when(categoryRepository.findByLabel(returnNotFound.getLabel())).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(returnNotFound);

            Category result = categoryService.createCategoryIfNotExists("started");
            // Assert
            assertThat(result.getLabel()).isEqualTo("Started");

            verify(categoryRepository, times(1)).save(any(Category.class));
            verify(categoryRepository, times(1)).findByLabel("Started");
        }

        @Test
        void shouldCheckExistingCategoryAndNotSaveIfExists() {
            // Act
            when(categoryRepository.findByLabel(returnFound.getLabel())).thenReturn(Optional.of(returnFound));
            Category result = categoryService.createCategoryIfNotExists("postponed");

            // Assert
            verify(categoryRepository, times(0)).save(any(Category.class));
            verify(categoryRepository, times(1)).findByLabel("Postponed");
            assertThat(result.getLabel()).isEqualTo("Postponed");
        }

        @Test
        void shouldSaveNewCategoryIfNotExistsAndTrimWhitespace() {
            Category returnTrimmed = new Category("Started");

            // Act
            when(categoryRepository.findByLabel("Started")).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(returnTrimmed);

            Category result = categoryService.createCategoryIfNotExists(" started ");
            // Assert
            assertThat(result.getLabel()).isEqualTo("Started");

            verify(categoryRepository, times(1)).findByLabel("Started");
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        void shouldSaveNewCategoryIfNotExistsAndUppercaseFirstLetter() {
            Category returnTrimmed = new Category("Started");

            // Act
            when(categoryRepository.findByLabel("Started")).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(returnTrimmed);

            Category result = categoryService.createCategoryIfNotExists(" started ");
            // Assert
            assertThat(result.getLabel()).isEqualTo("Started");

            verify(categoryRepository, times(1)).findByLabel("Started");
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        void shouldRejectNewCategoryIfBlankOrNull() {
            Category emptyLabel = new Category("");

            IllegalArgumentException errorMessage = assertThrows(IllegalArgumentException.class, () -> {
                categoryService.createCategoryIfNotExists(emptyLabel.getLabel());
            });

            // Assert
            assertEquals("Label cannot be null or empty", errorMessage.getMessage());
            verifyNoInteractions(categoryRepository);
        }

        @Test
        void shouldRejectNewCategoryIfLabelContainsInvalidCharacters() {

        }

    }
}