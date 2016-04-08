package edu.uiowa.slis.graphtaglib.CommunityDetection;

import java.util.Random;

public class SmartLocalMovingDetector extends Detector {
	
	public SmartLocalMovingDetector() {
	super();
	}
	
	public SmartLocalMovingDetector(double resolution) {
	super(resolution);
	}

    boolean detectCommunity(Network network, double resolution, Random random) {
	int i, j, k;
	int[] reducedNetworkCluster, subnetworkCluster;
	Network reducedNetwork;
	Network[] subnetwork;

	if ((network.getClusters() == null) || (network.getNNodes() == 1))
	    return false;

	network.runLocalMovingAlgorithm(resolution, random);

	if (network.getNClusters() < network.getNNodes()) {
	    if (!network.clusteringStatsAvailable)
		network.calcClusteringStats();

	    subnetwork = network.getSubnetworks();

	    network.nClusters = 0;
	    for (i = 0; i < subnetwork.length; i++) {
		subnetwork[i].initSingletonClusters();
		subnetwork[i].runLocalMovingAlgorithm(resolution, random);

		subnetworkCluster = subnetwork[i].getClusters();
		for (j = 0; j < subnetworkCluster.length; j++)
		    network.cluster[network.nodePerCluster[i][j]] = network.nClusters + subnetworkCluster[j];
		network.nClusters += subnetwork[i].getNClusters();
	    }
	    network.calcClusteringStats();

	    reducedNetwork = network.getReducedNetwork();

	    reducedNetworkCluster = new int[network.nClusters];
	    i = 0;
	    for (j = 0; j < subnetwork.length; j++)
		for (k = 0; k < subnetwork[j].getNClusters(); k++) {
		    reducedNetworkCluster[i] = j;
		    i++;
		}
	    reducedNetwork.setClusters(reducedNetworkCluster);

	    detectCommunity(reducedNetwork, resolution, random);

	    network.mergeClusters(reducedNetwork.getClusters());
	}

	network.deleteClusteringStats();

	return true;
    }

}
