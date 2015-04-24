package swp15.link_discovery.controller;

import de.uni_leipzig.simba.io.KBInfo;
import swp15.link_discovery.view.ResultView;

public class ResultController {
	private ResultView view;
	private KBInfo sourceInfo;
	private KBInfo targetInfo;
	
	public ResultController(ResultView view){
		this.view = view;
	}
	
	public void setSourceAndTargetInfo( KBInfo sourceInfo, KBInfo targetInfo){
		this.sourceInfo = sourceInfo;
		this.targetInfo = targetInfo;
	}
	public void showProperties(){
		
	}
}
