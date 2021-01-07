
layui.use(['jquery','table','form'], function(){
  var table = layui.table;
  var $ = layui.$;
  var form = layui.form;
  var tt = table.render({
    elem: '#courseList'
    ,url: '/course/findCourseVoByXuenianAndXueqi' //数据接口
    //,page: true //开启分页
    ,cols: [[
      {field: 'courseName', title: '课程名字'}
      ,{field: 'courseTeacher', title: '课程老师'}
      ,{field: 'studentCourse.score', title: '分数',templet:'<div>{{d.studentCourse.score}}</div>'}
      ,{field: 'examTime', title: '考试时间',templet: function(d){
        if (d.studentCourse.examId==null||d.exam==null||d.exam.startTime==null){
                return "暂未安排考试";
          }else{
              if (d.studentCourse.score>59){
                return '<span>已通过考试</span>'
              }
                return '<span style="color: #c00;">'+ d.exam.startTime +'</span>'
          }}
        }
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
    }  else if(layEvent === 'edit'){ //编辑
      //do something
      // WeAdminShow('编辑','/student/studentEdit?id='+data.id);
    }
  });

  form.on('submit(sreach)', function(data){
    tt.reload({where:data.field});
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
  });
});


