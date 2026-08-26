package beemo.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import beemo.BeemoException;

class TaskListTest {
    @Test
    void add_validTask_taskAppended() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertSame(todo, tasks.asList().get(0));
    }

    @Test
    void markAndUnmark_validTask_statusUpdated() throws BeemoException {
        Todo todo = new Todo("read book");
        TaskList tasks = new TaskList(List.of(todo));

        Task markedTask = tasks.markAsDone(1);
        assertSame(todo, markedTask);
        assertTrue(todo.isDone());

        Task unmarkedTask = tasks.markAsNotDone(1);
        assertSame(todo, unmarkedTask);
        assertFalse(todo.isDone());
    }

    @Test
    void delete_validTask_taskRemovedAndRemainingTasksRenumbered()
            throws BeemoException {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));

        Task removedTask = tasks.delete(1);

        assertSame(first, removedTask);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.asList().get(0));
    }

    @Test
    void taskNumberOutsideList_allMutationsThrowException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertAll(
                () -> assertThrows(BeemoException.class, () -> tasks.delete(0)),
                () -> assertThrows(BeemoException.class, () -> tasks.delete(2)),
                () -> assertThrows(BeemoException.class, () -> tasks.markAsDone(-1)),
                () -> assertThrows(BeemoException.class, () -> tasks.markAsNotDone(2)));
        assertEquals(1, tasks.size());
    }

    @Test
    void asList_attemptedModification_unsupportedOperationExceptionThrown() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(UnsupportedOperationException.class,
                () -> tasks.asList().add(new Todo("write report")));
        assertEquals(1, tasks.size());
    }
}
