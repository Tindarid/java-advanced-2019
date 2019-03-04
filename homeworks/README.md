# Solutions

## HW1
Класс RecursiveWalk, осуществляющий подсчет хеш-сумм файлов в директориях.

Формат запуска: ```java RecursiveWalk <входной файл> <выходной файл>```

Формат строки в выходном файле: ```<шестнадцатеричная хеш-сумма> <путь к файлу>```

Используется алгоритм [FNV](https://ru.wikipedia.org/wiki/FNV.html).

Кодировка входного и выходного файлов — UTF-8.

## HW2
Класс ArraySet, реализующий неизменяемое упорядоченное множество (интерфейс [NavigableSet](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/NavigableSet.html)).

Все операции над множествами производятся с максимально возможной асимптотической эффективностью.

## HW3
Класс StudentDB, осуществляющий поиск по базе студентов (реализует интерфейс [StudentGroupQuery](../modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentGroupQuery.java))

Каждый метод состоит из одного оператора ([использование потоков и лямбда-выражений](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html)).

