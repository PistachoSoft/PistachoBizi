angular.module('starter')

    .service('geoService', ['$http', 'API', function($http, API){

        var mapOptions;
        var map;
        var infowindow;

        return ({
            start: start,
            createAutocomplete: createAutocomplete,
            placeBizis: placeBizis,
            findRoute: findRoute
        });

        function start(){
            mapOptions = {
                center: { lat: 41.6590394, lng: -0.8745309},
                zoom: 14
            };
            map = new google.maps.Map(document.getElementById('map'), mapOptions);
            infowindow = new google.maps.InfoWindow();
        };

        function createAutocomplete($scope,id){
            var input = document.getElementById(id);
            var inputOptions = {
                types: ['address'],
                componentRestrictions: {country: 'es'}
            };

            var autocomplete = new google.maps.places.Autocomplete(input,inputOptions);

            google.maps.event.addListener(autocomplete, 'place_changed', function() {
                var data = document.getElementById(id).value;
                $scope.origin = data;
                //console.log(data);
                return false;
            });
        };

        function placeBizis($scope){
            $http.get(
                API.BIZIS,
                {
                    headers: {
                        'Accept': API.APP_GEOJSON
                    }
                }
            )
                .success(function(data){
                    console.log(data);
                    map.data.addGeoJson(data);
                    map.data.setStyle(function(feature) {
                        var icon = null;
                        icon = feature.getProperty('icon');
                        return {
                            icon: icon
                        };
                    });
                    map.data.addListener('click', function(event) {
                        //console.log(event.feature);
                        //console.log(event.feature.getGeometry().get());
                        //console.log(event.feature.getProperty("id"));
                        //console.log(event.feature.getProperty("title"));
                        $scope.destination = event.feature.getProperty("title");
                        $scope.destination_coords = event.feature.getGeometry().get();
                        $scope.$apply();
                        infowindow.setContent(
                            "<ul>"+
                            "<li>"+event.feature.getProperty("title")+"</li>"+
                            "<li>"+"Bicis: "+event.feature.getProperty("bicisDisponibles")+"</li>"+
                            "<li>"+"Huecos: "+event.feature.getProperty("anclajesDisponibles")+"</li>"+
                            "<li>"+"Id: "+event.feature.getProperty("id")+"</li>"+
                            "</ul>");
                        infowindow.setPosition(event.feature.getGeometry().get());
                        infowindow.setOptions({pixelOffset: new google.maps.Size(0,-30)});
                        infowindow.open(map);
                    });
                });
        };

        function findRoute(origin, dest_lat, dest_lng) {
            /*$http.get(
                "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin +
                "&destination=" + dest_lat + "," + dest_lng +
                "&key="+API.KEY,
                {
                    headers: {
                        'Access-Control-Allow-Origin': '*'
                    }
                }
            )
                .success(function (data) {
                    console.log(data);
                });*/
        }
    }]);