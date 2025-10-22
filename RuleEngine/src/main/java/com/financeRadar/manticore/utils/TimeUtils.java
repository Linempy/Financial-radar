package com.financeRadar.manticore.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * TimeUtils — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 22.10.2025
 */
public class TimeUtils {
    /**
     * Проверяет, является ли время ночным (23:00 - 06:00)
     */
    public static boolean isNight(LocalDateTime timestamp) {
        int hour = timestamp.getHour();
        return hour >= 23 || hour < 6;
    }

    /**
     * Проверяет, является ли день выходным
     */
    public static boolean isWeekend(LocalDateTime timestamp) {
        DayOfWeek day = timestamp.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

}