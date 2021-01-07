layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','form', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  $ = layui.$;
  var max = parseInt($("#maxScore").val());
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
  form.on('submit(ok)', function (data) {
    var s = data.field.getScore;

    if (s>max){
      layer.msg("该题得分大于可得最大得分，请确认并重新提交");
      return false;
    }

    $.ajax({
      url:'/essayQuestion/updateOneEssayRecordAndSendNext',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code!=100){
          //修改失败
          layer.alert("全部评阅完成", { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
          });
        }else {//修改成功，下一题
          if (data.data!=null&&data.data!=undefined){
            $("#answer").text("考生作答："+data.data.answer);
            $("#id").val(data.data.id);
            var p_count = parseInt($("#allCount").val());
            $("#allCount").val(p_count-1);
          }else {
            layer.alert("已结束", { icon: 6 }, function () {
              // 获得frame索引
              var index = parent.layer.getFrameIndex(window.name);
              //关闭当前frame
              parent.layer.close(index);
            });
          }
        }
      },
      error:function () {
        layer.alert("连接异常");
      }
    });
    return false;
  });

});