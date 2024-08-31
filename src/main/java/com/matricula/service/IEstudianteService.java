package com.matricula.service;

import com.matricula.model.Estudiante;
import reactor.core.publisher.Flux;

public interface IEstudianteService extends ICRUD<Estudiante,String>{
    public Flux<Estudiante> listarEstudiantesOrdenadosPorEdad();
    public Flux<Estudiante> listarEstudiantesOrdenadosPorEdadxDes();
}
