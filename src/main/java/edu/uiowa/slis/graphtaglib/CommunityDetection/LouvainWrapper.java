package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class LouvainWrapper extends DetectorWrapper {
	public LouvainWrapper() {
		super(new LouvainDetector());
	}
}
