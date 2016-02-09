package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class LouvainMultilevelRefinementWrapper extends DetectorWrapper {
	public LouvainMultilevelRefinementWrapper() {
		super(new LouvainMultilevelRefinementDetector());
	}
}