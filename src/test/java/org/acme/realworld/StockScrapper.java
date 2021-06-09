package org.acme.realworld;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StockScrapper {

    public static void main(String[] args) {

        // 1. Subarray sum with maximum profit , top highs , largest in a stream
        // Also see subaray of sum k , for which use sliding window and check if sum > K
        // , shrink window
        // Also see maxium sum subarray to be printed -
        int[] A = { 1, 2, 4, 2, 6, 3, 7, 2, 6 };

        System.out.println("The sum of contiguous subarray with the " + "largest sum is " + kadaneNeg(A));

        List<Integer> Aa = Arrays.asList(7, 4, 6, 3, 9, 1);
        int k = 3;

        System.out.println("k'th smallest array element is " + findKthSmallest(Aa, k));

        // Kth largest(min heap) number in number stream - use min heap since it will
        // have kth largets number(min heap will have larger numberd donwstraits)
        // kth smallest number(max heap) in a number stream - use max heap (max heap
        // will have smaller numbers donwstraits)

        // K largest numbers or kth largest number
        TreeSet<Integer> sortedSet = new TreeSet<>(Comparator.reverseOrder());
        sortedSet.add(1);
        sortedSet.add(2);
        sortedSet.add(4);
        sortedSet.add(5);
        sortedSet.add(6);
        sortedSet.add(18);
        System.out.println(sortedSet.stream().limit(5).collect(Collectors.toList()));
        System.out.println(sortedSet.toArray()[4]);

        // 2. Most frequent nos , you a tree map with reverse comparator , add the nos
        // and use map.merge()
        // to keep the counter for each key , to retrive use stream , limit to top K and
        // collect
        // ALternative is the to use a redissosn scored sorted set , keep adding to
        // elemnt the score
        // addScoreAndGetRevRank(V object, Number value) ,
        // valueRangeReversed(int startIndex, int endIndex) - top k most frequnet /
        // highest scores

        // 3. Tree DFS , BFS
        // https://github.com/eugenp/tutorials/blob/master/algorithms-searching/src/main/java/com/baeldung/algorithms/dfs/BinaryTree.java
        // https://github.com/eugenp/tutorials/blob/master/algorithms-searching/src/main/java/com/baeldung/algorithms/dfs/Graph.java

        // 4. Topological order of vertices , task scheuling exmaple, kahn's alogithm,
        // modiefied dfs etc
        // https://www.youtube.com/watch?v=gDNm1m3G4wo

    }

    class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            right = null;
            left = null;
        }
    }

    public static int kadaneNeg(int[] A) {
        // stores the maximum sum subarray found so far
        int maxSoFar = Integer.MIN_VALUE;

        // stores the maximum sum of subarray ending at the current position
        int maxEndingHere = 0;

        // traverse the given array
        for (int i : A) {
            // update the maximum sum of subarray "ending" at index `i` (by adding the
            // current element to maximum sum ending at previous index)
            maxEndingHere = maxEndingHere + i;

            // maximum sum should be more than the current element
            maxEndingHere = Integer.max(maxEndingHere, i);

            // update the result if the current subarray sum is found to be greater
            maxSoFar = Integer.max(maxSoFar, maxEndingHere);
        }

        return maxSoFar;
    }

    public static int findKthSmallest(List<Integer> A, int k) {
        // create a max-heap using the `PriorityQueue` class and
        // insert the first `k` array elements into the heap
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        pq.addAll(A.subList(0, k));

        // do for remaining array elements
        for (int i = k; i < A.size(); i++) {
            // if the current element is less than the root of the heap
            if (A.get(i) < pq.peek()) {
                // replace root with the current element
                pq.poll();
                pq.add(A.get(i));
            }
        }

        // return the root of max-heap
        return pq.peek();
    }

    static int binarySearchRec(int[] a, int key, int low, int high) {
        if (low > high) {
            return -1;
        }

        int mid = low + ((high - low) / 2);
        if (a[mid] == key) {
            return mid;
        } else if (key < a[mid]) {
            return binarySearchRec(a, key, low, mid - 1);
        } else {
            return binarySearchRec(a, key, mid + 1, high);
        }
    }

}
