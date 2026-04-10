# HabitTrackerCompose 🚀

Una aplicación moderna de seguimiento de hábitos construida con **Jetpack Compose** y una arquitectura escalable.

## 📋 Resumen del Proyecto

La aplicación permite a los usuarios registrar hábitos diarios, visualizar su progreso mensual en una cuadrícula intuitiva y realizar un seguimiento de sus rachas (streaks) actuales.

### Características Principales:
- **Vista Mensual:** Cuadrícula interactiva para marcar hábitos completados.
- **Cálculo de Rachas:** Lógica automática para determinar cuántos días seguidos se ha cumplido un hábito.
- **Gestión de Hábitos:** Añadir nuevos hábitos con emojis personalizados y eliminar los existentes.
- **Persistencia Local:** Almacenamiento robusto mediante Room Database.

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** y los principios de Clean Architecture en su capa de datos:

- **UI (Jetpack Compose):** Componentes declarativos y reactivos.
- **ViewModel:** Gestión del estado de la UI y lógica de negocio (rachas, fechas).
- **Repository Pattern:** Abstracción de datos con contratos (`HabitRepositoryContract`) para facilitar el desacoplamiento.
- **Local Data (Room):** Entidades, DAOs y base de datos relacional para registros de hábitos.

## 🧪 Próximos Pasos: Estrategia de Testing

Para asegurar la calidad y estabilidad del código, se ha definido el siguiente plan de pruebas:

### 1. Unit Tests (Lógica de Negocio)
- **HabitsViewModelTest:** 
    - Validar el cálculo de `calculateCurrentStreak` con diferentes combinaciones de días.
    - Asegurar que el estado del mes se genere correctamente según la fecha actual.
- **Repository Logic:**
    - Probar que la lógica de transformación de datos entre la DB y la UI sea precisa.

### 2. Integration Tests (Persistencia)
- **Room DAO Tests:**
    - Verificar consultas complejas de JOIN entre hábitos y registros diarios.
    - Confirmar que las operaciones CRUD funcionen como se espera.

### 3. UI Tests (Componentes)
- **Compose Previews & Screenshot Testing:** Visualizar componentes en diferentes estados.
- **Interactions:** Simular clicks en las celdas de hábito y verificar que el estado cambie visualmente.

---
Desarrollado con ❤️ en Kotlin y Jetpack Compose.
