package com.matricula.handler;

import com.matricula.dto.EstudianteDTO;
import com.matricula.model.Estudiante;
import com.matricula.service.IEstudianteService;
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
public class EstudianteHandler {

    private final IEstudianteService service;

    @Qualifier("defatulMapper")
    private final ModelMapper modelMapper;

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().
                        map(this::convertToDto), EstudianteDTO.class);

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
        Mono<EstudianteDTO> monoEstudianteDTO = request.bodyToMono(EstudianteDTO.class);

        return monoEstudianteDTO
                .flatMap(e -> service.save(convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );

//        return request.bodyToMono(EstudianteDTO.class)
//                .flatMap(e -> service.save(convertToDocument(e)))
//                .flatMap(e -> ServerResponse.ok()
//                        .bodyValue(e))
//                .switchIfEmpty(ServerResponse.badRequest().build());

    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        return request.bodyToMono(EstudianteDTO.class)
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

    private EstudianteDTO convertToDto(Estudiante model){
        return modelMapper.map(model,EstudianteDTO.class);
    }

    private Estudiante convertToDocument(EstudianteDTO dto){
        return modelMapper.map(dto,Estudiante.class);
    }

}
