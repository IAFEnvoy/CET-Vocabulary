package com.iafenvoy.cet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtil {
    public static <T> List<T> sample(List<T> list, int count) {
        if (count > list.size())
            throw new IllegalArgumentException("Count cannot be greater than list size");
        Random random = new Random();
        List<T> reservoir = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            reservoir.add(list.get(i));
        for (int i = count; i < list.size(); i++) {
            int j = random.nextInt(i + 1);
            if (j < count)
                reservoir.set(j, list.get(i));
        }
        return reservoir;
    }
}
