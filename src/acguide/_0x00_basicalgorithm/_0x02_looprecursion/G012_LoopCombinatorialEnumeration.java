package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 非递归实现组合型枚举：https://ac.nowcoder.com/acm/contest/998/H
 *
 * 题目要求和 {@link G006_CombinatorialEnumeration} 一样。
 */
public class G012_LoopCombinatorialEnumeration {

    public List<List<Integer>> enumeration(int n, int m) {
        List<List<Integer>> ans = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        int address = 0;
        // 开始首次调用
        call(1, 0);
        while (!stack.isEmpty()) {
            // 获得参数
            int i = stack.get(1);
            switch (address) {
                case 0:
                    if (path.size() == m) {
                        ans.add((List<Integer>) path.clone());
                        address = ret();
                        break;
                    }
                    // 先添加
                    path.add(i);
                    // 相当于 dfs(i+1)，返回后会从 case 1 继续
                    call(i + 1, 1);
                    // 回到 while 循环的开头，相当于开始新的递归
                    address = 0;
                    break;

                case 1:
                    path.remove(path.size() - 1);
                    // 后面数字足够的情况下不添加
                    if (n - i >= m - path.size()) {
                        // 相当于 dfs(i+1)，返回后会从 case 2 继续
                        call(i + 1, 2);
                        address = 0;
                    } else {
                        address = 2;
                    }
                    break;

                case 2:
                    address = ret();
                    break;
            }
        }

        return ans;
    }

    private LinkedList<Integer> stack = new LinkedList<>();

    /**
     * 模拟汇编指令 call
     */
    private void call(int i, int retAddr) {
        // 将参数 i 压入栈中
        stack.push(i);
        // 压入返回地址
        stack.push(retAddr);
    }

    /**
     * 模拟汇编指令 ret
     */
    private int ret() {
        // 弹出返回地址
        int retAddr = stack.pop();
        // 弹出参数，恢复到上一次调用之前的状态
        stack.pop();
        return retAddr;
    }

    @Test
    public void testEnumeration() {
        G006_CombinatorialEnumeration.test(this::enumeration);
    }
}
