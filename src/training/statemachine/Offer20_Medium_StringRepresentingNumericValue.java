package training.statemachine;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 剑指 Offer 20. 表示数值的字符串: https://leetcode-cn.com/problems/biao-shi-shu-zhi-de-zi-fu-chuan-lcof/
 *
 * 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。
 *
 * 数值（按顺序）可以分成以下几个部分：
 * 1. 若干空格
 * 2. 一个「小数」或者「整数」
 * 3. （可选）一个 'e' 或 'E' ，后面跟着一个「整数」
 * 4. 若干空格
 *
 * 小数（按顺序）可以分成以下几个部分：
 * 1. （可选）一个符号字符（'+' 或 '-'）
 * 2. 下述格式之一：
 *      1. 至少一位数字，后面跟着一个点 '.'
 *      2. 至少一位数字，后面跟着一个点 '.' ，后面再跟着至少一位数字
 *      3. 一个点 '.' ，后面跟着至少一位数字
 *
 * 整数（按顺序）可以分成以下几个部分：
 * 1. （可选）一个符号字符（'+' 或 '-'）
 * 2. 至少一位数字
 *
 * 部分数值列举如下：
 * - ["+100", "5e2", "-123", "3.1416", "-1E-16", "0123"]
 *
 * 部分非数值列举如下：
 * - ["12e", "1a3.14", "1.2.3", "+-5", "12e+5.4"]
 *
 * 例 1：
 * 输入：s = "0"
 * 输出：true
 *
 * 例 2：
 * 输入：s = "e"
 * 输出：false
 *
 * 例 3：
 * 输入：s = "."
 * 输出：false
 *
 * 例 4：
 * 输入：s = "    .1  "
 * 输出：true
 *
 * 约束：
 * - 1 <= s.length <= 20
 * - s 仅含英文字母（大写和小写），数字（0-9），加号 '+' ，减号 '-' ，空格 ' ' 或者点 '.' 。
 */
public class Offer20_Medium_StringRepresentingNumericValue {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("+100"));
        assertTrue(method.test("5e2"));
        assertTrue(method.test("-123"));
        assertTrue(method.test("3.1416"));
        assertTrue(method.test("-1.37E-16"));
        assertTrue(method.test("0123"));
        assertTrue(method.test("    .1  "));
        assertTrue(method.test(" 2."));
        assertFalse(method.test("1a3.14"));
        assertFalse(method.test("1.2.3"));
        assertFalse(method.test("+-5"));
        assertFalse(method.test("12e+5.4"));
        assertFalse(method.test("  1.37  5"));
        assertFalse(method.test(" "));
        assertFalse(method.test("0e "));
        assertFalse(method.test("92e1740e91"));
        assertFalse(method.test("."));
        assertTrue(method.test("3. "));
        assertTrue(method.test("0"));
        assertFalse(method.test(". "));
        assertFalse(method.test(" -."));
        assertTrue(method.test("46.e3"));
    }

    /**
     * 类似于状态机。
     *
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：38.5 MB - 61.15%
     */
    public boolean isNumber(String s) {
        // 是先导空格，还是后导空格
        boolean prevSpace = true;
        int numberPart = 0;
        // 分别解析三个部分（前导后导空格、小数或整数、指数）
        for (int i = 0; i < s.length();) {
            char c = s.charAt(i);
            // 如果 c 等于空格
            if (c == ' ') {
                // 一直到不是空格的字符为止
                while (++i < s.length() && s.charAt(i) == ' ');
                // 如果是前导空格
                if (prevSpace) {
                    prevSpace = false;
                    // 如果已经到字符串末尾，则返回 false
                    if (i == s.length())
                        return false;
                } else {
                    // 否则是后导空格，则返回是否到字符串末尾
                    return i == s.length();
                }
            }
            // 如果是第一部分的数字
            else if (numberPart == 0) {
                // 不能再有先导空格
                prevSpace = false;
                numberPart++;
                // 解析开头的符号
                if (c == '+' || c == '-') {
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 解析开头的小数点
                boolean dot = false;
                if (c == '.') {
                    dot = true;
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 遇到数字，解析成小数或整数
                if (c >= '0' && c <= '9') {
                    while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
                    if (c == '.') {
                        // 前面已经有小数点，则返回 false
                        if (dot)
                            return false;
                        while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
                    }
                }
                // 遇到其他字符，返回 false
                else {
                    return false;
                }
            }
            // 如果是第二部分的数字，即指数
            else if (numberPart == 1) {
                // 必须以 e 或 E 开头
                if (c != 'e' && c != 'E')
                    return false;
                // 不能再有先导空格
                prevSpace = false;
                numberPart++;
                // 指数必须存在
                if (i == s.length() - 1)
                    return false;
                c = s.charAt(++i);
                // 解析符号
                if (c == '+' || c == '-') {
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 如果后面没有数字，返回 false
                if (c < '0' || c > '9')
                    return false;
                while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
            }
            // 遇到其他字符，或其他情况返回 false
            else {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testIsNumber() {
        test(this::isNumber);
    }


    public enum NumberState {
        START,
        LEADING_SPACE,
        LEADING_SIGN,
        LEADING_INTEGER,
        DOT,
        DECIMAL,
        E,
        SUCCEED_SIGN,
        SUCCEED_INTEGER,
        SUCCEED_SPACE,
        ERROR,
        END
        ;

        public boolean can2End() {
            return this == LEADING_INTEGER || this == DOT || this == DECIMAL
                    || this == SUCCEED_INTEGER || this == SUCCEED_SPACE || this == END;
        }
    }

    /**
     * 状态机方法，这是后来复习题解时自己想的，其中的基本思想可以参见 {@link #betterDfmMethod(String)} 方法的注释。
     * 后面的方法使用 EnumMap 避免了 switch 和 if/else。
     *
     * {@link #betterDfmMethod(String)} 方法对小数点的处理更加优雅。
     *
     * LeetCode 耗时：2 ms - 88.77%
     *          内存消耗：38.3 MB - 64.59%
     */
    public boolean dfmMethod(String s) {
        /*
        有效数字符合以下模式：

            前导空格(可选) 正负号(可选) 整数|数.|.数|数.数 [e/E+正负号(可选)+整数](可选) 后继空格(可选)

        这些模式可以作为状态，从而构成一个状态机。

        需要注意的是，小数点状态的状态转移不仅取决于输入，也取决于上一个状态。
        我们可以在小数点状态进行处理，也可以在它的上一个状态（START、空格或正负号）进行处理。
         */
        NumberState state = NumberState.START;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (state) {
                /*
                start -> 前导空格
                      -> 前导正负号
                      -> 前导整数
                      -> 小数点
                      -> ERROR

                前导空格 -> 前导空格
                        -> 前导正负号
                        -> 前导整数
                        -> 小数点
                        -> ERROR
                 */
                case START:
                case LEADING_SPACE:
                    if (c == ' ') {
                        state = NumberState.LEADING_SPACE;
                    } else if (c == '+' || c == '-') {
                        state = NumberState.LEADING_SIGN;
                    } else if (Character.isDigit(c)) {
                        state = NumberState.LEADING_INTEGER;
                    }
                    // 注意，当在 START 或前导空格状态遇到小数点输入时，下一个输入必须是数字才能够进行转移
                    else if (c == '.' && (i < s.length() - 1 && Character.isDigit(s.charAt(i + 1)))) {
                        state = NumberState.DOT;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                前导正负号 -> 前导整数
                          -> 小数点
                          -> ERROR
                 */
                case LEADING_SIGN:
                    if (Character.isDigit(c)) {
                        state = NumberState.LEADING_INTEGER;
                    }
                    // 注意，当在前导正负号状态遇到小数点输入时，下一个输入必须是数字才能够进行转移
                    else if (c == '.' && (i < s.length() - 1 && Character.isDigit(s.charAt(i + 1)))) {
                        state = NumberState.DOT;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                前导整数 -> 前导整数
                        -> 小数点
                        -> E
                        -> 后导空格
                        -> END
                        -> ERROR
                 */
                case LEADING_INTEGER:
                    if (Character.isDigit(c)) {
                        continue;
                    } else if (c == '.') {
                        state = NumberState.DOT;
                    } else if (c == 'e' || c == 'E') {
                        state = NumberState.E;
                    } else if (c == ' ') {
                        state = NumberState.SUCCEED_SPACE;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                小数点 -> 小数
                      -> 后导空格（注意这里的转移必须有前导整数）
                      -> E（注意这里的转移必须有前导整数）
                      -> END（注意这里的转移必须有前导整数）
                      -> ERROR
                 */
                case DOT:
                    if (Character.isDigit(c)) {
                        state = NumberState.DECIMAL;
                    } else if (c == ' ') {
                        state = NumberState.SUCCEED_SPACE;
                    } else if (c == 'e' || c == 'E') {
                        state = NumberState.E;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                小数 -> 小数
                     -> E
                     -> 后导空格
                     -> END
                     -> ERROR
                 */
                case DECIMAL:
                    if (Character.isDigit(c)) {
                        continue;
                    } else if (c == 'e' || c == 'E') {
                        state = NumberState.E;
                    } else if (c == ' ') {
                        state = NumberState.SUCCEED_SPACE;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                E -> 后继符号
                  -> 后继整数
                  -> ERROR
                 */
                case E:
                    if (c == '-' || c == '+') {
                        state = NumberState.SUCCEED_SIGN;
                    } else if (Character.isDigit(c)) {
                        state = NumberState.SUCCEED_INTEGER;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                后继符号 -> 后继整数
                        -> ERROR
                 */
                case SUCCEED_SIGN:
                    if (Character.isDigit(c)) {
                        state = NumberState.SUCCEED_INTEGER;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                后继整数 -> 后继整数
                        -> 后继空格
                        -> END
                        -> ERROR
                 */
                case SUCCEED_INTEGER:
                    if (Character.isDigit(c)) {
                        continue;
                    } else if (c == ' ') {
                        state = NumberState.SUCCEED_SPACE;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                /*
                后继空格 -> 后继空格
                        -> END
                        -> ERROR
                 */
                case SUCCEED_SPACE:
                    if (c == ' ') {
                        continue;
                    } else {
                        state = NumberState.ERROR;
                    }
                    break;

                case ERROR:
                    break;

                /*
                END -> ERROR
                 */
                case END:
                    state = NumberState.ERROR;
                    break;
            }
            if (state == NumberState.ERROR) {
                break;
            }
        }

        return state.can2End();
    }


    /**
     * 确定有限状态自动机（以下简称「自动机」）是一类计算模型。它包含一系列状态，这些状态中：
     * - 有一个特殊的状态，被称作「初始状态」。
     * - 还有一系列状态被称为「接受状态」，它们组成了一个特殊的集合。其中，一个状态可能既是「初始状态」，也是「接受状态」。
     *
     * 起初，这个自动机处于「初始状态」。随后，它顺序地读取字符串中的每一个字符，并根据当前状态和读入的字符，
     * 按照某个事先约定好的「转移规则」，从当前状态转移到下一个状态；当状态转移完成后，它就读取下一个字符。
     * 当字符串全部读取完毕后，如果自动机处于某个「接受状态」，则判定该字符串「被接受」；否则，判定该字符串「被拒绝」。
     *
     * 如果输入的过程中某一步转移失败了，即不存在对应的「转移规则」，此时计算将提前中止。在这种情况下我们也判定该字符串「被拒绝」。
     *
     * 根据上面的描述，现在可以定义自动机的「状态集合」了。那么怎么挖掘出所有可能的状态呢？一个常用的技巧是，
     * 用「当前处理到字符串的哪个部分」当作状态的表述。根据这一技巧，不难挖掘出所有状态：
     *  1. 起始的空格
     *  2. 符号位
     *  3. 整数部分
     *  4. 左侧有整数的小数点
     *  5. 左侧无整数的小数点
     *  6. 小数部分
     *  7. 字符 e
     *  8. 指数部分的符号位
     *  9. 指数部分的整数部分
     * 10. 末尾的空格
     *
     * 下一步是找出「初始状态」和「接受状态」的集合。根据题意，「初始状态」应当为状态 1，
     * 而「接受状态」的集合则为状态 3、状态 4、状态 6、状态 9 以及状态 10。
     * 换言之，字符串的末尾要么是空格，要么是数字，要么是小数点，但前提是小数点的前面有数字。
     *
     * 参见：
     * https://leetcode-cn.com/problems/biao-shi-shu-zhi-de-zi-fu-chuan-lcof/solution/biao-shi-shu-zhi-de-zi-fu-chuan-by-leetcode-soluti/
     *
     * LeetCode 耗时：3 ms - 63.19%
     *          内存消耗：38.5 MB - 49.38%
     */
    public boolean betterDfmMethod(String s) {
        State state = State.STATE_INITIAL;
        for (int i = 0; i < s.length(); i++) {
            CharType charType = toCharType(s.charAt(i));
            // 查看当前状态是否能处理指定输入
            if (transferFunc.get(state).containsKey(charType)) {
                state = transferFunc.get(state).get(charType);
            } else {
                return false;
            }
        }

        // 合法的数字可以以数字、小数点、小数、或整个流程的终结态作为结束状态
        return state == State.STATE_INTEGER
                || state == State.STATE_POINT
                || state == State.STATE_FRACTION
                || state == State.STATE_EXP_NUMBER
                || state == State.STATE_END;
    }

    // 流程中的状态
    enum State {
        // 整个流程的开始状态。注意状态机的起始状态集合不只包含它
        STATE_INITIAL,
        STATE_INT_SIGN,
        STATE_INTEGER,
        STATE_POINT,
        STATE_POINT_WITHOUT_INT,
        STATE_FRACTION,
        STATE_EXP,
        STATE_EXP_SIGN,
        STATE_EXP_NUMBER,
        // 整个流程的结束状态。注意状态机的结束状态集合不只包含它
        STATE_END
    }

    // 输入的字符类型
    enum CharType {
        CHAR_NUMBER,
        CHAR_EXP,
        CHAR_POINT,
        CHAR_SIGN,
        CHAR_SPACE,
        CHAR_ILLEGAL
    }

    // 状态转移方程
    static Map<State, Map<CharType, State>> transferFunc = new EnumMap<>(State.class);
    static {
        // 合法的数字可以以空格、数字、小数点和符号开头
        Map<CharType, State> initialTransfer = new EnumMap<>(CharType.class);
        initialTransfer.put(CharType.CHAR_SPACE, State.STATE_INITIAL);
        initialTransfer.put(CharType.CHAR_NUMBER, State.STATE_INTEGER);
        initialTransfer.put(CharType.CHAR_POINT, State.STATE_POINT_WITHOUT_INT);
        initialTransfer.put(CharType.CHAR_SIGN, State.STATE_INT_SIGN);
        transferFunc.put(State.STATE_INITIAL, initialTransfer);

        Map<CharType, State> intSignTransfer = new EnumMap<>(CharType.class);
        intSignTransfer.put(CharType.CHAR_NUMBER, State.STATE_INTEGER);
        intSignTransfer.put(CharType.CHAR_POINT, State.STATE_POINT_WITHOUT_INT);
        transferFunc.put(State.STATE_INT_SIGN, intSignTransfer);

        Map<CharType, State> integerTransfer = new EnumMap<>(CharType.class);
        integerTransfer.put(CharType.CHAR_NUMBER, State.STATE_INTEGER);
        integerTransfer.put(CharType.CHAR_EXP, State.STATE_EXP);
        integerTransfer.put(CharType.CHAR_POINT, State.STATE_POINT);
        integerTransfer.put(CharType.CHAR_SPACE, State.STATE_END);
        transferFunc.put(State.STATE_INTEGER, integerTransfer);

        Map<CharType, State> pointTransfer = new EnumMap<>(CharType.class);
        pointTransfer.put(CharType.CHAR_NUMBER, State.STATE_FRACTION);
        pointTransfer.put(CharType.CHAR_EXP, State.STATE_EXP);
        pointTransfer.put(CharType.CHAR_SPACE, State.STATE_END);
        transferFunc.put(State.STATE_POINT, pointTransfer);

        Map<CharType, State> pointWithoutIntTransfer = new EnumMap<>(CharType.class);
        pointWithoutIntTransfer.put(CharType.CHAR_NUMBER, State.STATE_FRACTION);
        transferFunc.put(State.STATE_POINT_WITHOUT_INT, pointWithoutIntTransfer);

        Map<CharType, State> fractionTransfer = new EnumMap<>(CharType.class);
        fractionTransfer.put(CharType.CHAR_NUMBER, State.STATE_FRACTION);
        fractionTransfer.put(CharType.CHAR_EXP, State.STATE_EXP);
        fractionTransfer.put(CharType.CHAR_SPACE, State.STATE_END);
        transferFunc.put(State.STATE_FRACTION, fractionTransfer);

        Map<CharType, State> expTransfer = new EnumMap<>(CharType.class);
        expTransfer.put(CharType.CHAR_NUMBER, State.STATE_EXP_NUMBER);
        expTransfer.put(CharType.CHAR_SIGN, State.STATE_EXP_SIGN);
        transferFunc.put(State.STATE_EXP, expTransfer);

        Map<CharType, State> expSignTransfer = new EnumMap<>(CharType.class);
        expSignTransfer.put(CharType.CHAR_NUMBER, State.STATE_EXP_NUMBER);
        transferFunc.put(State.STATE_EXP_SIGN, expSignTransfer);

        Map<CharType, State> expNumberTransfer = new EnumMap<>(CharType.class);
        expNumberTransfer.put(CharType.CHAR_NUMBER, State.STATE_EXP_NUMBER);
        expNumberTransfer.put(CharType.CHAR_SPACE, State.STATE_END);
        transferFunc.put(State.STATE_EXP_NUMBER, expNumberTransfer);

        Map<CharType, State> endTransfer = new EnumMap<>(CharType.class);
        endTransfer.put(CharType.CHAR_SPACE, State.STATE_END);
        transferFunc.put(State.STATE_END, endTransfer);
    }

    // 将字符转换为对应的输入类型
    private CharType toCharType(char ch) {
        if (ch >= '0' && ch <= '9') {
            return CharType.CHAR_NUMBER;
        } else if (ch == 'e' || ch == 'E') {
            return CharType.CHAR_EXP;
        } else if (ch == '.') {
            return CharType.CHAR_POINT;
        } else if (ch == '+' || ch == '-') {
            return CharType.CHAR_SIGN;
        } else if (ch == ' ') {
            return CharType.CHAR_SPACE;
        } else {
            return CharType.CHAR_ILLEGAL;
        }
    }

    @Test
    public void testDfmMethod() {
        test(this::betterDfmMethod);
    }
}
