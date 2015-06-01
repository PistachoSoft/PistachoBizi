angular.module('pistachoBizi')

    .directive('navbar', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navbar.html',
            controller: 'NavbarCtrl'
        }
    });