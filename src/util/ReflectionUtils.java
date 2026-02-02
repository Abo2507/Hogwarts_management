package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    public static void inspectClass(Class<?> clazz) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("REFLECTION INSPECTION: " + clazz.getSimpleName());
        System.out.println("=".repeat(60));

        System.out.println("\nFull Class Name: " + clazz.getName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(clazz.getModifiers()));

        if (clazz.getSuperclass() != null) {
            System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("\nImplemented Interfaces:");
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getSimpleName());
            }
        }

        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  - " + Modifier.toString(field.getModifiers()) +
                    " " + field.getType().getSimpleName() +
                    " " + field.getName());
        }

        System.out.println("\nMethods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  - " + Modifier.toString(method.getModifiers()) +
                    " " + method.getReturnType().getSimpleName() +
                    " " + method.getName() + "()");
        }

        System.out.println("\n" + "=".repeat(60));
    }

    public static void inspectObject(Object obj) {
        if (obj == null) {
            System.out.println("Object is null");
            return;
        }

        Class<?> clazz = obj.getClass();
        inspectClass(clazz);

        System.out.println("\nField Values:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                System.out.println("  - " + field.getName() + " = " + value);
            } catch (IllegalAccessException e) {
                System.out.println("  - " + field.getName() + " = <inaccessible>");
            }
        }
    }

    public static void compareClasses(Class<?> class1, Class<?> class2) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPARING CLASSES");
        System.out.println("=".repeat(60));

        System.out.println("\nClass 1: " + class1.getSimpleName());
        System.out.println("Class 2: " + class2.getSimpleName());

        System.out.println("\nIs Class1 superclass of Class2? " + class1.isAssignableFrom(class2));
        System.out.println("Is Class2 superclass of Class1? " + class2.isAssignableFrom(class1));

        System.out.println("\n" + "=".repeat(60));
    }
}