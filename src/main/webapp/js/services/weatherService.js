angular.module('pistachoBizi')

    .service('weatherService', ['$http', 'API', function ($http, API) {

        return ({
            start: start,
            getTowns: getTowns,
            getWeather: getWeather
        });

        function start() {
            document.getElementById('weather').innerHTML = "Soon™";
        };

        function getTowns($scope) {
            $http.get(API.URL + API.TOWNS)
                .success(function (data) {
                    $scope.towns = data.towns;
                    $scope.town = $scope.towns[291];
                    getWeather({id: 50297, town: 'Zaragoza'}, "JSON");
                });
        };

        function getWeather(town, env) {
            $http.get(API.URL + API.WEATHER + town.id + "/" + env)
                .success(function (data) {
                    document.getElementById('weather').innerHTML = data;
                });
        };
    }]);