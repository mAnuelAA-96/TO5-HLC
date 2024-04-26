TO5 - HLC

Lo primero que debemos hacer es preparar el proyecto para que se puedan utilizar los mapas. Para ello, añadimos las dependencias necesarias para utilizar los mapas de Google y sus útiles. Son las siguientes:

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.0")

Para poder utilizar los servicios de Google es necesario registrarse para el uso de APIs. Una vez registrados recibimos el API KEY, el cual almacenaremos en un archivo xml en la carpeta res/value. Este archivo se ha llamado google_maps_api. Una vez hecho esto, debemos añadir al manifest la etiqueta <meta-data/> con los valores correspondientes dentro de <application/>. Quedaría así:

        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key"/>

Una vez configurado el proyecto correctamente, podemos empezar a darle forma.

El layout correspondiente a la actividad está compuesto por un MaterialToolbar y un Fragment que cargará el mapa. He creado el directorio menu con el layout menu_contextual.xml para añadir los elementos del menú con sus distintas funciones.

El toolbar se inicializa mediante la clase MyToolbar, encargada de iniciarlo. Esta clase recibe un solo parámetro, el contexto, quedando preparada por si se quisieran añadir más elementos como un título para el toolbar y para facilitar iniciarlo en más activities si fuera necesario. Esta clase es llamada en el método onCreate() del MainActivity. En este método también se crea el supportMapFragment mediante el método createFragment().

Para inflar el menú de opciones, se utiliza el método onCreateOptionsMenu(), el cual se sobreescribe pasándole el layout del menú que hemos creado anteriormente. Además, para otorgar funcionalidad a las diferentes opciones del menú, se utiliza el método onOptionsItemSelected().

Comenzamos con el mapa. Para cargarlo en el fragment se utiliza el método onMapReady(). En este método, además de asignar el mapa de Google Maps a la variable creada para la clase, se añade también el onMapLongClickListener y el onMapClickListener. Estos listeners llamarán a los métodos primerMarcador() y addPolyline() respectivamente. La última acción del método es la creación de un marcador y el establecimiento de un zoom de 4 segundos para facilitar las pruebas en la app.

Pasamos al método primerMarcador(), llamado en el onMapLongClickListener. Este método se encarga de recoger las coordenadas del punto seleccionado y lo almacena en la variable de la clase puntoInicial para guardar dicho punto y posteriormente cerrar el área seleccionada. Cuando se llama a este método, todos los marcadores y líneas del mapa son eliminadas, ya que se da por hecho que se creará una nueva zona. Al tratarse de una nueva zona, si la variable polylineOptions contiene algún punto guardado se elimina y se comienza de nuevo. Además, se establece un marcador en este punto. Si no hubiera ningún punto guardado simplemente comienza a añadirlos. Se ha establecido también el color de las líneas a rojo. Por último en este método, se añaden las polylineas al mapa, se establece para el punto de inicio un círculo y se añade también el marcador del punto.

Seguimos con el método addPolyline(), llamado en el onMapClickListener. Este método se encarga de seguir añadiendo puntos al mapa. En caso de no haber establecido un punto inicial mediante la pulsación larga, se muestra un mensaje indicando cómo se debe empezar a marcar la zona. En caso contrario, añade las polylineas correspondientes para dibujar la zona en el mapa.

Por último necesitamos las funciones correspondientes para calcular el área, el perímetro o borrar las marcas en el mapa. Para calcular el área se usa el método calcularArea(), el cual comprueba que haya al menos 3 puntos seleccionados, de lo contrario daría el área 0 metros. Si no se han seleccionado 3 puntos se muestra un mensaje indicando lo que se debe hacer. En caso contrario, se cierra la zona utilizando la coordenada almacenada en la variable puntoInicial y se calcula el área mediante el método computeArea() de la clase SphericalUtil propio de los útiles de los mapas de android. El área se muestra mediante un toast.

El perímetro se calcula en el método calcularPerimetro(). Primero comprueba que se hayan seleccionado al menos 2 puntos, de lo contrario muestra un mensaje indicando el error. Al igual que en el método para calcular el área, se utiliza la clase SphericalUtil con el método computeLength(), pasándole los puntos de la variable polylineOptions. En este caso he optado por dividir el resultado entre 2, ya que al utilizar el método cierra la zona en el punto inicial al igual que el método anterior y calcularía el perímetro de la "misma" línea dos veces. He decidido dividirlo para dar como resultado simplemente el tramo marcado por el usuario.

Por último, el método borrarMarcas() simplemente borra los puntos almacenados en la variable polylineOptions y las marcas del mapa.


CAPTURAS DE LA APLICACIÓN

- Estado inicial

![Estado inicial](https://manuelflo.com/images/to5-hlc/estado-inicial.png)


- Coordenadas de origen

![Coordenadas de origen](https://manuelflo.com/images/to5-hlc/punto-origen.png)


- Cálculo del área (pulsar enlace si da error)
https://manuelflo.com/images/to5-hlc/calculo-area.png
  
![Cálculo del área](https://manuelflo.com/images/to5-hlc/calculo-area.png)

  
- Cálculo del perímetro
  
![Cálculo del perímetro](https://manuelflo.com/images/to5-hlc/calculo-perimetro.png)
