package org.example;

import java.util.Arrays;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // ДЗ часть 2
        CustomLinkedList<Integer> customList1 = new CustomLinkedList<>();

        System.out.println("Добавляем элементы 1, 2, 3 в список:");
        customList1.add(1);
        customList1.add(2);
        customList1.add(3);

        System.out.println("Получаем элемент с индексом 0: " + customList1.get(0));
        System.out.println("Получаем элемент с индексом 1: " + customList1.get(1));
        System.out.println("Получаем элемент с индексом 2: " + customList1.get(2));

        System.out.println("Проверяем, содержит ли список элемент 2: " + customList1.contains(2));
        System.out.println("Проверяем, содержит ли список элемент 4: " + customList1.contains(4));

        System.out.println("Удаляем элемент с индексом 1:");
        customList1.remove(1);
        System.out.println("Теперь элемент с индексом 1: " + customList1.get(1));

        System.out.println("Добавляем элементы из списка [4, 5, 6]:");
        customList1.addAll(Arrays.asList(4, 5, 6));

        System.out.println("Все элементы в списке:");
        for (int i = 0; i < customList1.getSize(); i++) {
            System.out.println("Элемент с индексом " + i + ": " + customList1.get(i));
        }

        System.out.println();
        // ДЗ часть 3


        Stream<Integer> stream = Stream.of(202, 124, 591, 222, 15929);
        CustomLinkedList<Integer> customList2 = stream.reduce(
                new CustomLinkedList<>(),
                (CustomLinkedList<Integer> list, Integer element) -> {
                    list.add(element);
                    return list;
                },
                (list1, list2) -> list1
        );

        System.out.println("Элементы CustomLinkedList:");
        for (int i = 0; i < customList2.getSize(); i++) {
            System.out.println("Элемент с индексом " + i + ": " + customList2.get(i));
        }
    }
}