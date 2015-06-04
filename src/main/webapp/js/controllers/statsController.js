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

        //most info requested stations

        $scope.stations_info = [];
        $scope.stations_route = [];
        $scope.stations_info_data = [];
        $scope.stations_route_data = [];
        $scope.stations_info_ranking = [];
        $scope.stations_route_ranking = [];
        $scope.days = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
        $scope.days_series = ['Peaks'];
        $scope.days_info_data = [];
        $scope.days_route_data = [];

        //load data
        statsService.loadInfo($scope);
        statsService.loadRoute($scope);
        statsService.loadInfoDays($scope);
        statsService.loadRouteDays($scope);

    }]);