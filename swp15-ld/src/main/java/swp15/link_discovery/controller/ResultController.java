package swp15.link_discovery.controller;

import java.util.Iterator;

import swp15.link_discovery.model.Config;
import swp15.link_discovery.model.Result;
import swp15.link_discovery.view.ResultView;
import de.uni_leipzig.simba.io.KBInfo;

public class ResultController {
	private ResultView view;
	private KBInfo sourceInfo;
	private KBInfo targetInfo;
	private Config currentConfig;
	
	public ResultController(ResultView view){
		this.view = view;
	}
	public void setCurrentConfig(Config c){
		
		this.currentConfig = c;
		this.sourceInfo = c.getSourceInfo();
		this.targetInfo = c.getTargetInfo();
		
	}
	
	public void setSourceAndTargetInfo( KBInfo sourceInfo, KBInfo targetInfo){
		this.sourceInfo = sourceInfo;
		this.targetInfo = targetInfo;
	}
	public void showProperties(Result item){
		String sourceURI = item.getSourceURI();
		String targetURI = item.getTargetURI();
		Iterator<String> s = sourceInfo.properties.iterator();
		while(s.hasNext()){
			System.out.println(s.next());
			
		}
		//System.out.println(sourceInfo.properties);
	}
}
