package com.matricula.service.impl;

import com.matricula.model.Estudiante;
import com.matricula.repo.IEstudianteRepo;
import com.matricula.repo.IGenericRepo;
import com.matricula.service.IEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl extends CRUDImpl<Estudiante,String> implements IEstudianteService {

    private final IEstudianteRepo repo;

    @Override
    protected IGenericRepo<Estudiante, String> getRepo() {
        return repo;
    }

    @Override
    public Flux<Estudiante> listarEstudiantesOrdenadosPorEdad() {
        return repo.findAllByOrderByEdadAsc();
    }

    @Override
    public Flux<Estudiante> listarEstudiantesOrdenadosPorEdadxDes() {
        return repo.findAllByOrderByEdadDesc();
    }
}
