$(function () {


  $('#loginForm').submit(function (e) {
    var $form = $(this);
    var $method = $form.attr('method');
    var $action = $form.attr('action');
    var $data = {};
    $data['username'] = $form.find('#username').val();
    $data['password'] = $form.find('#password').val();
    console.log('sending...', $method, $action, $data);
    $.ajax({
      type: $method,
      url: $action,
      contentType: "application/json",
      data: JSON.stringify($data)
    }).success(function (data) {
      console.log(data);
      localStorage.token = data.token;
    }).fail(function (err) {
      console.log(err.responseText, err.statusText);
    });
    e.preventDefault();
  });

  $('#btnSecured').click(function (data) {
    var token = localStorage.token;
    $.ajax({
      type: 'GET',
      url:'secured',
      contentType: "application/json",
      headers: {'Authorization':'Bearer '+token}
    }).success(function (data) {
      $('#adminLink')[0].href = '/admin?token='+token;
      console.log('Successfully loggedin.', data);
    })
  });

  $('#btnLogout').click(function (e) {
      localStorage.clear();
      $.ajax({
        type: 'POST',
        url: 'logout'
      }).success(function (data) {
        if (data.result){
          $('#adminLink')[0].href = '/admin';
        }
      }).fail(function (err) {
        console.log(err.responseText, err.statusText);
      });
      e.preventDefault();
    });


});
