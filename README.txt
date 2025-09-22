![portada](https://github.com/user-attachments/assets/d266fa5f-ebde-4cde-9a2a-444f0d59a766)
![portada](https://github.com/user-attachments/assets/d2901f79-0693-42ad-a8d6-319c3a31d911)

# moviles2_1erParcial - Cat App

App Android (Kotlin) que muestra una lista de razas de gatos desde TheCatAPI, permite abrir un detalle con información básica y carga una imagen representativa de la raza seleccionada.

✨ Funcionalidades
```
Lista de razas (nombre y país de origen).
Pantalla de detalle con descripción, temperamento, esperanza de vida y origen.
Imagen representativa cargada de forma asíncrona.
Botón de volver en la toolbar del detalle.
Estados de carga, vacío y error (con Toast).
Accesibilidad básica (contentDescription en la imagen).
```

🧱 Arquitectura (resumen)
```
Single-Activity + Fragments:
MainActivity aloja un FragmentContainerView.
BreedsListFragment muestra la lista y pide datos a la API.
BreedDetailFragment muestra el detalle e imagen.
Sin Navigation Component: navegación manual con FragmentManager y addToBackStack.
ViewBinding habilitado.
Service Locator simple para exponer una instancia de Retrofit.
```

🌐 API utilizada
```
Base URL: https://api.thecatapi.com/v1/
Endpoints:
GET /breeds → lista de razas (List<BreedDTO>).
GET /images/search?breed_ids={id}&limit=1 → imagen para una raza (List<ImageDTO>).
```

🔄 Flujo de datos
```
BreedsListFragment muestra un loader.
Se llama a ServiceLocator.api.getBreeds().
Se actualiza el RecyclerView con BreedAdapter.
Al tocar una raza, se abre BreedDetailFragment pasando argumentos por Bundle.
En detalle, se muestran textos y se carga 1 imagen con Coil (crossfade).
```

🧠 Conceptos usados
```
DTO (Data Transfer Object): clases de datos que mapean el JSON de la API (no contienen lógica).
ViewBinding: acceso tipado a vistas; evita findViewById.
Coroutines: llamadas asíncronas con lifecycleScope.
Coil: carga de imágenes con caching y animación suave.
MaterialToolbar: toolbar con botón de back y título.
```

