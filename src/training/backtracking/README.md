# 1. 简介<sup id="a1">[\[1\]](#f1)</sup>

回溯法（back tracking）（探索与回溯法）是一种选优搜索法，又称为试探法，按选优条件向前搜索，以达到目标。
但当探索到某一步时，发现原先选择并不优或达不到目标，就退回一步重新选择，这种走不通就退回再走的技术为回溯法，
而满足回溯条件的某个状态的点称为**回溯点**。许多复杂的，规模较大的问题都可以使用回溯法，有“通用解题方法”的美称。

废话不多说，直接上回溯算法框架。**解决一个回溯问题，实际上就是一个决策树的遍历过程**。你只需要思考 3 个问题：
1. **路径**：也就是已经做出的选择。
2. **选择列表**：也就是你当前可以做的选择。
3. **结束条件**：也就是到达决策树底层，无法再做选择的条件。

下面就是回溯算法的代码框架：
```python
result = []
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return

    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
```

**其核心就是 `for` 循环里面的递归，在递归调用之前「做选择」，在递归调用之后「撤销选择」**，特别简单。

什么叫做选择和撤销选择呢，这个框架的底层原理是什么呢？下面我们就通过「全排列」和「N 皇后问题」这两个经典的回溯算法问题来解开之前的疑惑，
详细探究一下其中的奥妙！

# 2. 全排列问题

我们在高中的时候就做过排列组合的数学题，我们也知道 `n` 个不重复的数，全排列共有 `n!` 个。
PS：为了简单清晰起见，我们这次讨论的全排列问题不包含重复的数字。

那么我们当时是怎么穷举全排列的呢？比方说给三个数 `[1,2,3]`，你肯定不会无规律地乱穷举，一般是这样：
先固定第一位为 1，然后第二位可以是 2，那么第三位只能是 3；然后可以把第二位变成 3，第三位就只能是 2 了；
然后就只能变化第一位，变成 2，然后再穷举后两位……

其实这就是回溯算法，我们高中无师自通就会用，或者有的同学直接画出如下这棵回溯树：

![全排列回溯树][permutation]

只要从根遍历这棵树，记录路径上的数字，其实就是所有的全排列。我们不妨把这棵树称为**回溯算法的「决策树」**。

为啥说这是决策树呢，因为你在每个节点上其实都在做决策。比如说你站在下图的红色节点上：

![做决策][permutation-decision]

你现在就在做决策，可以选择 1 那条树枝，也可以选择 3 那条树枝。为啥只能在 1 和 3 之中选择呢？
因为 2 这个树枝在你身后，这个选择你之前做过了，而全排列是不允许重复使用数字的。

现在可以解答开头的几个名词：`[2]` 就是「路径」，记录你已经做过的选择；`[1,3]` 就是「选择列表」，表示你当前可以做出的选择；
「结束条件」就是遍历到树的底层，在这里就是选择列表为空的时候。

如果明白了这几个名词，可以把「选择列表」和「路径」作为决策树上每个节点的属性，比如下图列出了几个节点的属性：

![节点属性][permutation-attr]

我们定义的 `backtrack` 函数其实就像一个指针，在这棵树上游走，同时要正确维护每个节点的属性，每当走到树的底层，
其「路径」就是一个全排列。

再进一步，如何遍历一棵树？这个应该不难吧。各种搜索问题其实都是树的遍历问题，而多叉树的遍历框架就是这样：
```java
void traverse(TreeNode root) {
    for (TreeNode child : root.childern)
        // 前序遍历需要的操作
        traverse(child);
        // 后序遍历需要的操作
}
```

而所谓的前序遍历和后序遍历，他们只是两个很有用的时间点，我给你画张图你就明白了：

![前序和后序遍历][permutation-pp]

**前序遍历的代码在进入某一个节点之前的那个时间点执行，后序遍历代码在离开某个节点之后的那个时间点执行**。

回想我们刚才说的，「路径」和「选择」是每个节点的属性，函数在树上游走要正确维护节点的属性，那么就要在这两个特殊时间点搞点动作：

![做选择][permutation-choose]

**我们只要在递归之前做出选择，在递归之后撤销刚才的选择**，就能正确得到每个节点的选择列表和路径。
下面，直接看全排列代码：
```java
List<List<Integer>> res = new LinkedList<>();

/* 主函数，输入一组不重复的数字，返回它们的全排列 */
List<List<Integer>> permute(int[] nums) {
    // 记录「路径」
    LinkedList<Integer> track = new LinkedList<>();
    backtrack(nums, track);
    return res;
}

// 路径：记录在 track 中
// 选择列表：nums 中不存在于 track 的那些元素
// 结束条件：nums 中的元素全都在 track 中出现
void backtrack(int[] nums, LinkedList<Integer> track) {
    // 触发结束条件
    if (track.size() == nums.length) {
        res.add(new LinkedList(track));
        return;
    }

    for (int i = 0; i < nums.length; i++) {
        // 排除不合法的选择
        if (track.contains(nums[i]))
            continue;
        // 做选择
        track.add(nums[i]);
        // 进入下一层决策树
        backtrack(nums, track);
        // 取消选择
        track.removeLast();
    }
}
```

我们这里稍微做了些变通，没有显式记录「选择列表」，而是通过 `nums` 和 `track` 推导出当前的选择列表。
至此，我们就通过全排列问题详解了回溯算法的底层原理。当然，这个算法解决全排列不是很高效，
因为对链表使用 `contains` 方法需要 O(N) 的时间复杂度。有更好的方法通过交换元素达到目的，参见 [E46_Medium_Permutations.java][46]。

但是必须说明的是，不管怎么优化，都符合回溯框架，而且时间复杂度都不可能低于 O(N!)，因为穷举整棵决策树是无法避免的。
**这也是回溯算法的一个特点，不像动态规划存在重叠子问题可以优化，回溯算法就是纯暴力穷举，复杂度一般都很高**。

# 3. N 皇后问题

这个问题很经典了，简单解释一下：给你一个 N×N 的棋盘，让你放置 N 个皇后，使得它们不能互相攻击。
皇后可以攻击同一行、同一列、左上左下右上右下四个方向的任意单位。

这是 `N = 8` 的一种放置方法：

![N 皇后][queen]

这个问题本质上跟全排列问题差不多，**决策树的每一层表示棋盘上的每一行**；每个节点可以做出的选择是，在该行的任意一列放置一个皇后。
直接套用框架:
```java
public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    char[][] chess = new char[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            chess[i][j] = '.';
        }
    }

    dfs(result, 0, chess);

    return result;
}

// 每行放一个皇后。这真是绝妙的点子，这样就不需要考虑很多繁杂的情况了
private void dfs(List<List<String>> result, int row, char[][] chess) {
    final int n = chess.length;
    if (row == n) {
        String[] res = new String[n];
        for (int i = 0; i < n; i++)
            res[i] = new String(chess[i]);
        result.add(Arrays.asList(res));
        return;
    }

    for (int col = 0; col < n; col++) {
        if (isValid(chess, row, col)) {
            chess[row][col] = 'Q';
            dfs(result, row + 1, chess);
            chess[row][col] = '.';
        }
    }
}

private boolean isValid(char[][] chess, int row, int col) {
    // 确保同一列没有皇后
    for (int i = 0; i < row; i++) {
        if (chess[i][col] == 'Q')
            return false;
    }
    // 确保左上方没有皇后
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
        if (chess[i][j] == 'Q')
            return false;
    }
    // 确保右上方没有皇后
    for (int i = row - 1, j = col + 1; i >= 0 && j < chess.length; i--, j++) {
        if (chess[i][j] == 'Q')
            return false;
    }
    return true;
}
```

# 4. 总结

回溯算法就是个多叉树的遍历问题，关键就是在前序遍历和后序遍历的位置做一些操作，算法框架如下：
```python
def backtrack(...):
    for 选择 in 选择列表:
        做选择
        backtrack(...)
        撤销选择
```

写 `backtrack` 函数时，需要维护走过的「路径」和当前可以做的「选择列表」，当触发「结束条件」时，将「路径」记入结果集。

其实想想看，回溯算法和动态规划是不是有点像呢？动态规划的三个需要明确的点就是「状态」「选择」和「base case」，
是不是就对应着走过的「路径」，当前的「选择列表」和「结束条件」？

某种程度上说，动态规划的暴力求解阶段就是回溯算法。只是有的问题具有重叠子问题性质，可以用 dp table 或者备忘录优化，
将递归树大幅剪枝，这就变成了动态规划。而这两个问题，都没有重叠子问题，也就是回溯算法问题了，复杂度非常高是不可避免的。


[46]: E46_Medium_Permutations.java

[permutation]: ../../../res/img/bt-permutation.jpg
[permutation-decision]: ../../../res/img/bt-permutation-decision.jpg
[permutation-attr]: ../../../res/img/bt-permutation-attr.jpg
[permutation-pp]: ../../../res/img/bt-permutation-pp.jpg
[permutation-choose]: ../../../res/img/bt-permutation-choose.jpg
[queen]: ../../../res/img/bt-queen.jpg

<b id="f1">\[1\]</b> https://mp.weixin.qq.com/s/nMUHqvwzG2LmWA9jMIHwQQ [↩](#a1)  