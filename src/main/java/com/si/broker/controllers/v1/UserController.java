package com.si.broker.controllers.v1;

import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.Token;
import com.si.broker.services.IRoutingService;
import com.si.broker.services.IUserService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constants.USERS_BASE_URL)
// api/v1/users
public class UserController {

    private final IUserService userService;
    private final IRoutingService routingService;
    private Token token;

    @Autowired
    public UserController(IUserService userService, IRoutingService routingService) {
        this.userService = userService;
        this.routingService = routingService;
    }

    //  Dobijanje svih korisnika.
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);
        List<UserDTO> users = userService.getAllUsersByProviderId(token);
        if (users == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Dobijanje korisnika po ID-u.
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "id")
    public ResponseEntity<UserDTO> findUserById(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "id") Long id) {

        token = routingService.getTokenInfoByToken(jsonToken);
        UserDTO user = userService.getUserDtoByIdByPrivilege(id, token);
        if (user == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Dobijanje korisnika po email-u.
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "email")
    public ResponseEntity<UserDTO> findUserByEmail(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "email") String email) {
        token = routingService.getTokenInfoByToken(jsonToken);
        UserDTO user = userService.getUserByEmailByPrivilege(email, token);
        if (user == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Dobijanje korisnika po username.
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "username")
    public ResponseEntity<UserDTO> findUserByUsername(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "username") String username) {
        token = routingService.getTokenInfoByToken(jsonToken);
        UserDTO user = userService.getUserDtoByUsernameByPrivilege(username, token);
        if (user == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.OK);
    }

    // Izmena postojeceg korisnika
    @PreAuthorize("hasAnyRole({'ADMIN', 'KORISNIK', 'PROVIDER'})")
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/edit", params = "id")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "id") Long id, @RequestBody UserDTO userDTO) {
        token = routingService.getTokenInfoByToken(jsonToken);
        UserDTO user = userService.updateUser(id, userDTO, token);
        if (user == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Brisanje postojeceg korisnika
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @DeleteMapping(value = "/delete", params = "id")
    public ResponseEntity<Boolean> deleteUserById(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam Long id) {
        token = routingService.getTokenInfoByToken(jsonToken);
        Boolean deleted = userService.deleteUserById(id, token);
        if (token.getIsProvider())
            routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity(true, HttpStatus.OK);
    }
}

