# BMI Calculator

A sample application demonstrating the [JavaFX Builder API](https://github.com/sosuisen/javafx-builder-api-generator).

This BMI (Body Mass Index) calculator showcases modern JavaFX development with MVVM architecture, fluent UI construction, and internationalization support.

## Features

- **BMI Calculation**: Calculate BMI using height and weight inputs.
- **Unit System Support**: Switch between SI (metric) and Imperial units.
- **History Management**: Store and view BMI calculation history.
- **Multiple Languages**: Support for English and Japanese.
- **Data Visualization**: View history in various formats (list, table, chart).
- **Animation**: Animations for enhanced user experience.
- **Offline Storage**: Your health data is stored in a local database on your device.

## Screens

<img src="./images/screens.png" width="640">

## Tech Stack

- **Java 23**: Multiplatform (Windows, macOS, Linux)
- **JavaFX 24**: Modern desktop UI framework with data bindings and animations.
- **JavaFX Builder API**: Fluent API for UI construction.
  - The JavaFX Builder API is built with [JavaFX Builder API Generator](https://github.com/sosuisen/javafx-builder-api-generator).
  - Please note that this API has not yet been registered with Maven Central. You can try it out using the -SNAPSHOT version.
- **MVVM Architecture**: Clean separation of concerns.
- **SQLite + JOOQ**: Lightweight database with type-safe queries.
- **Maven**: Dependency management and build automation.

## Quick Start

⚠️ **Important**: Currently, this project depends on the SNAPSHOT version of the [JavaFX Builder API](https://github.com/sosuisen/javafx-builder-api-generator).

This SNAPSHOT will soon be discontinued and replaced by a release version. Please plan to update accordingly.

### Run the Application

- Install Java 23
- Install Maven 3

The command to execute the application is as follows:

```bash
mvn javafx:run
```

Your data is stored in the `.bmi-calculator` directory under your home directory.

## Project Structure

```
db/ # Configurations for JOOQ

diagram/ # Overview of package dependencies

src/main/java/com/example/
├── domain/          # Domain models and interfaces
├── presentation/    # UI views, view models, components
|                    # You can find useful examples about JavaFX Builder API here.
├── repository/      # Data access implementations
├── service/         # Business logic implementations
└── main/            # Application entry point

src/main/resources/com/example/
└── i18n/            # Internationalization resources

```
## Architecture

This application follows the **MVVM (Model-View-ViewModel)** pattern,
using simple dependency injection without frameworks.

<img src="diagram/diagram.png" width=320>

## Database

The application uses SQLite for local data storage with JOOQ for type-safe database access.
The database schema is automatically generated and managed through JOOQ's code generation plugin.

## Internationalization

The application supports multiple languages through Java resource bundles:
- English (default)
- Japanese

Language files are located in `src/main/resources/com/example/i18n/`.

### Build Native Installers

**Important**: This project currently contains SNAPSHOT libraries, which prevents it from building native installers correctly. Please wait for stable releases.

The project includes jpackage configuration for creating native installers.

This creates a native executable in `target/jpackage/`.

### Windows
```bash
mvn clean package -Pwin
```
Creates an APP_IMAGE or MSI installer (configure in profile)

### macOS
```bash
mvn clean package -Pmac
```
Creates a DMG or PKG installer

### Linux
```bash
mvn clean package -Punix
```
Creates an APP_IMAGE, RPM, or DEB package

## Development

### Running Tests
```bash
mvn test
```

### Code Generation

JOOQ code generation runs automatically during the `generate-sources` phase:
```bash
mvn generate-sources
```
