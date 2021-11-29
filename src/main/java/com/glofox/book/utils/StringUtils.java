package com.glofox.book.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.text.Normalizer;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String removeAccents(String str) {
        if ((str == null) || (str.length() == 0))
            return str;

        return getNormalizedString(str);
    }

    private static String getNormalizedString(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
