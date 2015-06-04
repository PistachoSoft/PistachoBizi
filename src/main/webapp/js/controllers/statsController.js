angular.module('pistachoBizi')

    .controller('StatsCtrl', ['$scope', 'statsService', function($scope,statsService){
        $scope.labels = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
        $scope.series = ['Series A', 'Series B'];

        $scope.data = [
            [65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]
        ];

        $scope.labels2 = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
        $scope.data2 = [300, 500, 100];

        //envelopes
        $scope.env_labels = ["JSON", "XML"];
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