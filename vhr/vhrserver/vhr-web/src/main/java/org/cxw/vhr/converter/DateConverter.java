package org.cxw.vhr.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateConverter implements Converter<String, Date> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    @Override
    public Date convert(String s) {
        try {
            if (!s.contains(":")) {
                return sdf.parse(s);
            } else {
                return sdf2.parse(s);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
