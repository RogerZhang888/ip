package alpha.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/** Tests task-list mutation, one-based indexing, and encapsulation. */
class TaskListTest {
    @Test
    void managesTasksUsingOneBasedNumbers() {
        TaskList tasks = new TaskList();
        Task todo = tasks.addTask("read book");
        Task deadline = tasks.addTask(new Deadline("return book", LocalDateTime.of(2019, 12, 2, 18, 0)));

        assertEquals(2, tasks.size());
        assertSame(todo, tasks.getTask(1));
        assertSame(deadline, tasks.getTask(2));
        assertTrue(tasks.getTask(0) == null);
        assertTrue(tasks.getTask(3) == null);

        tasks.markDone(2);
        assertTrue(deadline.isDone());
        tasks.markUndone(2);
        assertFalse(deadline.isDone());

        assertSame(todo, tasks.deleteTask(1));
        assertEquals(1, tasks.size());
        assertTrue(tasks.deleteTask(0) == null);
        assertTrue(tasks.deleteTask(2) == null);
    }

    @Test
    void exposesTasksAsReadOnlyCollection() {
        TaskList tasks = new TaskList();
        tasks.addTask("read book");

        assertThrows(UnsupportedOperationException.class,
                () -> tasks.getTasks().add(new Todo("write notes")));
    }
}
