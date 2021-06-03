package org.acme;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SnippetsTest {

    // Rate limiter test
    @Test
    public void testDates() {
        Instant now = Instant.now();
        Instant oneMinBack = now.minusSeconds(60);
        System.out.println(now.getEpochSecond());
        System.out.println(oneMinBack.getEpochSecond());

        // Put them into sorted set in redis , This is time series , if we have more
        // than 3 requests , reject
    }

    // Tries
    @Test
    public void testLookUp() {
        Trie<String, Integer> t = new PatriciaTrie<Integer>();
        t.put("vicky", 1);
        t.put("vishzl", 1);
        t.put("vishnu", 5);
        t.put("vishwa", 2);

        // Comparator<Map.Entry> sort = Comparator.comparing(Map.Entry::getValue);

        SortedMap<String, Integer> searches = t.prefixMap("vis");

        // Sort by map value desc
        HashMap<String, Integer> temp = searches.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        // Collections.sort(searches,Comparators.compari);
        temp.entrySet().stream().forEach(k -> System.out.println(k.getKey() + "," + k.getValue()));

        // Put them into sorted set in redis , This is time series , if we have more
        // than 3 requests , reject

        // Whenevr someone searches a term put that to map increment the count of its
        // searhc
        // Now ranking can be based on search popularity
    }

    // Jgrap test , DFS , BFS , shortest path etc- try wikiracer
    @Test
    public void testGraphs() {

        Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addVertex("4");
        g.addVertex("5");

        g.addEdge("1", "3");
        g.addEdge("1", "2");
        g.addEdge("2", "4");
        g.addEdge("4", "5");
        g.addEdge("3", "5");

        DijkstraShortestPath<String, DefaultEdge> f = new DijkstraShortestPath<>(g);
        // SHortedt path from source to destination
        System.out.println(f.getPath("1", "5").getVertexList());


        // Put them into sorted set in redis , This is time series , if we have more
        // than 3 requests , reject
    }
}
