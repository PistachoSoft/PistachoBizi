angular.module('starter')

    .controller('MainCtrl', [ '$scope', function($scope){
        var mapOptions = {
            center: { lat: 41.6590394, lng: -0.8745309},
            zoom: 14
        };

        var map = new google.maps.Map(document.getElementById('map'), mapOptions);

        $scope.origin = null;
        $scope.destination = null;

        $scope.findRoute = function(){
            console.log("=== Routing ===");
            console.log("Origin: ",$scope.origin);
            console.log("Destination: ",$scope.destination);
        };

        $scope.town = null;
        $scope.envelope = null;

        $scope.findRoute = function(){
            console.log("=== Weather ===");
            console.log("Town: ",$scope.town);
            console.log("Envelope: ",$scope.envelope);
        };
    }]);