angular.module('pistachoBizi')

    .service('statsService', ['$http', 'API', function($http,API){

        var mapOptions;
        var map;
        var once = false;

        return ({
            //constants
            GEO: 'geolocation',
            ROU: 'route',
            WEA: 'weather',
            INF: 'info',
            BRO: 'browser',
            //functions
            log: log,
            loadGeneral: loadGeneral,
            loadInfo: loadInfo,
            loadRoute: loadRoute,
            loadInfoDays: loadInfoDays,
            loadRouteDays: loadRouteDays,
            loadHeatMap: loadHeatMap
        });

        function log(method, params) {
            if(method==this.BRO && !once){
                once=true;
            }else{
                return;
            }
            var tmp = {
                method: method,
                params: params//,
                //location: geolocate(),
                //browser: browserify()
            };

            $http.post(API.URL + API.STATS,JSON.stringify(tmp))
                .success(function(data){
                    console.log(tmp);
                });
        }

        function loadGeneral($scope) {

            $http.get('data/dataMethods.json').success(function(data){
                $scope.methods_data.push(data.stats.geolocation);
                $scope.methods_data.push(data.stats.info);
                $scope.methods_data.push(data.stats.route);
                $scope.methods_data.push(data.stats.weather);
                $scope.methods_data = [$scope.methods_data];
            });

            $http.get('data/dataEnvelopes.json').success(function(data){
                $scope.env_data.push(data.stats.json);
                $scope.env_data.push(data.stats.xml);
                $scope.env_data = [$scope.env_data];
            });

            $http.get('data/dataBrowsers.json').success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.browser_labels.push(data.stats[i].browser);
                    $scope.browser_data.push(data.stats[i].number);
                }
            });
        }

        function loadInfo($scope) {

            $http.get('data/data1.json').success(function(data){
                $scope.stations_info_ranking = data.stats;
                for(var i = 0; i < data.stats.length; i++){
                    $scope.stations_info.push(data.stats[i].station);
                    $scope.stations_info_data.push(data.stats[i].data);
                }
            });
        }

        function loadRoute($scope) {

            $http.get('data/data2.json').success(function(data){
                $scope.stations_route_ranking = data.stats;
                for(var i = 0; i < data.stats.length; i++){
                    $scope.stations_route.push(data.stats[i].station);
                    $scope.stations_route_data.push(data.stats[i].data);
                }
            });
        }

        function loadInfoDays($scope){
            $http.get('data/data3.json').success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.days_info_data.push(data.stats[i].number);
                }
                $scope.days_info_data = [$scope.days_info_data];
            });
        }

        function loadRouteDays($scope){
            $http.get('data/data4.json').success(function(data){
                for(var i = 0; i < data.stats.length; i++){
                    $scope.days_route_data.push(data.stats[i].number);
                }
                $scope.days_route_data = [$scope.days_route_data];
            });
        }

        function loadHeatMap(){
            mapOptions = {
                center: { lat: 41.6590394, lng: -0.8745309},
                zoom: 14
            };
            map = new google.maps.Map(document.getElementById('heatmap'), mapOptions);

            $http.get('data/data5.json').success(function(data) {

                var pointArray = new google.maps.MVCArray(data.pos);

                var heatmap = new google.maps.visualization.HeatmapLayer({
                    data: pointArray
                });

                heatmap.setMap(map);
            });
        }

        function browserify() {
            var ua= navigator.userAgent, tem,
                M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
            if(/trident/i.test(M[1])){
                tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
                return 'IE '+(tem[1] || '');
            }
            if(M[1]=== 'Chrome'){
                tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
                if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
            }
            M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
            if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
            return M.join(' ');
        };
    }]);
