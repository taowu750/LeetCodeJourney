package training.statemachine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 722. 删除注释: https://leetcode-cn.com/problems/remove-comments/
 *
 * 给一个 C++ 程序，删除程序中的注释。这个程序source是一个数组，其中source[i]表示第i行源码。这表示每行源码由\n分隔。
 *
 * 在 C++ 中有两种注释风格，行内注释和块注释。
 * - 字符串"//"表示行注释，表示"//"和其右侧的其余字符应该被忽略。
 * - 字符串"/*"表示一个块注释，它表示直到"*\/"的下一个（非重叠）出现的所有字符都应该被忽略。（阅读顺序为从左到右）非重叠是指，
 *   字符串"/*\/"并没有结束块注释，因为注释的结尾与开头相重叠。
 *
 * 第一个有效注释优先于其他注释：如果字符串"//"出现在块注释中会被忽略。 同样，如果字符串"/*"出现在行或块注释中也会被忽略。
 *
 * 如果一行在删除注释之后变为空字符串，那么不要输出该行。即，答案列表中的每个字符串都是非空的。
 *
 * 样例中没有控制字符，单引号或双引号字符。比如，source = "string s = "/* Not a comment. *\/";"
 * 不会出现在测试样例里。（此外，没有其他内容（如定义或宏）会干扰注释。）
 *
 * 我们保证每一个块注释最终都会被闭合，所以在行或块注释之外的"/*"总是开始新的注释。
 *
 * 最后，隐式换行符可以通过块注释删除。有关详细信息，请参阅下面的示例。
 *
 * 从源代码中删除注释后，需要以相同的格式返回源代码。
 *
 * 例 1：
 * 输入:
 * source = [
 * "/*Test program *\/",
 * "int main()",
 * "{",
 * "  // variable declaration ",
 * "int a, b, c;",
 * "/* This is a test",
 * "   multiline  ",
 * "   comment for ",
 * "   testing *\/",
 * "a = b + c;", "}"
 * ]
 *
 * 示例代码可以编排成这样:
 * /*Test program *\/
 * int main()
 * {
 *   // variable declaration
 * int a,b,c;
 * /* This is a test
 *    multiline
 *    comment for
 *    testing *\/
 * a=b+c;
 * }
 *
 * 输出:["int main()","{ ","  ","int a, b, c;","a = b + c;","}"]
 *
 * 编排后:
 * int main()
 * {
 *
 * int a,b,c;
 * a=b+c;
 * }
 *
 * 解释:
 * 第 1行和第 6-9行的字符串 /* 表示块注释。第 4 行的字符串 // 表示行注释。
 *
 *
 * 例 2：
 * 输入:
 * source = ["a/*comment", "line", "more_comment*\/b"]
 * 输出:["ab"]
 * 解释:原始的 source 字符串是"a/*comment\nline\nmore_comment*\/b",其中我们用粗体显示了换行符。
 * 删除注释后，隐含的换行符被删除，留下字符串"ab"用换行符分隔成数组时就是["ab"].
 *
 *
 * 说明：
 * - source的长度范围为[1, 100].
 * - source[i]的长度范围为[0, 80].
 * - 每个块注释都会被闭合。
 * - 给定的源码中不会有单引号、双引号或其他控制字符。
 */
public class E722_Medium_RemoveComments {

    public static void test(Function<String[], List<String>> method) {
        assertEquals(asList("int main()","{ ","  ","int a, b, c;","a = b + c;","}"),
                method.apply(new String[]{"/*Test program */", "int main()", "{ ", "  // variable declaration ",
                        "int a, b, c;", "/* This is a test", "   multiline  ", "   comment for ", "   testing */",
                        "a = b + c;", "}"}));
        assertEquals(singletonList("ab"), method.apply(new String[]{"a/*comment", "line", "more_comment*/b"}));
        /*
        struct Node{
            /*\/ declare members;/**\/
            int size;
            /**\/int val;
        };
        */
        assertEquals(asList("struct Node{", "    ", "    int size;", "    int val;", "};"), method.apply(new String[]{"struct Node{", "    /*/ declare members;/**/", "    int size;", "    /**/int val;", "};"}));

        /*
        void func(int k) {
        // this function does nothing /*
           k = k*2/4;
           k = k/2;*\/
        }
        */
        assertEquals(asList("void func(int k) {","   k = k*2/4;","   k = k/2;*/","}"), method.apply(new String[]{"void func(int k) {", "// this function does nothing /*", "   k = k*2/4;", "   k = k/2;*/", "}"}));

        /*
        main() {
           func(1);
           /** / more comments here
           float f = 2.0
           f += f;

           cout << f; *\/
        }
        */
        assertEquals(asList("main() {","   func(1);","   ","}"), method.apply(new String[]{"main() {", "   func(1);", "   /** / more comments here", "   float f = 2.0", "   f += f;", "   cout << f; */", "}"}));
    }

    public enum State {
        NORMAL,
        // 遇到一个 "/"，则可能会进入注释状态
        COMMENT_POSSIBLE_START,
        LINE_COMMENT,
        BLOCK_COMMENT,
        // 遇到一个 "*"，则可能会退出块注释状态
        BLOCK_COMMENT_POSSIBLE_END,
    }

    /**
     * 状态机解法，可以使用 EnumMap 优化，参见 {@link Offer20_Medium_StringRepresentingNumericValue}。
     * 如果使用整数替代 Enum 就是 0ms
     *
     * LeetCode 耗时：1 ms - 66.27%
     *          内存消耗：37 MB - 41.85%
     */
    public List<String> removeComments(String[] source) {
        /*
        普通 --[/]--> 可能注释开始
        普通 --[\n]--> 普通(一行结束)
        普通 --[其他]--> 普通

        可能注释开始 --[/]--> 行注释
        可能注释开始 --[*]--> 块注释
        可能注释开始 --[\n]--> 普通(一行结束)
        可能注释开始 --[其他]--> 普通

        行注释，快速去掉后面所有字符 --> 普通

        块注释 --[*]--> 可能注释结束
        块注释 --[其他]--> 块注释

        可能注释结束 --[/]--> 普通
        可能注释结束 --[*]--> 预备结束
        可能注释结束 --[其他]--> 块注释
         */
        List<String> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        State state = State.NORMAL;
        for (String s : source) {
            LABEL: for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (state) {
                    case NORMAL:
                        if (c == '/') {
                            state = State.COMMENT_POSSIBLE_START;
                        } else {
                            sb.append(c);
                        }
                        break;

                    case COMMENT_POSSIBLE_START:
                        if (c == '/') {
                            state = State.NORMAL;
                            break LABEL;
                        } else if (c == '*') {
                            state = State.BLOCK_COMMENT;
                        } else {
                            state = State.NORMAL;
                            sb.append('/').append(c);
                        }
                        break;

                    case BLOCK_COMMENT:
                        if (c == '*') {
                            state = State.BLOCK_COMMENT_POSSIBLE_END;
                        }
                        break;

                    case BLOCK_COMMENT_POSSIBLE_END:
                        if (c == '/') {
                            state = State.NORMAL;
                        } else if (c != '*') { // 不要忘了 *
                            state = State.BLOCK_COMMENT;
                        }
                        break;
                }
            }
            // 预备注释遇到换行也需要转换
            if (state == State.COMMENT_POSSIBLE_START) {
                state = State.NORMAL;
                sb.append('/');
            }
            if (state == State.NORMAL && sb.length() > 0) {
                ans.add(sb.toString());
                sb.setLength(0);
            }
        }

        return ans;
    }

    @Test
    public void testRemoveComments() {
        test(this::removeComments);
    }
}
