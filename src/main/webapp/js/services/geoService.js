angular.module('pistachoBizi')

    .service('geoService', ['$http', 'API', function($http, API){

        var mapOptions;
        var map;
        var infowindow;
        var directionsDisplay;

        return ({
            start: start,
            createAutocomplete: createAutocomplete,
            placeBizis: placeBizis,
            getInfoBizi: getInfoBizi,
            findRoute: findRoute,
            geolocate: geolocate
        });

        function start(){
            mapOptions = {
                center: { lat: 41.6590394, lng: -0.8745309},
                zoom: 14
            };
            map = new google.maps.Map(document.getElementById('map'), mapOptions);
            infowindow = new google.maps.InfoWindow();
            directionsDisplay = new google.maps.DirectionsRenderer();
            directionsDisplay.setMap(map);
            directionsDisplay.setOptions( { suppressMarkers: true } );
        };

        function createAutocomplete($scope,id){
            var input = document.getElementById(id);
            var inputOptions = {
                bounds: new google.maps.LatLngBounds(new google.maps.LatLng(41.6590394,-0.8745309),new google.maps.LatLng(41.6590394,-0.8745309)),
                types: ['address'],
                componentRestrictions: {country: 'es'}
            };

            var autocomplete = new google.maps.places.Autocomplete(input,inputOptions);

            google.maps.event.addListener(autocomplete, 'place_changed', function() {
                var data = document.getElementById(id).value;
                $scope.origin = data;
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
                    map.data.addGeoJson(data);
                    map.data.setStyle(function(feature) {
                        var icon = null;
                        icon = feature.getProperty('icon');
                        return {
                            icon: icon
                        };
                    });
                    map.data.addListener('click', function(event) {
                        $scope.destination = event.feature.getProperty("title");
                        $scope.destination_id = event.feature.getProperty("id");
                        $scope.destination_coords = event.feature.getGeometry().get();
                        $scope.$apply();
                        $scope.info(event);
                    });
                });
        };

        function getInfoBizi(event, id, env) {
            if(env == "JSON") {
                $http.get(API.BIZI + id + '.' + 'json' + API.BIZI_END).success(function (data) {
                    infowindow.setContent(
                        "<ul class='infobizi'>" +
                        "<li>" + data.title + "</li>" +
                        "<li>" + "Bicis: " + data.bicisDisponibles + "</li>" +
                        "<li>" + "Huecos: " + data.anclajesDisponibles + "</li>" +
                        "<li>" + "Id: " + data.id + "</li>" +
                        "</ul>"
                    );
                    infowindow.setPosition(event.feature.getGeometry().get());
                    infowindow.setOptions({pixelOffset: new google.maps.Size(0, -30)});
                    infowindow.open(map);
                });
            }else {
                $http.get(API.BIZI + id + '.' + 'xml' + API.BIZI_END).success(function (data) {
                    var xml = $.parseXML(data);
                    var parsed = $(xml);
                    infowindow.setContent(
                        "<ul class='infobizi'>" +
                        "<li>" + parsed.find('title').text() + "</li>" +
                        "<li>" + "Bicis: " + parsed.find('bicisDisponibles').text() + "</li>" +
                        "<li>" + "Huecos: " + parsed.find('anclajesDisponibles').text() + "</li>" +
                        "<li>" + "Id: " + parsed.find('id').text() + "</li>" +
                        "</ul>"
                    );
                    infowindow.setPosition(event.feature.getGeometry().get());
                    infowindow.setOptions({pixelOffset: new google.maps.Size(0, -30)});
                    infowindow.open(map);
                });
            }
        };

        function findRoute(origin, dest_lat, dest_lng) {
            var directionsService = new google.maps.DirectionsService();
            var request = {
                origin:origin,
                destination: new google.maps.LatLng(dest_lat,dest_lng),
                travelMode: google.maps.TravelMode.WALKING
            };
            directionsService.route(request, function(response, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(response);
                }
            });
        };

        function geolocate($scope){
            if(navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    var pos = new google.maps.LatLng(position.coords.latitude,
                        position.coords.longitude);

                    $scope.origin = position.coords.latitude+","+position.coords.longitude;
                    $scope.$apply();
                    //infowindow = new google.maps.InfoWindow({
                    //    map: map,
                    //    position: pos,
                    //    content: 'You are here!'
                    //});
                    infowindow.setContent('You are here!');
                    infowindow.setPosition(pos);
                    infowindow.open(map);
                    map.setCenter(pos);
                }, function() {
                    console.log("HAHA NO GEOLOCATION 4 U PUSSY");
                });
            } else {
                // Browser doesn't support Geolocation
                console.log("HAHA NO GEOLOCATION 4 U PUSSY");
            }
        };
    }]);