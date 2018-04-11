package com.example.fp;

import java.util.Objects;

/**
 * Created by ZJK on 2018/1/9.
 */
public interface Consumer3<T1, T2, T3> {

    void accept(T1 t1, T2 t2, T3 t3);

    default Consumer3<T1, T2, T3> andThen(Consumer3<? super T1,? super T2, ? super T3> after) {
        Objects.requireNonNull(after);

        return (l1, l2, l3) -> {
            accept(l1, l2, l3);
            after.accept(l1, l2, l3);
        };
    }

}
