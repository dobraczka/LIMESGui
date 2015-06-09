package swp15.link_discovery.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import swp15.link_discovery.controller.WizardController;

/**
 * View class for wizards
 * 
 * @author Manuel Jacob
 */
public class WizardView {
	private WizardController controller;
	private Button buttonBack;
	private Button buttonNext;
	private BorderPane rootPane;
	private Stage stage;

	WizardView() {
		createWindow();
	}

	public void setController(WizardController controller) {
		this.controller = controller;
	}

	private void createWindow() {
		ButtonBar buttonBar = new ButtonBar();
		buttonBack = new Button("Back");
		buttonBack.setOnAction(e -> controller.back());
		buttonBar.getButtons().add(buttonBack);
		buttonNext = new Button("???");
		buttonNext.setOnAction(e -> controller.nextOrFinish());
		buttonBar.getButtons().add(buttonNext);
		Button buttonCancel = new Button("Cancel");
		buttonBar.getButtons().add(buttonCancel);

		rootPane = new BorderPane();
		rootPane.setCenter(null);
		rootPane.setBottom(buttonBar);

		Scene scene = new Scene(rootPane, 800, 600);
		stage = new Stage();
		stage.setTitle("LIMES");
		stage.setScene(scene);
		stage.show();
	}

	public void setEditView(IEditView editView, boolean canGoBack,
			boolean canGoNext) {
		buttonBack.setDisable(!canGoBack);
		buttonNext.setText(canGoNext ? "Next" : "Finish");
		rootPane.setCenter(editView.getPane());
	}

	public void close() {
		stage.close();
	}
}
