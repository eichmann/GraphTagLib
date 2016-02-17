package edu.uiowa.slis.graphtaglib.CommunityDetection;

import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public abstract class Detector {
    static Logger logger = Logger.getLogger(Detector.class);

    enum ModularityType {
	standard, alternative
    };

    static ModularityType modularityMode = ModularityType.standard;
    double resolution = 1.0;
    double resolution2 = 1.0;
    int randomStarts = 10;
    int iterations = 10;
    long randomSeed = 0;
    Random random = new Random();

    long beginTime;
    long endTime;

    double maxModularity = Double.NEGATIVE_INFINITY;
    double modularity = 0.0;
    Network maxNetwork;

    public static void main(String args[]) throws IOException {
	PropertyConfigurator.configure(args[0]);
	// Detector theDetector = new LouvainDetector();
	// Detector theDetector = new LouvainMultilevelRefinementDetector();
	Detector theDetector = new SmartLocalMovingDetector();
	Network network = ModularityOptimizer.readInputFile(args[1], (modularityMode == ModularityType.standard ? 1 : 2));
	theDetector.detect(network);
	theDetector.dumpAssignments(network);
    }

    public Detector() {

    }

    public Detector(int resolution, int randomStarts, int iterations) {
	this.resolution = resolution;
	this.randomStarts = randomStarts;
	this.iterations = iterations;
    }

    public void detect(Network network) {
	logger.debug("Number of nodes: " + network.getNNodes());
	logger.debug("Number of edges: " + (network.getNEdges() / 2));

	resolution2 = ((modularityMode == ModularityType.standard) ? (resolution / network.getTotalEdgeWeight()) : resolution);

	beginTime = System.currentTimeMillis();
	maxModularity = Double.NEGATIVE_INFINITY;
	random = (randomSeed == 0 ? new Random() : new Random(randomSeed));
	for (int i = 0; i < randomStarts; i++) {
	    if (randomStarts > 1)
		logger.debug("Random start: " + (i + 1));

	    network.initSingletonClusters();

	    boolean update = true;
	    for (int j = 0; j < iterations && update; j++) {
		if (iterations > 1)
		    logger.debug("Iteration: " + j);

		update = detectCommunity(network, resolution2, random);

		modularity = network.calcQualityFunction(resolution2);

		if (iterations > 1) {
		    logger.debug("Modularity: " + modularity);
		    logger.debug("Number of communities: " + network.getNClusters());
		}
	    }

	    if (modularity > maxModularity) {
		network.orderClustersByNNodes();
		maxModularity = modularity;
		maxNetwork = (Network) network.clone();
	    }

	    if (randomStarts > 1) {
		if (iterations == 1)
		    logger.debug("Modularity: " + modularity);
	    }
	}
	endTime = System.currentTimeMillis();

	if (randomStarts == 1) {
	    logger.debug("Modularity: " + maxModularity);
	} else
	    logger.debug("Maximum modularity in " + randomStarts + " random starts: " + maxModularity);
	logger.debug("Number of communities: " + maxNetwork.getNClusters());
	logger.debug("Elapsed time: " + Math.round((endTime - beginTime) / 1000.0) + " seconds.");
    }

    abstract boolean detectCommunity(Network network, double resolution, Random random);

    public void dumpAssignments(Network network) {
	int[] assignments = getClusterAssignments(network);
	for (int i = 0; i < assignments.length; i++) {
	    logger.info("node " + (i + 1) + ": " + assignments[i]);
	}
    }

    public int[] getClusterAssignments(Network network) {
	return network.getClusters();
    }

    public double getResolution() {
	return resolution;
    }

    public void setResolution(double resolution) {
	this.resolution = resolution;
    }

    public int getRandomStarts() {
	return randomStarts;
    }

    public void setRandomStarts(int randomStarts) {
	this.randomStarts = randomStarts;
    }

    public int getIterations() {
	return iterations;
    }

    public void setIterations(int iterations) {
	this.iterations = iterations;
    }

    public long getRandomSeed() {
	return randomSeed;
    }

    public void setRandomSeed(long randomSeed) {
	this.randomSeed = randomSeed;
    }

    public Random getRandom() {
	return random;
    }

    public void setRandom(Random random) {
	this.random = random;
    }

    public double getModularity() {
	return modularity;
    }

    public void setModularity(double modularity) {
	this.modularity = modularity;
    }

    public static ModularityType getModularityMode() {
	return modularityMode;
    }

    public static void setModularityMode(ModularityType modularityMode) {
	Detector.modularityMode = modularityMode;
    }

    public double getMaxModularity() {
	return maxModularity;
    }

    public Network getMaxNetwork() {
	return maxNetwork;
    }

    public int getNumberCommunities() {
	return maxNetwork.getNClusters();
    }

}
