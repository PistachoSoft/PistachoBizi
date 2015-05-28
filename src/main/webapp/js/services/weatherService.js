angular.module('starter')

    .service('weatherService', ['$http', function($http){

        return ({
            start: start,
            getWeather: getWeather
        });

        function start(){
            document.getElementById('weather').innerHTML = "Soon™";
        };

        function getWeather(town,env){

        };
    }]);