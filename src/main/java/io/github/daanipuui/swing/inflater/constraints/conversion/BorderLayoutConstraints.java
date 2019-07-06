package io.github.daanipuui.swing.inflater.constraints.conversion;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.EnhancedPackageProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConversion;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.awt.BorderLayout.CENTER;

public class BorderLayoutConstraints implements ConstraintsConversion<BorderLayout> {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(BorderLayout.class.getName(), BorderLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, BorderLayout layoutManager, Map<String, String> map) {
        EnhancedPackageProvider packageProvider = new EnhancedPackageProvider(contextProvider);
        packageProvider.addClass(BorderLayout.class);

        String value = map.getOrDefault("position", CENTER);
        return convertConstant(packageProvider, value);
    }
}
