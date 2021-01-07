layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','laydate','form', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  var laydate = layui.laydate;

  $ = layui.$;
  var startTime = $('#startTime').val();
  var endTime = $('#endTime').val();


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
      url:'/exam/addOneExam',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code==100){
          layer.alert(data.m, { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
            parent.layui.table.reload("examList",{});
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
      url:'/exam/updateOneExam',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code==100){
          layer.alert(data.m, { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
            parent.layui.table.reload("examList",{});
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

  laydate.render({
    elem: '#startTime' //指定元素
    ,type: 'datetime'
    ,format: 'yyyy-MM-dd HH:mm'
    ,value: startTime
  });

  laydate.render({
    elem: '#endTime' //指定元素
    ,type: 'datetime'
    ,format: 'yyyy-MM-dd HH:mm'
    ,value: endTime
  });



});