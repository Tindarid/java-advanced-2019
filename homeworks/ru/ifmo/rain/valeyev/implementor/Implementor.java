package ru.ifmo.rain.valeyev.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.nio.file.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Implementor implements Impler {
    private static final String PARAMETER = " param";
    private static final String NEW_LINE = System.lineSeparator();
    private static final String BEGIN_BLOCK = " {" + NEW_LINE;
    private static final String END_BLOCK = "}" + NEW_LINE;
    private static final String END_LINE = ";" + NEW_LINE;
    private static final String SPACE = " ";

    private String getName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    private String resolveModifiers(Executable executable) {
        return Modifier.toString(executable.getModifiers() & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT);
    }

    private String resolveParameters(Executable executable) {
        StringBuilder builder = new StringBuilder();
        Class<?>[] parameters = executable.getParameterTypes();
        for (int i = 0; i < parameters.length; ++i) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(parameters[i].getCanonicalName())
                .append(PARAMETER)
                .append(Integer.toString(i));
        }
        return builder.toString();
    }

    private String resolveExceptions(Executable executable) {
        StringBuilder builder = new StringBuilder();
        Class<?>[] exceptions = executable.getExceptionTypes();
        if (exceptions.length != 0) {
            builder.append(" throws ")
                .append(
                    Arrays.stream(exceptions)
                    .map(Class::getCanonicalName)
                    .collect(Collectors.joining(", "))
                );
        }
        return builder.toString();
    }

    private String resolveSignature(Executable executable, Class<?> token) {
        StringBuilder builder = new StringBuilder();
        builder.append(resolveModifiers(executable)).append(SPACE);
        if (token == null) {
            Method method = (Method) executable;
            builder.append(method.getReturnType().getCanonicalName())
                .append(SPACE)
                .append(method.getName());
        } else {
            builder.append(getName(token));
        }
        builder.append("(")
            .append(resolveParameters(executable))
            .append(") ")
            .append(resolveExceptions(executable));
        return builder.toString();
    }

    private String resolveBody(Constructor constructor) {
        StringBuilder builder = new StringBuilder();
        Class<?>[] parameters = constructor.getParameterTypes();
        for (int i = 0; i < parameters.length; ++i) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(PARAMETER).append(Integer.toString(i));
        }
        return builder.toString();
    }

    private String resolveBody(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            String typeName = method.getReturnType().getSimpleName();
            if (typeName.equals("boolean")) {
                return "false";
            } else if (typeName.equals("void")) {
                return "";
            } else {
                return "0";
            }
        } else {
            return "null";
        }
    }

    private String resolveExecutable(Executable executable, Class<?> token) {
        StringBuilder builder = new StringBuilder();
        builder.append(resolveSignature(executable, token)).append(BEGIN_BLOCK);
        if (token == null) {
            builder.append("return ")
                .append(resolveBody((Method) executable));
        } else {
            builder.append("super(")
                .append(resolveBody((Constructor) executable))
                .append(")");
        }
        builder.append(END_LINE).append(END_BLOCK);
        return builder.toString();
    }

    private String resolveHead(Class<?> token) {
        StringBuilder builder = new StringBuilder();
        String packageName = token.getPackageName();
        if (!packageName.isEmpty()) {
            builder.append("package ")
                .append(packageName)
                .append(END_LINE);
        }
        builder.append("public class ")
            .append(getName(token))
            .append(SPACE)
            .append(token.isInterface() ? "implements" : "extends")
            .append(SPACE)
            .append(token.getSimpleName());
        return builder.toString();
    }

    private class TrueMethod {
        private final Method method;
        private final String signature;

        public TrueMethod(Method method) {
            this.method = method;
            this.signature = resolveSignature(method, null);
        }

        public Method getMethod() {
            return method;
        }

        public boolean equals(Object obj) {
            if (obj instanceof TrueMethod) {
                return signature.equals(((TrueMethod) obj).signature);
            }
            return false;
        }

        public int hashCode() {
            return signature.hashCode();
        }
    }

    private void addMethods(Method[] methods, Set<TrueMethod> collection) {
        Arrays.stream(methods)
            .filter(method -> Modifier.isAbstract(method.getModifiers()))
            .map(TrueMethod::new)
            .collect(Collectors.toCollection(() -> collection));
    }

    private Set<TrueMethod> getMethods(Class<?> token) {
        Set<TrueMethod> methods = new HashSet<>();
        addMethods(token.getMethods(), methods);
        while (token != null) {
            addMethods(token.getDeclaredMethods(), methods);
            token = token.getSuperclass();
        }
        return methods;
    }

    private void check(Class<?> token, Path root) throws ImplerException {
        if (token == null || root == null) {
            throw new ImplerException("Required not null arguments");
        }
        if (token.isPrimitive()) {
            throw new ImplerException("Cannot implement: is primitive");
        }
        if (token.isArray()) {
            throw new ImplerException("Cannot implement: is array");
        }
        if (token == Enum.class) {
            throw new ImplerException("Cannot implement: is enum");
        }
        if (Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("Cannot implement: is final class");
        }
    }

    public void implement(Class<?> token, Path root) throws ImplerException {
        check(token, root);
        Path path = root.toAbsolutePath().resolve(token.getCanonicalName().replace(".", File.separator) + "Impl.java");
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(resolveHead(token));
                writer.write(BEGIN_BLOCK);
                for (Constructor constructor : token.getDeclaredConstructors()) {
                    writer.write(resolveExecutable(constructor, token));
                }
                for (TrueMethod trueMethod : getMethods(token)) {
                    writer.write(resolveExecutable(trueMethod.getMethod(), null));
                }
                writer.write(END_BLOCK);
            } catch (IOException e) {
                throw new ImplerException(e.getMessage());
            }
        } catch (IOException e) {
            throw new ImplerException("Cannot create directories for package");
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("Usage: java Implementor [class or interface to implement]");
        } else {
            try {
                (new Implementor()).implement(Class.forName(args[0]), Paths.get("."));
            } catch (ClassNotFoundException e) {
                System.out.println("Cannot locate your class");
            } catch (ImplerException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
