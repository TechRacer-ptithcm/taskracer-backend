package ptithcm.itmc.taskracer.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j(topic = "LOG-CONFIG")
public class LogConfig {

    @Value("${logging.logstash.enabled}")
    private boolean logstashEnabled;

    @Value("${logging.logstash.host}")
    private String logstashHost;

    @Value("${logging.logstash.port}")
    private int logstashPort;

    @PostConstruct
    public void configure() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg [trace.id=%X{trace.id}]%n");
        encoder.start();

        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        rootLogger.addAppender(consoleAppender);
        if (logstashEnabled) {
            LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
            logstashAppender.setName("LOGSTASH");
            logstashAppender.setContext(loggerContext);
            logstashAppender.addDestination(logstashHost + ":" + logstashPort);  // Logstash destination

            LogstashEncoder logstashEncoder = new LogstashEncoder();
            logstashAppender.setEncoder(logstashEncoder);

            logstashAppender.start();

            rootLogger.addAppender(logstashAppender);

            log.info("Logstash logging is enabled. Sending logs to: {}:{}", logstashHost, logstashPort);
        } else {
            log.warn("Logstash logging is disabled.");
        }
    }
}
