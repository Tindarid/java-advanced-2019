# Homeworks

## HW1 (Input and output)
Класс RecursiveWalk, осуществляющий подсчет хеш-сумм файлов в директориях.

Формат запуска: ```java RecursiveWalk <входной файл> <выходной файл>```

Формат строки в выходном файле: ```<шестнадцатеричная хеш-сумма> <путь к файлу>```

Используется алгоритм [FNV](https://ru.wikipedia.org/wiki/FNV.html).

Кодировка входного и выходного файлов — UTF-8.

## HW2 (Collections Framework)
Класс ArraySet, реализующий неизменяемое упорядоченное множество (интерфейс [NavigableSet](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/NavigableSet.html)).

Все операции над множествами производятся с максимально возможной асимптотической эффективностью.

## HW3 (Streams and lambda expressions)
Класс StudentDB, осуществляющий поиск по базе студентов (реализует интерфейс [StudentGroupQuery](../modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentGroupQuery.java)).

Каждый метод состоит из одного оператора.

## HW4 (Java Reflection)
Класс Implementor, который генерирует реализации классов и интерфейсов (реализует интерфейс [Impler](../modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java)).

Формат запуска: ```java Implementor <class to implement>```

В результате работы будет сгенерирован Java-код класса с суффиксом Impl, расширяющий (реализующий) указанный класс (интерфейс).

Методы сгенерированного класса игнорируют свои аргументы и возвращают значения по умолчанию.
