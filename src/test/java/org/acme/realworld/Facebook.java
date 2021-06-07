package org.acme.realworld;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Facebook {

    public static void main(String[] args) {

        // 1. Friends circle
        int n = 4;
        boolean[][] friends = { { true, true, false, false }, { true, true, true, false }, { false, true, true, false },
                { false, false, false, true } };
        // Create a graph from this and find stringly connected compoenets
        Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (int i = 0; i < friends.length; i++) {
            g.addVertex(String.valueOf(i + 1));

        }

        for (int i = 0; i < friends.length; i++) {
            for (int j = 0; j < friends.length; j++) {
                if (friends[i][j] == true) {
                    g.addEdge(String.valueOf(i + 1), String.valueOf(j + 1));
                }

            }
        }

        StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new KosarajuStrongConnectivityInspector<>(g);
        List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs = scAlg.getStronglyConnectedComponents();

        for (Graph<String, DefaultEdge> eachGraph : stronglyConnectedSubgraphs) {
            System.out.println(eachGraph.vertexSet());
        }

        // 2. For cloning a graph , try ajcency list or matrix vertex set or edge set

        // 4. Rate limiter
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        RedissonClient redisson = Redisson.create(config);
        RRateLimiter r = redisson.getRateLimiter("someKey");
        r.setRate(RateType.OVERALL, 1, 5, RateIntervalUnit.SECONDS);

        System.out.println(r.tryAcquire());
        System.out.println(r.tryAcquire());
        System.out.println(r.tryAcquire());
        System.out.println(r.tryAcquire());

        try {
            Thread.sleep(6000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Permit should be availabe now
        System.out.println(r.tryAcquire());

        // 5. Verify user session , there are sequences of push and pops
        // [1,2,3,4,5] -- pushes
        // [4,5,3,2,1] -- pops , user session is valid or not ? by looking at these 
        // AFter 4 was pushed , it waa ccesse dimmediately so pop 4 , then 5 was accessed etc, using back button
        // Some strange logic 

 
    }

}
