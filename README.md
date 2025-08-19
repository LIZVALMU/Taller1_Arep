# Taller 1 Diseño y estructuración de aplicaciones distribuidas en internet

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)

## Introducción

Este es un prototipo basado en Java diseñado para implementar un servidor web usando sockets puros, sin frameworks externos. El objetivo es proporcionar una plataforma funcional capaz de gestionar recursos como:

- Atender múltiples solicitudes HTTP concurrentes mediante hilos (threads)
- Servir archivos estáticos del disco: páginas HTML, scripts JavaScript, hojas de estilo CSS e imágenes
- Proporcionar servicios REST asíncronos implementados directamente con sockets Java
- Comunicación bidireccional entre frontend y backend mediante APIs JSON

## Autor

[Tu nombre aquí]

## Tecnologías Utilizadas

| Tecnología      | Versión       | Propósito                   |
|-----------------|---------------|-----------------------------|
| Java            | 17            | Desarrollo backend          |
| Sockets         | Java Nativo   | Comunicación de red         |
| HTTP/1.1        | -             | Implementación de protocolo |
| JSON            | Nativo        | Respuestas API              |


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



- The Escuela Colombiana de Ingeniería community for inspiration.

---

<div align="center">
  <b>a/b><br>
  <i>Empowering well-being through technology</i>
</div>