package swp15.link_discovery.controller;

import mockit.FullVerifications;
import mockit.Mocked;

import org.junit.Test;

import swp15.link_discovery.view.WizardView;

public class TestWizardController {
	@Mocked
	private Runnable finishCallback;
	@Mocked
	private Runnable cancelCallback;
	@Mocked
	private WizardView view;
	@Mocked
	private IEditController editController1;
	@Mocked
	private IEditController editController2;
	@Mocked
	private IEditController editController3;

	@Test
	public void testLinear() {
		WizardController controller = new WizardController(finishCallback,
				cancelCallback, view, editController1, editController2,
				editController3);
		new FullVerifications() {
			{
				view.setController(controller);
				editController1.load();
				view.setEditView(editController1.getView(), false, true);
			}
		};

		controller.back();
		new FullVerifications() {
			{
			}
		};

		controller.nextOrFinish();
		new FullVerifications() {
			{
				editController1.save();
				editController2.load();
				view.setEditView(editController2.getView(), true, true);
			}
		};

		controller.nextOrFinish();
		new FullVerifications() {
			{
				editController2.save();
				editController3.load();
				view.setEditView(editController3.getView(), true, false);
			}
		};

		controller.nextOrFinish();
		new FullVerifications() {
			{
				editController3.save();
				view.close();
				finishCallback.run();
			}
		};
	}

	@Test
	public void testCancel() {
		WizardController controller = new WizardController(finishCallback,
				cancelCallback, view, editController1, editController2,
				editController3);
		new FullVerifications() {
			{
				view.setController(controller);
				editController1.load();
				view.setEditView(editController1.getView(), false, true);
			}
		};

		controller.cancel();
		new FullVerifications() {
			{
				cancelCallback.run();
			}
		};
	}
}
