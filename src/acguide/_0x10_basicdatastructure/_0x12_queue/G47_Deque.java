package acguide._0x10_basicdatastructure._0x12_queue;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 双端队列：https://www.acwing.com/problem/content/136/
 *
 * 达达现在碰到了一个棘手的问题，有 N 个整数需要排序。达达手头能用的工具就是若干个双端队列。
 *
 * 她从 1 到 N 需要「依次处理」这 N 个数，对于每个数，达达能做以下两件事：
 * 1．新建一个双端队列，并将当前数作为这个队列中的唯一的数；
 * 2．将当前数放入已有的队列的头之前或者尾之后。
 *
 * 对所有的数处理完成之后，达达将这些队列按一定的顺序连接起来后就可以得到一个非降的序列。
 * 请你求出最少需要多少个双端序列。
 *
 * 输入格式:
 * - 第一行输入整数 N，代表整数的个数。
 * - 接下来 N 行，每行包括一个整数 Di，代表所需处理的整数。
 *
 * 输出格式:
 * - 输出一个整数，代表最少需要的双端队列数。
 *
 * 数据范围:
 * - 1≤N≤200000
 *
 *
 * 例 1：
 * 输入：
 * 6
 * 3
 * 6
 * 0
 * 9
 * 6
 * 3
 * 输出：
 * 2
 */
public class G47_Deque {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G047_input.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G047_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x12_queue/data/G047_input1.txt",
                "acguide/_0x10_basicdatastructure/_0x12_queue/data/G047_output1.txt");
    }

    public void minDequeNum() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] nums = new int[n][2];
        for (int i = 0; i < n; i++) {
            nums[i][0] = in.nextInt();
            nums[i][1] = i;
        }
        Arrays.sort(nums, (a, b) -> {
            int cmp = Integer.compare(a[0], b[0]);
            return cmp != 0 ? cmp : a[1] - b[1];
        });

        boolean isDown = true;
        int deque = 1;
        for (int i = 0, j = 1; j < n; j++) {
            if (nums[i][0] != nums[j][0]) {
                // [i,j) 是一段相等区间，[j,k) 是一段相等区间
                int k = j + 1;
                for (; k < n && nums[k][0] == nums[j][0]; k++);
                if (isDown) {
                    // 如果不能继续递减，则开始递增
                    if (nums[i][1] < nums[k-1][1]) {
                        isDown = false;
                    }
                } else {
                    // 如果不能继续递增，则开始递减，并产生一段单谷序列
                    if (nums[j-1][1] > nums[j][1]) {
                        isDown = true;
                        deque++;
                    }
                }
                i = j;
            }
        }
        System.out.println(deque);
    }

    @Test
    public void testMinDequeNum() {
        test(this::minDequeNum);
    }
}
