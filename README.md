# roony-error-rest

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roony11-1/roony-error-rest?style=flat-square)](https://search.maven.org/artifact/io.github.roony11-1/roony-error-rest)

Componentes REST comunes para el ecosistema `roony-error`. Actúa como puente entre el núcleo (`roony-error-core`) y los adaptadores de framework (`roony-error-spring`, y `roony-error-quarkus`, sin soporte activo).

Contiene el registro centralizado de mapeo de categorías de error a códigos HTTP (`HttpStatusRegistry`), asegurando consistencia en cualquier proyecto REST, independientemente del framework.

## Mapeo por defecto

| Categoría | HTTP |
|---|---|
| `NOT_FOUND` | 404 |
| `ALREADY_EXISTS` | 409 |
| `INVALID_INPUT` | 400 |
| `UNAUTHORIZED` | 401 |
| `FORBIDDEN` | 403 |
| `ACCESS_DENIED` | 401 |
| `INTERNAL_ERROR` | 500 |
| *cualquier otra* | 500 |

## Instalación

Normalmente **no se consume de forma directa**, sino a través de uno de los adaptadores:

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-spring</artifactId>
</dependency>
```

Si te hace falta (por ejemplo, para registrar categorías propias desde tu código):

```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-rest</artifactId>
    <version>1.0.2</version>
</dependency>
```

Versiones resueltas desde el BOM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>1.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.github.roony11-1</groupId>
        <artifactId>roony-error-rest</artifactId>
        <!-- sin versión: la hereda del BOM -->
    </dependency>
</dependencies>
```

## Registrar categorías personalizadas

Al iniciar tu aplicación:

```java
import io.github.roony11_1.error.rest.HttpStatusRegistry;

HttpStatusRegistry.register(MiCategoria.SALDO_INSUFICIENTE, 402); // Payment Required
```

`getStatus(category)` devuelve el código registrado o `500` si la categoría no está mapeada.

---

MIT License · [Roony11-1](https://github.com/roony11-1)