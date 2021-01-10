layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','form', 'layer', 'admin'], function () {
  var form = layui.form;
  var $ = layui.$;
  setTimeout("timeCal()",duration*60*1000);

  window.timeCal = function(){
    layer.msg("答题时间结束，自动提交答卷");
    $("#jiaoJuan").click();
  }

  //监听提交
  form.on('submit(pageSubmit)', function (data) {
    var o = new Object();
    for (i in data.field) {
        if (i.indexOf("[")!=-1){
          var id = i.split("[")[0];
          if (o.hasOwnProperty(id)){
            o[id]=o[id]+","+data.field[i];
          }else {
            o[id]=data.field[i];
          }
        }else {
          o[i] = data.field[i];
        }
    }
    o.examToken=examToken;
    o.studentId=studentId;
    o.pageId=pageId;
    o.scId = scId;

    $.ajax({
        url:'/question/receiveExamResult',
        type:'post',
        data:o,
        success:function(data){
          if (data.code==100){
            layer.alert("请稍候，正在统计成绩！", { icon: 6 }, function () {
              setInterval(function () {
                $.ajax({
                  url:'/question/getScoreByMark?mark='+data.data,
                  type:'get',
                  success:function(data2){
                    if (data2.code==100){
                      layer.alert(data2.m, { icon: 6 }, function () {
                        location.replace("/exam/examListPage");
                      });
                    }
                  }
                });
              },1000*2);
            });
          }
          else {
            layer.alert(data.m);
          }
        },
        error:function () {
          layer.alert("网络连接异常");
        }
      });
    return false;
  });
});