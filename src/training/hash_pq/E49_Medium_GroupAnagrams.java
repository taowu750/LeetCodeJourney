package training.hash_pq;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.deepEqualsIgnoreOrder;

/**
 * 49. 字母异位词分组: https://leetcode-cn.com/problems/group-anagrams/
 *
 * 给定一个字符串数组，将字母异位词组合在一起。可以按任意顺序返回结果列表。
 * 字母异位词指字母相同，但排列不同的字符串。
 *
 * 例 1：
 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * 例 2：
 * 输入: strs = [""]
 * 输出: [[""]]
 *
 * 例 3：
 * 输入: strs = ["a"]
 * 输出: [["a"]]
 *
 * 约束：
 * - 1 <= strs.length <= 10^4
 * - 0 <= strs[i].length <= 100
 * - strs[i] 仅包含小写字母
 */
public class E49_Medium_GroupAnagrams {

    static void test(Function<String[], List<List<String>>> method) {
        deepEqualsIgnoreOrder(asList(singletonList("bat"), asList("nat","tan"), asList("ate","eat","tea")),
                method.apply(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        deepEqualsIgnoreOrder(singletonList(singletonList("")), method.apply(new String[]{""}));
        deepEqualsIgnoreOrder(singletonList(singletonList("a")), method.apply(new String[]{"a"}));
    }

    /**
     * LeetCode 耗时：6 ms - 98.97%
     *          内存消耗：40.8 MB - 97.89%
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sortedStr = String.valueOf(chars);
            map.computeIfAbsent(sortedStr, k -> new ArrayList<>()).add(str);
        }

        List<List<String>> result = new ArrayList<>(map.size());
        result.addAll(map.values());

        return result;
    }

    @Test
    public void testGroupAnagrams() {
        test(this::groupAnagrams);
    }
}
