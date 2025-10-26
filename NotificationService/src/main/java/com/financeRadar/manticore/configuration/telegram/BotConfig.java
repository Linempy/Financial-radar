//package com.financeRadar.manticore.configuration.telegram;
//
///**
// * BotConfig — описание класса.
// * <p>
// * TODO: добавить описание назначения и поведения класса.
// * </p>
// *
// * @author Linempy
// * @since 26.10.2025
// */
//package com.example.transactionbot.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Configuration
//public class BotConfig {
//
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    @Value("${telegram.bot.channel-id}")
//    private String channelId;
//
//    @Bean
//    public String botToken() {
//        return botToken;
//    }
//
//    @Bean
//    public String channelId() {
//        return channelId;
//    }
//
//    @Bean
//    public TelegramBotsApi telegramBotsApi(TransactionAlertBot bot) throws TelegramApiException {
//        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
//        api.registerBot(bot);
//        return api;
//    }
//}