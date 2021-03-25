# 1. 二分查找真的简单吗？<sup id="a1">[\[1\]](#f1)</sup>

二分查找真的很简单吗？并不简单。看看 Knuth 大佬（发明 KMP 算法的那位）怎么说的：
> Although the basic idea of binary search is comparatively straightforward, the details can be surprisingly tricky...

这句话可以这样理解：**思路很简单，细节是魔鬼**。

本文就来探究几个最常用的二分查找场景：寻找一个数、寻找左侧边界、寻找右侧边界。
而且，我们就是要深入细节，比如不等号是否应该带等号，`mid` 是否应该加一等等。分析这些细节的差异以及出现这些差异的原因，
保证你能灵活准确地写出正确的二分查找算法。

# 2. 二分查找框架

```java
int binarySearch(int[] nums, int target) {
    int left = 0, right = ...;

    while(...) {
        int mid = (right + left) / 2;
        if (nums[mid] == target) {
            ...
        } else if (nums[mid] < target) {
            left = ...
        } else if (nums[mid] > target) {
            right = ...
        }
    }
    return ...;
}
```

**分析二分查找的一个技巧是：不要出现 `else`，而是把所有情况用 `else if` 写清楚，这样可以清楚地展现所有细节**。
本文都会使用 `else if`，旨在讲清楚，读者理解后可自行简化。

其中 `...` 标记的部分，就是可能出现细节问题的地方，当你见到一个二分查找的代码时，首先注意这几个地方。
后文用实例分析这些地方能有什么样的变化。

另外声明一下，计算 `mid` 时需要技巧防止溢出，本文暂时忽略这个问题。

# 3. 寻找一个数（基本的二分搜索）

这个场景是最简单的，可能也是大家最熟悉的，即搜索一个数，如果存在，返回其索引，否则返回 -1。
```java
int binarySearch(int[] nums, int target) {
    int left = 0; 
    int right = nums.length - 1; // 注意

    while(left <= right) { // 注意
        int mid = (right + left) / 2;
        if(nums[mid] == target)
            return mid; 
        else if (nums[mid] < target)
            left = mid + 1; // 注意
        else if (nums[mid] > target)
            right = mid - 1; // 注意
        }
    return -1;
}
```

## 3.1 为什么 while 循环的条件中是 <=，而不是 < ？

因为初始化 `right` 的赋值是 `nums.length - 1`，即最后一个元素的索引，而不是 `nums.length`。
这二者可能出现在不同功能的二分查找中，区别是：前者相当于两端都闭区间 `[left, right]`，后者相当于左闭右开区间 `[left, right)`，
因为索引大小为 `nums.length` 是越界的。

我们这个算法中使用的是 `[left, right]` 两端都闭的区间。**这个区间就是每次进行搜索的区间，我们不妨称为「搜索区间」**。

什么时候应该停止搜索呢？当然，找到了目标值的时候可以终止：
```java
if(nums[mid] == target)
    return mid; 
```

但如果没找到，就需要 `while` 循环终止，然后返回 -1。那 `while` 循环什么时候应该终止？**搜索区间为空的时候应该终止**，
意味着你没得找了，就等于没找到嘛。

`while(left <= right)` 的终止条件是 `left == right + 1`，写成区间的形式就是 `[right + 1, right]`，
或者带个具体的数字进去 `[3, 2]`，可见这时候搜索区间为空，因为没有数字既大于等于 3 又小于等于 2 的吧。
所以这时候 `while` 循环终止是正确的，直接返回 -1 即可。

`while(left < right)` 的终止条件是 `left == right`，写成区间的形式就是 `[right, right]`，
或者带个具体的数字进去 `[2, 2]`，这时候搜索区间非空，还有一个数 2，但此时 `while` 循环终止了。
也就是说这区间 `[2, 2]` 被漏掉了，索引 2 没有被搜索，如果这时候直接返回 -1 就可能出现错误。

当然，如果你非要用 `while(left < right)` 也可以，我们已经知道了出错的原因，就打个补丁好了：
```java
while(left < right) {
    // ...
}
return nums[left] == target ? left : -1;
```

## 3.2 为什么 left = mid + 1，right = mid - 1？我看有的代码是 right = mid 或者 left = mid，到底怎么判断？

刚才明确了「搜索区间」这个概念，而且本算法的搜索区间是两端都闭的，即 `[left, right]`。
那么当我们发现索引 `mid` 不是要找的 `target` 时，如何确定下一步的搜索区间呢？

当然是去搜索 `[left, mid - 1]` 或者 `[mid + 1, right]` 对不对？因为 `mid` 已经搜索过，应该从搜索区间中去除。

# 4. 寻找左侧边界的二分搜索

直接看代码，其中的标记是需要注意的细节：
```java
int left_bound(int[] nums, int target) {
    if (nums.length == 0) return -1;
    int left = 0;
    int right = nums.length; // 注意

    while (left < right) { // 注意
        int mid = (left + right) / 2;
        if (nums[mid] == target) {
            right = mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else if (nums[mid] > target) {
            right = mid; // 注意
        }
    }
    return left;
}
```

## 4.1 为什么 while(left < right) 而不是 <= ?

用相同的方法分析，因为初始化 `right = nums.length` 而不是 `nums.length - 1` 。
因此每次循环的「搜索区间」是 `[left, right)` 左闭右开。

`while(left < right)` 终止的条件是 `left == right`，此时搜索区间 `[left, left)` 恰巧为空，所以可以正确终止。

## 4.2 为什么没有返回 -1 的操作？如果 nums 中不存在 target 这个值，怎么办？

因为要一步一步来，先理解一下这个「左侧边界」有什么特殊含义：

![左侧边界][left-bound]

对于这个数组，算法会返回 1。这个 1 的含义可以这样解读：`nums` 中小于 2 的元素有 1 个。

比如对于有序数组 `nums = [2,3,5,7]`, `target = 1`，算法会返回 0，含义是：`nums` 中小于 1 的元素有 0 个。
如果 `target = 8`，算法会返回 4，含义是：`nums` 中小于 8 的元素有 4 个。

综上可以看出，函数的返回值（即 `left` 变量的值）取值区间是闭区间 `[0, nums.length]`，
所以我们简单添加两行代码就能在正确的时候 `return -1`：
```java
while (left < right) {
    //...
}
// target 比所有数都大
if (left == nums.length) return -1;
// 类似之前算法的处理方式
return nums[left] == target ? left : -1;
```

## 4.3 为什么 left = mid + 1，right = mid ？和之前的算法不一样？

这个很好解释，因为我们的「搜索区间」是 `[left, right)` 左闭右开，所以当 `nums[mid]` 被检测之后，
下一步的搜索区间应该去掉 `mid` 分割成两个区间，即 `[left, mid)` 或 `[mid + 1, right)`。

## 4.4 为什么该算法能够搜索左侧边界？

关键在于对于 `nums[mid] == target` 这种情况的处理：
```java
if (nums[mid] == target)
    right = mid;
```

可见，找到 `target` 时不要立即返回，而是缩小「搜索区间」的上界 `right`，在区间 `[left, mid)` 中继续搜索，即不断向左收缩，
达到锁定左侧边界的目的。

# 5. 寻找右侧边界的二分查找

寻找右侧边界和寻找左侧边界的代码差不多，只有两处不同，已标注：
```java
int right_bound(int[] nums, int target) {
    if (nums.length == 0) return -1;
    int left = 0, right = nums.length;

    while (left < right) {
        int mid = (left + right) / 2;
        if (nums[mid] == target) {
            left = mid + 1; // 注意
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else if (nums[mid] > target) {
            right = mid;
        }
    }
    return left - 1; // 注意
}
```

## 5.1 为什么这个算法能够找到右侧边界？

类似地，关键点还是这里：
```java
if (nums[mid] == target) {
    left = mid + 1;
```

当 `nums[mid] == target` 时，不要立即返回，而是增大「搜索区间」的下界 `left`，使得区间不断向右收缩，达到锁定右侧边界的目的。

## 5.2 为什么最后返回 left - 1 而不是 left？

首先，`while` 循环的终止条件是 `left == right`，所以 `left` 和 `right` 是一样的，你非要体现右侧的特点，返回 `right - 1` 好了。

至于为什么要减一，这是搜索右侧边界的一个特殊点，关键在这个条件判断：
```java
if (nums[mid] == target) {
    left = mid + 1;
    // 这样想: mid = left - 1
```

![右侧边界][right-bound]

因为我们对 `left` 的更新必须是 `left = mid + 1`，就是说 `while` 循环结束时，`nums[left]` 一定不等于 `target` 了，
而 `nums[left - 1]` 可能是 `target`。至于为什么 `left` 的更新必须是 `left = mid + 1`，同左侧边界搜索，就不再赘述。

## 5.3 为什么没有返回 -1 的操作？如果 nums 中不存在 target 这个值，怎么办？

类似之前的左侧边界搜索，因为 `while` 的终止条件是 `left == right`，就是说 `left` 的取值范围是 `[0, nums.length]`，
所以可以添加两行代码，正确地返回 -1：
```java
while (left < right) {
    // ...
}
if (left == 0) return -1;
return nums[left-1] == target ? (left-1) : -1;
```

# 6. 总结

第一个，最基本的二分查找算法：
```
因为我们初始化 right = nums.length - 1
所以决定了我们的「搜索区间」是 [left, right]
所以决定了 while (left <= right)
同时也决定了 left = mid+1 和 right = mid-1

因为我们只需找到一个 target 的索引即可
所以当 nums[mid] == target 时可以立即返回
```

第二个，寻找左侧边界的二分查找：
```
因为我们初始化 right = nums.length
所以决定了我们的「搜索区间」是 [left, right)
所以决定了 while (left < right)
同时也决定了 left = mid+1 和 right = mid

因为我们需找到 target 的最左侧索引
所以当 nums[mid] == target 时不要立即返回
而要收紧右侧边界以锁定左侧边界
```

第三个，寻找右侧边界的二分查找：
```
因为我们初始化 right = nums.length
所以决定了我们的「搜索区间」是 [left, right)
所以决定了 while (left < right)
同时也决定了 left = mid+1 和 right = mid

因为我们需找到 target 的最右侧索引
所以当 nums[mid] == target 时不要立即返回
而要收紧左侧边界以锁定右侧边界

又因为收紧左侧边界时必须 left = mid + 1
所以最后无论返回 left 还是 right，必须减一
```

# 7. 「循环不变式」论证法

除了上面的分析方法，我们还可以使用数学的方式更形式化地论证。具体参见下面三个题解：
 - [BinarySearch.java][b1]
 - [BinarySearchII.java][b2]
 - [BinarySearchIII.java][b3]


[b1]: ../../learn/binarysearch/BinarySearch.java
[b2]: ../../learn/binarysearch/BinarySearchII.java
[b3]: ../../learn/binarysearch/BinarySearchIII.java

[left-bound]: ../../../res/img/binarysearch-left-bound.png
[right-bound]: ../../../res/img/binarysearch-right-bound.png

<b id="f1">\[1\]</b> https://mp.weixin.qq.com/s/uA2suoVykENmCQcKFMOSuQ [↩](#a1)  