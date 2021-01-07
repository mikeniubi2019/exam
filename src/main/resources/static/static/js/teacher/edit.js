layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','form', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  $ = layui.$;
  //自定义验证规则

  //监听提交
  // form.on('submit(add)', function (data) {

  //   $.ajax({
  //     url:'/student/addOneStudent',
  //     type:'post',
  //     data:data.field,
  //     success:function(data){
  //       if (data.code==100){
  //         layer.alert(data.m, { icon: 6 }, function () {
  //           // 获得frame索引
  //           var index = parent.layer.getFrameIndex(window.name);
  //           //关闭当前frame
  //           parent.layer.close(index);
  //           parent.layui.table.reload("studentList",{});
  //         });
  //       }else {
  //         layer.alert(data.m);
  //       }
  //     },
  //     error:function () {
  //       layer.alert(data.m);
  //     }
  //   });
  //   return false;
  // });

  //监听提交
  form.on('submit(upd)', function (data) {
    $.ajax({
      url:'/teacher/updateOneTeacher',
      type:'post',
      data:data.field,
      success:function(data){
        if (data.code==100){
          layer.alert(data.m, { icon: 6 }, function () {
            // 获得frame索引
            var index = parent.layer.getFrameIndex(window.name);
            //关闭当前frame
            parent.layer.close(index);
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