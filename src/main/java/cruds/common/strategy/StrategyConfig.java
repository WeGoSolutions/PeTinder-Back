package cruds.common.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {
    @Bean
    public ImageStorageStrategy imageStorageStrategy() {
        return new LocalImageStorageStrategy();
    }
}

