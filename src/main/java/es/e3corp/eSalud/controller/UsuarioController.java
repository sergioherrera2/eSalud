
package es.e3corp.eSalud.controller;

import es.e3corp.eSalud.exception.UserNotFoundException;

import es.e3corp.eSalud.model.Usuario;

import es.e3corp.eSalud.Service.UsuarioService;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = { "http://localhost:4200", "https://esalud.herokuapp.com" }, allowedHeaders = "*")
public class UsuarioController {

    private static final Log log = LogFactory.getLog(UsuarioController.class);
    private final UsuarioService usersService;
    private Usuario user;

    @Autowired
    public UsuarioController(UsuarioService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Usuario> getUserPassword(@RequestParam(required = false) String dni,
            @RequestParam(required = false) String password) {
        Usuario usuario = usersService.getUserByDniAndPassword(dni, password);
        if (usuario != null) {
            System.out.println("[SERVER] Usuario encontrado: " + usuario.getNombre());
            return ResponseEntity.ok(usuario);
        } else {
            System.out.println("[SERVER] No se ha encontrado ningún usuario.");
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "Find an user", notes = "Return a user by Id")
    public ResponseEntity<Usuario> userById(@PathVariable String userId) throws UserNotFoundException {
        log.info("Get userById");
        try {
            user = usersService.findByUserId(userId);
        } catch (UserNotFoundException e) {
            user = null;
        }
        return ResponseEntity.ok(user);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<List<Usuario>> usuarioById() {
//        log.info("Get allUsers");
//        return ResponseEntity.ok(usersService.findAll());
//    }

    /*
     * @RequestMapping(method = RequestMethod.GET)
     * 
     * @ApiOperation(value = "Find all user", notes = "Return all users" )
     * 
     * public ResponseEntity<List<Usuario>> userById(){
     * 
     * log.info("Get allUsers");
     * 
     * return ResponseEntity.ok(usersService.findAll());
     * 
     * }
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete an user", notes = "Delete a user by Id")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Delete user " + userId);
        usersService.deleteUsuario(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody String p) {
        JSONObject jso = new JSONObject(p);
        String dni = jso.getString("dni");
        String contraseña = jso.getString("password");
        Usuario usuario1 = usersService.getUserByDniAndPassword(dni, contraseña);
        if (usuario1 == null) {
            String nombre = null, apellidos = null, email = null, localidad = null, centro = null, medico = null,
                    rol = null, especialidad = null;
            int numTelefono = 0;
            try {
                System.out.println("[SERVER] Registrando usuario...");
                nombre = jso.getString("nombre");
                apellidos = jso.getString("apellidos");
                numTelefono = jso.getInt("tel");
                email = jso.getString("correo");
                if (jso.getString("rol").equals("paciente")) {
                    localidad = jso.getString("localidad");
                } else {
                    rol = jso.getString("rol");
                    centro = jso.getString("centro");
                    medico = jso.getString("medico");
                    especialidad = jso.getString("especialidad");
                }
            } catch (JSONException j) {
                System.out.println("[SERVER] Error en la lectura del JSON.");
                System.out.println(j.getMessage());
                return ResponseEntity.badRequest().build();
            }

            usuario1 = new Usuario(dni, nombre, apellidos, contraseña, rol, especialidad, medico, numTelefono,
                    localidad, centro, email);
            usersService.saveUsuario(usuario1);
            System.out.println("[SERVER] Usuario registrado.");
            System.out.println("[SERVER] " + usuario1.toString());
            return ResponseEntity.ok().build();
        } else {
            System.out.println("[SERVER] Error: el usuario ya está registrado.");
            return ResponseEntity.badRequest().build();
        }
    }

}