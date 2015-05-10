package swp15.link_discovery.controller;

import mockit.FullVerifications;
import mockit.Mocked;

import org.junit.Test;

import swp15.link_discovery.view.IEditView;
import swp15.link_discovery.view.WizardView;

public class TestWizardController {
	@Mocked
	private Runnable finishCallback;
	@Mocked
	private Runnable cancelCallback;
	@Mocked
	private WizardView view;
	@Mocked
	private IEditView editView1;
	@Mocked
	private IEditView editView2;
	@Mocked
	private IEditView editView3;

	@Test
	public void testLinear() {
		WizardController controller = new WizardController(finishCallback,
				cancelCallback, view, editView1, editView2, editView3);
		new FullVerifications() {
			{
				view.setController(controller);
				view.setEditView(editView1, false, true);
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
				editView1.save();
				view.setEditView(editView2, true, true);
			}
		};

		controller.nextOrFinish();
		new FullVerifications() {
			{
				editView2.save();
				view.setEditView(editView3, true, false);
			}
		};

		controller.nextOrFinish();
		new FullVerifications() {
			{
				editView3.save();
				view.close();
				finishCallback.run();
			}
		};
	}

	@Test
	public void testCancel() {
		WizardController controller = new WizardController(finishCallback,
				cancelCallback, view, editView1, editView2, editView3);
		new FullVerifications() {
			{
				view.setController(controller);
				view.setEditView(editView1, false, true);
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
