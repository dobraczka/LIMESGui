package swp15.link_discovery.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javafx.concurrent.Task;
import swp15.link_discovery.view.TaskProgressView;

public class TaskProgressController {
	private static final ExecutorService executorService = Executors
			.newCachedThreadPool();

	private TaskProgressView view;
	private Set<Task<?>> tasks;

	public TaskProgressController(TaskProgressView view,
			Task<?>... tasks) {
		view.setController(this);
		this.view = view;
		this.tasks = new HashSet<Task<?>>();
	}

	public <T> void addTask(Task<T> task, Consumer<T> successCallback,
			Consumer<Throwable> errorCallback) {
		task.setOnSucceeded(event -> {
			T result;
			try {
				result = task.get();
			} catch (Exception e) {
				return;
			}
			successCallback.accept(result);

			tasks.remove(task);
			if (tasks.isEmpty()) {
				view.close();
			}
		});

		task.setOnFailed(event -> {
			cancel();
			errorCallback.accept(task.getException());
		});
		executorService.submit(task);
	}

	public void cancel() {
		for (Task<?> task : tasks) {
			task.cancel();
		}
		view.close();
	}

	public static ExecutorService getExecutorservice() {
		return executorService;
	}
}
