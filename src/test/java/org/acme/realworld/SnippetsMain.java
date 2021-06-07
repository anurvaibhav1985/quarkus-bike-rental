package org.acme.realworld;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;

import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SnippetsMain {

    public static void main(String[] args) {

        // USe set retainall for intersection of two sets

        // User StringJoiner or Arrays.ToString in Java as a fast way to join strings
        // https://redfin.engineering/java-string-concatenation-which-way-is-best-8f590a7d22a8

        // USe collections.rotate to rotae a collection by distince

        // Google Gauva Sets.combinations to geretae all subsets of a set 2 pow n. 

        // Sorting by mulple criteria
        // List<User> sortedList = users.stream()
        // .sorted(Comparator.comparingInt(User::getAge).reversed()).collect(Collectors.toList());

        // LCS using apache commons diff and # of inserta dn deletes bewteen two strings
        LongestCommonSubsequence n = new LongestCommonSubsequence();
        System.out.println(n.longestCommonSubsequence("helloj", "helloworldj"));
        System.out.println("No of chars  to be deleted in first string to tansform it to second is  "
                + ("helloj".length() - n.longestCommonSubsequence("helloj", "helloworldj").length()));
        System.out.println("No of chars  to be inserted in first string to tansform it to second is  "
                + ("helloworldj".length() - n.longestCommonSubsequence("helloj", "helloworldj").length()));
        // LevenshteinDetailedDistance.apply​("")
        

        // Phone no combinatins using JgraphT
        // https://www.techiedelight.com/find-possible-combinations-words-formed-from-mobile-keypad/
        // Appraoch starts from the last digit and goes up , 
        List<List<Character>> keypad = Arrays.asList(
                // 0 and 1 digit doesn't have any characters associated
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList( 'A', 'B', 'C' ),
                Arrays.asList( 'D', 'E', 'F' ),
                Arrays.asList( 'G', 'H', 'I' ),
                Arrays.asList( 'J', 'K', 'L' ),
                Arrays.asList( 'M', 'N', 'O' ),
                Arrays.asList( 'P', 'Q', 'R', 'S'),
                Arrays.asList( 'T', 'U', 'V' ),
                Arrays.asList( 'W', 'X', 'Y', 'Z')
        );
        // input number in the form of an array (number cannot start from 0 or 1)
        int[] input = { 2, 3, 4 };
        // find all combinations
        findCombinations(keypad, input, "", input.length - 1);

        

        // Redissosn leaderboard, get peopple between x and y , rank of a x , etc, get top
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RScoredSortedSet<String> t = redisson.getScoredSortedSet("test");
        t.add(1, "x");
        t.add(15, "i");
        t.add(10, "5");
        System.out.println(t.rank("x"));

        // Google gauva splitter
        System.out.println(Splitter.fixedLength(3).split("aksjhdkjadkjahd"));

        // Longest substring with K distnct characters: move window on either side
        // Fruits in basket and others
        System.out.println("Length of the longest substring: " + findLength("araaci", 2));
        System.out.println("Length of the longest substring: " + findLength("araaci", 1));
        System.out.println("Length of the longest substring: " + findLength("cbbebi", 3));

    }

    public static int findLength(String str, int k) {
        if (str == null || str.length() == 0 || str.length() < k)
            throw new IllegalArgumentException();

        int windowStart = 0, maxLength = 0;
        Map<Character, Integer> charFrequencyMap = new HashMap<>();
        // in the following loop we'll try to extend the range [windowStart, windowEnd]
        for (int windowEnd = 0; windowEnd < str.length(); windowEnd++) {
            char rightChar = str.charAt(windowEnd);
            charFrequencyMap.put(rightChar, charFrequencyMap.getOrDefault(rightChar, 0) + 1);
            // shrink the sliding window, until we are left with 'k' distinct characters in
            // the frequency map
            while (charFrequencyMap.size() > k) {
                char leftChar = str.charAt(windowStart);
                charFrequencyMap.put(leftChar, charFrequencyMap.get(leftChar) - 1);
                if (charFrequencyMap.get(leftChar) == 0) {
                    charFrequencyMap.remove(leftChar);
                }
                windowStart++; // shrink the window
            }
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1); // remember the maximum length so far
        }

        return maxLength;
    }

    public static void findCombinations(List<List<Character>> keypad,
                                        int[] input, String result, int index)
    {
        // if we have processed every digit of the key, print the result
        if (index == -1)
        {
            System.out.print(result + " ");
            return;
        }
 
        // stores the current digit
        int digit = input[index];
 
        // one by one, replace the digit with each character in the corresponding
        // list and recur for the next digit
        for (char c: keypad.get(digit)) {
            findCombinations(keypad, input, c + result, index - 1);
        }
    }
 

}
