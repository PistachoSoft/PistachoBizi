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
            }else if(method==this.BRO && once){
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

            $http.get(API.URL+API.STATS+"/"+this.ENV).success(function(data){
                //$scope.env_data.push(data.stats.json);
                //$scope.env_data.push(data.stats.xml);
                for(var i = 0; i < data.stats.length; i++){
                    if(data.stats[i].data=="JSON"||data.stats[i].data=="XML") {
                        $scope.env_labels.push(data.stats[i].data);
                        $scope.env_data.push(data.stats[i].number);
                    }
                }
                $scope.env_data = [$scope.env_data];
            });

            $http.get(API.URL+API.STATS+"/"+this.BRO).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.browser_labels.push(data.stats[i].data);
                    $scope.browser_data.push(data.stats[i].number);
                }
            });

            $http.get(API.URL+API.STATS+"/"+this.WEA).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.weather_labels.push(data.stats[i].data);
                    $scope.weather_data.push(data.stats[i].number);
                }
            });

            $http.get(API.URL+API.STATS+"/"+this.INF).success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.info_labels.push(unescape(data.stats[i].data));
                    $scope.info_data.push(data.stats[i].number);
                }
            });
        }
    }]);
