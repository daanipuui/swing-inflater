package com.danielpuiu.swing.inflater.type.conversion;

import com.danielpuiu.swing.inflater.PackageProvider;
import com.danielpuiu.swing.inflater.type.TypeConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Insets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InsetsConversion implements TypeConversion<Insets> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Insets.class);
    }

    @Override
    public Insets convertLiteral(PackageProvider packageProvider, String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        String[] values = value.split(",");
        if (values.length != 4) {
            String errorMessage = String.format("Insets conversion failed: Insets constructor takes 4 values. Given [%d]: [%s]", values.length, Arrays.toString(values));
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        int[] intValues = Arrays.stream(values).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();
        return new Insets(intValues[0], intValues[1], intValues[2], intValues[3]);
    }
}
