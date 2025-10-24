INSERT INTO rules (rule_type, name, expression, priority, enabled) VALUES
('THRESHOLD', 'Большая сумма', 'amount > 10000', 1, true),
('PATTERN', 'Частые транзакции', 'redis.txCount(userId, ''PT5M'') > 10', 2, true),
('COMPOSITE', 'Подозрительная активность', '(amount > 50000) OR (redis.txCount(userId, ''PT5M'') > 5 AND time.isNight(timestamp))', 1, true);
