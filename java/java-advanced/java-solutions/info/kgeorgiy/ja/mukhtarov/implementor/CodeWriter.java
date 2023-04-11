package info.kgeorgiy.ja.mukhtarov.implementor;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for writing class code in {@link Implementor#implement(Class, Path)}
 */
public class CodeWriter {

    /**
     * Writer using for printing generated code
     */
    private final Writer writer;
    /**
     * type token of class to create implementation
     */
    private final Class<?> token;

    /**
     * Creates CodeWriter with given writer and token
     *
     * @param writer for printing generated code
     * @param token  type token of class to create implementation
     */
    public CodeWriter(Writer writer, Class<?> token) {
        this.token = token;
        this.writer = writer;
    }

    /**
     * Writes class code
     *
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    public void writeClassCode() throws IOException {
        writePackage();
        writeClassInfo();
        writeConstructors();
        writeMethods();
        write(String.format("}%n"));
    }

    /**
     * Prints given string with writer
     *
     * @param someCode code to write
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void write(String someCode) throws IOException {
        writer.write(someCode);
    }

    /**
     * Returns string with {@code count} tabs.
     * <p>
     * Here tab is 4 whitespaces
     * </p>
     *
     * @param count numbers of tabs you need
     * @return string with string with {@code count} tabs
     */
    private String tab(int count) {
        return " ".repeat(4 * count);
    }

    /**
     * Write package of given class
     *
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    void writePackage() throws IOException {
        String packageName = token.getPackageName();
        if (!packageName.isEmpty()) {
            write(String.format("package %s;%n%n", packageName));
        }
    }

    /**
     * Writes class declaration.
     * <p>
     * implements given token if it is interface and extends if it is class
     * </p>
     *
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void writeClassInfo() throws IOException {
        write(String.format("public class %s %s %s {%n",
                getClassName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName()));
    }

    /**
     * Writes not private constructors of given class
     *
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void writeConstructors() throws IOException {
        for (Constructor<?> ctor : getConstructors()) {
            writeConstructor(ctor);
        }
    }

    /**
     * Takes not private constructors of given class and collect it to {@link List}.
     * <p>Here is used {@link Class#getDeclaredConstructors()} to take all constructors</p>
     *
     * @return {@link List} of not private constructors of given class
     * @see Class#getDeclaredConstructors()
     */
    private List<Constructor<?>> getConstructors() {
        return Arrays.stream(token.getDeclaredConstructors())
                .filter(ctor -> !Modifier.isPrivate(ctor.getModifiers())).toList();
    }

    /**
     * Prints code of given constructor to writer
     *
     * @param ctor constructor of class
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void writeConstructor(Constructor<?> ctor) throws IOException {
        write(String.format("%s%s %s(%s)%s {%n%ssuper(%s);%n%s}%n%n",
                tab(1),
                getModifier(ctor, Modifier.constructorModifiers()),
                getClassName(),
                getParameters(ctor, true),
                getThrows(ctor),
                tab(2),
                getParameters(ctor, false),
                tab(1))
        );
    }

    /**
     * Returns the simple name of given class with "Impl" ending
     *
     * @return Returns string with class name
     */
    private String getClassName() {
        return token.getSimpleName() + "Impl";
    }

    /**
     * Return string with modifier of given {@code executable} which is method or constructor
     *
     * @param executable    method or constructor which modifier you need
     * @param execModifiers every modifier that can appear in given {@link Executable}
     * @return modifier of {@code executable} which is method or constructor
     */
    private String getModifier(Executable executable, int execModifiers) {
        return Modifier.toString(executable.getModifiers() & ~Modifier.ABSTRACT & execModifiers);
    }

    /**
     * Returns empty string if {@code executable} doesn't throw exceptions and
     * string with exceptions that throws {@code executable}.
     * <p>
     * If {@code executable} throws Exception1, Exception2...
     * it returns " throws Exception1, Exception2..."
     * </p>
     *
     * @param executable method or constructor of which string with exceptions you need
     * @return string with exceptions
     */
    private String getThrows(Executable executable) {
        Class<?>[] exceptions = executable.getExceptionTypes();
        if (exceptions.length == 0) {
            return "";
        }
        return " throws " + Arrays.stream(exceptions)
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(", "));
    }

    /**
     * Writes not private methods of given class
     *
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void writeMethods() throws IOException {
        for (Method m : getMethods()) {
            writeMethod(m);
        }
    }

    /**
     * Takes not private methods of given class and collect it to {@link List}.
     * <p>Here is used {@link Class#getMethods()} and {@link Class#getDeclaredMethods()} to take methods</p>
     *
     * @return {@link List} of not private methods of given class
     * @see Class#getDeclaredMethods()
     * @see Class#getMethods()
     */
    private Set<Method> getMethods() {
        return Stream.concat(Arrays.stream(token.getMethods()),
                        Arrays.stream(token.getDeclaredMethods())
                                .filter(m -> !Modifier.isPrivate(m.getModifiers())))
                .filter(m -> Modifier.isAbstract(m.getModifiers())).collect(Collectors.toSet());
    }

    /**
     * Prints code of given method to writer
     *
     * @param method method of class
     * @throws IOException if {@link Writer#write(String)} threw exception
     */
    private void writeMethod(Method method) throws IOException {
        write(String.format("%s%s %s %s(%s)%s {%n%s%s}%n%n",
                tab(1),
                getModifier(method, Modifier.methodModifiers()),
                method.getReturnType().getCanonicalName(),
                method.getName(),
                getParameters(method, true),
                getThrows(method),
                getReturn(method),
                tab(1)));
    }

    /**
     * If method is void returns empty string
     * otherwise returns "return + {default value of return object}" + {@link System#lineSeparator()}
     * <p>
     *
     * @param method method of which return string is needed
     * @return empty string if method is void
     * otherwise returns "return " + {@link #getReturnObject(Method)} + ";" +
     * that is returns in method.
     * </p>
     */
    private String getReturn(Method method) {
        if (method.getReturnType().equals(Void.TYPE)) {
            return "";
        }
        return tab(2) + String.format("return %s;%n", getReturnObject(method));
    }

    /**
     * Return default value object for object that return this method.
     * It is false if method returns boolean,
     * for other primitive types 0,
     * and for others it is null.
     *
     * @param method method on which default value would be found
     * @return String with default value of return object of method
     */
    private String getReturnObject(Method method) {
        Class<?> returnType = method.getReturnType();
        if (!returnType.isPrimitive()) {
            return "null";
        }
        if (returnType.equals(boolean.class)) {
            return "false";
        }
        return "0";
    }

    /**
     * Returns parameters of given method or constructors, if needType is false
     * there will be only names of parameters, otherwise there will be pairs type and name.
     *
     * @param executable method or constructor from which you need parameters
     * @param needType   if it is false method returns only parameter names
     *                   and pairs type and name otherwise
     * @return Returns parameters of given method or constructors
     */
    private String getParameters(Executable executable, boolean needType) {
        return Arrays.stream(executable.getParameters())
                .map(c -> (needType ? (c.getType().getCanonicalName() + ' ') : "")
                        + c.getName())
                .collect(Collectors.joining(", "));
    }
}