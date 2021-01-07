layui.extend({
  admin: '{/}../../static/js/admin'
});
layui.use(['jquery','table', 'layer', 'admin'], function () {
  var form = layui.form,
    admin = layui.admin,
    layer = layui.layer;
  $ = layui.$;

  var table = layui.table;
  var nianJiTable = table.render({
    elem: '#nianJiTable'
    ,url:'/dictionnary/dictionnarySearch?name=nianJi'
    ,toolbar: '#nianJiToolbar' //开启头部工具栏，并为其绑定左侧模板
    ,title: '年级列表'
    ,cols: [[
      {field:'value', title:'年级', edit: 'text'}
      ,{fixed: 'right', title:'操作', toolbar: '#nianJiBar', width:150}
    ]]
    ,page: true
  });

  //头工具栏事件
  table.on('toolbar(nianJiTableFilter)', function(obj){

    switch(obj.event){
      case 'addOneNianJi':
        $.ajax({
          url:'/dictionnary/addOneDictionnary?name=nianJi',
          type:'get',
          success:function(data){
            nianJiTable.reload({page:1});
          }
        });
        break;
    };
  });

  //监听行工具事件
  table.on('tool(nianJiTableFilter)', function(obj){
    var data = obj.data;
    //console.log(obj)
    if(obj.event === 'del'){
      layer.confirm('真的删除行么', function(index){
        obj.del();
        $.ajax({
          url:'/dictionnary/delOneDictionnary?id='+data.id,
          type:'get',
          success:function(data){
            questionTable.reload();
          }
        });
        layer.close(index);
      });
    }
  });

  table.on('edit(nianJiTableFilter)', function(obj){
    var value = obj.value //得到修改后的值
        ,data = obj.data //得到所在行所有键值
        ,field = obj.field; //得到字段

    $.ajax({
      url:'/dictionnary/updateOneDictionnary?id='+data.id+'&'+field+'='+value,
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