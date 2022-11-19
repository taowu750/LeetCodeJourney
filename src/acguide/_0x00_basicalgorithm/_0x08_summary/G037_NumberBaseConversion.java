package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 数的进制转换: https://www.acwing.com/problem/content/126/
 *
 * 编写一个程序，可以实现将一个数字由一个进制转换为另一个进制。
 * 这里有 62 个不同数位 {0−9,A−Z,a−z}。
 *
 * 输入格式：
 * - 第一行输入一个整数，代表接下来的行数。
 * - 接下来每一行都包含三个数字，首先是输入进制（十进制表示），然后是输出进制（十进制表示），
 *   最后是用输入进制表示的输入数字，数字之间用空格隔开。
 * - 输入进制和输出进制都在 2 到 62 的范围之内。
 * -（在十进制下）A=10，B=11，…，Z=35，a=36，b=37，…，z=61 (0−9 仍然表示 0−9)。
 *
 * 输出格式：
 * - 对于每一组进制转换，程序的输出都由三行构成。
 * - 第一行包含两个数字，首先是输入进制（十进制表示），然后是用输入进制表示的输入数字。
 * - 第二行包含两个数字，首先是输出进制（十进制表示），然后是用输出进制表示的输入数字。
 * - 第三行为空白行。
 * - 同一行内数字用空格隔开。
 *
 * 例 1：
 * 输入：
 * 8
 * 62 2 abcdefghiz
 * 10 16 1234567890123456789012345678901234567890
 * 16 35 3A0C92075C0DBF3B8ACBC5F96CE3F0AD2
 * 35 23 333YMHOUE8JPLT7OX6K9FYCQ8A
 * 23 49 946B9AA02MI37E3D3MMJ4G7BL2F05
 * 49 61 1VbDkSIMJL3JjRgAdlUfcaWj
 * 61 5 dl9MDSWqwHjDnToKcsWE1S
 * 5 10 42104444441001414401221302402201233340311104212022133030
 * 输出：
 * 62 abcdefghiz
 * 2 11011100000100010111110010010110011111001001100011010010001
 *
 * 10 1234567890123456789012345678901234567890
 * 16 3A0C92075C0DBF3B8ACBC5F96CE3F0AD2
 *
 * 16 3A0C92075C0DBF3B8ACBC5F96CE3F0AD2
 * 35 333YMHOUE8JPLT7OX6K9FYCQ8A
 *
 * 35 333YMHOUE8JPLT7OX6K9FYCQ8A
 * 23 946B9AA02MI37E3D3MMJ4G7BL2F05
 *
 * 23 946B9AA02MI37E3D3MMJ4G7BL2F05
 * 49 1VbDkSIMJL3JjRgAdlUfcaWj
 *
 * 49 1VbDkSIMJL3JjRgAdlUfcaWj
 * 61 dl9MDSWqwHjDnToKcsWE1S
 *
 * 61 dl9MDSWqwHjDnToKcsWE1S
 * 5 42104444441001414401221302402201233340311104212022133030
 *
 * 5 42104444441001414401221302402201233340311104212022133030
 * 10 1234567890123456789012345678901234567890
 */
public class G037_NumberBaseConversion {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G037_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G037_output.txt");
    }

    public static final int[] C2N = new int[128];
    public static final char[] N2C = new char[62];
    static {
        for (int i = '0'; i <= '9'; i++) {
            C2N[i] = i - '0';
            N2C[i - '0'] = (char) i;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            C2N[i] = i - 'A' + 10;
            N2C[i - 'A' + 10] = (char) i;
        }
        for (int i = 'a'; i <= 'z'; i++) {
            C2N[i] = i - 'a' + 36;
            N2C[i - 'a' + 36] = (char) i;
        }
    }

    /**
     * 观察将数转换成所需进制的过程，发现确定每一位数时只需要得到这个数除以所需进制数的余数即可。
     * 一个数不论以何种进制表示，其除以一个数的余数都是一样的。
     * 所以可以直接将某进制的数除以另一进制的进制数，以此得到余数，构建所需进制的数。
     */
    public void convert() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            int T = Integer.parseInt(in.readLine());
            while (T-- > 0) {
                String[] s = in.readLine().split(" ");
                int inBase = Integer.parseInt(s[0]);
                int outBase = Integer.parseInt(s[1]);
                String str = s[2];

                System.out.println(inBase + " " + str);
                if (inBase == outBase) {
                    System.out.println(inBase + " " + str);
                } else {
                    StringBuilder sb = new StringBuilder();
                    int[] num = new int[str.length()];
                    for (int i = 0; i < str.length(); i++) {
                        num[i] = C2N[str.charAt(i)];
                    }
                    int start = 0;
                    while (start < num.length) {
                        /*
                        除法有余位的两种情况：
                        1. 31/2：3/2 余 1，1 给到后一位得到 1*10+1=11，所以后一位的除法就是 11/2
                        2. 15/2：1/2 余 1，1 给到后一位得到 1*10+5=15，所以后一位的除法就是 15/2
                         */
                        int remainder = 0;
                        for (int i = 0; i < num.length; i++) {
                            int quotient = (remainder * inBase + num[i]) / outBase;
                            remainder = (remainder * inBase + num[i]) % outBase;
                            num[i] = quotient;
                        }
                        while (start < num.length && num[start] == 0) {
                            start++;
                        }
                        sb.append(N2C[remainder]);
                    }
                    System.out.println(outBase + " " + sb.reverse());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvert() {
        test(this::convert);
    }
}
