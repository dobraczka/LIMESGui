package swp15.link_discovery.view.graphBuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ThresholdModifyView {

	public ThresholdModifyView(GraphBuildView gbv) {
		Stage stage = new Stage();
		VBox root = new VBox();

		HBox verThresh = new HBox(25);
		HBox accThresh = new HBox(23);
		HBox buttons = new HBox(100);

		Label verificationThresholdlabel = new Label("Verification threshold: ");
		TextField verificationThresholdinput = new TextField();
		verificationThresholdinput.setPromptText("value between 0 and 1");
		verThresh.getChildren().addAll(verificationThresholdlabel,
				verificationThresholdinput);

		Label acceptanceThresholdlabel = new Label("Acceptance threshold: ");
		TextField acceptanceThresholdinput = new TextField();
		acceptanceThresholdinput.setPromptText("value between 0 and 1");
		accThresh.getChildren().addAll(acceptanceThresholdlabel,
				acceptanceThresholdinput);

		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if ((acceptanceThresholdinput.getText() != null
						&& !acceptanceThresholdinput.getText().isEmpty()
						&& verificationThresholdinput.getText() != null && !verificationThresholdinput
						.getText().isEmpty())) {
					try {
						String acc = acceptanceThresholdinput.getText()
								.replaceAll(",", ".");
						gbv.nodeList.get(0).nodeData.param1 = Double
								.parseDouble(acc);
						String ver = verificationThresholdinput.getText()
								.replaceAll(",", ".");
						gbv.nodeList.get(0).nodeData.param2 = Double
								.parseDouble(ver);
					} catch (NumberFormatException exc) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setContentText("Values are not legitimate!");
						alert.showAndWait();
					}

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Entered values are incomplete!");
					alert.showAndWait();
				}

				if (gbv.nodeList.get(0).nodeData.param1 != null
						&& gbv.nodeList.get(0).nodeData.param2 != null) {
					stage.close();
					gbv.draw();
				}
			}
		});

		Button cancel = new Button("Cancel");
		cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});

		buttons.getChildren().addAll(save, cancel);
		buttons.setPadding(new Insets(5, 50, 1, 50));
		buttons.setSpacing(120);

		root.getChildren().addAll(verThresh, accThresh, buttons);
		root.setPadding(new Insets(5, 5, 5, 5));

		Scene scene = new Scene(root);
		stage.setTitle("Modify Thresholds");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

}
