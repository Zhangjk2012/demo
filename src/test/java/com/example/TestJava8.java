package com.example;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ZJK on 2017/9/19.
 */
public class TestJava8 {

    @Test
    public void testStream() {
        List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
        int sum = nums.stream().filter(e -> e != null)
                               .distinct()
                               .mapToInt(e-> e * 2)
                               .skip(2)
                               .limit(4)
                               .peek(System.out::println)
                               .sum();
        System.out.println(sum);
    }

    @Test
    public void testInterfaceMethod() {
        Interface1.staticMethod();
    }

    @Test
    public void testGroup() {
        List<String> items =
                Arrays.asList("apple", "apple", "banana",
                        "apple", "orange", "banana", "papaya");

        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        System.out.println(result);
    }

}
