package e_com.bichitra.e_com02092024.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
    @Bean
    public ModelMapper myMapper(){
        return new ModelMapper();
    }
}
