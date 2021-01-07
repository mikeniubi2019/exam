
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;

  var tt = table.render({
    elem: '#courseList'
    ,url: '/course/courseSearch' //数据接口
    ,page: true //开启分页
    ,cols: [[
      {checkbox: true}
      ,{field: 'courseName', title: '课程名字'}
      ,{field: 'courseTeacher', title: '课程老师'}
      ,{field: 'courseXueNian', title: '课程学年',templet: function(d){return '<span>'+ (d.courseXueNian+1) +'</span>'}}
      ,{field: 'courseXueQi', title: '课程学期'}
      ,{fixed: 'right', align:'center', toolbar: '#courseListBar'}
    ]]
    ,id: 'courseList'
  });

  table.on('tool(courseListFilter)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）

    if(layEvent === 'detail'){ //查看
      WeAdminShow(data.courseName+" 详细信息",'/course/courseDetails?id='+data.id);
    } else if(layEvent === 'del'){ //删除
      layer.confirm('真的删除行么', function(index){
        $.ajax({
          url:'/course/delOneCourse?id='+data.id,
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
      WeAdminShow('编辑','/course/courseEdit?id='+data.id);

    }
  });

});


