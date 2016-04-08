package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class SmartLocalMovingWrapper extends DetectorWrapper {

    public SmartLocalMovingWrapper() {
	this.theDetector = new SmartLocalMovingDetector();
    }
    
    public SmartLocalMovingWrapper(double resolution) {
    this.theDetector = new SmartLocalMovingDetector(resolution);
    }
}
