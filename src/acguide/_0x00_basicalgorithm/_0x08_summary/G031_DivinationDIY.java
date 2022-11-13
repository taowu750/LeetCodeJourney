package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 占卜DIY: https://www.acwing.com/problem/content/119/
 *
 * 达达学会了使用扑克 DIY 占卜。方法如下：
 *
 * 一副去掉大小王的扑克共 52 张，打乱后均分为 13 堆，编号 1∼13，每堆 4 张，其中第 13 堆称作“生命牌”，也就是说你有 4 条命。
 * 这里边，4 张 K 被称作死神。初始状态下，所有的牌背面朝上扣下。
 *
 * 流程如下：
 * 1. 抽取生命牌中的最上面一张(第一张)。
 * 2. 把这张牌翻开，正面朝上，放到牌上的数字所对应编号的堆的最上边。(例如抽到 2，正面朝上放到第 2 堆牌最上面，又比如抽到 J，
 *    放到第 11 堆牌最上边，注意是正面朝上放)
 * 3. 从刚放了牌的那一堆最底下(最后一张)抽取一张牌，重复第 2 步。（例如你上次抽了 2，放到了第二堆顶部，
 *    现在抽第二堆最后一张发现是 8，又放到第 8 堆顶部.........）
 * 4. 在抽牌过程中如果抽到 K，则称死了一条命，就扔掉 K 再从第 1 步开始。
 * 5. 当发现四条命都死了以后，统计现在每堆牌上边正面朝上的牌的数目，只要同一数字的牌出现 4 张正面朝上的牌(比如 4 个 A)，
 *    则称“开了一对”，当然 4 个 K 是不算的。
 * 6. 统计一共开了多少对，开了 0 对称作”极凶”，1∼2 对为“大凶”，3 对为“凶”，4∼5 对为“小凶”，6 对为“中庸”，7∼8 对“小吉”，
 *    9 对为“吉”，10∼11 为“大吉”，12 为“满堂开花，极吉”。
 *
 * 例 1：
 * 输入：
 * 8 5 A A
 * K 5 3 2
 * 9 6 0 6
 * 3 4 3 4
 * 3 4 4 5
 * 5 6 7 6
 * 8 7 7 7
 * 9 9 8 8
 * 9 0 0 0
 * K J J J
 * Q A Q K
 * J Q 2 2
 * A K Q 2
 * 输出：
 * 9
 * 解释：
 * 为了便于读入，用 0 代表 10。
 * 输出一个整数，代表统计得到的开出的总对数。
 */
public class G031_DivinationDIY {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G031_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G031_output.txt");
    }

    public void divination() {
        Scanner in = new Scanner(System.in);
        List<Integer>[] bottoms = new List[13];
        List<Integer>[] tops = new List[13];
        for (int i = 0; i < 13; i++) {
            bottoms[i] = new ArrayList<>(4);
            tops[i] = new ArrayList<>(4);
        }
        for (int i = 0; i < 13; i++) {
            String[] piles = in.nextLine().split(" ");
            for (int j = 0; j < 4; j++) {
                if (piles[j].equals("A")) {
                    bottoms[i].add(0);
                } else if (piles[j].equals("0")) {
                    bottoms[i].add(9);
                } else if (Character.isDigit(piles[j].charAt(0))) {
                    bottoms[i].add(piles[j].charAt(0) - '1');
                } else if (piles[j].equals("J")) {
                    bottoms[i].add(10);
                } else if (piles[j].equals("Q")) {
                    bottoms[i].add(11);
                } else {
                    bottoms[i].add(12);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int life = bottoms[12].get(i);
            if (life == 12) {
                continue;
            }
            tops[life].add(life);
            if (bottoms[life].isEmpty()) {
                continue;
            }
            int pile = bottoms[life].remove(bottoms[life].size() - 1);
            while (pile != 12) {
                tops[pile].add(pile);
                if (bottoms[pile].isEmpty()) {
                    break;
                }
                pile = bottoms[pile].remove(bottoms[pile].size() - 1);
            }
        }

        int ans = 0;
        for (List<Integer> top : tops) {
            if (top.size() == 4) {
                ans++;
            }
        }
        System.out.println(ans);
    }

    @Test
    public void testDivination() {
        test(this::divination);
    }
}
