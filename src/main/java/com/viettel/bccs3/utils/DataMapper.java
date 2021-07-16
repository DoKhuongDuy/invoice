package com.viettel.bccs3.utils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataMapper {
    public static Long longValue(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            try {
                return Long.valueOf((String) object);
            } catch (Exception e) {
                return null;
            }
        }
        return ((Number) object).longValue();
    }

    public static Double doubleValue(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            try {
                return Double.valueOf((String) object);
            } catch (Exception e) {
                return null;
            }
        }
        return ((Number) object).doubleValue();
    }

    public static String stringValue(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof BigInteger) {
            try {
                return object.toString();
            } catch (Exception e) {
                return null;
            }
        }
        return (String) object;
    }

    public static LocalDateTime localDateValue(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof BigInteger) {
            try {
                var epoch = ((BigInteger) object).longValue();
                epoch *= 1000;
                var timestamp = new Timestamp(epoch);
                return timestamp.toLocalDateTime();
            } catch (Exception e) {
                return null;
            }
        }
        if (object instanceof String) {
            try {
                var formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                //convert String to LocalDate
                return LocalDate.parse(object.toString(), formatter).atStartOfDay();
            } catch (Exception e) {
                return null;
            }
        }
        return ((Timestamp) object).toLocalDateTime();
    }
}
