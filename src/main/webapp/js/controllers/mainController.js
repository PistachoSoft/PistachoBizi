angular.module('starter')

    .controller('MainCtrl', [ '$scope', 'geoService', 'weatherService', function($scope,geoService,weatherService){

        geoService.start();
        weatherService.start();

        //Gmaps vars
        $scope.origin = null;
        $scope.destination = null;
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
        $scope.envelope = $scope.envelopes[0];

        geoService.createAutocomplete($scope,'origin');
        geoService.placeBizis($scope);
        weatherService.getTowns($scope);

        $scope.findRoute = function(){
            if($scope.origin==null) return;
            if($scope.destination==null) return;
            console.log("=== Routing ===");
            console.log("Origin: ",$scope.origin);
            console.log("Destination: ",$scope.destination);
            geoService.findRoute($scope.origin,$scope.destination_coords.lat(),$scope.destination_coords.lng());
        };

        $scope.getWeather = function(){
            console.log("=== Weather ===");
            console.log("Town: ",$scope.town.id);
            console.log("Envelope: ",$scope.envelope.env);
            weatherService.getWeather($scope.town,$scope.envelope.env);
        };
    }]);