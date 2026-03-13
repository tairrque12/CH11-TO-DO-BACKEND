package mil.t2com.moda.todo.task;

import mil.t2com.moda.todo.category.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldSaveNewTask() {
        // Arrange
        Category newCategory = new Category("important");
        Task newTask = new Task(
                "Learn about Mocks",
                "Learn about Inject mocks",
                false,
                newCategory
        );
        newTask.setId(1L);

        when(taskRepository.save(newTask)).thenReturn(newTask);

        // Act
        Task result = taskService.saveTask(newTask);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Learn about Mocks");
        assertThat(result.getDescription()).isEqualTo("Learn about Inject mocks");
        assertThat(result.getCategory().getLabel()).isEqualTo("important");

        verify(taskRepository, only()).save(newTask);
    }
}