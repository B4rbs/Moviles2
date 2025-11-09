![portada detalle](https://github.com/user-attachments/assets/b0f580a0-3ca0-4655-b0ec-26a612736bcc)
![portada lista](https://github.com/user-attachments/assets/c2436d95-194c-4c89-968b-a374a8793565)
![portada](https://github.com/user-attachments/assets/d2901f79-0693-42ad-a8d6-319c3a31d911)

# moviles2_1erParcial - Cat App

App Android (Kotlin) que muestra una lista de razas de gatos desde TheCatAPI, permite abrir un detalle con información básica y carga una imagen representativa de la raza seleccionada. Incluye buscador de razas en la pantalla principal.

✨ Funcionalidades
```
Lista de razas (nombre y país de origen).
Buscador por nombre (filtrado en tiempo real).
Pantalla de detalle con descripción, temperamento, esperanza de vida y origen.
Imagen representativa cargada de forma asíncrona.
Botón de volver en la toolbar del detalle.
Estados de carga, vacío y error (con Toast).
Accesibilidad básica (contentDescription en la imagen).
```

🧱 Arquitectura (resumen)
```
Single-Activity + Fragments.
Navigation Component con nav_graph para manejar transiciones y argumentos.
MainActivity aloja un FragmentContainerView.
BreedsListFragment: muestra la lista, maneja el buscador y pide datos a la API.
BreedDetailFragment: muestra el detalle y carga una imagen de la raza.
ViewBinding habilitado para acceso tipado a vistas.
Service Locator simple que expone una instancia de Retrofit.
```

🌐 API utilizada
```
Base URL: https://api.thecatapi.com/v1/

Endpoints:
GET /breeds → lista de razas (List<BreedDTO>).
GET /images/search?breed_ids={id}&limit=1 → imagen para una raza (List<String> con URLs).
```

🔄 Flujo de datos
```
BreedsListFragment muestra un loader.
Se llama a ServiceLocator.api.getBreeds().
Se actualiza el RecyclerView con BreedAdapter.
El buscador filtra la lista por nombre en memoria.
Al tocar una raza, se navega a BreedDetailFragment pasando argumentos (id, nombre, etc.).
En detalle, se muestran textos y se carga 1 imagen con Coil (crossfade + placeholder).
```

🧠 Conceptos usados
```
DTO (Data Transfer Object): clases de datos que mapean el JSON de la API (sin lógica).
ViewBinding: acceso tipado a vistas; evita findViewById.
Coroutines: llamadas asíncronas con lifecycleScope.
Coil: carga de imágenes con caching y animación suave.
MaterialToolbar: toolbar con botón de back y título.
Navigation Component: navegación entre fragments y paso de argumentos.
```

