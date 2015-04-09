package swp15.link_discovery.view;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import swp15.link_discovery.model.Result;

public class ResultView {
	private TableView<Result> table;

	public ResultView() {
		createWindow();
	}

	private void createWindow() {
		BorderPane root = new BorderPane();

		table = new TableView<Result>();
		TableColumn<Result, String> columnSource = new TableColumn<Result, String>(
				"Source URI");
		columnSource
				.setCellValueFactory(new PropertyValueFactory<>("sourceURI"));
		table.getColumns().add(columnSource);
		TableColumn<Result, String> columnTarget = new TableColumn<Result, String>(
				"Target URI");
		columnTarget
				.setCellValueFactory(new PropertyValueFactory<>("targetURI"));
		table.getColumns().add(columnTarget);
		TableColumn<Result, Double> columnValue = new TableColumn<Result, Double>(
				"value");
		columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
		table.getColumns().add(columnValue);
		root.setCenter(table);

		Scene scene = new Scene(root, 800, 600);
		Stage stage = new Stage();
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	public void showResults(ObservableList<Result> results) {
		table.setItems(results);
	}
}
