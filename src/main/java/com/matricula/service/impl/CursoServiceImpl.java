package com.matricula.service.impl;

import com.matricula.model.Curso;
import com.matricula.repo.ICursoRepo;
import com.matricula.repo.IGenericRepo;
import com.matricula.service.ICursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl extends CRUDImpl<Curso,String> implements ICursoService {

    private final ICursoRepo repo;

    @Override
    protected IGenericRepo<Curso, String> getRepo() {
        return repo;
    }
}
