package util.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * KMP 字符串匹配算法，参见：https://www.zhihu.com/question/21923021/answer/1032665486
 *
 *  所谓字符串匹配，是这样一种问题：“字符串 P 是否为字符串 S 的子串？如果是，它出现在 S 的哪些位置？”
 *  其中 S 称为主串；P 称为模式串。
 *
 * 尽可能利用残余的信息，是KMP算法的思想所在。
 * 一次失败的匹配，会给我们提供宝贵的信息——如果 S[i : i+len(P)] 与 P 的匹配是在第 r 个位置失败的，
 * 那么从 S[i] 开始的 (r-1) 个连续字符，一定与 P 的前 (r-1) 个字符一模一样！
 * 每一次失败都会给我们换来一些信息——能告诉我们，「主串的某一个子串等于模式串的某一个前缀」。
 *
 * 如果我们跳过那些绝不可能成功的字符串比较，则可以希望复杂度降低到能接受的范围。例如：
 * - 主串 S   = abcab???...?
 * - 模式串 P = abcabd
 * 和主串从S[0]开始匹配时，在 P[5] 处失配。现在我们来考虑：从 S[1]、S[2]、S[3] 开始的匹配尝试，有没有可能成功？
 * 从 S[1] 开始肯定没办法成功，因为 S[1] = P[1] = 'b'，和 P[0] 并不相等。从 S[2] 开始也是没戏的，
 * 因为 S[2] = P[2] = 'c'，并不等于P[0]. 但是从 S[3] 开始是有可能成功的——至少按照已知的信息，我们推不出矛盾。
 *
 *
 * 带着「跳过不可能成功的尝试」的思想，我们来看「next数组」。
 * next数组是对于模式串而言的。P 的 next 数组定义为：next[i] 表示 P[0] ~ P[i] 这一个子串，
 * 使得「前k个字符恰等于后k个字符」的最大的k. 特别地，k不能取i+1，因为这个子串一共才 i+1 个字符，自己肯定与自己相等，就没有意义了。
 * 例如当 P="abcabd"时，next=[0,0,0,1,2,0]
 *
 * 如果把模式串视为一把标尺，在主串上移动，那么暴力法就是每次失配之后只右移一位；改进算法则是每次失配之后，移很多位，跳过那些不可能匹配成功的位置。
 * 我们应该如何移动这把标尺呢？我们需要利用 next 数组中的信息，让「移动后旧的后缀要与新的前缀一致」。
 *
 * 回忆next数组的性质：P[0] 到 P[i] 这一段子串中，前next[i]个字符与后next[i]个字符一模一样。既然如此，
 * 如果失配在 P[r], 那么P[0]~P[r-1]这一段里面，前next[r-1]个字符恰好和后next[r-1]个字符相等——也就是说，
 * 我们可以拿长度为 next[r-1] 的那一段前缀，来顶替当前后缀的位置，让匹配继续下去！
 *
 *
 * 了解了利用next数组加速字符串匹配的原理，则代码实现可以分为两个部分：建立next数组、利用next数组进行匹配。
 *
 * 快速构建next数组，是KMP算法的精髓所在，核心思想是「P自己与自己做匹配」。为什么这样说呢？回顾next数组的完整定义：
 * - 定义 “k-前缀” 为一个字符串的前 k 个字符； “k-后缀” 为一个字符串的后 k 个字符。k 必须小于字符串长度。
 * - next[x] 定义为： P[0]~P[x] 这一段字符串，使得k-前缀恰等于k-后缀的最大的k.
 *
 * 接下来，我们考虑采用递推的方式求出next数组。如果next[0], next[1], ... next[x-1]均已知，那么如何求出 next[x] 呢？
 * 来分情况讨论。首先，已经知道了 next[x-1]（以下记为now）:
 * - 如果 P[x] 与 P[now] 一样，那最长相等前后缀的长度就可以扩展一位，很明显 next[x] = now + 1;
 * - 如果 P[x] 与 P[now] 不一样，例如下面这样：
 *
 *              [abcab]ddd[abcab]c
 *               子串A      子串B
 *     next数组  0001200012345?
 *
 *   长度为 now 的子串 A 和子串 B 是 P[0]~P[x-1] 中最长的公共前后缀。可惜 A 右边的字符和 B 右边的那个字符不相等，
 *   next[x]不能改成 now+1 了。因此，我们应该缩短这个now，把它改成小一点的值，再来试试 P[x] 是否等于 P[now].
 *
 *   我们决定，在保持“P[0]~P[x-1]的now-前缀仍然等于now-后缀”的前提下，让这个新的now尽可能大一点。
 *   换句话讲：接下来now应该改成：使得「A的k-前缀等于B的k-后缀」的最大的k.
 *
 *   您应该已经注意到了一个非常强的性质——串A和串B是相同的！B的后缀等于A的后缀！因此，使得A的k-前缀等于B的k-后缀的最大的k，
 *   其实就是串A的最长公共前后缀的长度 —— next[now-1]！
 *
 *   当P[now]与P[x]不相等的时候，我们需要缩小now——把now变成next[now-1]，直到P[now]=P[x]为止。
 *   P[now]=P[x]时，就可以直接向右扩展了。
 */
public class KMP {

    private final String pattern;
    private final int[] next;

    public KMP(String pattern) {
        this.pattern = pattern;
        next = new int[pattern.length()];
        buildNext();
    }

    public int search(String str) {
        // 主串中将要匹配的位置
        int target = 0;
        // 模式串中将要匹配的位置
        int pos = 0;

        final int n = str.length(), m = pattern.length();
        while (target < n) {
            // 匹配则移动主串和模式串的下标
            if (str.charAt(target) == pattern.charAt(pos)) {
                target++;
                pos++;
            } else if (pos > 0) {  // 失配且 pos > 0，则利用 next 数组移动标尺
                pos = next[pos - 1];
            } else {  // pattern[0] 失配了，则直接移动主串下标
                target++;
            }

            // 完全匹配，返回下标
            if (pos == m) {
                return target - m;
            }
        }

        return -1;
    }

    public List<Integer> searchAll(String str) {
        // 主串中将要匹配的位置
        int target = 0;
        // 模式串中将要匹配的位置
        int pos = 0;

        List<Integer> result = new ArrayList<>();
        final int n = str.length(), m = pattern.length();
        while (target < n) {
            if (str.charAt(target) == pattern.charAt(pos)) {
                target++;
                pos++;
            } else if (pos > 0) {
                pos = next[pos - 1];
            } else {
                target++;
            }

            if (pos == m) {
                result.add(target - m);
                // 找到了一个匹配，让 pos 退一步继续查找
                pos = next[pos - 1];
            }
        }

        return result;
    }

    private void buildNext() {
        final int m = pattern.length();
        // next[0] 必然是 0，因此下标从 1 开始
        int i = 1;
        int now = 0;

        while (i < m) {
            // 如果 P[now] == P[i]，那么 now 可以向右扩展一位
            if (pattern.charAt(now) == pattern.charAt(i)) {
                now++;
                // 注意 i 的赋值
                next[i++] = now;
            } else if (now > 0) {  // 缩小 now，改成 next[now - 1]
                now = next[now - 1];
            } else {  // now 为 0，无法再缩小
                i++;
            }
        }
    }
}
