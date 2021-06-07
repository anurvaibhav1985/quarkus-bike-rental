package org.acme.realworld;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.redisson.Redisson;
import org.redisson.api.EvictionMode;
import org.redisson.api.RMapCache;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Netflix {

    private static Queue<Integer> minHeap, maxHeap;

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        RedissonClient redisson = Redisson.create(config);

        // Netflix
        // 1. Group smiliar tites
        String[] titles = { "speed", "duel", "cars" };
        // For each word , sort chars and put into map the original word and the anagram

        Multimap<String, String> map = ArrayListMultimap.create();

        for (String title : titles) {
            // String originalTtle = title;
            char[] tempArray = title.toCharArray();
            Arrays.sort(tempArray);
            map.put(String.valueOf(tempArray), title);
        }

        System.out.println(map);

        // 2. Merge k sorted l
        int[] a = { 1, 2, 4, 6, 8, 9, 14, 156 };
        int[] b = { 12, 32, 54, 56, 18, 29 };
        int[] c = { 11, 23, 44, 46, 58, 39, 14, 16 };

        IntStream test = IntStream.concat(IntStream.of(a), IntStream.concat(IntStream.of(b), IntStream.of(c)));
        test.boxed().sorted(Comparator.naturalOrder()).mapToInt(i -> i).forEach(k -> System.out.print(k));

        // 3. Median of stream of integr
        minHeap = new PriorityQueue<>(); // Min heap is towards right smallest to greatest
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder()); // max heap on left greatets to smallet
        // If next num is smalller than min heap top , ie this should go to left, now if
        // max heap is 2 more than min heap , move top p fmax heap to min heap
        // vice cersa

        // 4 .Popularity anlysis: Easy check if array is increasing or decreaing
        // 5. Most recently watched titles , use Redisson or google gauva has key
        // expiration , LRU
        // LocalCachedMapOptions<String, String> options =
        // LocalCachedMapOptions.defaults();
        // options.cacheSize(2);
        // options.evictionPolicy(EvictionPolicy.LRU);
        RMapCache<String, String> u = redisson.getMapCache("uTest");
        u.clear();
        // u.setMaxSize(5);
        //u.setMaxSize(3, EvictionMode.LRU);
        u.setMaxSize(3, EvictionMode.LFU);
        u.put("Up", "Details of UP here");
        u.put("Cars", "Details of Cars here");
        u.put("Home", "Details of Home ALone here");

        u.get("Home");
        u.get("Up");
        u.get("Up");

        // LRU/LFU eviction
        u.put("YMCA", "Details of YMCA here");
        u.put("Bulls", "Details of Bulls here");

        System.out.println(u.keySet().stream().filter(k -> u.get(k) != null).collect(Collectors.toList()));

        // 6 . Most frequently watched titles: See above

        // 7 . USe two stacks one to keep history of user titles browsed , another max stack , the top 
        // of which has the maximum elemenst or use a redisson scored sorted set to store scores of movie ratings
        // Get the hihest rated elemsnts from top x.
        LinkedList<String> moviesBrowsed = new LinkedList<String>();
        RScoredSortedSet<Object>  stackHistoryHiestRating = redisson.getScoredSortedSet("moviesRating");
        moviesBrowsed.push("ABC");
        moviesBrowsed.push("DEF");
        moviesBrowsed.push("HHH");
        moviesBrowsed.push("JHU");

        stackHistoryHiestRating.add(10, "ABC");
        stackHistoryHiestRating.add(10, "DEF");
        stackHistoryHiestRating.add(7, "HHH");
        stackHistoryHiestRating.add(3, "JHU");

        // Back button pressed m peekLast is also smilar but doesnt remove from lst
        String lastBrowsed = moviesBrowsed.pop();
        System.out.println(lastBrowsed);
        // Get top 3 rated maovies from the user history only
        System.out.println(stackHistoryHiestRating.valueRangeReversed(0,1));

        

    }

    void add(int num) {
        if (!minHeap.isEmpty() && num < minHeap.peek()) {
            maxHeap.offer(num);
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            }
        } else {
            minHeap.offer(num);
            if (minHeap.size() > maxHeap.size() + 1) {
                maxHeap.offer(minHeap.poll());
            }
        }
    }

    double getMedian() {
        int median;
        if (minHeap.size() < maxHeap.size()) {
            median = maxHeap.peek();
        } else if (minHeap.size() > maxHeap.size()) {
            median = minHeap.peek();
        } else {
            median = (minHeap.peek() + maxHeap.peek()) / 2;
        }
        return median;
    }

}
