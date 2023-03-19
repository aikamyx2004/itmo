package info.kgeorgiy.ja.mukhtarov.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements GroupQuery {
    private static final Comparator<Student> STUDENT_NAME_COMPARATOR =
            Comparator.comparing(Student::getLastName)
                    .thenComparing(Student::getFirstName)
                    .reversed()
                    .thenComparing(Student::getId);
    private static final Comparator<Group> GROUP_COMPARATOR = Comparator.comparing(Group::getName);

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getStudentInfoListBy(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getStudentInfoListBy(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getStudentInfoListBy(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getStudentInfoListBy(students, s -> s.getFirstName() + " " + s.getLastName());
    }

    private <T> List<T> getStudentInfoListBy(List<Student> students, Function<Student, T> function) {
        return getStudentInfoBy(students, function).toList();
    }

    private <T> Stream<T> getStudentInfoBy(List<Student> students, Function<Student, T> function) {
        return students.stream().map(function);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return getStudentInfoBy(students, Student::getFirstName).collect(Collectors.toSet());
//        return getFirstNames(students).stream().collect(Collectors.toCollection(TreeSet::new));
//        return new TreeSet<>(getFirstNames(students));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.naturalOrder())
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsToListBy(students, Comparator.naturalOrder());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsToListBy(students, STUDENT_NAME_COMPARATOR);
    }

    private List<Student> sortStudentsToListBy(Collection<Student> students, Comparator<Student> comparator) {
        return sortStudentsBy(students, comparator).toList();
    }

    private Stream<Student> sortStudentsBy(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator);
    }

    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsBy(students, Student::getFirstName, name);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsBy(students, Student::getLastName, name);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudentsBy(students, Student::getGroup, group);
    }

    private <T> List<Student> findStudentsBy(Collection<Student> students, Function<Student, T> function, T field) {
        return sortStudentsBy(students, STUDENT_NAME_COMPARATOR)
                .filter(s -> field.equals(function.apply(s))).toList();
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByGroup(students, group).stream()
                .collect(Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(Comparator.naturalOrder())));
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getSortedGroupsBy(students, STUDENT_NAME_COMPARATOR);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getSortedGroupsBy(students, Comparator.naturalOrder());
    }

    private List<Group> getSortedGroupsBy(Collection<Student> students, Comparator<Student> comparator) {
        return getGroups(students.stream().sorted(comparator))
                .sorted(GROUP_COMPARATOR).toList();
    }

    private Stream<Group> getGroups(Stream<Student> students) {
        return students
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet()
                .stream()
                .map(e -> new Group(e.getKey(), e.getValue()));
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getMaxGroupBy(students,
                Comparator.comparingInt(g -> g.getStudents().size()), false);
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getMaxGroupBy(students,
                Comparator.comparingInt(g -> getDistinctFirstNames(g.getStudents()).size()), true);
    }

    private GroupName getMaxGroupBy(Collection<Student> students, Comparator<Group> comparator, boolean reversed) {
        return getGroups(students.stream())
                .max(comparator
                        .thenComparing(reversed ?
                                GROUP_COMPARATOR.reversed() :
                                GROUP_COMPARATOR))
                .map(Group::getName).orElse(null);
    }
}
