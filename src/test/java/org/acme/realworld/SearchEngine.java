package org.acme.realworld;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.gradle.internal.impldep.com.google.common.collect.Sets;

public class SearchEngine {

    public static void main(String[] args) {

        // 1. Store and fetch words , USe trie DS with prefix search available ,
        // O(n) time complexity
        Trie<String, Integer> t = new PatriciaTrie<Integer>();
        t.put("vicky", 1);
        t.put("vishzl", 1);
        t.put("vishnu", 5);
        t.put("vishwa", 2);
        SortedMap<String, Integer> searches = t.prefixMap("vis");
        System.out.println(searches);

        // 2. Design search autocmplete system ereocmmeding top most searches
        // Again usea a trie here stroing the most searched terms each time by inserting
        // into the trie using put(key,score) increemnt the score aftre orefix earch
        // Rank them by using a reverse comapartors working on map entries by value

        HashMap<String, Integer> temp = searches.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        // Collections.sort(searches,Comparators.compari);
        temp.entrySet().stream().forEach(k -> System.out.println(k.getKey() + "," + k.getValue()));

        // 3. Add whitespaces anf make corrections or from words , word break problem
        String query = "vegancookbook";
        String[] dict = { "i", "cream", "cook", "scream", "ice", "cat", "book", "icecream", "vegan" };
        String ans = "";
        wordBreak(query,ans, Sets.newHashSet(dict));
        System.out.println(ans);

        // 4. rreorganize a string so that no two letter are next to eahc other which
        // repeat
        // Use redisson scored sorted set , take top two and fill into another string alreatvvely
        // while(set.size > 1) -- fill string with first and second higest freq aletrnatvely , decremnt 
        // frequency after filling, if freq becomes 0 remove from set 


    }

    public static void wordBreak(String str, String ans, HashSet<String> dict) {
        if (str.length() == 0) {
            System.out.println(ans);
            return;
        }
        for (int i = 0; i < str.length(); i++) {
            String left = str.substring(0, i + 1);
            if (dict.contains(left)) {
                String right = str.substring(i + 1);
                wordBreak(right, ans + left + " ", dict);
            }
        }
    }

}
