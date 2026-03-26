package mil.t2com.moda.todo.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task saveNewTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    // Update POST using ResponseEntity
//    @PostMapping
//    public ResponseEntity<Task> saveNewTask(@RequestBody Task task){
//        return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.CREATED);
//    }

    @GetMapping()
    public List<Task> findAllTasks() { return taskService.findAllTasks(); }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.findTaskById(id);
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // ADD with Tests for: GetById, Put, Delete

    // Example
    //@GetMapping("/{taskId}")

}