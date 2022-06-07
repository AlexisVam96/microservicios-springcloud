package com.alexis.pruebajjwt.repository;


import com.alexis.pruebajjwt.entity.Contribuyente;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "contribuyentes")
public interface IContribuyenteRepository extends PagingAndSortingRepository<Contribuyente, Integer> {
}
