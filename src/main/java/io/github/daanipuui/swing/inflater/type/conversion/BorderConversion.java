package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;
import io.github.daanipuui.swing.inflater.util.StringUtil;
import io.github.daanipuui.swing.inflater.util.ObjectUtil;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.daanipuui.swing.inflater.type.TypeConverter.convertValues;

public class BorderConversion implements TypeConversion<Border> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Border.class);
    }

    @Override
    public Border convertLiteral(PackageProvider packageProvider, String value) {
        String[] values = value.split(",");

        String methodName = "create" + StringUtil.capitalize(values[0]) + "Border";
        List<Method> methods = Stream.of(BorderFactory.class.getMethods())
                .filter(method -> methodName.equals(method.getName()))
                .filter(method -> method.getParameterCount() == (values.length - 1))
                .collect(Collectors.toList());

        String[] arguments = Arrays.copyOfRange(values, 1, values.length);
        for (Method method : methods) {
            try {
                return ObjectUtil.cast(method.invoke(null, convertValues(packageProvider, method.getParameterTypes(), arguments)));
            } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                // nothing to do
            }
        }

        return null;
    }
}
