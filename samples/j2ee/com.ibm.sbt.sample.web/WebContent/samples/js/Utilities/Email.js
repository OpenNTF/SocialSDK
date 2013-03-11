require(['sbt/emailservice'], function(email) {
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
            var html = addedAddresses.innerHTML;
            html = html + '<span class="label label-info">' + array[array.length - 1] + '</span>';
            addedAddresses.innerHTML = html; 
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
      var html = addedMimeParts.innerHTML;
      html = html + '<span class="label label-info">' + mimeTypeInput.value + '</span>';
      addedMimeParts.innerHTML = html;
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
        email.send(emailJson, { 
            callback: function(response) {
                var successElement = document.getElementById('success');
                successElement.setAttribute('style', 'display:none;');
                var errorElement = document.getElementById('error');
                errorElement.setAttribute('style', 'display:none;');
                if(response.message || (response.error && response.error.length != 0)) {
                    if(response.message) {
                        errorElement.innerHTML = response.message;
                    } else {
                        errorElement.innerHTML = response.error[0].message;
                    }
                    errorElement.setAttribute('style', '');
                } else {
                    successElement.setAttribute('style', '');
                }
            }
        });
    };
    
});