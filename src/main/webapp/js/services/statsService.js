angular.module('pistachoBizi')

    .service('statsService', ['$http', 'API', function($http,API){

        var mapOptions;
        var map;
        var once = false;

        return ({
            //constants
            WEA: 'weather',
            INF: 'info',
            BRO: 'browser',
            ENV: 'envelope',
            //functions
            log: log,
            load: load
        });

        function log(method, params) {
            if(method==this.BRO && !once){
                once=true;
            }else{
                return;
            }
            var tmp = {
                method: method,
                data: params
            };

            $http.post(API.URL + API.STATS,JSON.stringify(tmp))
                .success(function(data){
                    console.log(tmp);
                });
        }

        function load($scope) {

            $http.get(API.URL+API.STATS+this.ENV).success(function(data){
                $scope.env_data.push(data.stats.json);
                $scope.env_data.push(data.stats.xml);
                $scope.env_data = [$scope.env_data];
            });

            $http.get(API.URL+API.STATS+this.BRO).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.browser_labels.push(data.stats[i].browser);
                    $scope.browser_data.push(data.stats[i].data);
                }
            });

            $http.get(API.URL+API.STATS+this.WEA).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.weather_labels.push(data.stats[i].town);
                    $scope.weather_data.push(data.stats[i].data);
                }
            });

            $http.get(API.URL+API.STATS+this.INF).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.info_labels.push(data.stats[i].station);
                    $scope.info_data.push(data.stats[i].data);
                }
            });
        }
    }]);
