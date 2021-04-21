package training.design;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，能够看见关注人（包括自己）
 * 的最近十条推文。你的设计需要支持以下的几个功能：
 * 1. postTweet(userId, tweetId): 创建一条新的推文
 * 2. getNewsFeed(userId): 检索最近的「十条」推文。每个推文都必须是由此用户关注的人或者是用户自己发出的。
 * 推文必须「按照时间顺序由最近」的开始排序。
 * 3. follow(followerId, followeeId): 关注一个用户
 * 4. unfollow(followerId, followeeId): 取消关注一个用户
 * <p>
 * 例 1：
 * Twitter twitter = new Twitter();
 * // 用户1发送了一条新推文 (用户id = 1, 推文id = 5).
 * twitter.postTweet(1, 5);
 * // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 * twitter.getNewsFeed(1);
 * // 用户1关注了用户2.
 * twitter.follow(1, 2);
 * // 用户2发送了一个新推文 (推文id = 6).
 * twitter.postTweet(2, 6);
 * // 用户1的获取推文应当返回一个列表，其中包含两个推文，id分别为 -> [6, 5].
 * // 推文id6应当在推文id5之前，因为它是在5之后发送的.
 * twitter.getNewsFeed(1);
 * // 用户1取消关注了用户2.
 * twitter.unfollow(1, 2);
 * // 用户1的获取推文应当返回一个列表，其中包含一个id为5的推文.
 * // 因为用户1已经不再关注用户2.
 * twitter.getNewsFeed(1);
 */
public class E355_Medium_DesignTwitter {

    static void test(Supplier<ITwitter> factory) {
        ITwitter twitter = factory.get();
        twitter.postTweet(1, 5);
        assertEquals(singletonList(5), twitter.getNewsFeed(1));
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        assertEquals(Arrays.asList(6, 5), twitter.getNewsFeed(1));
        twitter.unfollow(1, 2);
        assertEquals(singletonList(5), twitter.getNewsFeed(1));
    }

    @Test
    void testTwitter() {
        test(Twitter::new);
    }
}

interface ITwitter {

    void postTweet(int userId, int tweetId);

    List<Integer> getNewsFeed(int userId);

    void follow(int followerId, int followeeId);

    void unfollow(int followerId, int followeeId);
}

/**
 * LeetCode 耗时：13 ms - 81.32%
 *          内存消耗：36.7 MB - 91.95%
 */
class Twitter implements ITwitter {

    private static class Node implements Comparable<Node> {
        private static int timeGenerator = Integer.MAX_VALUE;

        final int tweetId;
        final int time;
        Node next;

        Node(int tweetId) {
            this.tweetId = tweetId;
            // 让时间最新的 time 最小
            time = timeGenerator--;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(time, o.time);
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }
    }

    private Map<Integer, Node> timelines;
    private Map<Integer, Set<Integer>> followed;

    public Twitter() {
        timelines = new HashMap<>();
        followed = new HashMap<>();
    }

    @Override
    public void postTweet(int userId, int tweetId) {
        timelines.merge(userId, new Node(tweetId), (list, node) -> {
            node.next = list;
            return node;
        });
    }

    @Override
    public List<Integer> getNewsFeed(int userId) {
        Node timeline = timelines.get(userId);
        Set<Integer> follows = followed.get(userId);
        if (timeline == null && (follows == null || follows.isEmpty()))
            return Collections.emptyList();

        TreeSet<Node> filter = new TreeSet<>();
        // 先添加自己和关注者的最新动态。当超过 10 个时，去除时间最晚的推文
        if (timeline != null)
            filter.add(timeline);
        if (follows != null) {
            for (Integer followeeId : follows) {
                if ((timeline = timelines.get(followeeId)) != null)
                    filter.add(timeline);
                if (filter.size() > 10)
                    filter.pollLast();
            }
        }

        // 添加最新的 10 条推文
        List<Integer> result = new ArrayList<>(10);
        while (result.size() < 10 && filter.size() > 0){
            Node node = filter.pollFirst();
            //noinspection ConstantConditions
            result.add(node.tweetId);
            if (node.next != null)
                filter.add(node.next);
        }

        return result;
    }

    @Override
    public void follow(int followerId, int followeeId) {
        followed.computeIfAbsent(followerId, id -> new HashSet<>())
                .add(followeeId);
    }

    @Override
    public void unfollow(int followerId, int followeeId) {
        if (followed.containsKey(followerId))
            followed.get(followerId).remove(followeeId);
    }
}
