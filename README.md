# roony-error-rest

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roony11-1/roony-error-rest?style=flat-square)](https://search.maven.org/artifact/io.github.roony11-1/roony-error-rest)

Componentes REST comunes para el ecosistema `roony-error`. Actúa como puente entre el núcleo (`roony-error-core`) y los adaptadores de framework (`roony-error-spring`, y `roony-error-quarkus`, sin soporte activo).

Contiene el mapeo de categorías de error a códigos HTTP mediante `HttpStatusMapper`, una abstracción inyectable que asegura consistencia en cualquier proyecto REST, independientemente del framework.

## Mapeo por defecto

`DefaultHttpStatusMapper` provee el siguiente mapeo para `StandardErrorCategories`:

| Categoría | HTTP |
|---|---|
| `NOT_FOUND` | 404 |
| `ALREADY_EXISTS` | 409 |
| `INVALID_INPUT` | 400 |
| `UNAUTHORIZED` | 401 |
| `FORBIDDEN` | 403 |
| `INTERNAL_ERROR` | 500 |
| `CONFLICT` | 409 |
| `SERVICE_UNAVAILABLE` | 503 |
| *cualquier otra* | 500 |

El fallback para categorías no mapeadas es **500** (error interno), no 400: si el status no está determinado, el servidor es el responsable.

## Instalación

Normalmente **no se consume de forma directa**, sino a través de uno de los adaptadores:

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-spring</artifactId>
</dependency>
```

# Si te hace falta (por ejemplo, para mapear categorías propias):

```xml
<dependency>
    <groupId>io.github.roony11-1</groupId>
    <artifactId>roony-error-rest</artifactId>
    <version>2.0.0</version>
</dependency>
```

# Versiones resueltas desde el BOM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.roony11-1</groupId>
            <artifactId>roony-bom</artifactId>
            <version>2.0.0</version>
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

# Uso
    IHttpStatusMapper es una interfaz con un solo método. DefaultHttpStatusMapper es la implementación incluida, con el mapeo por defecto.

```java
import io.github.roony11_1.error.rest.HttpStatusMapper;
import io.github.roony11_1.error.rest.DefaultHttpStatusMapper;
import io.github.roony11_1.error.core.StandardErrorCategories;

HttpStatusMapper mapper = new DefaultHttpStatusMapper();

int status = mapper.getStatus(StandardErrorCategories.NOT_FOUND); // 404
int fallback = mapper.getStatus(MiCategoria.SALDO_INSUFICIENTE);  // 500
```

# Mapear categorías personalizadas

    El mapeo custom se pasa por constructor, no se registra globalmente. Cada instancia tiene su propio mapeo; dos instancias con configuraciones distintas coexisten sin pisarse.

```java
import io.github.roony11_1.error.rest.DefaultHttpStatusMapper;
import java.util.Map;

HttpStatusMapper mapper = new DefaultHttpStatusMapper(
    Map.of(
        MiCategoria.SALDO_INSUFICIENTE, 402,   // Payment Required
        MiCategoria.RATE_LIMITED, 429          // Too Many Requests
    )
);

mapper.getStatus(MiCategoria.SALDO_INSUFICIENTE);    // 402
mapper.getStatus(StandardErrorCategories.NOT_FOUND); // 404 (default)
```

Los mapeos custom pisan los defaults para la misma categoría, pero no afectan a las demás categorías ni a otras instancias del mapper.

# Por qué no hay register()
    Las versiones anteriores de este módulo exponían HttpStatusRegistry.register(category, status) sobre un mapa estático global. Ese diseño causaba que:

    Dos partes de la aplicación que registraban mapeos distintos se pisaran.

    Los tests dependieran del orden de ejecución (estado global compartido).

    Cualquier código pudiera mutar el mapeo en runtime sin control.

    IHttpStatusMapper reemplaza ese diseño con una abstracción instanciable. Si necesitás un mapper custom (por ejemplo, uno que lea de YAML o de base de datos), implementá HttpStatusMapper directamente:

```java
public class YamlHttpStatusMapper implements HttpStatusMapper 
{
    private final Map<ErrorCategory, Integer> mappings;

    public YamlHttpStatusMapper(Map<ErrorCategory, Integer> mappings) 
    {
        this.mappings = mappings;
    }

    @Override
    public int getStatus(ErrorCategory category) 
    {
        return mappings.getOrDefault(category, 500);
    }
}
```

# Integración con frameworks
    roony-error-rest es framework-agnostic: no depende de Spring, Quarkus ni Jakarta. Los adaptadores de framework (roony-error-spring, roony-error-quarkus) consumen este módulo y agregan la capa específica de cada framework.

    Para un adaptador HTTP genérico (sin framework), el consumidor compone HttpStatusMapper con el ErrorHandler del core y serializa el ErrorResponse resultante con su librería JSON preferida.

# Migración desde 1.x

| 1.x | 2.0 |
| --- | --- |
| `HttpStatusRegistry.getStatus(c)` (estático) | `new DefaultHttpStatusMapper().getStatus(c)` (instancia) |
| `HttpStatusRegistry.register(c, status)` (mutaba estado global) | `new DefaultHttpStatusMapper(Map.of(c, status))` (inyección por constructor) |
| `ACCESS_DENIED` → 401 | `ACCESS_DENIED` eliminado del enum; usar `FORBIDDEN` → 403 |
| Fallback 500 | Fallback 500 (sin cambios) |

## Si necesitás mantener compatibilidad con HttpStatusRegistry, podés envolver el mapper:

```java
@Deprecated
public final class HttpStatusRegistry 
{
    private static final HttpStatusMapper DELEGATE = new DefaultHttpStatusMapper();

    public static int getStatus(ErrorCategory category) 
    {
        return DELEGATE.getStatus(category);
    }

    private HttpStatusRegistry() {}
}
```

### Pero para un release 2.0.0, se recomienda migrar al mapper instanciable directamente.

MIT License · Roony11-1