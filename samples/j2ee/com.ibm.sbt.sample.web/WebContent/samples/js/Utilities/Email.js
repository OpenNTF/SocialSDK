require(['sbt/emailService', 'sbt/dom'], function(email, dom) {
    var to = [];
    var cc = [];
    var bcc = [];
    var mimeParts = [];
    
    var createAddAddressClickHandler = function(inputId, addAddressId, array) {
        return function(e) {
            var input = document.getElementById(inputId);
            var email = input.value;
            input.value = '';
            array.push(email);
            var addedAddresses = document.getElementById(addAddressId);
            var span = dom.create("span", null, addedAddresses);      
            dom.setText(span, array[array.length - 1]);
            dom.setAttr(span, "class", "label label-info");
        };
    };
    
    document.getElementById('addTo').onclick = createAddAddressClickHandler('toInput', 'addedToAddresses', to);
    document.getElementById('addCC').onclick = createAddAddressClickHandler('ccInput', 'addedCCAddresses', cc);
    document.getElementById('addBCC').onclick = createAddAddressClickHandler('bccInput', 'addedBCCAddresses', bcc);
    document.getElementById('addMimePart').onclick = function(e) {
      var mimeTypeInput = document.getElementById('mimeTypeInput');
      var mimeContentInput = document.getElementById('mimeContentInput');
      mimeParts.push({
          mimeType: mimeTypeInput.value,
          content: mimeContentInput.value
      });
      var addedMimeParts = document.getElementById('addedMimeParts');
      var span = dom.create("span", null, addedMimeParts);      
      dom.setText(span, mimeTypeInput.value);
      dom.setAttr(span, "class", "label label-info");
    
      mimeTypeInput.value = '';
      mimeContentInput.value = '';
    };
    
    document.getElementById('sendBtn').onclick = function(e) {
        var subject = document.getElementById('subjectInput').value;
        var emailJson = {
          to: to,
          cc: cc,
          bcc: bcc,
          subject: subject,
          mimeParts: mimeParts
        };
        email.send(emailJson).then(
            function(response) {
            	//This callback may still be called even if there is a problem sending the email.
            	//This API allows you to send multiple individual emails at once so it is possible
            	//that one email may fail to send while the other may succeed.
                var successElement = document.getElementById('success');
                successElement.setAttribute('style', 'display:none;');
                var errorElement = document.getElementById('error');
                errorElement.setAttribute('style', 'display:none;');
                if(response.message || (response.error && response.error.length != 0)) {
                	dom.setText(errorElement, response.message || response.error[0].message);                	
                    errorElement.setAttribute('style', '');
                } else {
                    successElement.setAttribute('style', '');
                }
            },
            function(error) {
            	//This will only be executed if there is an error with the request to 
            	//the server to send all the emails.
            	var errorElement = document.getElementById('error');
            	dom.setText(errorElement, error.message || 'There was an error sending the email.');            	
            	errorElement.setAttribute('style', '');
            });
    };
});