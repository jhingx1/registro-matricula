package com.matricula.config;

import com.matricula.dto.CursoDTO;
import com.matricula.model.Curso;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.convention.MatchingStrategies;

@Configuration
public class MapperConfig {

    @Bean(name = "defatulMapper")
    public ModelMapper defatulMapper(){
        return new ModelMapper();
    }

    @Bean(name = "cursoMapper")
    public ModelMapper cursoMapper(){
        return new ModelMapper();
    }

}
