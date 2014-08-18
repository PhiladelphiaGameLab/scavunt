package org.philadelphiagamelab.scavunt;

import java.util.ArrayList;

/**
 * Created by aaronmsegal on 8/13/14.
 */
public class GameHolder {

    private static ArrayList<EventCluster> clusters;

    public static void setClusters(ArrayList<EventCluster> clustersIn) {
        clusters = clustersIn;
    }

    public static ArrayList<EventCluster> getClusters() {
        return clusters;
    }

}