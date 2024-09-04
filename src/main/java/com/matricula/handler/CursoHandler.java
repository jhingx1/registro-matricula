package com.matricula.handler;

import com.matricula.dto.CursoDTO;
import com.matricula.model.Curso;
import com.matricula.service.ICursoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class CursoHandler {
    private final ICursoService service;

    @Qualifier("defatulMapper")
    private final ModelMapper modelMapper;

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().
                        map(this::convertToDto), CursoDTO.class);

    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");

        return service.findById(id)
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Mono<CursoDTO> monoCursoDTO = request.bodyToMono(CursoDTO.class);

        return monoCursoDTO
                .flatMap(e -> service.save(convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        return request.bodyToMono(CursoDTO.class)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(id,convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                ).switchIfEmpty(ServerResponse.notFound().build())
                ;
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");
        return service.delete(id)
                .flatMap(result -> {
                    if (result){
                        return ServerResponse.noContent().build();
                    }else {
                        return ServerResponse.notFound().build();
                    }
                });
    }

    private CursoDTO convertToDto(Curso model){
        return modelMapper.map(model,CursoDTO.class);
    }

    private Curso convertToDocument(CursoDTO dto){
        return modelMapper.map(dto,Curso.class);
    }
}
