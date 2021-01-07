
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;

  var tt = table.render({
    elem: '#pageList'
    ,url: '/page/pageSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {checkbox: true}
      ,{field: 'pageName', title: '试卷名字'}
      // ,{field: 'studentCount', title: '参考人数'}
      ,{field: 'questionCount', title: '题目数量'}
      ,{field: 'maxScore', title: '题目总分'}
      ,{field: 'pageTeacher', title: '出题老师'}
      ,{fixed: 'right', width:300,align:'center', toolbar: '#pageListBar'}
    ]]
    ,id: 'pageList'
  });

  table.on('tool(pageListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(data.pageName+" 详细信息",'/page/pageDetails?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        $.ajax({
          url:'/page/delOnePage?id='+data.id,
          type:'get',
          success:function(data){
            obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
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
      WeAdminEdit('编辑','/page/pageEdit?id='+data.id);

    }else if(layEvent === 'addList'){
      WeAdminEdit(data.pageName+" 题目批量添加",'/page/questionAddList?id='+data.id);
    }
  });


});


