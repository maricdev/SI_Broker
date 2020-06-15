package com.si.broker.controllers.v1;

import com.si.broker.api.v1.mappers.IUserMapper;
import com.si.broker.api.v1.model.ServiceDTO;
import com.si.broker.model.Endpoint;
import com.si.broker.model.Token;
import com.si.broker.model.User;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IRoutingService;
import com.si.broker.services.IServiceService;
import com.si.broker.services.IUserService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(Constants.SERVICES_BASE_URL)
// api/v1/services
public class ServiceController {

    private final IServiceService serviceService;
    private final IRoutingService routingService;
    private final IUserService userService;
    private final IUserMapper userMapper;
    private final IProviderService providerService;
    private Token token;

    @Autowired
    public ServiceController(IServiceService serviceService, IRoutingService routingService, IUserService userService, IUserMapper userMapper, IProviderService providerService) {
        this.serviceService = serviceService;
        this.routingService = routingService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.providerService = providerService;
    }

    //  Dobijanje svih servisa.
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<ServiceDTO>> getAllServices(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);
        List<ServiceDTO> services = serviceService.getAllServicesByPrivilege(token);


        if (services == null) {
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

        return new ResponseEntity(services, HttpStatus.OK);
    }

    // DOBIJANJE SERVISA PO ID
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "id")
    public ResponseEntity<ServiceDTO> findServiceById(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "id") Long id) {
        token = routingService.getTokenInfoByToken(jsonToken);
        ServiceDTO service = serviceService.getServiceByIdByPrivilege(id, token);

        if (service == null) {
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


        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    // REGISTROVANJE SERVISA
    @PreAuthorize("hasRole('PROVIDER')")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        return new ResponseEntity<>(serviceService.createService(serviceDTO), HttpStatus.OK);
    }

    // IZMENA SERVISA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/edit", params = "id")
    public ResponseEntity<ServiceDTO> updateService(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam(required = true, value = "id") Long id, @RequestBody ServiceDTO serviceDTO) {
        token = routingService.getTokenInfoByToken(jsonToken);
        ServiceDTO service = serviceService.getServiceByIdByPrivilege(id, token);
        if (service == null) {
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


        return new ResponseEntity<>(serviceService.updateService(id, serviceDTO, token), HttpStatus.OK);
    }

    // BRISANJE SERVISA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @DeleteMapping(value = "/delete", params = "id")
    public ResponseEntity<Boolean> deleteServiceById(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken, @RequestParam Long id) {
        token = routingService.getTokenInfoByToken(jsonToken);
        Boolean deleted = serviceService.deleteServiceById(id, token);
        if (deleted == false) {
            if (token.getIsProvider())
                routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(DeleteMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(DeleteMapping.class).value()[0], null, null, null, token.getId());
            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(DeleteMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(deleted, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(DeleteMapping.class).value()[0], null, null, null, token.getId());
        return new ResponseEntity(deleted, HttpStatus.OK);
    }

    // ORKESTRACIJA
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/{service}")
    public ResponseEntity dynamicCompositeService(@RequestHeader(Constants.HEADER_STRING) String jsonToken, @PathVariable Long service, @RequestParam Map<String, String> params, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response) {
        Token token = routingService.getTokenInfoByToken(jsonToken);
        User user = routingService.getUserByToken(token);

        if (routingService.checkIfServiceIsComposite(service) == false)
            return new ResponseEntity("Servis nije kompozitni!", HttpStatus.BAD_REQUEST);
        if (routingService.checkIfUserCanAccessToCompositeService(token, service) == false)
            return new ResponseEntity("Nemate dozvolu za pristup svim endpointima servisa!", HttpStatus.FORBIDDEN);

        List<Endpoint> definedPlan = routingService.getEndpointsPlan(service);

        for (Endpoint e : definedPlan) {
            String ApiUrl = routingService.getFullPath(e.getId());
            if (ApiUrl.trim().equals(""))
                return new ResponseEntity("Greška u putanji!", HttpStatus.BAD_REQUEST);

            Object[] keys = params.keySet().toArray();

            for (int i = 0; i < params.size(); i++) {
                if (i == 0)
                    ApiUrl += "?" + keys[0] + "=" + params.get(keys[0]);
                else
                    ApiUrl += "&" + keys[i] + "=" + params.get(keys[i]);
            }
            //DODAJ LOGGER
            return dynamicRequest(false, ApiUrl, body, request, response);
        }
        return null;
    }

    // UKOLIKO JE U PITANJU RUTIRANJE SA N PARAMETARA.
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/{service}/{endpoint}/")
    public ResponseEntity dynamicParam(@RequestHeader(Constants.HEADER_STRING) String jsonToken, @PathVariable Long service, @PathVariable Long endpoint, @RequestParam(required = false) Map<String, String> params, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response) {
        Token token = routingService.getTokenInfoByToken(jsonToken);
        User user = routingService.getUserByToken(token);

        if (routingService.checkIfUserCanAccess(token, endpoint) == false)
            return new ResponseEntity("Nemate dozvolu za pristup servisu!", HttpStatus.FORBIDDEN);
        if (routingService.checkEndpointService(service, endpoint) == false)
            return new ResponseEntity("Izabrani endpoint ne sadrži izabrani servis!", HttpStatus.BAD_REQUEST);

        String ApiUrl = routingService.getFullPath(endpoint);
        if (ApiUrl.trim().equals(""))
            return new ResponseEntity("Greška u putanji!", HttpStatus.BAD_REQUEST);

        Object[] keys = params.keySet().toArray();

        for (int i = 0; i < params.size(); i++) {
            if (i == 0)
                ApiUrl += "?" + keys[0] + "=" + params.get(keys[0]);
            else
                ApiUrl += "&" + keys[i] + "=" + params.get(keys[i]);
        }

        return getResponseEntity(service, endpoint, body, request, response, token, ApiUrl);
    }

    // UKOLIKO JE U PITANJU DINAMICKI LINK (PUTANJA SA N PODRUTA KOJE PREDSTAVLJAJU PARAMETRE)
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/{service}/{endpoint}/**")
    public ResponseEntity dynamicPath(@RequestHeader(Constants.HEADER_STRING) String jsonToken, @PathVariable Long service, @PathVariable Long endpoint, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response) {

        String params = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        Token token = routingService.getTokenInfoByToken(jsonToken);
        User user = routingService.getUserByToken(token);
        String ApiUrl = null;
        ServiceDTO serviceComposite = serviceService.getServiceById(service);

        if (routingService.checkIfUserCanAccess(token, endpoint) == false)
            return new ResponseEntity("Nemate dozvolu za pristup servisu!", HttpStatus.FORBIDDEN);
        if (routingService.checkEndpointService(service, endpoint) == false)
            return new ResponseEntity("Izabrani endpoint ne sadrži izabrani servis!", HttpStatus.BAD_REQUEST);
        ApiUrl = routingService.getFullPath(endpoint);


        if (ApiUrl.trim().equals(""))
            return new ResponseEntity("Greška u putanji!", HttpStatus.BAD_REQUEST);

        List<String> paramsList = Arrays.asList(params.split("/"));

        for (int i = 6; i < paramsList.size(); i++)
            ApiUrl += "/" + paramsList.get(i);

        return getResponseEntity(service, endpoint, body, request, response, token, ApiUrl);
    }

    private ResponseEntity getResponseEntity(@PathVariable Long service, @PathVariable Long endpoint, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response, Token token, String apiUrl) {

        boolean success = false;
        ResponseEntity responseEntity = dynamicRequest(false, apiUrl, body, request, response);

        if (responseEntity.getStatusCode() == HttpStatus.OK)
            success = true;

        if (token.getIsProvider())
            routingService.createLogger(success, false, null, null, service, endpoint, token.getId(), null);
        else
            routingService.createLogger(success, false, null, null, service, endpoint, null, token.getId());

        //String body2 = routingService.createJsonFilter(routingService.getFieldsByRole(token, endpoint), responseEntity.getBody().toString());
        String test = routingService.createJsonFilter(routingService.getFieldsByRole(token, endpoint), responseEntity.getBody().toString());
        ResponseEntity responseEntityFilter = dynamicRequest(true, "http://localhost:8081/api/v1/filter", routingService.createJsonFilter(routingService.getFieldsByRole(token, endpoint), responseEntity.getBody().toString()), request, response);
        System.out.println("Server response (no filter): " + System.lineSeparator() + responseEntity.getBody());

        return new ResponseEntity(responseEntityFilter.getBody(), HttpStatus.OK);
    }

    //@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity dynamicRequest(Boolean filter, String apiUrl, @RequestBody(required = false) String body, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer content = null;
        ResponseEntity r;
        URL url;
        HttpURLConnection con;
        //Boolean isGetMethod = false;
        System.out.println("ZAHTEV: => " + apiUrl);
        try {
            System.out.println("dynamicRequest - " + request.getMethod() + " method");
            url = new URL(apiUrl);
            //url = new URL("http://localhost:8080/api/v1/roles/find");
            con = (HttpURLConnection) url.openConnection();
            if (filter == true)
                con.setRequestMethod("POST");
            else
                con.setRequestMethod(request.getMethod());

            con.setDoOutput(true);
            if ((body != null && (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) || filter == true)) {
                System.out.println("body");
                con.setRequestProperty("Content-Type", "application/json; utf-8");

                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                osw.write(body);

                osw.flush();
                osw.close();
                os.close();
            }

            con.connect();
            int status = con.getResponseCode();
            System.out.println("HTTP Status: " + status);

            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                //String header = con.getHeaderField("Authorization");
                //System.out.println("Authorization: " + header);
                System.out.println("Body Response: " + System.lineSeparator() + content.toString());

                return new ResponseEntity(content.toString(), HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity("Došlo je do greške prilikom kontaktiranja servisa!", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}