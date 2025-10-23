package com.financeRadar.manticore.dto;

/**
 * RequestContext — описание класса.
 * <p>
 * TODO: добавить описание назначения и поведения класса.
 * </p>
 *
 * @author Linempy
 * @since 23.10.2025
 */
public record RequestContext(
        String ip,
        String userAgent
) {
}