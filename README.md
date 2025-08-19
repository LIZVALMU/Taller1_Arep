# Taller 1 Diseño y estructuración de aplicaciones distribuidas en internet

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)

## Introducción

Este es un prototipo basado en Java diseñado para implementar un servidor web usando sockets puros, sin frameworks externos.  
El objetivo es proporcionar una plataforma funcional capaz de gestionar recursos como:

- Atender múltiples solicitudes HTTP secuenciales o concurrentes (según la configuración con threads).  
- Servir archivos estáticos del disco: páginas HTML, scripts JavaScript, hojas de estilo CSS e imágenes.  
- Proporcionar servicios REST asíncronos implementados directamente con sockets Java.  
- Comunicación bidireccional entre frontend y backend mediante APIs JSON.  

---

## Autor

- **Alison Valderrama**

---

## Tecnologías Utilizadas

| Tecnología      | Versión       | Propósito                   |
|-----------------|---------------|-----------------------------|
| Java            | 17            | Desarrollo backend          |
| Sockets         | Java Nativo   | Comunicación de red         |
| HTTP/1.1        | -             | Implementación de protocolo |
| JSON            | Nativo        | Respuestas API              |

---

## Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/LIZVALMU/arep_httpserver.git
   cd arep_httpserver
   ```

2. Compilar el proyecto (Maven):
   ```bash
   mvn clean package
   ```

3. Asegurarse de tener instalado:
   - **Java 17**
   - **Maven 3.x**

---

## Ejecución

1. Ejecutar el servidor:
   ```bash
   java -cp target/arep_httpserver-1.0-SNAPSHOT.jar escuela.edu.co.HttpServer
   ```

2. Abrir en un navegador:
   ```
   http://localhost:35000/index.html
   ```

   > El servidor por defecto corre en el puerto `35000`.  

3. Para probar los servicios REST (ejemplo):
   - GET: `http://localhost:35000/hello?name=Alison`
   - POST: `http://localhost:35000/hellopost?name=Alison`

---

## Arquitectura del Prototipo

El sistema está compuesto por tres módulos principales:

1. **Servidor HTTP (HttpServer.java)**  
   - Maneja las conexiones entrantes mediante sockets TCP.  
   - Interpreta las solicitudes HTTP y envía respuestas con código de estado, cabeceras y cuerpo.  

2. **Gestor de Recursos Estáticos (www/)**  
   - Contiene las páginas HTML, archivos JavaScript, CSS e imágenes.  
   - El servidor localiza y devuelve estos recursos según la ruta solicitada.  

3. **Módulo REST**  
   - Endpoints personalizados (`/hello`, `/hellopost`, etc.).  
   - Devuelven datos en formato JSON para ser consumidos por el cliente.  
   - Probados con llamadas asíncronas desde `app.js` en el frontend.  

---

## Evaluación (Pruebas Realizadas)

- **Prueba de archivos estáticos**:  
  Verificación de que el servidor devuelve correctamente `index.html`, `style.css`, `app.js` y recursos multimedia.  

- **Prueba de servicios REST (GET y POST)**:  
  - `GET /hello?name=John` → Respuesta con saludo personalizado.  
  - `POST /hellopost?name=John` → Respuesta en formato JSON.  

- **Prueba de concurrencia**:  
  Ejecución con múltiples clientes enviando solicitudes simultáneas para validar que el servidor puede atenderlas con hilos.  

- **Prueba de navegador**:  
  Acceso al frontend (`index.html`) y comunicación exitosa con el backend mediante AJAX y Fetch API.  

---

## Project Structure

```bash
arep_httpserver/
├── src/
│   └── main/
│       └── java/
│           └── escuela/
│               └── edu/
│                   └── co/
│                       └── HttpServer.java    # Servidor HTTP principal
└── www/                                        # Archivos estáticos
    ├── index.html                             # Página principal
    ├── app.js                                 # JavaScript frontend
    ├── style.css                              # Estilos CSS
    └── logo.png                               # Recursos multimedia
```

---

- The Escuela Colombiana de Ingeniería community for inspiration.