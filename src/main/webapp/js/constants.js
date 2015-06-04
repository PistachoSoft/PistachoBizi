angular.module('pistachoBizi')

    .constant('API', {
        'KEY':'AIzaSyC9RPylLn5DtBKnmFoSLLvNcRdnHCGmAKg',
        'APP_GEOJSON': 'application/geo+json',
        'BIZIS': 'http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/estacion-bicicleta?srsname=wgs84&start=0&rows=500',
        'BIZI': 'http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/estacion-bicicleta/',
        'BIZI_END': '?srsname=wgs84',
        'URL': 'http://'+location.host+'/bizi/api',
        'TOWNS': '/towns',
        'WEATHER': '/weather/',
        'STATS': '/stats/'
    });