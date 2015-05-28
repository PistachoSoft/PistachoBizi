angular.module('starter')

    .controller('MainCtrl', [ '$scope', 'geoService', 'weatherService', function($scope,geoService,weatherService){

        geoService.start();
        weatherService.start();

        geoService.createAutocomplete($scope,'origin');
        geoService.placeBizis($scope);

        $scope.origin = null;
        $scope.destination = null;
        $scope.destination_coords = null;

        $scope.findRoute = function(){
            if($scope.origin==null) return;
            if($scope.destination==null) return;
            console.log("=== Routing ===");
            console.log("Origin: ",$scope.origin);
            console.log("Destination: ",$scope.destination);
            geoService.findRoute($scope.origin,$scope.destination_coords.lat(),$scope.destination_coords.lng());
        };

        $scope.town = null;
        $scope.envelope = null;

        $scope.getWeather = function(){
            console.log("=== Weather ===");
            console.log("Town: ",$scope.town);
            console.log("Envelope: ",$scope.envelope);
            weatherService.getWeather($scope.town,$scope.envelope);
        };
    }]);