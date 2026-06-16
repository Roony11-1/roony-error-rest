# roony-error-rest

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roony11-1/roony-error-rest?style=flat-square)](https://search.maven.org/artifact/io.github.roony11-1/roony-error-rest)

Componentes REST comunes para el ecosistema `roony-error`.  
Actúa como puente entre el núcleo (`roony-error-core`) y los adaptadores de framework (`roony-error-quarkus`, `roony-error-spring`). Contiene el registro centralizado de mapeo de categorías de error a códigos HTTP (`HttpStatusRegistry`), asegurando consistencia en cualquier proyecto REST que uses.

## Instalación

Normalmente no se consume de forma directa, sino a través de uno de los adaptadores:

```xml
<!-- Quarkus -->
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-quarkus</artifactId>
</dependency>

<!-- Spring Boot -->
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-spring</artifactId>
</dependency>
```
```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-rest</artifactId>
    <version>1.0.1</version>
</dependency>
```

Resolviendo las versiones desde el BOM

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```