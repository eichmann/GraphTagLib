package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class SmartLocalMovingWrapper extends DetectorWrapper{
	
	public SmartLocalMovingWrapper() {
		this.theDetector = new SmartLocalMovingDetector();
	}
}
