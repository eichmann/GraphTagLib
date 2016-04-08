package edu.uiowa.slis.graphtaglib.CommunityDetection;

public class LouvainMultilevelRefinementWrapper extends DetectorWrapper {

    public LouvainMultilevelRefinementWrapper() {
	this.theDetector = new LouvainMultilevelRefinementDetector();
    }
    
    public LouvainMultilevelRefinementWrapper(double resolution) {
    this.theDetector = new LouvainMultilevelRefinementDetector(resolution);
    }

}