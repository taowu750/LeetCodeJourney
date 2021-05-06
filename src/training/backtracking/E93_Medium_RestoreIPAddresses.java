package training.backtracking;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 93. 复原 IP 地址: https://leetcode-cn.com/problems/restore-ip-addresses/
 *
 * 给定一个只包含数字的字符串，用以表示一个 IP 地址，返回所有可能从 s 获得的「有效 IP 地址」。
 * s 中的字符必须都用上。你可以按「任何顺序」返回答案。
 *
 * 有效 IP 地址正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
 *
 * 例如："0.1.2.201" 和 "192.168.1.1" 是有效 IP 地址，但是 "0.011.255.245"、"192.168.1.312"
 * 和 "192.168@1.1" 是无效 IP 地址。
 *
 * 例 1：
 * 输入：s = "25525511135"
 * 输出：["255.255.11.135","255.255.111.35"]
 *
 * 例 2：
 * 输入：s = "0000"
 * 输出：["0.0.0.0"]
 *
 * 例 3：
 * 输入：s = "1111"
 * 输出：["1.1.1.1"]
 *
 * 例 4：
 * 输入：s = "010010"
 * 输出：["0.10.0.10","0.100.1.0"]
 *
 * 例 5：
 * 输入：s = "101023"
 * 输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 *
 * 约束：
 * - 0 <= s.length <= 3000
 * - s 仅由数字组成
 */
public class E93_Medium_RestoreIPAddresses {

    static void test(Function<String, List<String>> method) {
        CollectionUtil.equalsIgnoreOrder(asList("255.255.11.135","255.255.111.35"),
                method.apply("25525511135"));

        CollectionUtil.equalsIgnoreOrder(singletonList("0.0.0.0"), method.apply("0000"));
        CollectionUtil.equalsIgnoreOrder(singletonList("1.1.1.1"), method.apply("1111"));

        CollectionUtil.equalsIgnoreOrder(asList("0.10.0.10","0.100.1.0"),
                method.apply("010010"));

        CollectionUtil.equalsIgnoreOrder(asList("1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"),
                method.apply("101023"));

        assertTrue(method.apply("123").isEmpty());
        assertTrue(method.apply("").isEmpty());
        assertTrue(method.apply("128474628394058").isEmpty());
    }

    /**
     * LeetCode 耗时：1 ms - 99.93%
     *          内存消耗：37.2 MB - 83.11%
     */
    public List<String> restoreIpAddresses(String s) {
        int n = s.length();
        if (n < 4 || n > 12)
            return Collections.emptyList();

        StringBuilder sb = new StringBuilder(15);
        List<Integer> path = new ArrayList<>(3);
        List<String> result = new ArrayList<>();
        dfs(s, 1, sb, path, result);

        return result;
    }

    private void dfs(String s, int part,
                     StringBuilder sb, List<Integer> path, List<String> result) {
        int n = s.length(), startIdx = part == 1 ? 0 : path.get(path.size() - 1) + 1;
        // 开始下标大于字符串长度，不是有效的 ip
        if (startIdx >= s.length())
            return;
        // 如果已经到了最后一部分，则尝试解析 ip
        if (part == 4) {
            // 最后一部分之后还有剩余字符，不是有效的 ip
            if (n - startIdx > 3)
                return;
            // 有前导 0，不是有效的 ip
            if (s.charAt(startIdx) == '0' && startIdx < n - 1)
                return;

            int part4 = parseInt(s, startIdx, n - 1);
            // 数字大于 255，不是有效的 ip
            if (part4 > 255)
                return;

            // 计算 ip 的四个部分
            int last = 0;
            for (int i = 0; i < path.size(); i++) {
                int partEnd = path.get(i), num = parseInt(s, last, partEnd);
                sb.append(num).append('.');
                last = partEnd + 1;
            }
            sb.append(part4);
            result.add(sb.toString());
            // 清空 sb
            sb.setLength(0);

            return;
        }

        // 开头是 0，则只能用这个 0
        if (s.charAt(startIdx) == '0') {
            path.add(startIdx);
            dfs(s, part + 1, sb, path, result);
            path.remove(path.size() - 1);
        } else {
            // 尝试长度 1、2 的 ip 部分
            for (int len = 1; len <= 2; len++) {
                path.add(startIdx + len - 1);
                dfs(s, part + 1, sb, path, result);
                path.remove(path.size() - 1);
            }
            // 如果长度 3 的 ip 部分没有超过字符串长度，并且大小小于 255，则也可以尝试
            if (startIdx + 2 < s.length() && parseInt(s, startIdx, startIdx + 2) <= 255) {
                path.add(startIdx + 2);
                dfs(s, part + 1, sb, path, result);
                path.remove(path.size() - 1);
            }
        }
    }

    private int parseInt(String s, int lo, int hi) {
        int num = 0;
        for (int i = lo; i <= hi; i++) {
            char c = s.charAt(i);
            num = num * 10 + c - '0';
        }
        return num;
    }

    @Test
    public void testRestoreIpAddresses() {
        test(this::restoreIpAddresses);
    }
}
