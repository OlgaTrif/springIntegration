package ru.gb.spring_integration.config;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.feed.dsl.Feed;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class IntegrationConfig {

    @Bean
    public IntegrationFlow feedFlow() throws MalformedURLException {
        return IntegrationFlow.from(Feed.inboundAdapter(new URL("https://lenta.ru/rss"), "news"),
        e -> e.poller(p -> p.fixedDelay(5000)))
                .transform(extractLinkFromRSS())
                .handle(messageHandler())
                .get();
    }

    @Bean
    public AbstractPayloadTransformer<SyndEntry,String> extractLinkFromRSS(){

        return new AbstractPayloadTransformer<SyndEntry, String>() {
            @Override
            protected String transformPayload(SyndEntry payload) {
                return payload.getTitle() + "\n" + payload.getAuthor() + "\n" + payload.getLink();
            }
        };
    }

    @Bean
    public FileWritingMessageHandler messageHandler(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
                new File("D:/GEEKBRAINS/Spring/spring_integration/infos"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        handler.setAutoCreateDirectory(true);
        handler.setCharset("UTF8");
        handler.setExpectReply(false);
        return handler;
    }
}
