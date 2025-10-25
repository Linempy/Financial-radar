INSERT INTO rules (rule_type, name, expression, priority, enabled) VALUES
('THRESHOLD', 'Большая сумма', 'amount > 100000', 1, true),
('THRESHOLD', 'Транзакция в USD', 'currency == "USD" && amount > 10000', 1, true),
('THRESHOLD', 'Ночная транзакция', 'isNight(timestamp)', 1, true),
('THRESHOLD', 'Выходной день и большая сумма', 'isWeekend(timestamp) && amount > 30000', 1, false),
('PATTERN', 'Частые транзакции', 'count(senderId, "PT5M") > 10', 2, true),
('PATTERN', 'Сумма более 100000 за 15 минут', 'amountSum(userId, "PT15M") > 100000', 2, true),
('PATTERN', 'Частые мелкие транзакции', 'count(userId, "PT10M") > 4 && amount < 1000', 2, false),
('COMPOSITE', 'Подозрительная активность', '(amount > 50000) || (count(senderId, "PT5M") > 5 && isNight(timestamp))', 3, true),
('COMPOSITE', 'Подозрительная активность (большая итоговая сумма)', 'count(senderId, "PT15M") > 3 && amountSum(senderId, "PT15M") > 75000', 3, true);
