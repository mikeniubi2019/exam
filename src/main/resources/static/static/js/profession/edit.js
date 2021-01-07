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
  form.on('submit(add)', function (data) {

    $.ajax({
      url:'/profession/addOneProfession',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code==100){
          layer.alert(data.m, { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
            parent.layui.table.reload("professionList",{});
          });
        }else {
          layer.alert(data.m);
        }
      },
      error:function () {
        layer.alert(data.m);
      }
    });
    return false;
  });

  //监听提交
  form.on('submit(upd)', function (data) {
    $.ajax({
      url:'/profession/updateOneProfession',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code==100){
          layer.alert(data.m, { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
            parent.layui.table.reload("professionList",{});
          });
        }else {
          layer.alert(data.m);
        }
      },
      error:function () {
        layer.alert(data.m);
      }
    });
    return false;
  });

});