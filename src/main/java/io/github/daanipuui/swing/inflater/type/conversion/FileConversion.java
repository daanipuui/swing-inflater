package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileConversion implements TypeConversion<File> {

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(File.class);
    }

    @Override
    public File convertLiteral(PackageProvider packageProvider, String value) {
        return convertLiteral(File::new, value);
    }
}
