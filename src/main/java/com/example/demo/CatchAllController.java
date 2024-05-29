package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CatchAllController {
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public JsonNode catchAll(HttpServletRequest request, @RequestBody(required = false) String body) throws IOException {
        System.out.println("Ruta solicitada: " + request.getRequestURI());
        System.out.println("Método: " + request.getMethod());

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println("Parámetro: " + paramName + " = " + request.getParameter(paramName));
        }

        if (body != null && !body.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode;

                if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                    // Convert form data to JSON
                    Map<String, String> params = new HashMap<>();
                    String[] pairs = body.split("&");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split("=");
                        params.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                    }
                    jsonNode = objectMapper.valueToTree(params);
                } else {
                    // Handle JSON body
                    jsonNode = objectMapper.readTree(body);
                    if (jsonNode.has("frontImage")) {
                        ((ObjectNode) jsonNode).put("frontImage", "IMAGEN RECIBIDA");
                    }
                    if (jsonNode.has("backImage")) {
                        ((ObjectNode) jsonNode).put("backImage", "IMAGEN RECIBIDA");
                    }
                }

                System.out.println("Cuerpo de la solicitud: " + jsonNode.toString());
            } catch (Exception e) {
                System.out.println("Error al procesar el cuerpo de la solicitud: " + e.getMessage());
                System.out.println("Cuerpo de la solicitud (Texto): " + body);
            }
        } else {
            System.out.println("Cuerpo de la solicitud: (vacío)");
        }

//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.createObjectNode()
//                .put("codigoValidacion", "ine1666916017985")
//                .put("informacionAdicional", "")
//                .put("mensaje", "Esta vigente como medio de identificacion y puede votar")
//                .put("cic", "169656660")
//                .put("claveElector", "RNGVTN87090725M900")
//                .put("numeroEmision", "2")
//                .put("distritoFederal", "6")
//                .put("ocr", "2691073622688")
//                .put("anioRegistro", "2005")
//                .put("anioEmision", "2018")
//                .put("claveMensaje", "3")
//                .put("estatus", "OK")
//                .put("vigencia", "31 de diciembre de 2028");
//
//        return responseJson;
        return null;
    }
}