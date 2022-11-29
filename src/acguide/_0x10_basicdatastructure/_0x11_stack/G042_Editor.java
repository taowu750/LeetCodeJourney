package acguide._0x10_basicdatastructure._0x11_stack;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 编辑器: https://www.acwing.com/problem/content/130/
 *
 * 你将要实现一个功能强大的整数序列编辑器。
 *
 * 在开始时，序列是空的。编辑器共有五种指令，如下：
 * 1、I x，在光标处插入数值 x。
 * 2、D，将光标前面的第一个元素删除，如果前面没有元素，则忽略此操作。
 * 3、L，将光标向左移动，跳过一个元素，如果左边没有元素，则忽略此操作。
 * 4、R，将光标向右移动，跳过一个元素，如果右边没有元素，则忽略此操作。
 * 5、Q k，假设此刻光标之前的序列为 a1,a2,…,an，输出 max_{1≤i≤k}Si，其中 Si=a1+a2+…+ai。
 *
 * 输入格式：
 * - 第一行包含一个整数 Q，表示指令的总数。
 * - 接下来 Q 行，每行一个指令，具体指令格式如题目描述。
 *
 * 输出格式：
 * - 每一个 Q k 指令，输出一个整数作为结果，每个结果占一行。
 *
 * 数据范围：
 * - 1 ≤ Q ≤ 10^6,
 * - |x| ≤ 10^3,
 * - 1 ≤ k ≤ n
 *
 *
 * 例 1：
 * 输入：
 * 8
 * I 2
 * I -1
 * I 1
 * Q 3
 * L
 * D
 * R
 * Q 2
 * 输出：
 * 2
 * 3
 */
public class G042_Editor {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x11_stack/data/G042_input.txt",
                "acguide/_0x10_basicdatastructure/_0x11_stack/data/G042_output.txt");
    }

    public interface IEditor {

        void insert(int i);

        void delete();

        void moveLeft();

        void moveRight();

        int query(int k);
    }

    public static class Editor implements IEditor {

        private final List<int[]> seq;
        private int cursor;

        public Editor() {
            seq = new ArrayList<>();
        }

        public void insert(int i) {
            if (cursor > 0) {
                int[] prev = seq.get(cursor - 1);
                seq.add(cursor, new int[]{i, prev[1] + i, Math.max(prev[1] + i, prev[2])});
            } else {
                seq.add(cursor, new int[]{i, i, i});
            }
            cursor++;
        }

        public void delete() {
            if (cursor > 0) {
                seq.remove(--cursor);
            }
        }

        public void moveLeft() {
            if (cursor > 0) {
                cursor--;
            }
        }

        public void moveRight() {
            // 每次往右移就更新 maxQuery，防止之前删除的脏数据
            if (cursor < seq.size() && ++cursor > 1) {
                int[] prev = seq.get(cursor - 2), cur = seq.get(cursor - 1);
                cur[1] = prev[1] + cur[0];
                cur[2] = Math.max(cur[1], prev[2]);
            }
        }

        public int query(int k) {
            return seq.get(k - 1)[2];
        }
    }

    public void execute(IEditor editor) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(in.readLine());
            for (int i = 0; i < n; i++) {
                String s = in.readLine();
                char operator = s.charAt(0);
                switch (operator) {
                    case 'I':
                        editor.insert(Integer.parseInt(s.substring(2)));
                        break;

                    case 'D':
                        editor.delete();
                        break;

                    case 'L':
                        editor.moveLeft();
                        break;

                    case 'R':
                        editor.moveRight();
                        break;

                    case 'Q':
                        System.out.println(editor.query(Integer.parseInt(s.substring(2))));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editorExecute() {
        execute(new Editor());
    }

    @Test
    public void testEditorExecute() {
        test(this::editorExecute);
    }


    /**
     * 双栈法，参见 https://www.acwing.com/solution/content/1275/
     */
    public static class StackEditor implements IEditor {

        private static class Seq {

            private final List<Integer> list = new ArrayList<>();

            public int get(int i) {
                return list.get(i);
            }

            public void set(int i, int num) {
                if (i >= list.size()) {
                    list.add(num);
                } else {
                    list.set(i, num);
                }
            }
        }

        // 保存光标前数字的栈，和光标后数字的栈
        private final Deque<Integer> prevStack, nextStack;
        private final Seq prefix, max;

        public StackEditor() {
            prevStack = new ArrayDeque<>();
            nextStack = new ArrayDeque<>();
            prefix = new Seq();
            max = new Seq();
        }

        public void insert(int i) {
            prevStack.push(i);
            if (prevStack.size() == 1) {
                prefix.set(0, i);
                max.set(0, i);
            } else {
                int prev = prevStack.size() - 2, cur = prevStack.size() - 1;
                int sum = prefix.get(prev) + i;
                prefix.set(cur, sum);
                max.set(cur, Math.max(sum, max.get(prev)));
            }
        }

        public void delete() {
            if (!prevStack.isEmpty()) {
                prevStack.pop();
            }
        }

        public void moveLeft() {
            if (!prevStack.isEmpty()) {
                nextStack.push(prevStack.pop());
            }
        }

        public void moveRight() {
            if (!nextStack.isEmpty()) {
                insert(nextStack.pop());
            }
        }

        public int query(int k) {
            return max.get(k - 1);
        }
    }

    public void executeStackEditor() {
        execute(new StackEditor());
    }

    @Test
    public void testExecuteStackEditor() {
        test(this::executeStackEditor);
    }
}
