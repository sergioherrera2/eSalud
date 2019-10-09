package es.e3corp.eSalud.repository;

import es.e3corp.eSalud.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository  extends MongoRepository<Usuario, String> {

} 