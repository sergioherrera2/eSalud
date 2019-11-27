
package es.e3corp.eSalud.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import es.e3corp.eSalud.Service.UsuarioService;
import es.e3corp.eSalud.exception.UserNotFoundException;
import es.e3corp.eSalud.model.Usuario;
import es.e3corp.eSalud.utilidades.Utilidades;

@RestController
@RequestMapping("/usuarios")
/**
 * @author e3corp
 */
@CrossOrigin(origins = { "http://localhost:4200", "https://esalud.herokuapp.com" }, allowedHeaders = "*")
public class UsuarioController {

  // Variable estática LOG que nos permitirá obtener logs en todo momento
  private static final Log LOG = LogFactory.getLog(UsuarioController.class);

  // Servicio de usuarios para obtener sus recursos
  private transient final UsuarioService usersService;

  @Autowired
  /**
   * @author e3corp
   */
  public UsuarioController(final UsuarioService usersService) {
    this.usersService = usersService;
  }

  /**
   * Obtiene la contraseña del usuario mediante su dni.
   * 
   * @author e3corp
   */

  @RequestMapping(method = RequestMethod.GET)

  public ResponseEntity<Usuario> getUserPassword(@RequestParam("dni") final String dni,
      @RequestParam("password") final String password) {

    final String dniEncriptado = Utilidades.encriptar(dni);
    final String contrasenaEncrip = Utilidades.encriptar(password);

    final Usuario usuario = usersService.getUserByDniAndPassword(dniEncriptado, contrasenaEncrip);
    if (usuario == null) {
      LOG.info("[SERVER] No se ha encontrado ningún usuario con esos datos.");
      return ResponseEntity.badRequest().build();
    } else {
      LOG.info("[SERVER] Usuario encontrado: " + usuario.getNombre());
      return ResponseEntity.ok(usuario);
    }
  }

  /**
   * Obtiene un usuario mediante su dni.
   * 
   * @author e3corp
   */
  @RequestMapping(value = "/{userDni}", method = RequestMethod.GET)
  @ApiOperation(value = "Find an user", notes = "Return a user by DNI")

  public ResponseEntity<Usuario> userByDni(@PathVariable final String userDni) {
    LOG.info("[SERVER] Buscando usuario: " + userDni);
    Usuario user = null;
    try {
      // System.out.println("Se recibe el dni: " +userDni);
      final String dniEncriptado = Utilidades.encriptar(userDni);
      // System.out.println("Se recibe el dni encriptado: " +dniEncriptado);
      user = usersService.findByUserDni(dniEncriptado);
      LOG.info("[SERVER] Usuario encontrado.");
    } catch (UserNotFoundException e) {
      LOG.error("[SERVER] Usuario no encontrado.");
    }
    return ResponseEntity.ok(user);
  }

  /**
   * Método para obtener todos los usuarios de la bbdd.
   * 
   * @return los usuarios
   */
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @ApiOperation(value = "Find all user", notes = "Return all users")

  public ResponseEntity<List<Usuario>> allUsers() {
    LOG.info("Get allUsers");
    return ResponseEntity.ok(usersService.findAll());
  }

  /**
   * Devuelve todos los pacientes de la bbdd.
   * 
   * @return los pacientes
   */
  @RequestMapping(value = "/pacientes", method = RequestMethod.GET)
  @ApiOperation(value = "Find all user", notes = "Return all users")

  public ResponseEntity<List<Usuario>> allPacientes() {
    LOG.info("Get allUsers");
    return ResponseEntity.ok(usersService.getUsersByRol(Utilidades.encriptar("paciente")));
  }

  /**
   * Obtiene todos los médicos.
   * 
   * @author e3corp
   */
  @RequestMapping(value = "/medicos", method = RequestMethod.GET)
  @ApiOperation(value = "Find all user", notes = "Return all users")

  public ResponseEntity<List<Usuario>> allMedicos() {
    LOG.info("Get allUsers");
    return ResponseEntity.ok(usersService.getUsersByRol(Utilidades.encriptar("medico")));
  }

  /**
   * Borra un usuario en funcion de su id.
   * 
   * @author e3corp
   */
  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Delete an user", notes = "Delete a user by Id")

  public ResponseEntity<Void> deleteUser(@PathVariable final String userId) {
    LOG.info("Delete user " + userId);
    usersService.deleteUsuario(userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Registramos un usuario y guardamos ese usuario en la base de datos.
   * 
   * @author e3corp.
   */
  @RequestMapping(method = RequestMethod.POST)

  public ResponseEntity<Usuario> registrarUsuario(@RequestBody final String usuario) {
    final JSONObject jso = new JSONObject(usuario);
    final String dni = jso.getString("dni");
    final String contrasena = jso.getString("password");

    // System.out.println("Se recibe el dni: " +dni);
    final String dniEncriptado = Utilidades.encriptar(dni);
    // System.out.println("Se recibe el dni encriptado: " +dniEncriptado);

    final String contrasenaEncrip = Utilidades.encriptar(contrasena);

    Usuario usuario1 = usersService.getUserByDniAndPassword(dniEncriptado, contrasenaEncrip);
    if (usuario1 == null) {
      String nombre = null;
      String apellidos = null;
      String email = null;
      String localidad = null;
      String centro = null;
      String medico = null;
      String rol = null;
      String especialidad = null;
      String numTelefono = null;
      try {
        LOG.info("[SERVER] Registrando usuario...");
        nombre = jso.getString("nombre");
        apellidos = jso.getString("apellidos");
        numTelefono = jso.getString("tel");
        email = jso.getString("correo");
        if (jso.getString("rol").equals("paciente")) {
          localidad = jso.getString("localidad");
          rol = jso.getString("rol");
          centro = jso.getString("centro");
        } else {
          rol = jso.getString("rol");
          centro = jso.getString("centro");
          medico = jso.getString("medico");
          especialidad = jso.getString("especialidad");
        }
      } catch (JSONException j) {
        LOG.info("[SERVER] Error en la lectura del JSON.");
        LOG.info(j.getMessage());
        return ResponseEntity.badRequest().build();
      }

      usuario1 = new Usuario(dni, nombre, apellidos, contrasena, rol, especialidad, medico, numTelefono, localidad,
          centro, email);
      usersService.saveUsuario(usuario1);
      LOG.info("[SERVER] Usuario registrado.");
      LOG.info("[SERVER] " + usuario1.toString());
      return ResponseEntity.ok().build();
    } else {
      LOG.info("[SERVER] Error: El usuario ya está registrado.");
      LOG.info("[SERVER] " + usuario1.toString());
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Método que actualiza un usuario dada su id.
   * 
   * @param mensajerecibido el mensaje que contiene a un usuario.
   * @param userId          el dni del usuario que buscamos.
   * @return
   */
  @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
  @ApiOperation(value = "Update usuario", notes = "Finds a cita ID and updates its fields")
  public ResponseEntity<Usuario> updateUsuario(@RequestBody final String mensajerecibido,
      @PathVariable final String userId) {
    final JSONObject jso = new JSONObject(mensajerecibido);
    final String UserIdEncriptado = Utilidades.encriptar(userId);
    final Usuario usuario = usersService.findByUserDni(UserIdEncriptado);
    if (usuario == null) {
      LOG.info("[SERVER] Error: el usuario no existe.");
      return ResponseEntity.badRequest().build();
    } else {
      try {
        LOG.info("[SERVER] Actualizando usuario...");

        // Depende de los campos que queramos que puedan actualizarse
        final String dni = jso.getString("dni");
        final String nombre = jso.getString("nombre");
        final String apellidos = jso.getString("apellidos");
        final String numTelefono = jso.getString("numTelefono");
        final String centro = jso.getString("centro");
        final String email = jso.getString("email");
        final String rol = jso.getString("rol");
        final String contrasena = jso.getString("contrasena");

        if (rol.equals("medico")) {
          final String especialidad = jso.getString("especialidad");
          final String especialidadEnc = Utilidades.encriptar(especialidad);
          usuario.setEspecialidad(especialidadEnc);
        }

        if (rol.equals("paciente")) {
          final String localidad = jso.getString("localidad");
          final String localidadEnc = Utilidades.encriptar(localidad);
          usuario.setLocalidad(localidadEnc);
        }

        final String dniEnc = Utilidades.encriptar(dni);
        final String nombreEnc = Utilidades.encriptar(nombre);
        final String apellidosEnc = Utilidades.encriptar(apellidos);
        final String numTelefonoEnc = Utilidades.encriptar(numTelefono);
        final String centroEnc = Utilidades.encriptar(centro);
        final String emailEnc = Utilidades.encriptar(email);
        final String rolEnc = Utilidades.encriptar(rol);
        final String contrasenaEnc = Utilidades.encriptar(contrasena);

        usuario.setDni(dniEnc);
        usuario.setNombre(nombreEnc);
        usuario.setApellidos(apellidosEnc);
        usuario.setNumTelefono(numTelefonoEnc);
        usuario.setCentro(centroEnc);
        usuario.setEmail(emailEnc);
        usuario.setRol(rolEnc);
        usuario.setcontrasena(contrasenaEnc);
      } catch (JSONException j) {
        LOG.error("[SERVER] Error en la lectura del JSON.");
        LOG.info(j.getMessage());
        return ResponseEntity.badRequest().build();
      }

      usersService.updateUsuario(usuario);
      LOG.info("[SERVER] Usuario actualizada.");
      LOG.info("[SERVER] " + usuario.toString());
      return ResponseEntity.ok().build();
    }
  }

}
