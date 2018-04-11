package com.example.spring;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * Created by zhangjiangke on 2017/5/9.
 */
@Component
public abstract class WorkerCreator {

    @Lookup
    public abstract TestScope create(int i, int j);


}
