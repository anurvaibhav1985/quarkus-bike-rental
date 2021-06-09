package org.acme.realworld;

import java.util.ArrayList;
import java.util.Arrays;

public class GoogleCalendar {

    public static void main(String[] args) {

        // 1. No fo meeting roorms required to schedule given array
        // SImilar to no of stattions required ,
        // Sor the arrivals and departunres, at any poitn in time , how manhy trains we
        // have at sttation determines the platform needed.
        // https://www.geeksforgeeks.org/sorting-2d-array-according-values-given-column-java/

        int[][] meetingTimes = { { 2, 8 }, { 3, 4 }, { 3, 9 }, { 5, 11 }, { 8, 20 }, { 11, 15 } };

        // Get the arrivals and departures and sort them
        int[] arrivals = new int[meetingTimes.length];
        int[] departures = new int[meetingTimes.length];

        for (int i = 0; i < meetingTimes.length; i++) {
            arrivals[i] = meetingTimes[i][0];
            departures[i] = meetingTimes[i][1];
        }

        System.out.println(findMinPlatforms(arrivals, departures));


        //2. Merge intervals .
        // sort the intervals by the start time and then check if the next meeting has start time less than or equal to the end time of previous meeting
        // Performe merge and move on 
        int[][] meetingTimes1 = {{1, 4}, {2, 5}, {6, 8}, {7, 9}, {10, 13}};
        System.out.println(Arrays.deepToString(mergeMeetings(meetingTimes1)));


        // 3. CHeck if we can insert some inverval . is Is a meeting possible ? 
        // Insret the meetings as Node(start,end,leftNode,rightNode) into a BST 
        // See if it can be inserted ie O(log N) time , If meeting start > node end ,go as right child
        // or meeting end < node start , this will go as left child
        // return false if cannot be insrted



        // 4. Given two users's intervals , check when both are not avilable
        // ie As you travese both , check if max(start of both meetings of user 1 and 2)
        // and min(end times of both user 1 and 2 meeting ) , this is the intersection of
        // those meetings and btoh users are not aviabel during that time
        // if we cant find interction with one , move to next meeting og either user 1 or user 2
        // depending on whether 


        // 5. LOgest consecutive sequence , after sorting this becomes a 
        // longest subarray with consective nos
        // {9,1,4,7,3,-1,0,5,8,-1,6}
        int a[] = {9,1,4,7,3,-1,0,5,8,-1,6};
        Arrays.sort(a);
        // Keep 2 pointers , as soon as you find two elemsnts differing by 1 keep i stable and see if you can move j as much as poissble
        // Once the seqeunec breaks , move i to j+1 and search continues , print the sequence found when it breaks
        // Arrays.copyOfRange(original, from, to) - subarray of original array
     




    }

    public static int[][] mergeMeetings(int[][] meetingTimes){
        Arrays.sort(meetingTimes, (a, b) -> Integer.compare(a[0], b[0])); 
        
        ArrayList<int[]> merged = new ArrayList<>();
        for (int[] meeting: meetingTimes){
            int size = merged.size();
            // End time of meeting less than start time
            if(size == 0 || merged.get(size - 1)[1] < meeting[0]){
                merged.add(meeting);
            } // Merge max(end times)
            else{
                merged.get(size - 1)[1] = Math.max(merged.get(size - 1)[1], meeting[1]);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }

    public static int findMinPlatforms(int[] arrival, int[] departure) {
        // sort arrival time of trains
        Arrays.sort(arrival);

        // sort departure time of trains
        Arrays.sort(departure);

        // maintains the count of trains
        int count = 0;

        // stores minimum platforms needed
        int platforms = 0;

        // take two indices for arrival and departure time
        int i = 0, j = 0;

        // run till all trains have arrived
        while (i < arrival.length) {
            // if a train is scheduled to arrive next
            if (arrival[i] < departure[j]) {
                // increase the count of trains and update minimum
                // platforms if required
                platforms = Integer.max(platforms, ++count);

                // move the pointer to the next arrival
                i++;
            }

            // if the train is scheduled to depart next i.e.
            // `departure[j] < arrival[i]`, decrease trains' count
            // and move pointer `j` to the next departure.

            // If two trains are arriving and departing simultaneously,
            // i.e., `arrival[i] == departure[j]`, depart the train first
            else {
                count--;
                j++;
            }
        }

        return platforms;
    }

}
