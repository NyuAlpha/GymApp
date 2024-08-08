# Changelog

## [Alpha 1.2] - 2024-08-08

### Bugs resueltos:
- **No se pueden modificar nombres de ejercicios**:
    - Reestablecido el funcionamiento de `updateExercise`.
- **Arreglado el problema al eliminar series en los ejercicios**:
    - Ahora hay un método para reestablecer el orden de todas las series posteriores a la eliminada.
- **No aparece la última serie clonada en el formulario en algunas ocasiones**:
    - El error estaba en `GymSetRepository`, concretamente en `gs.id.exercise.id`, ahora es `gs2.id.exercise.id`.
- **No se pueden borrar ejercicios**:
    - El error se debía a que el método `delete` del controlador no recibía el parámetro `trainingId` desde la vista.

### Modificaciones:
- **Resumen de series integrado en la vista con el ejercicio**:
    - Ahora el resumen de series aparece integrado en la vista con el ejercicio, en lugar de sobre la tabla de series.