
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;

  var tt = table.render({
    elem: '#questionList'
    ,url: '/reading/readingSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {field: 'title', title: '题目'}
      ,{fixed: 'right', align:'center', toolbar: '#questionListBar'}
    ]]
    ,id: 'readingList'
  });

  table.on('tool(questionListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(" 批量修改",'/reading/readingQuestionList?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存

        $.ajax({
          url:'/reading/delOneReading?id='+data.id,
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
      WeAdminShow('编辑','/reading/readingEdit?id='+data.id);
    }
  });
});


