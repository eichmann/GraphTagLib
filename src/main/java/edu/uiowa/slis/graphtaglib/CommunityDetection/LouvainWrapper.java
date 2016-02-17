package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class LouvainWrapper extends DetectorWrapper {
    public LouvainWrapper() {
	this.theDetector = new LouvainDetector();
    }
}
