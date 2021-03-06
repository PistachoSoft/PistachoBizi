angular.module('pistachoBizi')

    .controller('MainCtrl', ['$scope', 'geoService', 'weatherService', 'statsService',
        function ($scope, geoService, weatherService, statsService) {

            geoService.start();
            weatherService.start();
            statsService.log(statsService.BRO,null);

            //Gmaps vars
            $scope.origin = null;
            $scope.destination = null;
            $scope.destination_id = null;
            $scope.destination_coords = null;

            //Weather vars
            $scope.town = null;
            $scope.towns = [];
            $scope.envelopes = [
                {
                    env: "JSON"
                },
                {
                    env: "XML"
                }
            ];
            $scope.weaEnvelope = $scope.envelopes[0];
            $scope.infEnvelope = $scope.envelopes[0];

            geoService.createAutocomplete($scope, 'origin');
            geoService.placeBizis($scope);
            weatherService.getTowns($scope);

            $scope.info = function (event) {
                //console.log("=== Info ===");
                //console.log("Id: ",$scope.destination_id);
                //console.log("Envelope: ",$scope.infEnvelope.env);
                geoService.getInfoBizi(event, $scope.destination_id, $scope.infEnvelope.env);
                statsService.log(statsService.ENV,$scope.infEnvelope.env);
                statsService.log(statsService.INF,escape($scope.destination + " [" + $scope.destination_id + "]"));
            };

            $scope.findRoute = function () {
                if ($scope.origin == null) return;
                if ($scope.destination == null) return;
                //console.log("=== Routing ===");
                //console.log("Origin: ",$scope.origin);
                //console.log("Destination: ",$scope.destination);
                geoService.findRoute($scope.origin, $scope.destination_coords.lat(), $scope.destination_coords.lng());
            };

            $scope.getWeather = function () {
                //console.log("=== Weather ===");
                //console.log("Town: ",$scope.town.id);
                //console.log("Envelope: ",$scope.weaEnvelope.env);
                weatherService.getWeather($scope.town, $scope.weaEnvelope.env);
                statsService.log(statsService.ENV,$scope.weaEnvelope.env);
                statsService.log(statsService.WEA,$scope.town.town);
            };

            $scope.geolocate = function () {
                //console.log("=== Geolocate ===");
                geoService.geolocate($scope);
            };
        }]);