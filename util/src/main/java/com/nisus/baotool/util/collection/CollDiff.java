package com.nisus.baotool.util.collection;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 计算集合差集, 交集
 *
 * @param <E>
 * @author L&J
 * @date 2021-10-01 21:15:44
 */
@Getter
public class CollDiff<E> {

    /**
     * 集合 1 - 集合 2
     */
    private List<E> onlyCollection1;
    /**
     * 集合 2 - 集合 1
     */
    private List<E> onlyCollection2;
    /**
     * 集合 1 ∩ 集合 2
     */
    private List<E> intersection;
    /**
     * 集合 1
     */
    private Collection<E> collection1;
    /**
     * 集合 2
     */
    private Collection<E> collection2;

    private CollDiff() {}

    /**
     * {@link CollDiff#of(Collection, Collection, Function)} 快捷方式. key 就是元素自身.
     */
    public static <E> CollDiff<E> of(Collection<E> collection1, Collection<E> collection2) {
        return of(collection1, collection2, t -> t);
    }

    /**
     * @param collection1 集合1
     * @param collection2 集合2
     * @param key key 的获取 Function
     * @param <E> 元素类型, 两个集合里元素类型必须一致
     * @param <K> key 的类型
     * @return CollDiff
     */
    public static <E, K> CollDiff<E> of(Collection<E> collection1, Collection<E> collection2, Function<E, K> key) {
        Set<K> set1 = collection1.stream().map(key).collect(Collectors.toSet());
        Set<K> set2 = collection2.stream().map(key).collect(Collectors.toSet());

        List<E> intersection = new ArrayList<>();

        // collection1 - collection2
        List<E> onlyCollection1 = collection1.stream().filter(e -> {
            if (set2.contains(key.apply(e))) { // 交集
                intersection.add(e);
                return false;
            } else return true;
        }).collect(Collectors.toList());

        // collection2 - collection1
        List<E> onlyCollection2 = collection2.stream().filter(e -> !set1.contains(key.apply(e))).collect(Collectors.toList());

        CollDiff<E> collDiff = new CollDiff<>();
        collDiff.onlyCollection1 = onlyCollection1;
        collDiff.onlyCollection2 = onlyCollection2;
        collDiff.intersection = intersection;
        collDiff.collection1 = collection1;
        collDiff.collection2 = collection2;

        return collDiff;
    }



}