package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.EnhancedPackageProvider;
import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;
import io.github.daanipuui.swing.inflater.type.TypeConverter;

import java.awt.Font;
import java.util.Collections;
import java.util.List;

public class FontConversion implements TypeConversion<Font> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Font.class);
    }

    @Override
    public Font convertLiteral(PackageProvider packageProvider, String value) {
        String[] values = value.split(",");
        if (values.length == 3) {
            EnhancedPackageProvider enhancedPackageProvider = new EnhancedPackageProvider(packageProvider);
            enhancedPackageProvider.addClass(Font.class);

            String name = TypeConverter.convert(enhancedPackageProvider, String.class, values[0].trim());
            int style = TypeConverter.convert(enhancedPackageProvider, int.class, values[1].trim());
            int size = TypeConverter.convert(enhancedPackageProvider, int.class, values[2].trim());
            return new Font(name, style, size);
        }
        return convertLiteral(Font::getFont, value);
    }
}
