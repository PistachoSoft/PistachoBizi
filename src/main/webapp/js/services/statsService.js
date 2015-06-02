angular.module('pistachoBizi')

    .service('statsService', ['$http', 'API', function($http,API){

        return ({
            //constants
            GEO: 'geolocation',
            ROU: 'route',
            WEA: 'weather',
            INF: 'info',
            //functions
            log: log
        });

        function log(method, params) {

            var tmp = {
                method: method,
                params: params,
                //location: geolocate(),
                browser: browserify()
            };

            //$http.post(API.STATS,JSON.stringify(tmp)).success(console.log("Successfully logged stats"));
            console.log(tmp);
        }

        function geolocate() {
            if(navigator.geolocation) {

                var pos;

                navigator.geolocation.getCurrentPosition(function(position){
                    pos = position;
                });

                return({
                    lat: pos.coords.latitude,
                    lng: pos.coords.longitude
                });
            } else {
                return -1;
            }
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
