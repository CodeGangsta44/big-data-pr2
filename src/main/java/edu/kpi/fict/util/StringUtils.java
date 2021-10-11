package edu.kpi.fict.util;

import java.util.Optional;

public class StringUtils {

    public static String emptyIfNull(final String value) {

        return Optional.ofNullable(value)
                .orElse("");
    }
}
