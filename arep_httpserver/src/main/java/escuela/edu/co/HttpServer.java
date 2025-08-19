package escuela.edu.co;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        int port = 35000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor HTTP iniciado en http://localhost:" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerParts = headerLine.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0].toLowerCase(), headerParts[1]);
                }
            }

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                send400(out);
                return;
            }

            String method = parts[0];
            String fullPath = parts[1];
            
            // Separar path y query parameters
            String path = fullPath;
            String queryString = "";
            if (fullPath.contains("?")) {
                String[] pathQuery = fullPath.split("\\?", 2);
                path = pathQuery[0];
                queryString = pathQuery[1];
            }

            Map<String, String> queryParams = parseQueryString(queryString);

            // rutas de API
            if (path.startsWith("/app/")) {
                handleApiRequest(method, path, queryParams, headers, in, out);
                return;
            }

            if (!method.equals("GET")) {
                send405(out);
                return;
            }

            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File("www" + path);
            
            if (file.exists() && !file.isDirectory()) {
                String mimeType = Files.probeContentType(file.toPath());
                if (mimeType == null) {
                    if (path.endsWith(".html")) mimeType = "text/html";
                    else if (path.endsWith(".css")) mimeType = "text/css";
                    else if (path.endsWith(".js")) mimeType = "application/javascript";
                    else if (path.endsWith(".png")) mimeType = "image/png";
                    else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) mimeType = "image/jpeg";
                    else mimeType = "application/octet-stream";
                }

                byte[] content = Files.readAllBytes(file.toPath());

                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n\r\n";

                out.write(response.getBytes());
                out.write(content);
            } else {
                send404(out, path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleApiRequest(String method, String path, Map<String, String> queryParams, 
                                       Map<String, String> headers, BufferedReader in, OutputStream out) throws IOException {
        
        String jsonResponse = "";
        
        switch (path) {
            case "/app/hello":
                if (method.equals("GET") || method.equals("POST")) {
                    String name = queryParams.getOrDefault("name", "Mundo");
                    if (name.isEmpty()) name = "Mundo";
                    jsonResponse = String.format("{\"message\": \"¡Hola, %s!\", \"timestamp\": \"%s\"}", 
                                               name, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } else {
                    send405(out);
                    return;
                }
                break;
                
            case "/app/time":
                if (method.equals("GET")) {
                    LocalDateTime now = LocalDateTime.now();
                    jsonResponse = String.format("{\"current_time\": \"%s\", \"formatted_time\": \"%s\"}", 
                                               now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                               now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                } else {
                    send405(out);
                    return;
                }
                break;
                
            case "/app/sum":
                if (method.equals("GET")) {
                    try {
                        double a = Double.parseDouble(queryParams.getOrDefault("a", "0"));
                        double b = Double.parseDouble(queryParams.getOrDefault("b", "0"));
                        double result = a + b;
                        jsonResponse = String.format("{\"a\": %.2f, \"b\": %.2f, \"sum\": %.2f}", a, b, result);
                    } catch (NumberFormatException e) {
                        jsonResponse = "{\"error\": \"Parámetros inválidos. Use números válidos para a y b.\"}";
                    }
                } else {
                    send405(out);
                    return;
                }
                break;
                
            default:
                send404(out, path);
                return;
        }
        
        sendJsonResponse(out, jsonResponse);
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }
        
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                
                }
            }
        }
        return params;
    }

    private static void sendJsonResponse(OutputStream out, String json) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.getBytes().length + "\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Connection: close\r\n\r\n" + json;
        out.write(response.getBytes());
    }

    private static void send400(OutputStream out) throws IOException {
        String response = "HTTP/1.1 400 Bad Request\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>400 - Solicitud Incorrecta</h1>";
        out.write(response.getBytes());
    }

    private static void send405(OutputStream out) throws IOException {
        String response = "HTTP/1.1 405 Method Not Allowed\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>405 - Método no permitido</h1>";
        out.write(response.getBytes());
    }

    private static void send404(OutputStream out, String path) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>404 - No encontrado</h1>" +
                "<p>Recurso no encontrado: " + path + "</p>" +
                "<a href=\"/\">Volver al inicio</a>";
        out.write(response.getBytes());
    }
}
