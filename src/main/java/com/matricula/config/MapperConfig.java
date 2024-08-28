package com.matricula.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean(name = "defatulMapper")
    public ModelMapper defatulMapper(){
        return new ModelMapper();
    }

}
