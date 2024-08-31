package com.matricula.repo;

import com.matricula.model.Estudiante;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;

public interface IEstudianteRepo extends IGenericRepo<Estudiante,String>{

    @Query(sort = "{ 'edad': 1 }")
    Flux<Estudiante> findAllByOrderByEdadAsc();

    @Query(sort = "{ 'edad': -1 }")
    Flux<Estudiante> findAllByOrderByEdadDesc();

}
