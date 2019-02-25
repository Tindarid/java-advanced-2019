package ru.ifmo.rain.valeyev.student;

import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.StudentGroupQuery;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class StudentDB implements StudentGroupQuery {
    private static final Comparator<Student> nameComparator = Comparator
        .comparing(Student::getLastName, String::compareTo)
        .thenComparing(Student::getLastName, String::compareTo)
        .thenComparing(Student::compareTo);

    private <T extends Collection<String>> T transform(List<Student> students, Function<Student, String> functor, Collector<String, ?, T> collector) {
        return students.stream().map(functor).collect(collector);
    }

    /** Returns student {@link Student#getFirstName() first names}. */
    public List<String> getFirstNames(final List<Student> students) {
        return transform(students, Student::getFirstName, Collectors.toList());
    }

    /** Returns student {@link Student#getLastName() last names}. */
    public List<String> getLastNames(final List<Student> students) {
        return transform(students, Student::getLastName, Collectors.toList());
    }

    /** Returns student {@link Student#getGroup() groups}. */
    public List<String> getGroups(final List<Student> students) {
        return transform(students, Student::getGroup, Collectors.toList());
    }

    /** Returns student {@link Student#getGroup() groups}. ? */ 
    public List<String> getFullNames(final List<Student> students) {
        return transform(students, student -> student.getFirstName() + " " + student.getLastName(), Collectors.toList());
    }

    /** Returns distinct student {@link Student#getFirstName() first names} in alphabetical order. */
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        return transform(students, Student::getFirstName, Collectors.toCollection(TreeSet::new));
    }

    /** Returns name of the student with minimal {@link Student#getId() id}. */
    public String getMinStudentFirstName(final List<Student> students) {
        return students.stream().min(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    private List<Student> sort(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    /** Returns list of students sorted by {@link Student#getId() id}. */
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sort(students, Student::compareTo);
    }

    /**
     * Returns list of students sorted by name
     * (students are ordered by {@link Student#getLastName() lastName},
     * students with equal last names are ordered by {@link Student#getFirstName() firstName},
     * students having equal both last and first names are ordered by {@link Student#getId() id}.
     */
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sort(students, nameComparator);
    }

    private Stream<Student> filter(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream().filter(predicate);
    }

    private List<Student> filterAndSort(Collection<Student> students, Predicate<Student> predicate) {
        return filter(students, predicate).sorted(nameComparator).collect(Collectors.toList());
    }

    /** Returns list of students having specified first name. Students are ordered by name. */
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return filterAndSort(students, student -> student.getFirstName().equals(name));
    }

    /** Returns list of students having specified last name. Students are ordered by name. */
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return filterAndSort(students, student -> student.getLastName().equals(name));
    }

    /** Returns list of students having specified groups. Students are ordered by name. */
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return filterAndSort(students, student -> student.getGroup().equals(group));
    }

    /** Returns map of group's student last names mapped to minimal first name. */
    public Map<String, String> findStudentNamesByGroup(final Collection<Student> students, final String group) {
        return filter(students, student -> student.getGroup().equals(group))
            .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }

    private Stream<Map.Entry<String, List<Student>>> grouping(Collection<Student> students) {
        return students.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream();
    }

    private List<Group> sortedGroups(Collection<Student> students, Function<List<Student>, List<Student>> functor) {
        return grouping(students).map(entry -> new Group(entry.getKey(), functor.apply(entry.getValue()))).collect(Collectors.toList());
    }

    /** Returns student groups, where both groups and students within a group are ordered by name. */
    public List<Group> getGroupsByName(Collection<Student> students) {
        return sortedGroups(students, this::sortStudentsByName);
    }

    /** Returns student groups, where groups are ordered by name, and students within a group are ordered by id. */
    public List<Group> getGroupsById(Collection<Student> students) {
        return sortedGroups(students, this::sortStudentsById);
    }

    public String largestGroupName(Collection<Student> students, Function<List<Student>, Integer> functor) {
        return grouping(students)
            .max(Comparator
                    .comparingInt((Map.Entry<String, List<Student>> entry) -> functor.apply(entry.getValue()))
                    .thenComparing(Map.Entry::getKey, Collections.reverseOrder(String::compareTo)))
            .map(Map.Entry::getKey).orElse("");
    }

    /**
     * Returns name of the group containing maximum number of students.
     * If there are more than one largest group, the one with smallest name is returned.
     */
    public String getLargestGroup(Collection<Student> students) {
        return largestGroupName(students, list -> list.size());
    }

    /**
     * Returns name of the group containing maximum number of students with distinct first names.
     * If there are more than one largest group, the one with smallest name is returned.
     */
    public String getLargestGroupFirstName(Collection<Student> students) {
        return largestGroupName(students, list -> getDistinctFirstNames(list).size());
    }
}