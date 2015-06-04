angular.module('pistachoBizi')

    .controller('StatsCtrl', ['$scope', 'statsService', function($scope,statsService){

        //envelopes
        $scope.env_labels = [];
        $scope.env_data = [];

        //browser
        $scope.browser_labels = [];
        $scope.browser_data = [];

        //weather
        $scope.weather_labels = [];
        $scope.weather_data = [];

        //info
        $scope.info_labels = [];
        $scope.info_data = [];

        //load data
        statsService.load($scope);

    }]);