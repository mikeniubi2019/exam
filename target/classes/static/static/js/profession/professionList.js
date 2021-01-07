
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;

  var tt = table.render({
    elem: '#professionList'
    ,url: '/profession/professionSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {checkbox: true}
      ,{field: 'professionName', title: '专业名字'}
      ,{field: 'professionDescription', title: '专业描述'}
      ,{field: 'studentCout', title: '专业人数'}
      ,{fixed: 'right', align:'center', toolbar: '#professionListBar'}
    ]]
    ,id: 'professionList'
  });

  table.on('tool(professionListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(data.professionName+" 详细信息",'/profession/professionDetails?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

        $.ajax({
          url:'/profession/delOneProfession?id='+data.id,
          type:'get',
          success:function(data){
            layer.msg('删除成功');
          },
          error:function(error){
            layer.msg('删除链接失败');
          }
        });
        layer.close(index);
      });
    } else if(layEvent === 'edit'){ //编辑
      //do something
      WeAdminShow('编辑','/profession/professionEdit?id='+data.id);

      // $.ajax({
      //   url:'/student/findOneStudentById?id='+data.id,
      //   type:'get',
      //   success:function(data){
      //     //同步更新缓存对应的值
      //     obj.update({
      //       name:data.name
      //       ,username: data.user
      //       ,nianJi: data.nianJi
      //     });
      //   },
      //   error:function(error){
      //     layer.msg('失败,请刷新');
      //   }
      // });
    }
  });


});


