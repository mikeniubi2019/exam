layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','table', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  $ = layui.$;

  var table = layui.table;
  var questionTable = table.render({
    elem: '#questionTable'
    ,url:'/reading/readingQuestionSearch?titleId='+readingId
    ,toolbar: '#questionToolbar' //开启头部工具栏，并为其绑定左侧模板
    ,defaultToolbar: ['filter', 'exports', 'print']
    ,title: '列表'
    ,cols: [[
      {field:'questionScore', title:'分数', edit: 'text'}
      ,{field:'questionTitle', title:'题目', width:80, edit: 'text'}
      ,{field:'questionChoice1', title:'选项1', edit: 'text'}
      ,{field:'questionChoice2', title:'选项2', edit: 'text'}
      ,{field:'questionChoice3', title:'选项3', edit: 'text'}
      ,{field:'questionChoice4', title:'选项4', edit: 'text'}
      ,{field:'questionChoice5', title:'选项5', edit: 'text'}
      ,{field:'questionAnswer', title:'正确答案', edit: 'text'}
      ,{field:'questionChoice6', title:'答案解析', edit: 'text'}
      ,{fixed: 'right', title:'操作', toolbar: '#questionBar', width:150}
    ]]
    ,page: true
  });

  //头工具栏事件
  table.on('toolbar(uestionTableFilter)', function(obj){

    switch(obj.event){
      case 'addOneQuestion':
        $.ajax({
          url:'/reading/addOneBlankReadingQuestion?readingId='+readingId,
          type:'get',
          success:function(data){
            questionTable.reload({page:1});
          }
        });
        break;
    };
  });

  //监听行工具事件
  table.on('tool(uestionTableFilter)', function(obj){
    var data = obj.data;
    //console.log(obj)
    if(obj.event === 'del'){
      layer.confirm('真的删除行么', function(index){
        obj.del();
        $.ajax({
          url:'/reading/delOneReadingQuestion?id='+data.id,
          type:'get',
          success:function(data){
            questionTable.reload();
          }
        });
        layer.close(index);
      });
    }
  });

  table.on('edit(uestionTableFilter)', function(obj){
    var value = obj.value //得到修改后的值
        ,data = obj.data //得到所在行所有键值
        ,field = obj.field; //得到字段

    $.ajax({
      url:'/reading/updateOneReadingQuestion?id='+data.id+'&'+field+'='+value,
      type:'post',
      success:function(data){
        if (data.code!=100){
          layer.msg("更新失败");
        }
      },
      error:function () {
        layer.msg("更新失败");
      }
    });
  });

});