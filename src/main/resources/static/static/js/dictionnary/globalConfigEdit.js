layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','form', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  $ = layui.$;
  //自定义验证规则
  form.verify({
    nikename: function (value) {
      if (value.length < 5) {
        return '昵称至少得5个字符啊';
      }
    }
    , pass: [/(.+){6,12}$/, '密码必须6到12位']
    , repass: function (value) {
       if ($('#L_pass').val() != $('#L_repass').val()) {
        return '两次密码不一致';
      }
    }
  });

  //监听提交
  form.on('submit(upd)', function (data) {

    $.ajax({
      url:'/dictionnary/updateGlobalConfig',
      type:'post',
      data:data.field,
      dataType:"json",
      success:function(data){

          layer.alert(data.m);

      },
      error:function () {
        layer.alert('失败');
      }
    });
    return false;
  });

});