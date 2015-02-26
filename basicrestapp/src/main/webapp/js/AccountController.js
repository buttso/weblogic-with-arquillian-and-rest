/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller("AccountController", function ($scope, $http) {
    $scope.name = "Jack";
    $scope.balance = 0;
    $scope.accountExists = false;
    $scope.firstPage = true;
    $scope.transaction = 0;

    $scope.checkAccountExists = function () {
        $scope.firstPage = false;
        $http.get("http://localhost:7001/webapp/rest/account/" + $scope.name)
                .success(function (response) {
                    $scope.accountExists = true;
                    $scope.balance = response;
                })
                .error(function (response) {
                    $scope.accountExists = false;
                })
    }

    $scope.createAccount = function () {
        alert('create');
    }

    $scope.depositAccount = function () {
        alert('deposit: $' + $scope.transaction);
    }

    $scope.withdrawAccount = function () {
        alert('withdraw: $' + $scope.transaction);
    }
    
    $scope.exitAccount = function () {
        $scope.accountExists = false;
        $scope.firstPage = true;
        $scope.name = "";
        $scope.balance = 0;
    }
    


});


