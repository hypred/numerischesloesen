import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WebServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {
            handleStatic(exchange);
        });

        server.createContext("/solve", exchange -> {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            String body = readAll(exchange.getRequestBody());
            Map<String, String> params = parseForm(body);

            double a = parseDouble(params.get("a"));
            double b = parseDouble(params.get("b"));
            double c = parseDouble(params.get("c"));
            double d = parseDouble(params.get("d"));

            double[] interval = loesung.hVonX(a, b, c, d);
            Double root = null;
            String msg;
            if (interval != null) {
                root = loesung.bisectRoot(a, b, c, d, interval[0], interval[1], 1e-12, 1000);
                msg = "ok";
            } else {
                msg = "kein Vorzeichenwechsel gefunden";
            }

            String json = toJson(interval, root, msg);
            byte[] out = json.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, out.length);
            OutputStream os = exchange.getResponseBody();
            os.write(out);
            os.close();
        });

        server.setExecutor(null);
        System.out.println("Server läuft: http://localhost:8080/");
        server.start();
    }

    static void handleStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/") || path.equals("/index.html")) {
            Path file = Paths.get("web", "index.html");
            byte[] bytes = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
            return;
        }

        Path file = Paths.get("web", path.startsWith("/") ? path.substring(1) : path);
        if (Files.exists(file) && !Files.isDirectory(file)) {
            String ct = Files.probeContentType(file);
            if (ct == null) ct = "application/octet-stream";
            byte[] bytes = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", ct + "; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } else {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
    }

    static String readAll(InputStream is) throws IOException {
        return new String(is.readAllBytes(), "UTF-8");
    }

    static Map<String, String> parseForm(String s) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (s == null || s.isEmpty()) return map;
        String[] pairs = s.split("&");
        for (String p : pairs) {
            String[] kv = p.split("=", 2);
            String k = URLDecoder.decode(kv[0], "UTF-8");
            String v = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
            map.put(k, v);
        }
        return map;
    }

    static double parseDouble(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }

    static String toJson(double[] interval, Double root, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"message\":\"").append(escape(msg)).append("\"");
        if (interval != null) {
            sb.append(",\"interval\":[").append(interval[0]).append(",").append(interval[1]).append("]");
        } else {
            sb.append(",\"interval\":null");
        }
        if (root != null) {
            sb.append(",\"root\":").append(root);
        } else {
            sb.append(",\"root\":null");
        }
        sb.append("}");
        return sb.toString();
    }

    static String escape(String s) { return s.replace("\"", "\\\""); }
}
