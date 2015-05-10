package swp15.link_discovery.controller;

import swp15.link_discovery.view.IEditView;
import swp15.link_discovery.view.WizardView;

public class WizardController {
	private Runnable finishCallback;
	private Runnable cancelCallback;
	private WizardView view;
	private IEditView[] editViews;
	private int currentEditView = -1;

	public WizardController(Runnable finishCallback, Runnable cancelCallback,
			WizardView view, IEditView... editViews) {
		this.finishCallback = finishCallback;
		this.cancelCallback = cancelCallback;
		view.setController(this);
		this.view = view;
		assert editViews.length != 0;
		this.editViews = editViews;
		setCurrentEditView(0);
	}

	private void setCurrentEditView(int nextEditView) {
		if (nextEditView < 0 || nextEditView > editViews.length) {
			return;
		}
		if (this.currentEditView != -1) {
			editViews[this.currentEditView].save();
		}
		if (nextEditView == editViews.length) {
			this.currentEditView = -1;
			view.close();
			finish();
		} else {
			this.currentEditView = nextEditView;
			view.setEditView(editViews[nextEditView], nextEditView != 0,
					nextEditView < editViews.length - 1);
		}
	}

	public void back() {
		setCurrentEditView(currentEditView - 1);
	}

	public void nextOrFinish() {
		setCurrentEditView(currentEditView + 1);
	}

	private void finish() {
		finishCallback.run();
	}

	public void cancel() {
		cancelCallback.run();
	}
}
