package com.alexis.pruebajjwt.repository;


import com.alexis.pruebajjwt.entity.Documento;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "documentos")
public interface IDocumentoRepository extends PagingAndSortingRepository<Documento, Integer> {
}
