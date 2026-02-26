# Magister - Gestión Académica

Aplicación de gestión académica para gestionar alumnos y asignaturas.

## Requisitos

- Java 17+
- Maven 3.6+

## Compilación

```bash
mvn compile
```

## Crear fat-jar

```bash
mvn package
```

Esto genera el archivo `target/magister-1.0-SNAPSHOT-fatjar.jar`.

## Ejecución

```bash
java -jar target/magister-1.0-SNAPSHOT-fatjar.jar
```

## Uso

### Menú Datos

- **Abrir archivo**: Abre un archivo de base de datos SQLite existente
- **Exportar archivo**: Guarda la base de datos actual en un nuevo archivo
