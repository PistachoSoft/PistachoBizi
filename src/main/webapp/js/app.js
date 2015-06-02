angular.module('pistachoBizi', ['ui.router', 'ui.bootstrap', 'chart.js'])

    .config(function($stateProvider, $urlRouterProvider){
        $stateProvider

            .state('home', {
                url: "/home",
                templateUrl: "templates/main/home.html",
                controller: "MainCtrl"
            })

            .state('stats', {
                url: "/stats",
                templateUrl: "templates/main/stats.html",
                controller: "StatsCtrl"
            });
        
        $urlRouterProvider.otherwise('home');
    });