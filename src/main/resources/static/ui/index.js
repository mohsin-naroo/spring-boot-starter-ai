(function() {

    'use strict';

    var applicationName = "ApiApp";
    angular.module(applicationName, []);

    function ApiController($http, $scope) {

        var self = this;
        self.data = { prompt: 'Tell me a joke', output: '', type: 'Chat' };

        self.init = function() {
            self.loadingOverlay = $("#loading-overlay");
        };

        self.chat = function() {
            self.loadingOverlay.show();
            // self.data = { prompt: self.data.prompt, type: self.data.type};
            var formData = new FormData();
            formData.append('prompt', self.data.prompt);
            var url = '../api/v1/chat';
            if (self.data.type == 'RAG') {
                url = '../api/v1/chat/rag';
            }
            $http({
                method: 'POST',
                url: url,
                headers: { 'Content-Type': undefined },
                data: formData
            }).then(function(response) {
                self.loadingOverlay.hide();
                self.data = response.data;
            }, function(response) {
                self.loadingOverlay.hide();
                self.data.output = JSON.stringify(response.data);
            });
        };

        self.init();
    }

    angular.module(applicationName).controller('ApiController', ['$http', '$scope', ApiController]);

}());