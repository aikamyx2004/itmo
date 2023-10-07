package info.kgeorgiy.ja.mukhtarov.implementor;

import info.kgeorgiy.java.advanced.implementor.JarImpler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Generates class implementation by {@link Class type token} of class or interface.
 *
 * @see info.kgeorgiy.java.advanced.implementor.Impler
 * @see info.kgeorgiy.java.advanced.implementor.JarImpler
 */
public class Implementor implements JarImpler {
    /**
     * DELETE_VISITOR is SimpleFileVisitor for deleting temporary
     * class implementation and its compiled file after creating jar file
     *
     * @see #implementJar(Class, Path)
     */
    private static final SimpleFileVisitor<Path> DELETE_VISITOR = new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /**
     * default constructor of Implementor
     */
    public Implementor(){

    }

    /**
     * Runs {@link  #implement(Class, Path)} or {@link #implementJar(Class, Path)} depends on {@code args}.
     * <p>
     * If first argument is {@code "-jar"}, runs {@link #implementJar(Class, Path)},
     * otherwise runs {@link  #implement(Class, Path)}.
     * </p>
     *
     * @param args command line arguments. It can be
     *             {@code [-jar] <implementing class> <path where the class would be written>}
     * @see #implement(Class, Path)
     * @see #implementJar(Class, Path)
     */
    public static void main(String[] args) {
        if (args == null || !(args.length == 2 || args.length == 3) || args[0] == null || args[1] == null
                || (args.length == 3 && args[2] == null && args[0].equals("-jar"))) {
            System.err.println("Usage: java Implementor [-jar] <className> <path>");
            return;
        }

        try {
            Class<?> token = Class.forName(args[args.length - 2]);
            Path root = Path.of(args[args.length - 1]);
            if (args.length == 2) {
                new Implementor().implement(token, root);
            } else {
                new Implementor().implementJar(token, root);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("There is no such class " + e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("No such path: " + e.getMessage());
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @throws ImplerException If token class couldn't be implemented
     *                         or if error occurred while writing to file.
     *                         Class can`t be implemented if
     *                         <ol>
     *                         <li> it is primitive or enum/</li>
     *                         <li> class is final or private</li>
     *                         <li> it is utility class</li>
     *                         </ol>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        int modifiers = token.getModifiers();
        checkClass(token.isPrimitive(), "primitive");
        checkClass(token == Enum.class, "enum");
        checkClass(Modifier.isFinal(modifiers), "final");
        checkClass(Modifier.isPrivate(modifiers), "private");
        checkClass(!token.isInterface() &&
                        Arrays.stream(token.getDeclaredConstructors())
                                .allMatch(c -> Modifier.isPrivate(c.getModifiers())),
                "utility class");

        Path path = getJavaPath(token, root);
        createDirectories(path);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            try {
                new CodeWriter(writer, token).writeClassCode();
            } catch (IOException e) {
                throw new ImplerException("Could not write to class file", e);
            }
        } catch (IOException e) {
            throw new ImplerException("Could not create class file", e);
        }
    }

    /**
     * Throws {@link ImplerException} if {@code isBadClass} is true
     *
     * @param isBadClass put here true if class is bad
     * @param message    String that would be in exception message
     * @throws ImplerException with {@code message}
     */
    private void checkClass(boolean isBadClass, String message) throws ImplerException {
        if (isBadClass) {
            throw new ImplerException("Cant implement or extend class, because it is " + message);
        }
    }

    /**
     * Creates a {@code path} directory by creating all nonexistent parent directories first
     *
     * @param path directory path
     */
    private void createDirectories(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            // no operations
        }
    }

    /**
     * Realization of {@link info.kgeorgiy.java.advanced.implementor.Impler#implement(Class, Path)}
     * {@see info.kgeorgiy.java.advanced.implementor.Impler#implement}
     *
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     *                                                                 generated.
     *                                                                 It can happen if couldn't:
     *                                                                 <ol>
     *                                                                 <li>create temporary directory where should be
     *                                                                 class implementation and its compiled file</li>
     *                                                                 <li>implement class</li>
     *                                                                 <li>find java compiler</li>
     *                                                                 <li>find classpath of token</li>
     *                                                                 <li>write to given path</li>
     *                                                                 </ol>
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        createDirectories(jarFile);

        Path tempDir;
        try {
            tempDir = jarFile.toAbsolutePath().getParent().resolve("ImplementorTemp");
            Files.createDirectories(tempDir);
        } catch (IOException e) {
            throw new ImplerException("Could not create temp directory", e);
        }
        try {
            implement(token, tempDir);
            compile(token, tempDir);
            createJar(token, tempDir, jarFile);
            // :NOTE: в случае ошибки временная директория не удалится
            deleteTempDirectory(tempDir);
        } catch (IOException e) {
            throw new ImplerException("Could not create jar file", e);
        }
    }

    /**
     * Compiles generated class code by {@link #implement(Class, Path)}
     *
     * @param token type token of class for compilation.
     * @param root  path to directory with class code
     * @throws ImplerException when couldn't find java compiler or if compilation failed
     */
    private void compile(Class<?> token, Path root) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Could not find java compiler, include tools.jar to classpath");
        }
        String classpath = getClassPath(token);
        String[] args = new String[]{getJavaPath(token, root).toString(), "-encoding", "UTF-8", "-cp", classpath};
        int exitCode = compiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new ImplerException("Code compiled with errors");
        }
    }

    /**
     * returns classpath of token to compile file
     *
     * @param token type token which
     * @return string with class path
     * @throws ImplerException if couldn't find classpath
     */
    private String getClassPath(Class<?> token) throws ImplerException {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new ImplerException("Could not find classpath", e);
        }
    }

    /**
     * Returns class path with java extension with given root. In path every dot replaced by
     * {@link File#separatorChar}, that depends on system.
     *
     * @param token type token of which classpath would return
     * @param root  type token of which classpath would return
     * @return class path with java extension
     */
    private Path getJavaPath(Class<?> token, Path root) {
        return root.resolve(getPath(token, "java"));
    }

    /**
     * Returns classpath with java extension. In path every dot replaced by
     * {@link File#separatorChar}, that depends on system.
     *
     * @param token     type token of which classpath would return
     * @param extension extension with which will be appended to classpath
     * @return class path with given extension
     */
    private Path getPath(Class<?> token, String extension) {
        return Path.of(token.getPackageName().replace('.', File.separatorChar))
                .resolve(token.getSimpleName() + "Impl." + extension);
    }

    /**
     * Creates jar file from compiled file.
     *
     * @param token   type token of class which jar file is needed
     * @param tempDir temporary directory with compiled class file
     * @param jarFile path where jar file needed
     * @throws ImplerException if couldn't create jar file or couldn't write to jar file.
     */
    private void createJar(Class<?> token, Path tempDir, Path jarFile) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

        Path classFile = getPath(token, "class");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            output.putNextEntry(new ZipEntry(classFile.toString().replace(File.separatorChar, '/')));
            try {
                Files.copy(tempDir.resolve(classFile), output);
            } catch (IOException e) {
                throw new ImplerException("could not write to jar file", e);
            }
        } catch (IOException e) {
            throw new ImplerException("could not create writer to write jar file", e);
        }
    }

    /**
     * deletes all temporary directory by {@code tempDir}
     *
     * @param tempDir path to temporary directory
     * @throws IOException if I/O exception occurred in deleting directory
     */
    private void deleteTempDirectory(Path tempDir) throws IOException {
        if (Files.exists(tempDir)) {
            Files.walkFileTree(tempDir, DELETE_VISITOR);
        }
    }
}
