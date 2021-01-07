
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;
  var form = layui.form;
  var tt = table.render({
    elem: '#studentList'
    ,url: '/student/studentSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {checkbox: true}
      ,{field: 'name', title: '名字'}
      ,{field: 'username', title: '账号'}
      ,{field: 'nianJi', title: '年级'}
      ,{fixed: 'right', align:'center', toolbar: '#studentListBar'}
    ]]
    ,id: 'studentList'
  });

  table.on('tool(studentListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(data.name+" 详细信息",'/student/studentDetails?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

        $.ajax({
          url:'/student/delOneStudent?id='+data.id,
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
      WeAdminShow('编辑','/student/studentEdit?id='+data.id);

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

  form.on('submit(sreach)', function(data){
    tt.reload({where:data.field,page:{page:1}});
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });

  $("#deleteSelectFilter").click(function(){
    var checkStatus = table.checkStatus('studentList');
    var data = checkStatus.data;
    var idList = "";
    if (data.length>0){
      for (x in data){
        idList = idList + data[x].id +","
      }
      $.ajax({
        url:'/student/delSelectStudent?ids='+idList,
        type:'get',
        success:function(data){
          layer.msg('删除成功');
          tt.reload({where:data.field});
        },
        error:function(error){
          layer.msg('删除失败');
        }
      });
    }else {
      layer.msg("未选中任何数据！");
    }
    return false;
  });
});


