package com.matricula.service.impl;

import com.matricula.model.Matricula;
import com.matricula.repo.IGenericRepo;
import com.matricula.repo.IMatriculaRepo;
import com.matricula.service.IMatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl extends CRUDImpl<Matricula,String> implements IMatriculaService {

    private final IMatriculaRepo repo;

    @Override
    protected IGenericRepo<Matricula, String> getRepo() {
        return repo;
    }
}
