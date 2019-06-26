package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;
import com.danielpuiu.swing.inflater.util.StringUtil;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.danielpuiu.swing.inflater.type.TypeConverter.convertValues;
import static com.danielpuiu.swing.inflater.util.ObjectUtil.cast;

public class BorderConversion implements TypeConversion<Border> {

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Border.class.getName(), Border.class.getSimpleName(), "border");
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
                return cast(method.invoke(null, convertValues(packageProvider, method.getGenericParameterTypes(), arguments)));
            } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                // nothing to do
            }
        }

        return null;
    }
}
