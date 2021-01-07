
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;

  var tt = table.render({
    elem: '#questionList'
    ,url: '/question/questionSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {checkbox: true}
      ,{field: 'questionTitle', title: '题目'}
      ,{field: 'questionScore', title: '分数'}
      ,{field: 'questionChoice1', title: '选项1'}
      ,{field: 'questionChoice2', title: '选项2'}
      ,{field: 'questionChoice3', title: '选项3'}
      ,{field: 'questionChoice4', title: '选项4'}
      ,{field: 'questionChoice5', title: '选项5'}
      ,{fixed: 'right', align:'center', toolbar: '#questionListBar'}
    ]]
    ,id: 'questionList'
  });

  table.on('tool(questionListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(" 详细信息",'/question/questionDetails?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

        $.ajax({
          url:'/question/delOneQuestion?id='+data.id,
          type:'get',
          success:function(data){
            layer.msg('删除成功');
          },
          error:function(error){
            layer.msg('删除失败');
          }
        });
        layer.close(index);
      });
    } else if(layEvent === 'edit'){ //编辑
      //do something
      WeAdminShow('编辑','/question/questionEdit?id='+data.id);

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


