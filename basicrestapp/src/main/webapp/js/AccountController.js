/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
app.controller("AccountController", function ($scope, $http, $location) {
    $scope.name = "Jack";
    $scope.balance = 0;
    $scope.accountExists = false;
    $scope.firstPage = true;
    $scope.transaction = 0;
    $scope.base_url=$location.absUrl().substring(0,$location.absUrl().lastIndexOf("/"));

    $scope.checkAccountExists = function () {
        $scope.firstPage = false;
        $http.get($scope.base_url + "/rest/account/" + $scope.name)
                .success(function (response, status) {
                    $scope.accountExists = true;
                    $scope.balance = response;
                })
                .error(function (response, status) {
                    $scope.accountExists = false;
                })
    }

    $scope.createAccount = function () {
        //alert('create ' + $scope.name + ' ' + $sceop.transaction);
        $http.post($scope.base_url + "/rest/account/create/" + $scope.name, "amount=" + $scope.transaction)        
                .success(function (response, status) {
                    //alert(response + " " + status);
                    $scope.message = "[ create " + $scope.name + " " + $scope.transaction + " ]";
                    $scope.accountExists = true;
                    $scope.balance = response;
                    $scope.transaction = 0;

                })
                .error(function (response, status) {
                    //alert(response + " " + status);
                    $scope.accountExists = false;
                })
    }

    $scope.depositAccount = function () {
        //alert('deposit: $' + $scope.transaction);
        $http.post($scope.base_url + "/rest/account/deposit/" + $scope.name, "amount=" + $scope.transaction)
                .success(function (response, status) {
                    //alert(response + " " + status);
                    $scope.message = "[ deposit " + $scope.transaction + " ]";
                    $scope.balance = response;
                    $scope.transaction = 0;
                })
                .error(function (response, status) {
                    //alert(response + " " + status);
                })
    }

    $scope.withdrawAccount = function () {
        //alert('withdraw: $' + $scope.transaction);
        $http.post($scope.base_url + "/rest/account/withdraw/" + $scope.name, "amount=" + $scope.transaction)
                .success(function (response, status) {
                    //alert(response + " " + status);
                    $scope.message = "[ withdraw " + $scope.transaction + " ]";
                    $scope.balance = response;
                    $scope.transaction = 0;

                })
                .error(function (response, status) {
                    //alert(response + " " + status);
                })

    }

    $scope.exitAccount = function () {
        $scope.accountExists = false;
        $scope.firstPage = true;
        $scope.name = "";
        $scope.balance = "$0";
        $scope.transaction = 0
        $scope.message = "[ cleared ]";
    }

});


